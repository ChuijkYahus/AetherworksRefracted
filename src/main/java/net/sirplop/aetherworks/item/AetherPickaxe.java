package net.sirplop.aetherworks.item;

import com.rekindled.embers.particle.GlowParticleOptions;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.sirplop.aetherworks.lib.AWHarvestHelper;
import net.sirplop.aetherworks.lib.AWHarvestNode;
import net.sirplop.aetherworks.util.AWConfig;
import net.sirplop.aetherworks.util.AetheriumTiers;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

public class AetherPickaxe  extends AOEEmberDiggerItem {
    public AetherPickaxe(Properties properties) {
        super(1, -2.8f, AetheriumTiers.AETHERIUM, BlockTags.MINEABLE_WITH_PICKAXE, properties);
    }

    private final GlowParticleOptions particle = new GlowParticleOptions(getParticleColor(), 1, 15);

    @Override
    public Vector3f getParticleColor() {
        return new Vector3f(0, 0.72F, 0.95F);
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        InteractionResult result = super.useOn(context);
        if (context.getLevel().isClientSide)
            return result;

        if (context.getPlayer() == null || context.getLevel().isClientSide()
                || !context.getLevel().getBlockState(context.getClickedPos()).getTags().anyMatch(blockTagKey -> blockTagKey == blocks)
                || AWConfig.getAetherPickaxeBanned().contains(context.getLevel().getBlockState(context.getClickedPos()).getBlock())
        )
            return result;
        if (result == InteractionResult.PASS && context.getHand() == InteractionHand.MAIN_HAND
                && context.getLevel().getBlockState(context.getClickedPos()).canHarvestBlock(context.getLevel(), context.getClickedPos(), context.getPlayer()))
        {
            if (AWHarvestHelper.addNode(context.getPlayer(),
                    new AWHarvestNode(context.getPlayer(), context.getLevel(), context.getClickedPos(),
                            AWConfig.AETHER_PICKAXE_RANGE.get(), p -> p.getMainHandItem().getItem() == this, particle)))
            {
                context.getPlayer().swing(context.getHand());
            }
        }

        return result;
    }
}
