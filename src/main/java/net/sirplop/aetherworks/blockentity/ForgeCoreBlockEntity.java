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
import net.sirplop.aetherworks.capabilities.AWCapabilities;
import net.sirplop.aetherworks.api.capabilities.IHeatCapability;
import net.sirplop.aetherworks.datagen.AWSounds;
import net.sirplop.aetherworks.power.DefaultHeatCapability;

import java.util.ArrayList;
import java.util.List;

public class ForgeCoreBlockEntity extends BlockEntity {
    public ForgeCoreBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(AWRegistry.FORGE_CORE_BLOCK_ENTITY.get(), pPos, pBlockState);
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


    @Override
    public AABB getRenderBoundingBox() {
        return new AABB(this.getBlockPos().below(1).north(2).east(2),
                this.getBlockPos().south(2).west(2));
    }
}
