package net.sirplop.aetherworks.blockentity;

import com.rekindled.embers.Embers;
import com.rekindled.embers.api.tile.IExtraCapabilityInformation;
import com.rekindled.embers.particle.GlowParticleOptions;
import com.rekindled.embers.util.Misc;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.FluidHandlerBlockEntity;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.sirplop.aetherworks.AWConfig;
import net.sirplop.aetherworks.AWRegistry;
import net.sirplop.aetherworks.Aetherworks;
import net.sirplop.aetherworks.recipe.IAetheriumAnvilRecipe;
import net.sirplop.aetherworks.recipe.IMetalFormerRecipe;
import net.sirplop.aetherworks.recipe.MetalFormerContext;
import net.sirplop.aetherworks.util.Utils;
import org.joml.Vector3f;

import java.util.List;

public class MetalFormerBlockEntity extends FluidHandlerBlockEntity implements IForgePart, IExtraCapabilityInformation {
    public MetalFormerBlockEntity(BlockPos pos, BlockState state) {
        super(AWRegistry.METAL_FORMER_BLOCK_ENTITY.get(), pos, state);
        tank = new FluidTank(1000) {
            @Override
            public void onContentsChanged() {
                MetalFormerBlockEntity.this.setChanged();
            }
        };
    }

    public float renderOffset;
    public boolean hasEmber;

    public int progress;
    public IMetalFormerRecipe cachedRecipe = null;

    public ItemStackHandler inventory = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            MetalFormerBlockEntity.this.setChanged();
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
    }

    @Override
    public void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        nbt.put("inventory", inventory.serializeNBT());
        nbt.putInt("progress", progress);
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag nbt = super.getUpdateTag();
        this.tank.writeToNBT(nbt);
        nbt.put("inventory", inventory.serializeNBT());
        nbt.putInt("progress", progress);
        return nbt;
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public int getCapacity(){
        return tank.getCapacity();
    }

    public FluidStack getFluidStack() {
        return tank.getFluid();
    }

    public FluidTank getTank() {
        return tank;
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
        return capability == ForgeCapabilities.FLUID_HANDLER || capability == ForgeCapabilities.ITEM_HANDLER;
    }

    @Override
    public void addCapabilityDescription(List<Component> strings, Capability<?> capability, Direction facing) {
        if (capability == ForgeCapabilities.FLUID_HANDLER)
            strings.add(IExtraCapabilityInformation.formatCapability(EnumIOType.INPUT, Embers.MODID + ".tooltip.goggles.fluid", Component.translatable(Aetherworks.MODID + ".tooltip.goggles.fluid.aether")));
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

    @Override
    public void onForgeTick(IForge forge) {
        if (!level.isClientSide()) {
            FluidStack fs = this.tank.getFluid();
            if (fs.isEmpty() || fs.getAmount() < this.tank.getCapacity()) {
                for (IFluidHandler iFluidHandler : forge.getAttachedFluidHandlers()) {
                    FluidStack tryDrain = fs.isEmpty() ? null : new FluidStack(fs.getFluid(), this.tank.getCapacity() - this.tank.getFluidAmount());
                    FluidStack drainAttempt = tryDrain == null ? iFluidHandler.drain(this.tank.getCapacity(), IFluidHandler.FluidAction.SIMULATE) : iFluidHandler.drain(tryDrain, IFluidHandler.FluidAction.SIMULATE);
                    if (drainAttempt.getAmount() > 0) {
                        this.tank.fill(iFluidHandler.drain(drainAttempt, IFluidHandler.FluidAction.EXECUTE), IFluidHandler.FluidAction.EXECUTE);
                        if (this.tank.getFluidAmount() >= this.tank.getCapacity()) {
                            break;
                        }
                    }
                }
            }
        }

        if (!this.tank.isEmpty() && !this.inventory.getStackInSlot(0).isEmpty()) {
            MetalFormerContext context = new MetalFormerContext(inventory, this.getTank(), (int)forge.getHeatCapability().getHeat());
            cachedRecipe = Misc.getRecipe(cachedRecipe, AWRegistry.METAL_FORMING.get(), context, level);

            hasEmber = forge.getEmberCapability().getEmber() > 1;
            if (cachedRecipe != null) {
                if (!level.isClientSide()) {
                    if (forge.getEmberCapability().removeAmount(1,true) > 0)
                    {
                        forge.getHeatCapability().removeAmount(0.5, true);
                        ++this.progress;
                        this.setChanged();

                        if (this.progress >= cachedRecipe.getCraftTime()) //15 second craft time.
                        {
                            this.progress = 0;
                            inventory.setStackInSlot(0, cachedRecipe.assemble(context, level.registryAccess()));
                        }
                    }
                } else if (hasEmber) {
                    final GlowParticleOptions glow = new GlowParticleOptions(Utils.multiLerp( progress / (float) cachedRecipe.getCraftTime(),
                            GlowParticleOptions.EMBER_COLOR, Utils.AETHERIUM_COLOR), 1f, 30);

                    BlockPos pos = getBlockPos();
                    final float speed = 0.1f;
                    level.addParticle(glow,
                            pos.getX() + level.random.nextFloat(),
                            pos.getY() + 0.2f,
                            pos.getZ() + level.random.nextFloat(),
                            (level.random.nextFloat() * speed) - (speed * 0.5f),
                            speed,
                            (level.random.nextFloat() * speed) - (speed * 0.5f));
                }
            }
            else
            {
                this.progress = 0;
            }
        }
        else
        {
            this.progress = 0;
        }
    }

    @Override
    public boolean isTopPart() {
        return true;
    }

    @Override
    public boolean isInvalid() {
        return (level != null ? level.getBlockEntity(getBlockPos()) : null) == null;
    }
}
