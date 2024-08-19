package net.sirplop.aetherworks.block.forge;

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
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.sirplop.aetherworks.AWRegistry;
import net.sirplop.aetherworks.blockentity.ForgeCoreBlockEntity;
import org.jetbrains.annotations.Nullable;

public class ForgeCoreBlock extends BaseEntityBlock {
    public ForgeCoreBlock(Properties pProperties) {
        super(pProperties);
    }
    protected static final VoxelShape AABB = Shapes.or(
            Block.box(1,0,1,15,1,15),
            Block.box(2,1,2,14,2,14),
            Block.box(3,2,3,13,16,13));

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return AWRegistry.FORGE_CORE_BLOCK_ENTITY.get().create(pPos, pState);
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
}
