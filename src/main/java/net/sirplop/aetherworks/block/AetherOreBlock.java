package net.sirplop.aetherworks.block;

import com.rekindled.embers.particle.GlowParticleOptions;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Vector3f;

public class AetherOreBlock extends DropExperienceBlock {
    public AetherOreBlock(Properties pProperties, IntProvider pXpRange) {
        super(pProperties, pXpRange);
    }
    public static final GlowParticleOptions GLOW = new GlowParticleOptions(new Vector3f(0, 0.72F, 0.95F), 0.75F, 20);

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        for(Direction direction : Direction.values()) {
            BlockPos blockpos = pos.relative(direction);
            if (!level.getBlockState(blockpos).isSolidRender(level, blockpos)) {
                int rnd = random.nextInt(3);
                for (int i = 0; i < rnd; i++) {
                    Direction.Axis direction$axis = direction.getAxis();
                    double d1 = direction$axis == Direction.Axis.X ? 0.5D + 0.5625D * (double) direction.getStepX() : random.nextFloat();
                    double d2 = direction$axis == Direction.Axis.Y ? 0.5D + 0.5625D * (double) direction.getStepY() : random.nextFloat();
                    double d3 = direction$axis == Direction.Axis.Z ? 0.5D + 0.5625D * (double) direction.getStepZ() : random.nextFloat();
                    level.addParticle(GLOW,
                            (double) pos.getX() + d1,
                            (double) pos.getY() + d2,
                            (double) pos.getZ() + d3,
                            (random.nextFloat() - 0.5f) * 0.3f,
                            (random.nextFloat()) * 0.3f,
                            (random.nextFloat() - 0.5f) * 0.3f);
                }
            }
        }
    }

}
