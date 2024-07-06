package net.sirplop.aetherworks.block.forge;

import com.rekindled.embers.RegistryManager;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.sirplop.aetherworks.AWEvents;
import net.sirplop.aetherworks.lib.OctDirection;
import net.sirplop.aetherworks.lib.OctFacingHorizontalProperty;
import org.jetbrains.annotations.NotNull;

public class ForgeStructureBlock extends Block {
    public ForgeStructureBlock(Properties pProperties) {
        super(pProperties);
        registerDefaultState(this.stateDefinition.any().setValue(OctFacingHorizontalProperty.OCT_DIRECTIONS, OctDirection.FRONT));;

    }
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(OctFacingHorizontalProperty.OCT_DIRECTIONS);
    }
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext context) {
        BlockState state = super.getStateForPlacement(context);
        return state == null ? null : state.setValue(OctFacingHorizontalProperty.OCT_DIRECTIONS, OctDirection.FRONT);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (state == newState)
            return;
        int[] offsets = state.getValue(OctFacingHorizontalProperty.OCT_DIRECTIONS).opposite().getOffset();
        BlockPos center = pos.offset(offsets[0], 0, offsets[1]);

        boolean structValid = true;
        for (int[] ints : AWEvents.FORGE_OFFSETS)
        {
            BlockPos at = center.offset(ints[0], 0, ints[1]);
            if (!level.getBlockState(at).is(state.getBlock()))
            {
                structValid = false;
                break;
            }
        };
        if (!structValid) {
            BlockState dawnstoneBlock = RegistryManager.DAWNSTONE_BLOCK.get().defaultBlockState();
            for (int[] ints : AWEvents.FORGE_OFFSETS)
            {
                BlockPos at = center.offset(ints[0], 0, ints[1]);
                if (level.getBlockState(at).is(state.getBlock()))
                {
                    level.setBlock(at, dawnstoneBlock, 1 | 2);
                }
            }
        }

        super.onRemove(state, level, pos, newState, movedByPiston);
    }
}
