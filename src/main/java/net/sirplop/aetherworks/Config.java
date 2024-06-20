package net.sirplop.aetherworks;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Forge's config APIs
@Mod.EventBusSubscriber(modid = Aetherworks.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config
{
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    /*
    private static final ForgeConfigSpec.BooleanValue LOG_DIRT_BLOCK = BUILDER
            .comment("Whether to log the dirt block on common setup")
            .define("logDirtBlock", true);

    private static final ForgeConfigSpec.IntValue MAGIC_NUMBER = BUILDER
            .comment("A magic number")
            .defineInRange("magicNumber", 42, 0, Integer.MAX_VALUE);

    public static final ForgeConfigSpec.ConfigValue<String> MAGIC_NUMBER_INTRODUCTION = BUILDER
            .comment("What you want the introduction message to be for the magic number")
            .define("magicNumberIntroduction", "The magic number is... ");
*/
    private static final ForgeConfigSpec.IntValue AETHERIC_STRENGTH = BUILDER
            .comment("How strong is the self-repair effect of aetherium tools? Set to -1 to disable. [default: 4.0]")
            .defineInRange("tool_ember", 2, -1, 40);
    private static final ForgeConfigSpec.DoubleValue TOOL_EMBER_USE = BUILDER
            .comment("Ember used when using the AOE mode on aetheric tools. [default: 4.0]")
            .defineInRange("tool_ember", 4.0, 0, Integer.MAX_VALUE);
    private static final ForgeConfigSpec.IntValue AETHER_PICKAXE_RANGE = BUILDER
            .comment("Maximum vein mine radius of the Pickaxe of the Boundless Sky. [default: 8]")
            .defineInRange("pobs_range", 8, 0, Integer.MAX_VALUE);
    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> AETHER_PICKAXE_BANNED = BUILDER
            .comment("Blocks that the Pickaxe of the Boundless Sky should NOT be able to vein mine.")
            .defineListAllowEmpty("pobs_banned", List.of(
                    "minecraft:bedrock",
                    "minecraft:reinforced_deepslate"
            ), Config::validateBlockNames);
    private static final ForgeConfigSpec.IntValue EMBER_PICKAXE_CHANCE = BUILDER
            .comment("Chance (1/X) to drop a geode when mining using the Pickaxe of the Molten Depths. [default: 16]")
            .defineInRange("pomd_chance", 16, 1, Integer.MAX_VALUE);

    static final ForgeConfigSpec SPEC = BUILDER.build();

    public static int aethericRepairStrength;
    public static double aetherToolEmberUse;
    public static int aetherPickRange;
    public static Set<Block> aetherPickBannedBlocks;
    public static int emberPickChance;

    private static boolean validateItemName(final Object obj)
    {
        return obj instanceof final String itemName && ForgeRegistries.ITEMS.containsKey(new ResourceLocation(itemName));
    }
    private static boolean validateBlockNames(final Object obj)
    {
        return obj instanceof final String blockName && ForgeRegistries.BLOCKS.containsKey(new ResourceLocation(blockName));
    }

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {
        aethericRepairStrength = AETHERIC_STRENGTH.get();
        aetherToolEmberUse = TOOL_EMBER_USE.get();
        aetherPickRange = AETHER_PICKAXE_RANGE.get();
        aetherPickBannedBlocks = AETHER_PICKAXE_BANNED.get().stream()
                .map(blockName -> ForgeRegistries.BLOCKS.getValue(new ResourceLocation(blockName)))
                .collect(Collectors.toSet());
        emberPickChance = EMBER_PICKAXE_CHANCE.get();
    }
}
