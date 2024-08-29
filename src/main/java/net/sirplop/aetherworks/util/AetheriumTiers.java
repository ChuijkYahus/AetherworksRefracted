package net.sirplop.aetherworks.util;

import com.rekindled.embers.util.EmbersTiers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.ForgeTier;
import net.minecraftforge.common.TierSortingRegistry;
import net.sirplop.aetherworks.Aetherworks;
import net.sirplop.aetherworks.datagen.AWBlockTags;
import net.sirplop.aetherworks.datagen.AWItemTags;

import java.util.List;

public class AetheriumTiers {
    public static final Tier AETHERIUM = new ForgeTier(
            4, 3841, 9.5f, 2f, 18,
            AWBlockTags.NEEDS_AETHERIUM_TOOL, () -> Ingredient.of(AWItemTags.AETHERIUM_INGOT));

    static {
        TierSortingRegistry.registerTier(AETHERIUM, new ResourceLocation(Aetherworks.MODID, "aetherium_tool"),
                List.of(Tiers.DIAMOND, EmbersTiers.CLOCKWORK_PICK, EmbersTiers.CLOCKWORK_AXE), List.of());

    }
}
