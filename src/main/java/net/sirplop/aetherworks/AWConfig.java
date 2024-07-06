package net.sirplop.aetherworks;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
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
    public static ConfigValue<Integer> ENDER_AXE_RANGE;
    private static ConfigValue<List<? extends String>> ENDER_AXE_ALLOWED_CONFIG;
    public static ConfigValue<Integer> SKULK_AXE_MINE_RANGE;
    public static ConfigValue<Integer> SKULK_AXE_GROW_RANGE;
    private static ConfigValue<List<? extends String>> SKULK_AXE_ALLOWED_CONFIG;
    public static ConfigValue<Integer> SLIME_SHOVEL_RANGE;
    private static ConfigValue<List<? extends String>> SLIME_SHOVEL_BANNED_CONFIG;


    public static ConfigValue<Integer> AUGMENT_TUNING_CYLINDER_CHANCE;
    public static ConfigValue<Double> AETHER_CROWN_EFFECT_RADIUS;
    private static ConfigValue<List<? extends  String>> POTION_GEM_BANNED;

    private static Set<Block> AETHER_PICKAXE_BANNED;
    private static Set<Block> EMBER_PICKAXE_ALLOWED;
    private static Set<Block> ENDER_AXE_ALLOWED;
    private static Set<Block> SKULK_AXE_ALLOWED;
    private static Set<Block> SLIME_SHOVEL_ALLOWED;

    public enum Tool {
        AETHER_PICKAXE,
        EMBER_PICKAXE,
        ENDER_AXE,
        SCULK_AXE,
        SLIME_SHOVEL
    }

    public static Set<Block> getConfigSet(Tool tool) {
        switch (tool) {
            case AETHER_PICKAXE -> {
                if (AETHER_PICKAXE_BANNED == null) {
                    AETHER_PICKAXE_BANNED = mixedListToBlocks(AETHER_PICKAXE_BANNED_CONFIG);
                }
                return AETHER_PICKAXE_BANNED;
            }
            case EMBER_PICKAXE -> {
                if (EMBER_PICKAXE_ALLOWED == null) {
                    EMBER_PICKAXE_ALLOWED = mixedListToBlocks(EMBER_PICKAXE_ALLOWED_CONFIG);
                }
                return EMBER_PICKAXE_ALLOWED;
            }
            case ENDER_AXE -> {
                if (ENDER_AXE_ALLOWED == null) {
                    ENDER_AXE_ALLOWED = mixedListToBlocks(ENDER_AXE_ALLOWED_CONFIG);
                }
                return ENDER_AXE_ALLOWED;
            }
            case SCULK_AXE -> {
                if (SKULK_AXE_ALLOWED == null) {
                    SKULK_AXE_ALLOWED = mixedListToBlocks(SKULK_AXE_ALLOWED_CONFIG);
                }
                return SKULK_AXE_ALLOWED;
            }
            case SLIME_SHOVEL -> {
                if (SLIME_SHOVEL_ALLOWED == null) {
                    SLIME_SHOVEL_ALLOWED = mixedListToBlocks(SLIME_SHOVEL_BANNED_CONFIG);
                }
                return SLIME_SHOVEL_ALLOWED;
            }
        }
        return new HashSet<>();
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

    public static Map<MobEffect, MobEffect> getPotionGemReplacements() {
        Map<MobEffect, MobEffect> ret = new HashMap<>();
        for (String val : POTION_GEM_BANNED.get()) {
            String[] split = val.split("\\|");
            ResourceLocation left = new ResourceLocation(split[0]);
            ResourceLocation right = new ResourceLocation(split[1]);
            if (ForgeRegistries.MOB_EFFECTS.containsKey(left)) {
                if (ForgeRegistries.MOB_EFFECTS.containsKey(right)) {
                    ret.put(ForgeRegistries.MOB_EFFECTS.getValue(left), ForgeRegistries.MOB_EFFECTS.getValue(right));
                }
                else
                    Aetherworks.LOGGER.atError().log("Malformed potion effect: "+split[1]);
            } else
                Aetherworks.LOGGER.atError().log("Malformed potion effect: "+split[0]);
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

        AETHER_PICKAXE_RANGE = COMMON.comment("Maximum vein mine radius of the Pickaxe of the Boundless Sky. [default: 8]").defineInRange("pobs.range", 8, 0, 128);
        AETHER_PICKAXE_BANNED_CONFIG = COMMON.comment("Blocks that the Pickaxe of the Boundless Sky should NOT be able to vein mine.")
                .defineListAllowEmpty("pobs.banned", List.of(
                        "minecraft:bedrock",
                        "minecraft:reinforced_deepslate",
                        "aetherworks:forge_block"
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

        ENDER_AXE_RANGE = COMMON.comment("Maximum recursions the Axe of the Twisted Realm can do while chopping. [default: 512]").defineInRange("aotr.range", 512, 0, 2048);
        ENDER_AXE_ALLOWED_CONFIG = COMMON.comment("Blocks that the Axe of the Twisted Realm is allowed to vein mine. [defaults to logs]")
                .defineListAllowEmpty("aotr.allowed", List.of(
                        "#minecraft:logs"
                ), a -> true);

        SKULK_AXE_MINE_RANGE = COMMON.comment("Maximum vein mine radius of the Axe of the Sonorous Archives. [default: 12]").defineInRange("aosa.range", 12, 0, 128);
        SKULK_AXE_GROW_RANGE = COMMON.comment("Maximum growing radius of the Axe of the Sonorous Archives. [default: 24]").defineInRange("aosa.range", 24, 0, 128);
        SKULK_AXE_ALLOWED_CONFIG = COMMON.comment("Blocks that the Axe of the Sonorous Archives is allowed to vein mine. [defaults to leaves]")
                .defineListAllowEmpty("aosa.allowed", List.of(
                        "#minecraft:leaves"
                ), a -> true);

        SLIME_SHOVEL_RANGE = COMMON.comment("Maximum replacement radius of the Shovel of the Ineluctable Changes. [default: 16]").defineInRange("soic.range", 16, 0, 128);
        SLIME_SHOVEL_BANNED_CONFIG = COMMON.comment("Blocks that the Shovel of the Ineluctable Changes is NOT allowed to exchange.")
                .defineListAllowEmpty("soic.banned", List.of(
                        "minecraft:bedrock",
                        "minecraft:reinforced_deepslate",
                        "aetherworks:forge_block"
                ), a -> true);

        AETHER_CROWN_EFFECT_RADIUS = COMMON.comment("Cube radius of the area of effect the Aetherium Crown applies to. [default: 8.0")
                .define("crown.radius", 8.0);

        POTION_GEM_BANNED = COMMON.comment("Syntax is ( original|replacement ). Effects that should be replaced with another when put into a Vessel Gem.")
                .defineListAllowEmpty("crown.potion_banned", List.of(
                        "minecraft:instant_health|minecraft:regeneration",
                        "minecraft:instant_damage|minecraft:wither"
                ), AWConfig::validatePotions);
        AUGMENT_TUNING_CYLINDER_CHANCE = COMMON.comment("Base chance (1/X) per level to drop a geode when using the Tuning Cylinder augment. [default: 32]").define("tuning_cylinder.chance", 32);

        COMMON.pop();

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, COMMON.build());
    }
    private static boolean validateBlockNames(final Object obj)
    {
        return obj instanceof final String blockName && ForgeRegistries.BLOCKS.containsKey(new ResourceLocation(blockName));
    }

    private static boolean validatePotions(final Object obj)
    {
        if (obj instanceof final String potions) {
            String[] split = potions.split("\\|");
            ResourceLocation left = new ResourceLocation(split[0]);
            ResourceLocation right = new ResourceLocation(split[1]);
            return ForgeRegistries.MOB_EFFECTS.containsKey(left) && ForgeRegistries.MOB_EFFECTS.containsKey(right);
        }
        return false;
    }
}
