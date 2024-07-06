package net.sirplop.aetherworks.blockentity;

import com.rekindled.embers.api.tile.IExtraCapabilityInformation;
import com.rekindled.embers.particle.GlowParticleOptions;
import com.rekindled.embers.particle.SparkParticleOptions;
import com.rekindled.embers.util.Misc;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.sirplop.aetherworks.AWRegistry;
import net.sirplop.aetherworks.recipe.AetheriumAnvilContext;
import net.sirplop.aetherworks.recipe.IAetheriumAnvilRecipe;
import org.joml.Vector3f;

public class AetheriumAnvilBlockEntity extends BlockEntity implements IForgePart, IExtraCapabilityInformation {

    public AetheriumAnvilBlockEntity(BlockPos pos, BlockState state) {
        super(AWRegistry.AETHERIUM_ANVIL_BLOCK_ENTITY.get(), pos, state);
        progress = 0;
    }

    public int progressPrev = -1;
    public int progress;
    public int hitTimeout;
    public float heatFluctuationsMemory;
    public int mistakes;
    public IAetheriumAnvilRecipe cachedRecipe;
    public boolean hasEmber = false;

    public ItemStackHandler inventory = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            AetheriumAnvilBlockEntity.this.setChanged();
        }

        @Override
        public int getSlotLimit(int slot)
        {
            return 1;
        }
    };
    public LazyOptional<IItemHandler> holder = LazyOptional.of(() -> inventory);
    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        inventory.deserializeNBT(nbt.getCompound("inventory"));
        progress = nbt.getInt("progress");
        hitTimeout = nbt.getInt(("hitTimeout"));
        heatFluctuationsMemory = nbt.getFloat("heatFluctuation");
        mistakes = nbt.getInt("mistakes");
    }

    @Override
    public void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        nbt.put("inventory", inventory.serializeNBT());
        nbt.putInt("progress", progress);
        nbt.putInt("hitTimeout", hitTimeout);
        nbt.putFloat("heatFluctuation", heatFluctuationsMemory);
        nbt.putInt("mistakes", mistakes);
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag nbt = super.getUpdateTag();
        nbt.put("inventory", inventory.serializeNBT());
        nbt.putInt("progress", progress);
        nbt.putInt("hitTimeout", hitTimeout);
        nbt.putFloat("heatFluctuation", heatFluctuationsMemory);
        nbt.putInt("mistakes", mistakes);
        return nbt;
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (!this.remove && cap == ForgeCapabilities.ITEM_HANDLER) {
            return ForgeCapabilities.ITEM_HANDLER.orEmpty(cap, holder);
        }
        return super.getCapability(cap, side);
    }
    @Override
    public boolean hasCapabilityDescription(Capability<?> capability) {
        return capability == ForgeCapabilities.ITEM_HANDLER;
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        holder.invalidate();
    }

    @Override
    public void setChanged() {
        super.setChanged();
        if (level instanceof ServerLevel)
            ((ServerLevel) level).getChunkSource().blockChanged(worldPosition);
    }
    public static final GlowParticleOptions GLOW = new GlowParticleOptions(new Vector3f(0, 1F, 1F), 1f, 30);

    @Override
    public void onForgeTick(IForge forge)
    {
        if (!inventory.getStackInSlot(0).isEmpty()) {
            IAetheriumAnvilRecipe prevRecipe = cachedRecipe;
            AetheriumAnvilContext context = new AetheriumAnvilContext(inventory,  (int)forge.getHeatCapability().getHeat());
            cachedRecipe = Misc.getRecipe(cachedRecipe, AWRegistry.AETHERIUM_ANVIL.get(), context, level);

            if (!level.isClientSide() && cachedRecipe != prevRecipe) {
                resetProgress();
            }
        }
        else{
            cachedRecipe = null;
        }

        if (cachedRecipe != null) {
            hasEmber = forge.getEmberCapability().getEmber() > cachedRecipe.getEmberPerHit();

            if (level.isClientSide()) {
                BlockPos pos = getBlockPos();
                float pX, pZ;
                switch (getBlockState().getValue(HorizontalDirectionalBlock.FACING)) {
                    case EAST, WEST -> {
                        pX = pos.getX() + 0.25f + level.random.nextFloat() * 0.5f;
                        pZ = pos.getZ() + 0.125f + level.random.nextFloat() * 0.875f;
                    }
                    default -> {
                        pX = pos.getX() + 0.125f + level.random.nextFloat() * 0.875f;
                        pZ = pos.getZ() + 0.25f + level.random.nextFloat() * 0.5f;
                    }
                }

                final float speed = 0.1f;
                level.addParticle(GLOW,
                        pX,
                        pos.getY() + 0.2f,
                        pZ,
                        (level.random.nextFloat() * speed) - (speed * 0.5f),
                        speed,
                        (level.random.nextFloat() * speed) - (speed * 0.5f));
                return;
            }

            boolean cont = true;
            if (progress >= cachedRecipe.getNumberOfHits()) {
                AetheriumAnvilContext context = new AetheriumAnvilContext(inventory, (int)forge.getHeatCapability().getHeat());
                inventory.setStackInSlot(0, cachedRecipe.assemble(context, level.registryAccess()));
                level.playSound(null, getBlockPos(), SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 1, 0.8f + (level.random.nextFloat() * 0.2f));
                resetProgress();
            } else if (hitTimeout > 0) {
                if (--hitTimeout == 0) {
                    cont = false;
                    makeMistake();
                }
            }

            if (cont && progressPrev != progress && progressPrev != -1) {
                if (forge.getEmberCapability().removeAmount(cachedRecipe.getEmberPerHit(), true) != cachedRecipe.getEmberPerHit()) {
                    makeMistake();
                }
            }

            if (heatFluctuationsMemory != 0) {
                forge.getHeatCapability().addAmount(heatFluctuationsMemory, true);
                heatFluctuationsMemory = 0;
                setChanged();
            }

            if (hitTimeout == 0) {
                if (level.random.nextFloat() < 0.01f + cachedRecipe.getDifficulty() / 100f) {
                    //more lenient than the original - gotta account for lag a little.
                    this.hitTimeout = Math.max(20, 100 - cachedRecipe.getDifficulty() * 12);
                    setChanged();
                }
            }
        } else {
            hitTimeout = 0;
            mistakes = 0;
            heatFluctuationsMemory = 0;
            progress = 0;
        }

        progressPrev = progress;
    }

    @Override
    public boolean isTopPart() {
        return true;
    }

    @Override
    public boolean isInvalid() {
        return level.getBlockEntity(getBlockPos()) == null;
    }

    public boolean onHit() {
        if (!level.isClientSide() && cachedRecipe != null) {
            if (hitTimeout > 0 && hasEmber)  {
                if (progress < cachedRecipe.getNumberOfHits() - 1) {
                    level.playSound(null, getBlockPos(), SoundEvents.ANVIL_USE, SoundSource.PLAYERS, 1, 0.8f + (level.random.nextFloat() * 0.4f));
                }

                ++progress;
                hitTimeout = 0;
                heatFluctuationsMemory += (10 + (cachedRecipe.getDifficulty() * 5)) * (level.random.nextBoolean() ? -1 : 1);
                setChanged();
            } else {
                makeMistake();
            }
            ((ServerLevel)level).sendParticles(new SparkParticleOptions(GlowParticleOptions.EMBER_COLOR, 1.0f),
                    worldPosition.getX() + 0.3f + level.random.nextFloat() * 0.4f,
                    worldPosition.getY() + 0.35f,
                    worldPosition.getZ() + 0.3f + level.random.nextFloat() * 0.4f,
                    10, 0.1, 0.0, 0.1, 1.0);
            return true;
        }
        return false;
    }

    public void makeMistake() {
        level.playSound(null, getBlockPos(), SoundEvents.ANVIL_DESTROY, SoundSource.PLAYERS, 1, 0.8f + (level.random.nextFloat() * 0.4f));
        if (++mistakes >= 3) {
            inventory.setStackInSlot(0, ItemStack.EMPTY);
            resetProgress();
        }
    }

    public void resetProgress() {
        hitTimeout = 0;
        mistakes = 0;
        heatFluctuationsMemory = 0;
        progress = 0;
        setChanged();
    }
}
