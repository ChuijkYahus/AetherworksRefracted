package net.sirplop.aetherworks.blockentity;

import com.rekindled.embers.api.capabilities.EmbersCapabilities;
import com.rekindled.embers.api.power.IEmberCapability;
import com.rekindled.embers.power.DefaultEmberCapability;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.FluidHandlerBlockEntity;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;

public class HeaterBaseBlockEntity extends FluidHandlerBlockEntity implements IForgePart {
    public HeaterBaseBlockEntity(BlockEntityType<?> pType, BlockPos pos, BlockState state,
                                 int heatPerOperation, int fluidPerOperation, @NotNull Fluid fluid, @NotNull TagKey<Block> belowBlocksAllowed,
                                 @NotNull TagKey<Fluid> belowFluidsAllowed, boolean consumeBelowBlock, double emberPerOperation,
                                 boolean instantHeat, double cooldown, SoundEvent sound) {
        super(pType, pos, state);
        this.heatPerOperation = heatPerOperation;
        this.fluidPerOperation = fluidPerOperation;
        this.tankFluid = fluid;
        this.belowBlocksAllowed = belowBlocksAllowed;
        this.belowFluidsAllowed = belowFluidsAllowed;
        this.consumesBelowBlock = consumeBelowBlock;
        this.emberPerOperation = emberPerOperation;
        this.instantHeat = instantHeat;
        this.sound = sound;
        this.cooldownSet =  cooldown;

        this.emberCapability.setEmberCapacity(1000);
        this.emberCapability.setEmber(0);
    }

    public final double emberPerOperation;
    public final int heatPerOperation;
    public final boolean consumesBelowBlock;
    public final TagKey<Block> belowBlocksAllowed;
    public final TagKey<Fluid> belowFluidsAllowed;
    public final int fluidPerOperation;
    public final Fluid tankFluid;
    public final boolean instantHeat;
    public final SoundEvent sound;
    public final double cooldownSet;

    protected double cooldown;

    protected Block currentBelowBlock;
    protected Fluid currentBelowFluid;
    protected boolean currentPasses;

    public IEmberCapability emberCapability = new DefaultEmberCapability() {
        @Override
        public void onContentsChanged() {
            super.onContentsChanged();
            HeaterBaseBlockEntity.this.setChanged();
        }
    };

    @Override
    public boolean isTopPart() {
        return false;
    }

    @Override
    public boolean isInvalid() {
        return (level != null ? level.getBlockEntity(getBlockPos()) : null) == null;
    }

    @Override
    public void onForgeTick(IForge forge) {
        if (getLevel().isClientSide())
            return;

        --this.cooldown;

        if (cooldown <= 0 && level.hasNeighborSignal(getBlockPos())) {
            Block belowBlock = level.getBlockState(getBlockPos().below()).getBlock();
            Fluid belowFluid = level.getFluidState(getBlockPos().below()).getType();

            if (belowBlock != currentBelowBlock || belowFluid != currentBelowFluid) {
                currentBelowFluid = belowFluid;
                currentBelowBlock = belowBlock;
                currentPasses = belowBlock.defaultBlockState().getTags().anyMatch((pred) -> pred == belowBlocksAllowed)
                        || belowFluid.defaultFluidState().getTags().anyMatch((pred) -> pred == belowFluidsAllowed);
            }

            if (currentPasses) {
                FluidStack tankReq = new FluidStack(tankFluid, fluidPerOperation);

                if (tank.drain(tankReq, IFluidHandler.FluidAction.SIMULATE).getAmount() >= 0 &&
                    emberCapability.removeAmount(emberPerOperation, false) == emberPerOperation)
                {
                    if (consumesBelowBlock)
                        level.setBlock(getBlockPos().below(), Blocks.AIR.defaultBlockState(), 1 | 2);

                    this.tank.drain(tankReq, IFluidHandler.FluidAction.EXECUTE);
                    this.emberCapability.removeAmount(emberPerOperation, true);
                    forge.transferHeat(heatPerOperation, instantHeat);
                    cooldown = cooldownSet;

                    if (sound != null) {
                        level.playSound(null, getBlockPos(), sound, SoundSource.BLOCKS, 0.1f, 0.25f + level.random.nextFloat() * 0.5f);
                    }

                    if (forge.getHeatCapability().getHeat() < 0)
                    {
                        forge.getHeatCapability().setHeat(0);
                    }
                }
            }
        }
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag nbt = super.getUpdateTag();
        emberCapability.writeToNBT(nbt);
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
    }

    @Override
    public void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        emberCapability.writeToNBT(nbt);
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (!this.remove) {
            if (cap == EmbersCapabilities.EMBER_CAPABILITY) {
                return emberCapability.getCapability(cap, side);
            }
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        emberCapability.invalidate();
    }
}
