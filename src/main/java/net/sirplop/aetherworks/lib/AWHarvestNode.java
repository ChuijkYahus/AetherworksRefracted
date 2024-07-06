package net.sirplop.aetherworks.lib;

import com.rekindled.embers.particle.GlowParticleOptions;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.sirplop.aetherworks.util.Utils;

import javax.annotation.Nullable;
import java.util.Stack;
import java.util.function.Predicate;

public class AWHarvestNode {
    public final Player harvester;
    public final Level level;
    public final BlockPos beginning;
    public final Predicate<Player> canHarvest;
    public final int range;
    public final Stack<BlockPos> toHarvest = new Stack<>();
    protected BlockState baseState;
    protected final GlowParticleOptions particle;
    protected final ItemStack usedItem;
    protected double damageChance;

    public boolean isInvalid()
    {
        return invalid;
    }

    protected boolean invalid;

    public boolean isLoaded(BlockPos pos)
    {
        return level.isLoaded(pos);
    }
    public double distanceToSqr(BlockPos pos)
    {
        return beginning.distToCenterSqr(pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f);
    }


    public AWHarvestNode(Player harvester, Level level, BlockPos beginning,
                         int range, Predicate<Player> canHarvest, @Nullable GlowParticleOptions particle, double damageChance)
    {
        this.harvester = harvester;
        this.level = level;
        this.beginning = beginning;
        this.canHarvest = canHarvest;
        this.range = range;
        this.particle = particle;
        this.usedItem = harvester.getMainHandItem();
        this.damageChance = damageChance;
    }
    public void initNode()
    {
        if (!isLoaded(this.beginning))
        {
            this.invalid = true;
            return;
        }

        this.toHarvest.add(this.beginning);
        this.baseState = this.level.getBlockState(this.beginning);
        this.traverseRecursive(this.beginning);

        this.toHarvest.sort((BlockPos l, BlockPos r) -> (int) (distanceToSqr(r) - distanceToSqr(l)));
        if (this.toHarvest.isEmpty())
        {
            this.invalid = true;
        }
    }

    public void traverseRecursive(BlockPos from)
    {
        if (from.distToCenterSqr(this.beginning.getX() + 0.5, this.beginning.getY() + 0.5, this.beginning.getZ() + 0.5) >= (range * range)-1)
        {
            return;
        }

        for (Direction facing : Direction.values())
        {
            BlockPos offset = from.relative(facing);
            if (this.toHarvest.contains(offset))
            {
                continue;
            }

            if (!this.isLoaded(offset))
            {
                continue;
            }

            BlockState state = this.level.getBlockState(offset);
            if (state.getBlock().equals(this.baseState.getBlock()))
            {
                this.toHarvest.add(0, offset);
                this.traverseRecursive(offset);
            }
        }
    }
    public void tick()
    {
        if (!canHarvest.test(harvester) || !Utils.hasEnoughDurability(harvester.getMainHandItem(), 1)) {
            this.invalid = true;
            return;
        }
        BlockPos pos = this.toHarvest.pop();
        boolean val = Utils.breakAndHarvestBlock((ServerLevel)level, pos, (ServerPlayer)harvester, harvester.getMainHandItem(),
                Direction.getRandom(level.random), (state) -> state.getBlock().equals(baseState.getBlock()), false, true);
        if (val) {
            if (!harvester.isCreative() && level.random.nextFloat() <= damageChance)
                harvester.getMainHandItem().hurt(1, level.random, (ServerPlayer) harvester);
            if (particle != null) {
                ((ServerLevel)level).sendParticles(particle,
                        pos.getX() + 0.5f,
                        pos.getY() + 0.5f,
                        pos.getZ() + 0.5f,
                        10, 0.25f, 0.25f, 0.25f, 0.25f);
            }
        }
        if (this.toHarvest.isEmpty())
            this.invalid = true;
    }
}
