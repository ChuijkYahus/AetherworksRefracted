package net.sirplop.aetherworks.block;

import com.rekindled.embers.particle.GlowParticleOptions;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.Vec3;
import net.sirplop.aetherworks.AWRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

public class MoonlightAmplifier extends HorizontalDirectionalBlock {
    public MoonlightAmplifier(Properties pProperties) {
        super(pProperties);
        registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }
    public static final GlowParticleOptions GLOW_SKY = new GlowParticleOptions(new Vector3f(0, 0.72F, 0.95F), 1f, 80);
    public static final GlowParticleOptions GLOW_BEAM = new GlowParticleOptions(new Vector3f(0, 0.72F, 0.95F), new Vec3(0, -0.1f, 0),2.5f, 30);

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext context) {
        Direction dir = context.getHorizontalDirection().getOpposite();
        return defaultBlockState().setValue(FACING, dir);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (!level.canSeeSky(pos.above()) || level.getDayTime() < 15000 || level.getDayTime() > 21000)
            return;
        Direction dir = state.getValue(FACING);
        if (!level.getBlockState(pos.relative(dir, 3)).is(AWRegistry.PRISM.get()))
            return;
        float rX = pos.getX() + 0.5F;
        float rZ = pos.getZ() + 0.5F;

        for (float i = 0; i < 8; ++i)
        {
            float sX = (random.nextFloat() - random.nextFloat()) * 4;
            float sZ = (random.nextFloat() - random.nextFloat()) * 4;
            float sY = random.nextFloat() * 3;
            level.addParticle(GLOW_SKY,
                    rX + sX,
                    pos.getY() + 1 + random.nextFloat() * 3,
                    rZ + sZ,
                    -sX / 5,
                    -sY / 5,
                    -sZ / 5);

        }
        for (float i = 0; i < 25; ++i)
        {
            level.addParticle(GLOW_BEAM,
                    rX + dir.getStepX() * (i / 8),
                    pos.getY() + 0.5F,
                    rZ + dir.getStepZ() * (i / 8),
                    0, -0.1f, 0);
        }

        super.animateTick(state, level, pos, random);
    }
}
