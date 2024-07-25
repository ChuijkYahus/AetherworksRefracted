package net.sirplop.aetherworks.datagen;

import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.alchemy.Potion;
import net.sirplop.aetherworks.AWRegistry;
import net.sirplop.aetherworks.Aetherworks;
import net.sirplop.aetherworks.item.PotionGemItem;

public class AWItemProperties {

    public static void register() {
        //Copied from vanilla to mimic normal crossbow

        ItemProperties.register(AWRegistry.POTION_GEM.get(), new ResourceLocation(Aetherworks.MODID, "has_gem"), (stack, world, living, intIn) -> {
            if (stack.isEmpty())
                return 0;
            return PotionGemItem.getColor(stack) == PotionGemItem.DEFAULT_COLOR ? 0 : 1;
        });

        ItemProperties.register(AWRegistry.CROSSBOW_QUARTZ.get(), new ResourceLocation(Aetherworks.MODID, "pull"), (p_239427_0_, p_239427_1_, p_239427_2_, intIn) -> {
            if (p_239427_2_ == null) {
                return 0.0F;
            } else {
                return CrossbowItem.isCharged(p_239427_0_) ? 0.0F : (float)(p_239427_0_.getUseDuration() - p_239427_2_.getUseItemRemainingTicks()) / (float)CrossbowItem.getChargeDuration(p_239427_0_);
            }
        });
        ItemProperties.register(AWRegistry.CROSSBOW_QUARTZ.get(), new ResourceLocation(Aetherworks.MODID, "pulling"), (p_239426_0_, p_239426_1_, p_239426_2_, intIn) -> {
            return p_239426_2_ != null && p_239426_2_.isUsingItem() && p_239426_2_.getUseItem() == p_239426_0_ && !CrossbowItem.isCharged(p_239426_0_) ? 1.0F : 0.0F;
        });
        ItemProperties.register(AWRegistry.CROSSBOW_QUARTZ.get(), new ResourceLocation(Aetherworks.MODID, "charged"), (p_239425_0_, p_239425_1_, p_239425_2_, intIn) -> {
            return p_239425_2_ != null && CrossbowItem.isCharged(p_239425_0_) ? 1.0F : 0.0F;
        });
        //Copied from vanilla to mimic normal crossbow
        ItemProperties.register(AWRegistry.CROSSBOW_MAGMA.get(), new ResourceLocation(Aetherworks.MODID, "pull"), (p_239427_0_, p_239427_1_, p_239427_2_, intIn) -> {
            if (p_239427_2_ == null) {
                return 0.0F;
            } else {
                return CrossbowItem.isCharged(p_239427_0_) ? 0.0F : (float)(p_239427_0_.getUseDuration() - p_239427_2_.getUseItemRemainingTicks()) / (float)CrossbowItem.getChargeDuration(p_239427_0_);
            }
        });
        ItemProperties.register(AWRegistry.CROSSBOW_MAGMA.get(), new ResourceLocation(Aetherworks.MODID, "pulling"), (p_239426_0_, p_239426_1_, p_239426_2_, intIn) -> {
            return p_239426_2_ != null && p_239426_2_.isUsingItem() && p_239426_2_.getUseItem() == p_239426_0_ && !CrossbowItem.isCharged(p_239426_0_) ? 1.0F : 0.0F;
        });
        ItemProperties.register(AWRegistry.CROSSBOW_MAGMA.get(), new ResourceLocation(Aetherworks.MODID, "charged"), (p_239425_0_, p_239425_1_, p_239425_2_, intIn) -> {
            return p_239425_2_ != null && CrossbowItem.isCharged(p_239425_0_) ? 1.0F : 0.0F;
        });
    }
}
