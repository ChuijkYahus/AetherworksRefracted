package net.sirplop.aetherworks.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.sirplop.aetherworks.AWRegistry;

import java.util.ArrayList;

public class ForgeHeaterBlockEntity extends HeaterBaseBlockEntity {

    public ForgeHeaterBlockEntity(BlockPos pos, BlockState state) {
        super(AWRegistry.FORGE_HEATER_BLOCK_ENTITY.get(), pos, state, 1, 1,
                Fluids.WATER, null, new ArrayList<>() {{ add(Fluids.LAVA); }},
                false, 0.25, false, 0, null);
    }
}
