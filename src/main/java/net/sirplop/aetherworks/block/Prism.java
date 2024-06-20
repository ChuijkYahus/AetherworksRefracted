package net.sirplop.aetherworks.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.sirplop.aetherworks.AWRegistry;
import net.sirplop.aetherworks.blockentity.PrismBlockEntity;
import org.jetbrains.annotations.Nullable;

public class Prism extends BaseEntityBlock {
    public Prism(Properties pProperties) {
        super(pProperties);
    }

    protected static final VoxelShape AABB = Shapes.join(Shapes.block(), Block.box(2,2,2,14,16,14), BooleanOp.ONLY_FIRST);

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return AWRegistry.PRISM_BLOCK_ENTITY.get().create(pPos, pState);
    }
    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            super.onRemove(state, level, pos, newState, isMoving);
        }
    }
    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return AABB;
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return pLevel.isClientSide ? createTickerHelper(pBlockEntityType, AWRegistry.PRISM_BLOCK_ENTITY.get(), PrismBlockEntity::clientTick)
                : createTickerHelper(pBlockEntityType, AWRegistry.PRISM_BLOCK_ENTITY.get(), PrismBlockEntity::serverTick);
    }
}
