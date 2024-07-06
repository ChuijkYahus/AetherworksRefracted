package net.sirplop.aetherworks.blockentity;

import com.rekindled.embers.api.capabilities.EmbersCapabilities;
import com.rekindled.embers.api.power.IEmberCapability;
import com.rekindled.embers.blockentity.FluidVesselBlockEntity;
import com.rekindled.embers.particle.GlowParticleOptions;
import com.rekindled.embers.power.DefaultEmberCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.sirplop.aetherworks.AWRegistry;
import net.sirplop.aetherworks.api.capabilities.AetherworksCapabilities;
import net.sirplop.aetherworks.api.power.IHeatCapability;
import net.sirplop.aetherworks.datagen.AWSounds;
import net.sirplop.aetherworks.power.DefaultHeatCapability;

import java.util.ArrayList;
import java.util.List;

public class ForgeCoreBlockEntity extends BlockEntity implements IForge {
    public ForgeCoreBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(AWRegistry.FORGE_CORE_BLOCK_ENTITY.get(), pPos, pBlockState);
        emberCapability.setEmberCapacity(5000);
        emberCapability.setEmber(0);
        heatCapability.setHeat(0);
        heatCapability.setHeatCapacity(3000);
    }
    public static final int[][] FORGE_OFFSETS = {
            {-1, -1},
            {-1,  1},
            {1,  -1},
            {1,   1},
            {1,   0},
            {-1,  0},
            {0,  -1},
            {0,   1},
            {0,   0}
    };
    public static final int[][] FORGE_SIDES = {
            {-2, -1},
            {-2,  0},
            {-2,  1},
            {2, -1},
            {2,  0},
            {2,  1},
            {-1, -2},
            { 0, -2},
            { 1, -2},
            {-1, 2},
            { 0, 2},
            { 1, 2},
    };

    private List<IFluidHandler> fluidHandlers = new ArrayList<>();
    private List<IForgePart> parts = new ArrayList<>();
    public boolean isStructureValid;
    private double storedHeat;
    private int ticksInDanger = 0;
    public int ticksExisted;

    public IEmberCapability emberCapability = new DefaultEmberCapability() {
        @Override
        public void onContentsChanged() {
            super.onContentsChanged();
            ForgeCoreBlockEntity.this.setChanged();
        }
    };

    public IHeatCapability heatCapability = new DefaultHeatCapability() {
        @Override
        public void onContentsChanged() {
            super.onContentsChanged();
            ForgeCoreBlockEntity.this.setChanged();
        }
    };

    @Override
    public ForgeCoreBlockEntity getOwner() {
        return this;
    }

    @Override
    public IHeatCapability getHeatCapability() {
        return heatCapability;
    }

    @Override
    public IEmberCapability getEmberCapability() {
        return emberCapability;
    }

    @Override
    public void transferHeat(float value, boolean immediate) {
        if (!immediate)
        {
            this.storedHeat += value;
        }
        else
        {
            this.heatCapability.setHeat(this.heatCapability.getHeat() + value);
        }
    }

    @Override
    public boolean canFunction() {
        return false;
    }

    @Override
    public List<IFluidHandler> getAttachedFluidHandlers() {
        return fluidHandlers;
    }
    @Override
    public List<IForgePart> getParts() {
        return parts;
    }

    private int groanTick = 0;
    public static final GlowParticleOptions EMBER = new GlowParticleOptions(GlowParticleOptions.EMBER_COLOR, 2f, 40);
    @OnlyIn(Dist.CLIENT)
    public static void clientTick(Level level, BlockPos pos, BlockState state, ForgeCoreBlockEntity blockEntity) {
        if (!level.isClientSide() || !blockEntity.isStructureValid)
            return;
        if (blockEntity.ticksExisted++ == 0 || blockEntity.ticksExisted % 20 == 0)
        { //the client needs to populate the list of parts themselves.
            blockEntity.checkStructureAndPopulate();
        }

        for (IForgePart part : blockEntity.parts) {
            if (part != null && !part.isInvalid())
                part.onForgeTick(blockEntity);
        }

        double current = blockEntity.heatCapability.getHeat();
        double max = blockEntity.heatCapability.getHeatCapacity();
        for (int i = 0; i <= current / max * 10; i++)
        {
            float sX = pos.getX() + 0.4F + level.random.nextFloat() * 0.2F;
            float sY = pos.getY() + 0.2F + level.random.nextFloat() * 0.2F;
            float sZ = pos.getZ() + 0.4F + level.random.nextFloat() * 0.2F;
            float speedUp = level.random.nextFloat() * 0.2F;
            level.addParticle(EMBER, sX, sY, sZ, 0, speedUp, 0);
        }

        if (current >= 2100)
        {
            float sX = pos.getX() - 1 + level.random.nextFloat() * 3;
            float sY = pos.getY();
            float sZ = pos.getZ() - 1 + level.random.nextFloat() * 3;
            float speedUp = level.random.nextFloat() * 0.02F;
            level.addParticle(EMBER, sX, sY, sZ, 0, speedUp, 0);
        }

        if (current >= 2800)
        {
            float sX = pos.getX() - 1 + level.random.nextFloat() * 3;
            float sY = pos.getY();
            float sZ = pos.getZ() - 1 + level.random.nextFloat() * 3;
            float speedUp = level.random.nextFloat() * 0.02F;
            level.addParticle(ParticleTypes.SMOKE, sX, sY, sZ, 0, speedUp, 0);

            if (blockEntity.groanTick++ % 200 == 0) { //every ten seconds
                level.playSound(Minecraft.getInstance().player, blockEntity.getBlockPos(), AWSounds.FORGE_GROAN.get(),
                        SoundSource.BLOCKS, 0.25f, 0.75f + level.random.nextFloat() * 0.5f);
            }
        } else {
            blockEntity.groanTick = 160;
        }

        if (current >= 2900)
        {
            float sX = pos.getX() - 1 + level.random.nextFloat() * 3;
            float sY = pos.getY();
            float sZ = pos.getZ() - 1 + level.random.nextFloat() * 3;
            float speedUp = level.random.nextFloat() * 0.02F;
            level.addParticle(ParticleTypes.FLAME, sX, sY, sZ, 0, speedUp, 0);
        }
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, ForgeCoreBlockEntity blockEntity) {
        if (level.isClientSide())
            return;
        if (blockEntity.ticksExisted++ == 0 || blockEntity.ticksExisted % 20 == 0)
        {
            blockEntity.checkStructureAndPopulate();
            level.sendBlockUpdated(pos, state, state, 1 | 2);
        }

        if (blockEntity.isStructureValid) {
            for (IForgePart part : blockEntity.parts) {
                if (part != null && !part.isInvalid())
                    part.onForgeTick(blockEntity);
            }
        }

        if (blockEntity.storedHeat > 0) {
            double added = Math.max(0.1F, blockEntity.storedHeat * 0.35F);
            added *= Math.max(0.01F, blockEntity.getHeatCapability().getHeat() / blockEntity.heatCapability.getHeatCapacity());
            blockEntity.heatCapability.addAmount(added, true);
            blockEntity.storedHeat -= added;
        }
        if (blockEntity.storedHeat <= 0)
        {
            double removed = Math.max(0.1F, blockEntity.getHeatCapability().getHeat() / blockEntity.heatCapability.getHeatCapacity());
            if (blockEntity.getHeatCapability().getHeat() > removed)
            {
                blockEntity.heatCapability.removeAmount(removed, true);
            }
        }

        level.sendBlockUpdated(pos, state, blockEntity.getBlockState(), 1 | 2);

        if (blockEntity.heatCapability.getHeat() >= blockEntity.heatCapability.getHeatCapacity())
        {
            if (++blockEntity.ticksInDanger >= 60)
            {
                level.setBlock(pos, Blocks.AIR.defaultBlockState(), 1 | 2);
                for (int[] ints : FORGE_OFFSETS)
                {
                    level.setBlock(pos.offset(ints[0], -1, ints[1]), Blocks.AIR.defaultBlockState(), 1 | 2);
                }

                level.explode(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 10, true, Level.ExplosionInteraction.TNT);
            }
        }
        else
        {
            blockEntity.ticksInDanger = Math.max(0, --blockEntity.ticksInDanger);
        }
    }

    private void checkStructureAndPopulate()
    {
        BlockPos pos = this.worldPosition;
        for (int[] ints : FORGE_OFFSETS)
        { //check for forge base 3x3
            BlockPos offset = pos.offset(ints[0], -1, ints[1]);
            if (AWRegistry.FORGE_BLOCK.get() != getLevel().getBlockState(offset).getBlock())
            {
                this.isStructureValid = false;
                return;
            }
        }
        this.isStructureValid = true;
        this.fluidHandlers.clear();
        this.parts.clear();
        for (int[] ints : FORGE_OFFSETS)
        { //scan the top of the forge for stuff
            if (ints[0] == 0 && ints[1] == 0)
                continue; //ignore the center - that's this object!

            BlockPos offset = pos.offset(ints[0], 0, ints[1]);
            BlockEntity entity = getLevel().getBlockEntity(offset);
            if (entity instanceof  IForgePart part && part.isTopPart()) {
                parts.add(part);
            }
        }

        for (int[] ints : FORGE_SIDES)
        {
            BlockPos offset = pos.offset(ints[0], -1, ints[1]);
            BlockEntity entity = getLevel().getBlockEntity(offset);
            if (entity instanceof  IForgePart part && !part.isTopPart()) {
                parts.add(part);
            }
            else
            {
                if (entity instanceof FluidVesselBlockEntity vessel)
                {
                    this.fluidHandlers.add(vessel.getCapability(ForgeCapabilities.FLUID_HANDLER).resolve().get());
                }
            }
        }
    }

    private void clientPopulateStructure() {
        BlockPos pos = this.worldPosition;
        this.fluidHandlers.clear();
        this.parts.clear();
        for (int[] ints : FORGE_OFFSETS)
        { //scan the top of the forge for stuff
            if (ints[0] == 0 && ints[1] == 0)
                continue; //ignore the center - that's this object!

            BlockPos offset = pos.offset(ints[0], 0, ints[1]);
            BlockEntity entity = getLevel().getBlockEntity(offset);
            if (entity instanceof  IForgePart part && part.isTopPart()) {
                parts.add(part);
            }
        }

        for (int[] ints : FORGE_SIDES)
        {
            BlockPos offset = pos.offset(ints[0], -1, ints[1]);
            BlockEntity entity = getLevel().getBlockEntity(offset);
            if (entity instanceof  IForgePart part && !part.isTopPart()) {
                parts.add(part);
            }
            else
            {
                if (entity instanceof FluidVesselBlockEntity vessel)
                {
                    this.fluidHandlers.add(vessel.getCapability(ForgeCapabilities.FLUID_HANDLER).resolve().get());
                }
            }
        }
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag nbt = super.getUpdateTag();
        emberCapability.writeToNBT(nbt);
        heatCapability.writeToNBT(nbt);
        nbt.putBoolean("valid", isStructureValid);
        return nbt;
    }
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        // Will get tag from #getUpdateTag
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        emberCapability.deserializeNBT(nbt);
        heatCapability.deserializeNBT(nbt);
        if (nbt.contains("valid"))
            isStructureValid = nbt.getBoolean("valid");
    }

    @Override
    public void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        emberCapability.writeToNBT(nbt);
        heatCapability.writeToNBT(nbt);
        if (nbt.contains("valid"))
            isStructureValid = nbt.getBoolean("valid");
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (!this.remove) {
            if (cap == EmbersCapabilities.EMBER_CAPABILITY) {
                return emberCapability.getCapability(cap, side);
            }
            else if (cap == AetherworksCapabilities.HEAT_CAPABILITY) {
                return heatCapability.getCapability(cap, side);
            }
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        emberCapability.invalidate();
        heatCapability.invalidate();
    }

    @Override
    public AABB getRenderBoundingBox() {
        return new AABB(this.getBlockPos().below(1).north(2).east(2),
                this.getBlockPos().south(2).west(2));
    }
}
