package net.sirplop.aetherworks.api;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.PositionalRandomFactory;

/**
 * A random source that just returns the seed every time. Good for forcing events.
 * Do not fork it.
 */
public class ForceRandom implements RandomSource {

    public long returnValue;

    public ForceRandom(long value) {
        returnValue = value;
    }

    @Override
    public RandomSource fork() {
        return null;
    }

    @Override
    public PositionalRandomFactory forkPositional() {
        return null;
    }

    @Override
    public void setSeed(long pSeed) {
        returnValue = pSeed;
    }

    @Override
    public int nextInt() {
        return (int)returnValue;
    }

    @Override
    public int nextInt(int pBound) {
        return (int)returnValue;
    }

    @Override
    public long nextLong() {
        return (int)returnValue;
    }

    @Override
    public boolean nextBoolean() {
        return returnValue != 0;
    }

    @Override
    public float nextFloat() {
        return returnValue;
    }

    @Override
    public double nextDouble() {
        return returnValue;
    }

    @Override
    public double nextGaussian() {
        return returnValue;
    }
}
