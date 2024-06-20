package net.sirplop.aetherworks.block;

import com.rekindled.embers.particle.GlowParticleOptions;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Vector3f;

public class AetherBlock extends Block {
    public AetherBlock(Properties pProperties) {
        super(pProperties);
    }
    public static final GlowParticleOptions STAR = new GlowParticleOptions(new Vector3f(0, 0.72F, 0.95F), 1.0F, 10);

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        for (int i = 0; i < 2; i++)
        {
            level.addParticle(STAR,
                    pos.getX()+(random.nextFloat() * 2f)-0.5f,
                    pos.getY()+(random.nextFloat() * 2f)-0.5f,
                    pos.getZ()+(random.nextFloat() * 2f)-0.5f,
                    (random.nextFloat()-0.5f)*0.3f,
                    (random.nextFloat())*0.3f,
                    (random.nextFloat()-0.5f)*0.3f);
        }
    }
}
