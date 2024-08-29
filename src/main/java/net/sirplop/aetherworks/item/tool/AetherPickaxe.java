package net.sirplop.aetherworks.item.tool;

import com.rekindled.embers.particle.GlowParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.sirplop.aetherworks.lib.AWHarvestHelper;
import net.sirplop.aetherworks.lib.AWHarvestNode;
import net.sirplop.aetherworks.AWConfig;
import net.sirplop.aetherworks.util.AetheriumTiers;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

public class AetherPickaxe  extends AOEEmberDiggerItem {
    public AetherPickaxe(Properties properties) {
        super(1.5f, -2.8f, AetheriumTiers.AETHERIUM, BlockTags.MINEABLE_WITH_PICKAXE, properties);
    }

    private final GlowParticleOptions particle = new GlowParticleOptions(getParticleColor(), 1, 15);

    @Override
    public Vector3f getParticleColor() {
        return new Vector3f(0, 0.72F, 0.95F);
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        InteractionResult result = super.useOn(context);
        if (!(context.getLevel() instanceof ServerLevel) || context.getLevel().isClientSide)
            return result;

        if (context.getPlayer() == null || context.getLevel().isClientSide()
                || !context.getLevel().getBlockState(context.getClickedPos()).getTags().anyMatch(blockTagKey -> blockTagKey == blocks)
                || AWConfig.getConfigSet(AWConfig.Tool.AETHER_PICKAXE).contains(context.getLevel().getBlockState(context.getClickedPos()).getBlock())
        )
            return result;
        if (result == InteractionResult.PASS && context.getHand() == InteractionHand.MAIN_HAND
                && context.getLevel().getBlockState(context.getClickedPos()).canHarvestBlock(context.getLevel(), context.getClickedPos(), context.getPlayer()))
        {
            if (AWHarvestHelper.addNode(context.getPlayer(),
                    new AWHarvestNode(context.getPlayer(), context.getLevel(), context.getClickedPos(),
                            AWConfig.AETHER_PICKAXE_RANGE.get(), p -> p.getMainHandItem().getItem() == this, particle, 0.75)))
            {
                context.getPlayer().swing(context.getHand(), true);
                return InteractionResult.SUCCESS;
            }
        }

        return result;
    }

    @Override
    public boolean canPerformAction(ItemStack stack, net.minecraftforge.common.ToolAction toolAction) {
        return net.minecraftforge.common.ToolActions.DEFAULT_PICKAXE_ACTIONS.contains(toolAction);
    }
}
