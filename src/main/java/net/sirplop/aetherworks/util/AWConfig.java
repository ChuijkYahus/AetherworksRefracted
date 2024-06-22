package net.sirplop.aetherworks.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.tags.ITag;

import java.util.*;

public class AWConfig {

    public static ConfigValue<Integer> AETHERIC_STRENGTH;
    public static ConfigValue<Double> TOOL_EMBER_USE;
    public static ConfigValue<Integer> AETHER_PICKAXE_RANGE;
    private static ConfigValue<List<? extends String>> AETHER_PICKAXE_BANNED_CONFIG;
    public static ConfigValue<Integer> EMBER_PICKAXE_RANGE;
    private static ConfigValue<List<? extends String>> EMBER_PICKAXE_ALLOWED_CONFIG;
    //public static ConfigValue<Double> AUGMENT_TUNING_CYLINDER_COST;
    public static ConfigValue<Integer> AUGMENT_TUNING_CYLINDER_CHANCE;

    private static Set<Block> AETHER_PICKAXE_BANNED;
    private static Set<Block> EMBER_PICKAXE_ALLOWED;

    public static Set<Block> getAetherPickaxeBanned() {
        if (AETHER_PICKAXE_BANNED == null) {
            AETHER_PICKAXE_BANNED = mixedListToBlocks(AETHER_PICKAXE_BANNED_CONFIG);
        }
        return AETHER_PICKAXE_BANNED;
    }

    public static Set<Block> getEmberPickaxeAllowed() {

        if (EMBER_PICKAXE_ALLOWED == null) {
            EMBER_PICKAXE_ALLOWED = mixedListToBlocks(EMBER_PICKAXE_ALLOWED_CONFIG);
        }
        return EMBER_PICKAXE_ALLOWED;
    }

    public static Set<Block> mixedListToBlocks(ConfigValue<List<? extends String>> list) {
        Set<Block> ret = new HashSet<>();

        for (String val : list.get()) {
            if (val.charAt(0) == '#') { //it's a tag, get it!
                ResourceLocation location = new ResourceLocation(val.substring(1));
                ITag<Block> tag = getTagFrom(location);
                if (tag != null) {
                    for (Block block : tag) {
                        ret.add(block);
                    }
                }
            } else {
                ResourceLocation location = new ResourceLocation(val);
                if (ForgeRegistries.BLOCKS.containsKey(location)) {
                    ret.add(ForgeRegistries.BLOCKS.getValue(location));
                }
            }
        }
        return ret;
    }

    private static ITag<Block> getTagFrom(ResourceLocation location) {
        TagKey<Block> blockKey = BlockTags.create(location);
        if (Objects.requireNonNull(ForgeRegistries.BLOCKS.tags()).isKnownTagName(blockKey)) {
            return ForgeRegistries.BLOCKS.tags().getTag(blockKey);
        }
        return null;
    }

    public static void register() {
        //registerClientConfigs();
        registerCommonConfigs();
        //registerServerConfigs();
    }

    private static void registerCommonConfigs() {
        ForgeConfigSpec.Builder COMMON = new ForgeConfigSpec.Builder();

        COMMON.comment("Settings for tool parameters").push("tool");

        AETHERIC_STRENGTH = COMMON.comment("How strong is the self-repair effect of aetherium tools? Set to -1 to disable. [default: 2]").defineInRange("aetheric_strength", 2, -1, 40);
        TOOL_EMBER_USE = COMMON.comment("Ember used when using the AOE mode on aetheric tools. [default: 4.0]").define("ember_use", 4.0);

        AETHER_PICKAXE_RANGE = COMMON.comment("Maximum vein mine radius of the Pickaxe of the Boundless Sky. [default: 8]").defineInRange("pobs.range", 8, 0, Integer.MAX_VALUE);
        AETHER_PICKAXE_BANNED_CONFIG = COMMON.comment("Blocks that the Pickaxe of the Boundless Sky should NOT be able to vein mine.")
                .defineListAllowEmpty("pobs.banned", List.of(
                        "minecraft:bedrock",
                        "minecraft:reinforced_deepslate"
                ), AWConfig::validateBlockNames);

        EMBER_PICKAXE_RANGE = COMMON.comment("Maximum number of bore holes the Pickaxe of the Molten Depths makes when tunneling. [default: 8]").defineInRange("pomd.range", 8, 0, 128);
        EMBER_PICKAXE_ALLOWED_CONFIG = COMMON.comment("Blocks that the Pickaxe of the Molten Depths can vein mine.")
                .defineListAllowEmpty("pomd.allowed", List.of(
                        "#minecraft:base_stone_overworld",
                        "minecraft:dripstone_block",
                        "minecraft:calcite",
                        "#forge:sandstone",
                        "#minecraft:base_stone_nether",
                        "#minecraft:nylium",
                        "minecraft:smooth_basalt",
                        "minecraft:basalt",
                        "minecraft:end_stone"
                ), a -> true);

        //AUGMENT_TUNING_CYLINDER_COST = COMMON.comment("Ember cost to trigger the Tuning Cylinder augment. [default: 5.0]").define("augment.tuning_cylinder.cost", 5.0);
        AUGMENT_TUNING_CYLINDER_CHANCE = COMMON.comment("Base chance (1/X) per level to drop a geode when using the Tuning Cylinder augment. [default: 32]").define("tuning_cylinder.chance", 32);

        COMMON.pop();

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, COMMON.build());

        //AETHER_PICKAXE_BANNED = mixedListToBlocks(AETHER_PICKAXE_BANNED_CONFIG);
        //EMBER_PICKAXE_ALLOWED = mixedListToBlocks(EMBER_PICKAXE_ALLOWED_CONFIG);
    }
    private static boolean validateBlockNames(final Object obj)
    {
        return obj instanceof final String blockName && ForgeRegistries.BLOCKS.containsKey(new ResourceLocation(blockName));
    }
}
