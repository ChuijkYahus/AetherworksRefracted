package net.sirplop.aetherworks.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.sirplop.aetherworks.AWRegistry;

import java.util.ArrayList;

public class ForgeCoolerBlockEntity  extends HeaterBaseBlockEntity{
    public ForgeCoolerBlockEntity(BlockPos pos, BlockState state) {
        super(AWRegistry.FORGE_COOLER_BLOCK_ENTITY.get(), pos, state, -500, 1000,
                Fluids.WATER, new ArrayList<>() {{ add(Blocks.ICE);  add(Blocks.PACKED_ICE); add(Blocks.BLUE_ICE);}}, null,
                true, 500, true, 1200, SoundEvents.LAVA_EXTINGUISH);
    }
}
