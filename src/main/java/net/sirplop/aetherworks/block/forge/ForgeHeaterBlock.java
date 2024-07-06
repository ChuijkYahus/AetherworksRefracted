package net.sirplop.aetherworks.block.forge;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluids;
import net.sirplop.aetherworks.AWRegistry;
import net.sirplop.aetherworks.api.block.HorizontalWaterloggableEntityBlock;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ForgeHeaterBlock extends HorizontalWaterloggableEntityBlock {

    public ForgeHeaterBlock(Properties pProperties) { super(pProperties); }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return AWRegistry.FORGE_HEATER_BLOCK_ENTITY.get().create(blockPos, blockState);
    }
    @Nullable
    @Override
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext context) {
        BlockState state = super.getStateForPlacement(context);
        Direction dir = context.getHorizontalDirection().getOpposite();
        return state == null ? null : state.setValue(FACING, dir).setValue(BlockStateProperties.WATERLOGGED,
                context.getLevel().getFluidState(context.getClickedPos()).getType() == Fluids.WATER);
    }
}
