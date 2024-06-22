package net.sirplop.aetherworks.item;

import com.rekindled.embers.particle.GlowParticleOptions;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.sirplop.aetherworks.lib.AWHarvestHelper;
import net.sirplop.aetherworks.lib.AWTunnelNode;
import net.sirplop.aetherworks.util.AWConfig;
import net.sirplop.aetherworks.util.AetheriumTiers;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

public class EmberPickaxe  extends AOEEmberDiggerItem{
    public EmberPickaxe(Properties properties) {
        super(1, -2.8f, AetheriumTiers.AETHERIUM, BlockTags.MINEABLE_WITH_PICKAXE, properties);
    }

    private final GlowParticleOptions particle = new GlowParticleOptions(getParticleColor(), 1, 15);

    private final static Vector3f particleColor = GlowParticleOptions.EMBER_COLOR;
    @Override
    public Vector3f getParticleColor() {
        return particleColor;
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        InteractionResult result = super.useOn(context);
        if (context.getLevel().isClientSide || AWConfig.getEmberPickaxeAllowed().isEmpty())
            return result;

        if (context.getPlayer() == null
                || !AWConfig.getEmberPickaxeAllowed().contains(context.getLevel().getBlockState(context.getClickedPos()).getBlock())
        )
            return result;
        if (result == InteractionResult.PASS && context.getHand() == InteractionHand.MAIN_HAND
                && context.getLevel().getBlockState(context.getClickedPos()).canHarvestBlock(context.getLevel(), context.getClickedPos(), context.getPlayer()))
        {
            HitResult pick = context.getPlayer().pick(20D, 0.0F, false);

            // Hit something that wasn't a block.
            if (!(pick instanceof BlockHitResult blockHitResult)) {
                return result;
            }
            if (AWHarvestHelper.addNode(context.getPlayer(),
                    new AWTunnelNode(context.getPlayer(), context.getLevel(), context.getClickedPos(),
                            AWConfig.EMBER_PICKAXE_RANGE.get() * 3, p -> p.getMainHandItem().getItem() == this,
                            particle, blockHitResult.getDirection().getOpposite(), (state) -> AWConfig.getEmberPickaxeAllowed().contains(state.getBlock()))))
            {
                context.getPlayer().swing(context.getHand());
            }
        }

        return result;
    }
}
