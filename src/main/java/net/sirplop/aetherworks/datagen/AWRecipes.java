package net.sirplop.aetherworks.datagen;

import com.rekindled.embers.RegistryManager;
import com.rekindled.embers.datagen.EmbersFluidTags;
import com.rekindled.embers.datagen.EmbersItemTags;
import com.rekindled.embers.datagen.EmbersRecipes;
import com.rekindled.embers.recipe.*;
import com.rekindled.embers.util.ConsumerWrapperBuilder;
import com.rekindled.embers.util.FluidAmounts;
import com.rekindled.embers.util.Misc;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import net.minecraftforge.common.crafting.conditions.NotCondition;
import net.minecraftforge.common.crafting.conditions.TagEmptyCondition;
import net.minecraftforge.fluids.FluidStack;
import net.sirplop.aetherworks.AWRegistry;
import net.sirplop.aetherworks.Aetherworks;
import net.sirplop.aetherworks.compat.curios.CuriosCompat;
import net.sirplop.aetherworks.item.PotionGemItem;
import net.sirplop.aetherworks.recipe.*;
import net.sirplop.aetherworks.util.Utils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class AWRecipes extends RecipeProvider implements IConditionBuilder {


    public static String metalFormerFolder = "metal_former";
    public static String anvilFolder = "aetherium_anvil";
    public static String toolStationFolder = "tool_station";

    public AWRecipes(PackOutput pOutput) {
        super(pOutput);
    }

    @Override
    protected void buildRecipes(@NotNull Consumer<FinishedRecipe> consumer) {
        //dawnstone anvil
        AnvilAugmentRecipeBuilder.create(AWRegistry.TUNING_CYLINDER_AUGMENT).folder(EmbersRecipes.anvilFolder).tool(HeatIngredient.of(Ingredient.of(EmbersItemTags.AUGMENTABLE_TOOLS))).input(AWRegistry.TUNING_CYLINDER.get()).save(consumer);

        //metal former
        MetalFormerRecipeBuilder.create(AWRegistry.INGOT_AETHER.get()).domain(Aetherworks.MODID).folder(metalFormerFolder).temperature(2100).craftTime(200).fluid(new FluidStack(AWRegistry.AETHERIUM_GAS.FLUID.get(), FluidAmounts.INGOT_AMOUNT)).input(Ingredient.of(RegistryManager.DAWNSTONE_INGOT.get())).save(consumer);
        MetalFormerRecipeBuilder.create(AWRegistry.GEM_AETHER.get()).domain(Aetherworks.MODID).folder(metalFormerFolder).temperature(2600).craftTime(300).fluid(new FluidStack(AWRegistry.AETHERIUM_GAS.FLUID.get(), FluidAmounts.INGOT_AMOUNT * 4)).input(Ingredient.of(Items.DIAMOND)).save(consumer);
        MetalFormerRecipeBuilder.create(AWRegistry.GEM_AETHER.get()).id(Aetherworks.MODID, "gem_aether_alt").folder(metalFormerFolder).temperature(2800).craftTime(300).fluid(new FluidStack(AWRegistry.AETHERIUM_GAS.FLUID.get(), FluidAmounts.INGOT_AMOUNT * 4)).input(Ingredient.of(Items.EMERALD)).save(consumer);
        MetalFormerRecipeBuilder.create(AWRegistry.PLATE_AETHER.get()).domain(Aetherworks.MODID).folder(metalFormerFolder).temperature(2400).craftTime(250).fluid(new FluidStack(AWRegistry.AETHERIUM_GAS.FLUID.get(), FluidAmounts.INGOT_AMOUNT * 2)).input(Ingredient.of(RegistryManager.DAWNSTONE_PLATE.get())).save(consumer);

        ItemStack moonfireGemStack = new ItemStack(AWRegistry.POTION_GEM.get());
        PotionGemItem.setEffectsForRecipe(List.of(new MobEffectInstance(AWRegistry.EFFECT_MOONFIRE.get(), 200, 0, true, true)), moonfireGemStack, Utils.AETHERIUM_PROJECTILE_COLOR.getRGB());
        MetalFormerRecipeBuilder.create(moonfireGemStack).id(Aetherworks.MODID, "moongaze_gem").folder(metalFormerFolder).temperature(2400).craftTime(600).fluid(new FluidStack(AWRegistry.AETHERIUM_GAS.FLUID.get(), FluidAmounts.BLOCK_AMOUNT)).input(Ingredient.of(AWRegistry.POTION_GEM.get())).mustMatchExactly().save(consumer);

        MetalFormerRecipeBuilder.create(AWRegistry.GLASS_AETHERIUM.get().asItem()).domain(Aetherworks.MODID).folder(metalFormerFolder).temperature(2550).craftTime(30).fluid(new FluidStack(AWRegistry.AETHERIUM_GAS.FLUID.get(), FluidAmounts.NUGGET_AMOUNT)).input(Ingredient.of(Blocks.GLASS)).save(consumer);

        //aetherium anvil
        AetheriumAnvilRecipeBuilder.create(AWRegistry.TOOL_ROD_CRUDE.get()).id(AWRegistry.TOOL_ROD.getId()).domain(Aetherworks.MODID).folder(anvilFolder).temperatureRange(2100, 3000).difficulty(2).hitInfo(15, 50).result(AWRegistry.TOOL_ROD.get(), 1).save(consumer);
        AetheriumAnvilRecipeBuilder.create(AWRegistry.PICKAXE_HEAD_CRUDE.get()).id(AWRegistry.PICKAXE_HEAD.getId()).domain(Aetherworks.MODID).folder(anvilFolder).temperatureRange(2400, 2900).difficulty(4).hitInfo(25, 60).result(AWRegistry.PICKAXE_HEAD.get(), 1).save(consumer);
        AetheriumAnvilRecipeBuilder.create(AWRegistry.AXE_HEAD_CRUDE.get()).id(AWRegistry.AXE_HEAD.getId()).domain(Aetherworks.MODID).folder(anvilFolder).temperatureRange(2400, 2900).difficulty(4).hitInfo(25, 60).result(AWRegistry.AXE_HEAD.get(), 1).save(consumer);
        AetheriumAnvilRecipeBuilder.create(AWRegistry.SHOVEL_HEAD_CRUDE.get()).id(AWRegistry.SHOVEL_HEAD.getId()).domain(Aetherworks.MODID).folder(anvilFolder).temperatureRange(2400, 2900).difficulty(4).hitInfo(25, 60).result(AWRegistry.SHOVEL_HEAD.get(), 1).save(consumer);
        AetheriumAnvilRecipeBuilder.create(AWRegistry.CROSSBOW_FRAME_CRUDE.get()).id(AWRegistry.CROSSBOW_FRAME.getId()).domain(Aetherworks.MODID).folder(anvilFolder).temperatureRange(2600, 3000).difficulty(5).hitInfo(35, 70).result(AWRegistry.CROSSBOW_FRAME.get(), 1).save(consumer);
        AetheriumAnvilRecipeBuilder.create(AWRegistry.CROSSBOW_LIMBS_CRUDE.get()).id(AWRegistry.CROSSBOW_LIMBS.getId()).domain(Aetherworks.MODID).folder(anvilFolder).temperatureRange(2600, 3000).difficulty(5).hitInfo(35, 70).result(AWRegistry.CROSSBOW_LIMBS.get(), 1).save(consumer);
        AetheriumAnvilRecipeBuilder.create(AWRegistry.AETHER_CROWN_CRUDE.get()).id(AWRegistry.AETHER_CROWN_MUNDANE.getId()).domain(Aetherworks.MODID).folder(anvilFolder).temperatureRange(2500, 2900).difficulty(6).hitInfo(30, 80).result(AWRegistry.AETHER_CROWN_MUNDANE.get(), 1).save(consumer);

        AetheriumAnvilRecipeBuilder.create(AWRegistry.GEODE_BASIC.get()).domain(Aetherworks.MODID).folder(anvilFolder).temperatureRange(700, 2100).difficulty(1).hitInfo(1, 10)
                .result(Items.STONE, 10)
                .result(Items.CALCITE, 10)
                .result(Items.TUFF, 10)
                .result(Items.ANDESITE, 10)
                .result(Items.DIORITE, 10)
                .result(Items.GRANITE, 10)
                .result(AWRegistry.SUEVITE.get(), 10)
                .result(AWRegistry.AETHER_SHARD.get(), 5)
                .result(Items.COAL, 5)
                .result(Items.AMETHYST_SHARD, 5)
                .result(Items.RAW_IRON, 5)
                .result(Items.RAW_COPPER, 5)
                .result(Items.RAW_GOLD, 5)
                .result(RegistryManager.RAW_LEAD.get(), 5)
                .result(RegistryManager.RAW_SILVER.get(), 5)
                .result(Items.MOSS_BLOCK, 5)
                .result(Items.EMERALD, 0.5)
                .result(Items.DIAMOND, 0.5)
                .result(Items.BUDDING_AMETHYST, 0.25)
                .save(consumer);
        AetheriumAnvilRecipeBuilder.create(AWRegistry.GEODE_DEEP.get()).domain(Aetherworks.MODID).folder(anvilFolder).temperatureRange(700, 2100).difficulty(1).hitInfo(1, 10)
                .result(RegistryManager.EMBER_CRYSTAL.get(), 10)
                .result(RegistryManager.EMBER_SHARD.get(), 10)
                .result(RegistryManager.EMBER_GRIT.get(), 10)
                .result(Items.DEEPSLATE, 10)
                .result(Items.SCULK, 7.5)
                .result(Items.DRIPSTONE_BLOCK, 7.5)
                .result(AWRegistry.AETHER_SHARD.get(), 5)
                .result(new ItemStack(Items.LAPIS_LAZULI, 4), 5)
                .result(Items.POINTED_DRIPSTONE, 5)
                .result(new ItemStack(Items.REDSTONE, 4), 5)
                .result(Items.RAW_IRON, 5)
                .result(Items.RAW_COPPER, 5)
                .result(Items.RAW_GOLD, 5)
                .result(RegistryManager.RAW_LEAD.get(), 5)
                .result(RegistryManager.RAW_SILVER.get(), 5)
                .result(Items.DIAMOND, 1)
                .result(Items.ECHO_SHARD, 0.25)
                .save(consumer);
        AetheriumAnvilRecipeBuilder.create(AWRegistry.GEODE_HOT.get()).domain(Aetherworks.MODID).folder(anvilFolder).temperatureRange(700, 2100).difficulty(1).hitInfo(1, 10)
                .result(RegistryManager.RAW_LEAD.get(), 10)
                .result(RegistryManager.RAW_SILVER.get(), 10)
                .result(Items.SAND, 10)
                .result(Items.RED_SAND, 10)
                .result(Items.CLAY, 10)
                .result(Items.GRANITE, 10)
                .result(new ItemStack(Items.REDSTONE, 4), 7.5)
                .result(AWRegistry.AETHER_SHARD.get(), 5)
                .result(Items.RAW_GOLD_BLOCK, 5)
                .result(AWRegistry.AETHERIUM_SHARD_BLOCK.get(), 1)
                .save(consumer);
        AetheriumAnvilRecipeBuilder.create(AWRegistry.GEODE_COLD.get()).domain(Aetherworks.MODID).folder(anvilFolder).temperatureRange(700, 2100).difficulty(1).hitInfo(1, 10)
                .result(Items.RAW_GOLD, 10)
                .result(RegistryManager.RAW_SILVER.get(), 10)
                .result(Items.ICE, 10)
                .result(Items.PACKED_ICE, 10)
                .result(Items.SNOW_BLOCK, 10)
                .result(Items.DIORITE, 10)
                .result(AWRegistry.AETHER_SHARD.get(), 5)
                .result(Items.RAW_IRON_BLOCK, 5)
                .result(Items.BLUE_ICE, 5)
                .result(Items.EMERALD, 3)
                .result(AWRegistry.AETHERIUM_SHARD_BLOCK.get(), 1)
                .save(consumer);
        AetheriumAnvilRecipeBuilder.create(AWRegistry.GEODE_OCEAN.get()).domain(Aetherworks.MODID).folder(anvilFolder).temperatureRange(700, 2100).difficulty(1).hitInfo(1, 10)
                .result(Items.PRISMARINE_SHARD, 10)
                .result(Items.PRISMARINE_CRYSTALS, 10)
                .result(Items.RAW_IRON, 10)
                .result(Items.RAW_COPPER, 10)
                .result(Items.CLAY, 10)
                .result(Items.ANDESITE, 10)
                .result(Items.MUD, 10)
                .result(AWRegistry.AETHER_SHARD.get(), 5)
                .result(RegistryManager.RAW_LEAD_BLOCK.get(), 5)
                .result(Items.PRISMARINE, 5)
                .result(Items.SEA_LANTERN, 5)
                .result(Items.WET_SPONGE, 5)
                .result(Items.NAUTILUS_SHELL, 5)
                .result(Items.HEART_OF_THE_SEA, 1)
                .result(Items.SNIFFER_EGG, 1)
                .result(AWRegistry.AETHERIUM_SHARD_BLOCK.get(), 1)
                .save(consumer);
        AetheriumAnvilRecipeBuilder.create(AWRegistry.GEODE_MAGIC.get()).domain(Aetherworks.MODID).folder(anvilFolder).temperatureRange(700, 2100).difficulty(1).hitInfo(1, 10)
                .result(RegistryManager.RAW_LEAD.get(), 10)
                .result(Items.RAW_IRON, 10)
                .result(AWRegistry.AETHER_SHARD.get(), 5)
                .result(new ItemStack(AWRegistry.AETHER_SHARD.get(), 2), 2.5)
                .result(new ItemStack(AWRegistry.AETHER_SHARD.get(), 3), 2.5)
                .result(RegistryManager.RAW_SILVER_BLOCK.get(), 5)
                .result(new ItemStack(RegistryManager.DAWNSTONE_NUGGET.get(), 2), 5)
                .result(new ItemStack(RegistryManager.DAWNSTONE_NUGGET.get(), 3), 5)
                .result(new ItemStack(RegistryManager.DAWNSTONE_NUGGET.get(), 4), 5)
                .result(AWRegistry.AETHERIUM_SHARD_BLOCK.get(), 1)
                .save(consumer);
        AetheriumAnvilRecipeBuilder.create(AWRegistry.GEODE_NETHER.get()).domain(Aetherworks.MODID).folder(anvilFolder).temperatureRange(700, 2100).difficulty(1).hitInfo(1, 10)
                .result(Items.NETHERRACK, 10)
                .result(Items.BASALT, 10)
                .result(Items.BLACKSTONE, 10)
                .result(new ItemStack(Items.GOLD_NUGGET, 4), 2.5)
                .result(new ItemStack(Items.GOLD_NUGGET, 3), 2.5)
                .result(new ItemStack(Items.GOLD_NUGGET, 2), 2.5)
                .result(Items.GOLD_NUGGET, 2.5)
                .result(new ItemStack(Items.QUARTZ, 3), 2.5)
                .result(new ItemStack(Items.QUARTZ, 2), 5)
                .result(Items.QUARTZ, 2.5)
                .result(new ItemStack(Items.GLOWSTONE_DUST, 2), 5)
                .result(Items.GLOWSTONE_DUST, 5)
                .result(Items.BLAZE_POWDER, 10)
                .result(Items.SOUL_SOIL, 5)
                .result(Items.SOUL_SAND, 5)
                .result(Items.GILDED_BLACKSTONE, 5)
                .result(Items.MAGMA_BLOCK, 5)
                .result(Items.OBSIDIAN, 5)
                .result(Items.GLOWSTONE, 5)
                .result(Items.ANCIENT_DEBRIS, 0.33)
                .save(consumer);
        AetheriumAnvilRecipeBuilder.create(AWRegistry.GEODE_END.get()).domain(Aetherworks.MODID).folder(anvilFolder).temperatureRange(700, 2100).difficulty(1).hitInfo(1, 10)
                .result(Items.END_STONE, 10)
                .result(Items.OBSIDIAN, 10)
                .result(Items.CHORUS_FRUIT, 10)
                .result(Items.ENDER_PEARL, 10)
                .result(Items.CRYING_OBSIDIAN, 5)
                .result(Items.SHULKER_SHELL, 5)
                .result(Items.CHORUS_FLOWER, 2.5)
                .save(consumer);

        //tool forge
        ToolStationRecipeBuilder.create(AWRegistry.PICKAXE_AETHER.get()).domain(Aetherworks.MODID).folder(toolStationFolder).temperature(2800).temperatureRate(10).input(null, AWRegistry.PICKAXE_HEAD_AETHER.get(), AWRegistry.AETHER_PEARL.get(), AWRegistry.TOOL_ROD_INFUSED.get(), null).save(consumer);
        ToolStationRecipeBuilder.create(AWRegistry.PICKAXE_EMBER.get()).domain(Aetherworks.MODID).folder(toolStationFolder).temperature(2800).temperatureRate(10).input(null, AWRegistry.PICKAXE_HEAD_EMBER.get(), AWRegistry.AETHER_PEARL.get(), AWRegistry.TOOL_ROD_INFUSED.get(), null).save(consumer);
        ToolStationRecipeBuilder.create(AWRegistry.AXE_ENDER.get()).domain(Aetherworks.MODID).folder(toolStationFolder).temperature(2800).temperatureRate(10).input(null, AWRegistry.AXE_HEAD_ENDER.get(), AWRegistry.AETHER_PEARL.get(), AWRegistry.TOOL_ROD_INFUSED.get(), null).save(consumer);
        ToolStationRecipeBuilder.create(AWRegistry.AXE_SCULK.get()).domain(Aetherworks.MODID).folder(toolStationFolder).temperature(2800).temperatureRate(10).input(null, AWRegistry.AXE_HEAD_SCULK.get(), AWRegistry.AETHER_PEARL.get(), AWRegistry.TOOL_ROD_INFUSED.get(), null).save(consumer);
        ToolStationRecipeBuilder.create(AWRegistry.SHOVEL_PRISMARINE.get()).domain(Aetherworks.MODID).folder(toolStationFolder).temperature(2800).temperatureRate(10).input(null, AWRegistry.SHOVEL_HEAD_PRISMARINE.get(), AWRegistry.AETHER_PEARL.get(), AWRegistry.TOOL_ROD_INFUSED.get(), null).save(consumer);
        ToolStationRecipeBuilder.create(AWRegistry.SHOVEL_SLIME.get()).domain(Aetherworks.MODID).folder(toolStationFolder).temperature(2800).temperatureRate(10).input(null, AWRegistry.SHOVEL_HEAD_SLIME.get(), AWRegistry.AETHER_PEARL.get(), AWRegistry.TOOL_ROD_INFUSED.get(), null).save(consumer);
        ToolStationRecipeBuilder.create(AWRegistry.CROSSBOW_QUARTZ.get()).domain(Aetherworks.MODID).folder(toolStationFolder).temperature(2800).temperatureRate(10).input(AWRegistry.CROSSBOW_LIMBS_QUARTZ.get(), RegistryManager.DAWNSTONE_PLATE.get(), AWRegistry.AETHER_PEARL.get(), Items.LEVER, AWRegistry.CROSSBOW_FRAME_INFUSED.get()).save(consumer);
        ToolStationRecipeBuilder.create(AWRegistry.CROSSBOW_MAGMA.get()).domain(Aetherworks.MODID).folder(toolStationFolder).temperature(2800).temperatureRate(10).input(AWRegistry.CROSSBOW_LIMBS_MAGMA.get(), RegistryManager.DAWNSTONE_PLATE.get(), AWRegistry.AETHER_PEARL.get(), Items.LEVER, AWRegistry.CROSSBOW_FRAME_INFUSED.get()).save(consumer);

        ToolStationRecipeBuilder.create(AWRegistry.AETHER_EMBER_JAR.get()).domain(Aetherworks.MODID).folder(toolStationFolder).temperature(2500).temperatureRate(15).input(AWRegistry.AETHER_SHARD.get(), RegistryManager.DAWNSTONE_PLATE.get(), RegistryManager.EMBER_JAR.get(), RegistryManager.DAWNSTONE_PLATE.get(), AWRegistry.AETHER_SHARD.get()).save(consumer);
        ToolStationRecipeBuilder.create(AWRegistry.AETHER_EMBER_CARTRIDGE.get()).domain(Aetherworks.MODID).folder(toolStationFolder).temperature(2500).temperatureRate(15).input(AWRegistry.AETHER_SHARD.get(), RegistryManager.DAWNSTONE_PLATE.get(), RegistryManager.EMBER_CARTRIDGE.get(), RegistryManager.DAWNSTONE_PLATE.get(), AWRegistry.AETHER_SHARD.get()).save(consumer);
        ToolStationRecipeBuilder.create(CuriosCompat.AETHER_EMBER_BULB.get()).domain(Aetherworks.MODID).folder(toolStationFolder).temperature(2500).temperatureRate(15).input(AWRegistry.AETHER_SHARD.get(), RegistryManager.DAWNSTONE_PLATE.get(), com.rekindled.embers.compat.curios.CuriosCompat.EMBER_BULB.get(), RegistryManager.DAWNSTONE_PLATE.get(), AWRegistry.AETHER_SHARD.get()).save(consumer);

        //stamping
        StampingRecipeBuilder.create(AWRegistry.AETHER_SHARD.get()).domain(Aetherworks.MODID).folder(EmbersRecipes.stampingFolder).stamp(RegistryManager.FLAT_STAMP.get()).fluid(AWRegistry.AETHERIUM_GAS_IMPURE.FLUID.get(), FluidAmounts.NUGGET_AMOUNT).save(ConsumerWrapperBuilder.wrap().build(consumer));
        StampingRecipeBuilder.create(AWRegistry.FOCUS_CRYSTAL.get()).domain(Aetherworks.MODID).folder(EmbersRecipes.stampingFolder).stamp(RegistryManager.FLAT_STAMP.get()).input(AWRegistry.GEM_AETHER.get()).fluid(AWRegistry.ALCHEMIC_PRECURSOR.FLUID.get(), FluidAmounts.INGOT_AMOUNT).save(ConsumerWrapperBuilder.wrap().build(consumer));
        StampingRecipeBuilder.create(AWRegistry.AETHER_ASPECTUS.get()).domain(Aetherworks.MODID).folder(EmbersRecipes.stampingFolder).stamp(RegistryManager.INGOT_STAMP.get()).input(RegistryManager.EMBER_SHARD.get()).fluid(AWRegistry.AETHERIUM_GAS.FLUID.get(), FluidAmounts.INGOT_AMOUNT).save(ConsumerWrapperBuilder.wrap().build(consumer));

        //mixing
        MixingRecipeBuilder.create(new FluidStack(AWRegistry.ALCHEMIC_PRECURSOR.FLUID.get(), FluidAmounts.NUGGET_AMOUNT * 10)).domain(Aetherworks.MODID).folder(EmbersRecipes.mixingFolder).input(FluidIngredient.of(EmbersFluidTags.MOLTEN_GOLD, FluidAmounts.NUGGET_AMOUNT * 4)).input(FluidIngredient.of(EmbersFluidTags.MOLTEN_LEAD, FluidAmounts.NUGGET_AMOUNT * 4)).input(FluidIngredient.of(RegistryManager.DWARVEN_OIL.FLUID.get(), FluidAmounts.NUGGET_AMOUNT * 2)).save(ConsumerWrapperBuilder.wrap().build(consumer));
        MixingRecipeBuilder.create(new FluidStack(AWRegistry.AETHERIUM_GAS.FLUID.get(), FluidAmounts.INGOT_AMOUNT)).domain(Aetherworks.MODID).folder(EmbersRecipes.mixingFolder).input(FluidIngredient.of(AWRegistry.AETHERIUM_GAS_IMPURE.FLUID.get(), FluidAmounts.RAW_AMOUNT)).input(FluidIngredient.of(AWRegistry.ALCHEMIC_PRECURSOR.FLUID.get(), FluidAmounts.INGOT_AMOUNT)).save(ConsumerWrapperBuilder.wrap().build(consumer));

        //melting
        MeltingRecipeBuilder.create(AWRegistry.AETHER_SHARD.get()).domain(Aetherworks.MODID).folder(EmbersRecipes.meltingFolder).output(new FluidStack(AWRegistry.AETHERIUM_GAS_IMPURE.FLUID.get(), FluidAmounts.NUGGET_AMOUNT)).save(ConsumerWrapperBuilder.wrap().build(consumer));

        //special recipes
        GenericRecipeBuilder.create(new PotionGemSocketRecipe(new ResourceLocation(Aetherworks.MODID, "crown_socket"))).save(consumer);
        GenericRecipeBuilder.create(new PotionGemUnsocketRecipe(new ResourceLocation(Aetherworks.MODID, "crown_unsocket"))).save(consumer);
        GenericRecipeBuilder.create(new PotionGemImbueRecipe(new ResourceLocation(Aetherworks.MODID, "potion_gem_imbue"))).save(consumer);
        GenericRecipeBuilder.create(new DrainRecipe(new ResourceLocation(Aetherworks.MODID, "drain_prismarine_shovel"))).save(consumer);

        //alchemy
        AlchemyRecipeBuilder.create(AWRegistry.AETHER_AMALGAM.get()).tablet(RegistryManager.EMBER_CRYSTAL_CLUSTER.get()).folder(EmbersRecipes.alchemyFolder)
                .inputs(AWRegistry.AETHER_SHARD.get(), AWRegistry.AETHER_SHARD.get(), AWRegistry.AETHER_SHARD.get(), AWRegistry.AETHER_SHARD.get(), AWRegistry.AETHER_SHARD.get())
                .aspects(EmbersItemTags.COPPER_ASPECTUS, EmbersItemTags.SILVER_ASPECTUS).save(consumer);
        AlchemyRecipeBuilder.create(AWRegistry.AETHER_PEARL.get()).tablet(AWRegistry.GEM_AETHER.get()).folder(EmbersRecipes.alchemyFolder)
                .inputs(AWRegistry.AETHER_AMALGAM.get(), AWRegistry.GEM_AETHER.get(), AWRegistry.AETHER_AMALGAM.get(), AWRegistry.AETHER_AMALGAM.get(), AWRegistry.GEM_AETHER.get())
                .aspects(EmbersItemTags.SILVER_ASPECTUS, AWItemTags.AETHERIUM_ASPECTUS, EmbersItemTags.DAWNSTONE_ASPECTUS).save(consumer);

        AlchemyRecipeBuilder.create(AWRegistry.TOOL_ROD_INFUSED.get()).tablet(AWRegistry.TOOL_ROD.get()).folder(EmbersRecipes.alchemyFolder)
                .inputs(RegistryManager.EMBER_GRIT.get(), RegistryManager.ASH.get(), RegistryManager.EMBER_GRIT.get(), RegistryManager.ASH.get())
                .aspects(EmbersItemTags.COPPER_ASPECTUS, AWItemTags.AETHERIUM_ASPECTUS).save(consumer);
        AlchemyRecipeBuilder.create(AWRegistry.CROSSBOW_FRAME_INFUSED.get()).tablet(AWRegistry.CROSSBOW_FRAME.get()).folder(EmbersRecipes.alchemyFolder)
                .inputs(AWRegistry.SUEVITE.get(), AWRegistry.SUEVITE.get(), AWRegistry.SUEVITE.get(), RegistryManager.FOCAL_LENS.get(), AWRegistry.SUEVITE.get())
                .aspects(EmbersItemTags.DAWNSTONE_ASPECTUS, AWItemTags.AETHERIUM_ASPECTUS).save(consumer);

        AlchemyRecipeBuilder.create(AWRegistry.PICKAXE_HEAD_AETHER.get()).tablet(AWRegistry.PICKAXE_HEAD.get()).folder(EmbersRecipes.alchemyFolder)
                .inputs(AWRegistry.AETHER_AMALGAM.get(), AWRegistry.AETHER_AMALGAM.get(), AWRegistry.AETHER_SHARD.get(), AWRegistry.AETHER_SHARD.get())
                .aspects(EmbersItemTags.SILVER_ASPECTUS, AWItemTags.AETHERIUM_ASPECTUS, EmbersItemTags.COPPER_ASPECTUS).save(consumer);
        AlchemyRecipeBuilder.create(AWRegistry.PICKAXE_HEAD_EMBER.get()).tablet(AWRegistry.PICKAXE_HEAD.get()).folder(EmbersRecipes.alchemyFolder)
                .inputs(RegistryManager.EMBER_CRYSTAL_CLUSTER.get(), RegistryManager.EMBER_CRYSTAL_CLUSTER.get(), RegistryManager.EMBER_CRYSTAL.get(), RegistryManager.EMBER_CRYSTAL.get())
                .aspects(EmbersItemTags.SILVER_ASPECTUS, AWItemTags.AETHERIUM_ASPECTUS, EmbersItemTags.COPPER_ASPECTUS).save(consumer);
        AlchemyRecipeBuilder.create(AWRegistry.AXE_HEAD_ENDER.get()).tablet(AWRegistry.AXE_HEAD.get()).folder(EmbersRecipes.alchemyFolder)
                .inputs(Ingredient.of(Tags.Items.OBSIDIAN), Ingredient.of(Tags.Items.OBSIDIAN))
                .inputs(Items.ENDER_EYE, Items.ENDER_EYE)
                .aspects(EmbersItemTags.SILVER_ASPECTUS, EmbersItemTags.LEAD_ASPECTUS, AWItemTags.AETHERIUM_ASPECTUS).save(consumer);
        AlchemyRecipeBuilder.create(AWRegistry.AXE_HEAD_SCULK.get()).tablet(AWRegistry.AXE_HEAD.get()).folder(EmbersRecipes.alchemyFolder)
                .inputs(Items.SCULK_SENSOR, Items.SCULK_SENSOR, Items.SCULK, Items.SCULK)
                .aspects(EmbersItemTags.SILVER_ASPECTUS, EmbersItemTags.LEAD_ASPECTUS, AWItemTags.AETHERIUM_ASPECTUS).save(consumer);
        AlchemyRecipeBuilder.create(AWRegistry.SHOVEL_HEAD_SLIME.get()).tablet(AWRegistry.SHOVEL_HEAD.get()).folder(EmbersRecipes.alchemyFolder)
                .inputs(Items.SLIME_BLOCK, Items.SLIME_BLOCK, Items.MAGMA_CREAM, Items.MAGMA_CREAM)
                .aspects(EmbersItemTags.SILVER_ASPECTUS, EmbersItemTags.IRON_ASPECTUS, AWItemTags.AETHERIUM_ASPECTUS).save(consumer);
        AlchemyRecipeBuilder.create(AWRegistry.SHOVEL_HEAD_PRISMARINE.get()).tablet(AWRegistry.SHOVEL_HEAD.get()).folder(EmbersRecipes.alchemyFolder)
                .inputs(Items.PRISMARINE_SHARD, Items.PRISMARINE_SHARD, Items.PRISMARINE_CRYSTALS, Items.PRISMARINE_CRYSTALS)
                .aspects(EmbersItemTags.SILVER_ASPECTUS, EmbersItemTags.IRON_ASPECTUS, AWItemTags.AETHERIUM_ASPECTUS).save(consumer);
        AlchemyRecipeBuilder.create(AWRegistry.CROSSBOW_LIMBS_MAGMA.get()).tablet(AWRegistry.CROSSBOW_LIMBS.get()).folder(EmbersRecipes.alchemyFolder)
                .inputs(Ingredient.of(Tags.Items.NETHERRACK), Ingredient.of(Tags.Items.NETHERRACK))
                .inputs(Items.MAGMA_BLOCK, Items.MAGMA_BLOCK)
                .aspects(EmbersItemTags.SILVER_ASPECTUS, EmbersItemTags.DAWNSTONE_ASPECTUS, AWItemTags.AETHERIUM_ASPECTUS).save(consumer);
        AlchemyRecipeBuilder.create(AWRegistry.CROSSBOW_LIMBS_QUARTZ.get()).tablet(AWRegistry.CROSSBOW_LIMBS.get()).folder(EmbersRecipes.alchemyFolder)
                .inputs(Ingredient.of(Tags.Items.GEMS_QUARTZ), Ingredient.of(Tags.Items.GEMS_QUARTZ))
                .inputs(Items.QUARTZ_BLOCK, Items.QUARTZ_BLOCK)
                .aspects(EmbersItemTags.SILVER_ASPECTUS, EmbersItemTags.DAWNSTONE_ASPECTUS, AWItemTags.AETHERIUM_ASPECTUS).save(consumer);
        AlchemyRecipeBuilder.create(AWRegistry.AETHER_CROWN.get()).tablet(AWRegistry.AETHER_CROWN_MUNDANE.get()).folder(EmbersRecipes.alchemyFolder)
                .inputs(AWRegistry.AETHER_AMALGAM.get(), AWRegistry.AETHER_AMALGAM.get(), AWRegistry.AETHER_PEARL.get(), RegistryManager.ELDRITCH_INSIGNIA.get(), AWRegistry.AETHER_AMALGAM.get())
                .aspects(EmbersItemTags.SILVER_ASPECTUS, EmbersItemTags.IRON_ASPECTUS, AWItemTags.AETHERIUM_ASPECTUS, EmbersItemTags.DAWNSTONE_ASPECTUS).save(consumer);

        //deco
        decoRecipes(AWRegistry.SUEVITE_COBBLE_DECO, consumer);
        decoRecipes(AWRegistry.SUEVITE_BRICKS_DECO, consumer);

        //normal smelting
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(AWRegistry.SUEVITE_COBBLE.get()), RecipeCategory.MISC, AWRegistry.SUEVITE.get(), 0.1F, 200)
                .unlockedBy("has_suevite_cobble", has(AWRegistry.SUEVITE_COBBLE.get())).save(consumer, getResource("smooth_suevite"));

        //shaped crafting
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, AWRegistry.SUEVITE_BRICKS.get(), 4)
                .pattern("XX")
                .pattern("XX")
                .define('X', AWRegistry.SUEVITE.get())
                .unlockedBy("has_suevite", has(AWRegistry.SUEVITE.get()))
                .save(consumer, getResource("suevite_bricks"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, AWRegistry.AETHERIUM_LENS.get())
                .pattern(" P ")
                .pattern("PSP")
                .pattern(" P ")
                .define('P', RegistryManager.DAWNSTONE_PLATE.get())
                .define('S', AWRegistry.AETHER_AMALGAM.get())
                .unlockedBy("has_aether_shard", has(AWRegistry.AETHER_SHARD.get()))
                .save(consumer, getResource("aetherium_lens"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, AWRegistry.TOOL_ROD_CRUDE.get())
                .pattern("  X")
                .pattern(" X ")
                .pattern("X  ")
                .define('X', AWRegistry.INGOT_AETHER.get())
                .unlockedBy("has_aether_ingot", has(AWRegistry.INGOT_AETHER.get()))
                .save(consumer, getResource("tool_rod_crude"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, AWRegistry.PICKAXE_HEAD_CRUDE.get())
                .pattern("XGX")
                .define('X', AWRegistry.INGOT_AETHER.get())
                .define('G', AWRegistry.GEM_AETHER.get())
                .unlockedBy("has_aether_ingot", has(AWRegistry.INGOT_AETHER.get()))
                .save(consumer, getResource("pick_head_crude"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, AWRegistry.AXE_HEAD_CRUDE.get())
                .pattern("GX")
                .pattern("X ")
                .define('X', AWRegistry.INGOT_AETHER.get())
                .define('G', AWRegistry.GEM_AETHER.get())
                .unlockedBy("has_aether_ingot", has(AWRegistry.INGOT_AETHER.get()))
                .save(consumer, getResource("axe_head_crude"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, AWRegistry.SHOVEL_HEAD_CRUDE.get())
                .pattern("G")
                .pattern("P")
                .define('P', AWRegistry.PLATE_AETHER.get())
                .define('G', AWRegistry.GEM_AETHER.get())
                .unlockedBy("has_aether_ingot", has(AWRegistry.INGOT_AETHER.get()))
                .save(consumer, getResource("shovel_head_crude"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, AWRegistry.CROSSBOW_FRAME_CRUDE.get())
                .pattern("G  ")
                .pattern(" XP")
                .pattern("  G")
                .define('X', AWRegistry.INGOT_AETHER.get())
                .define('G', AWRegistry.GEM_AETHER.get())
                .define('P', AWRegistry.PLATE_AETHER.get())
                .unlockedBy("has_aether_ingot", has(AWRegistry.INGOT_AETHER.get()))
                .save(consumer, getResource("crossbow_frame_crude"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, AWRegistry.CROSSBOW_LIMBS_CRUDE.get())
                .pattern(" PX")
                .pattern("PG ")
                .pattern("X  ")
                .define('X', AWRegistry.INGOT_AETHER.get())
                .define('G', AWRegistry.GEM_AETHER.get())
                .define('P', AWRegistry.PLATE_AETHER.get())
                .unlockedBy("has_aether_ingot", has(AWRegistry.INGOT_AETHER.get()))
                .save(consumer, getResource("crossbow_limbs_crude"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, AWRegistry.AETHER_CROWN_CRUDE.get())
                .pattern("   ")
                .pattern("PGP")
                .pattern("X X")
                .define('X', AWRegistry.INGOT_AETHER.get())
                .define('G', AWRegistry.GEM_AETHER.get())
                .define('P', AWRegistry.PLATE_AETHER.get())
                .unlockedBy("has_aether_ingot", has(AWRegistry.INGOT_AETHER.get()))
                .save(consumer, getResource("aether_crown_crude"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, AWRegistry.CONTROL_MATRIX.get())
                .pattern("DFD")
                .pattern("DID")
                .pattern("AAA")
                .define('D', RegistryManager.DAWNSTONE_INGOT.get())
                .define('F', AWRegistry.FOCUS_CRYSTAL.get())
                .define('I', RegistryManager.INTELLIGENT_APPARATUS.get())
                .define('A', RegistryManager.ARCHAIC_BRICK.get())
                .unlockedBy("has_aether_shard", has(AWRegistry.AETHER_SHARD.get()))
                .save(consumer, getResource("control_matrix_block"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, AWRegistry.MOONLIGHT_AMPLIFIER.get())
                .pattern("DLD")
                .pattern("DPL")
                .pattern("AAA")
                .define('D', RegistryManager.DAWNSTONE_INGOT.get())
                .define('L', AWRegistry.AETHERIUM_LENS.get())
                .define('P', Tags.Items.GLASS_PANES)
                .define('A', RegistryManager.ARCHAIC_BRICK.get())
                .unlockedBy("has_aether_shard", has(AWRegistry.AETHER_SHARD.get()))
                .save(consumer, getResource("moonlight_amp_block"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, AWRegistry.PRISM_SUPPORT.get())
                .pattern("C C")
                .pattern("C C")
                .pattern("C C")
                .define('C', RegistryManager.CAMINITE_BRICKS_DECO.wallItem.get())
                .unlockedBy("has_aether_shard", has(AWRegistry.AETHER_SHARD.get()))
                .save(consumer, getResource("prism_support_block"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, AWRegistry.PRISM.get())
                .pattern("DLD")
                .pattern("LCL")
                .pattern("DLD")
                .define('D', RegistryManager.DAWNSTONE_INGOT.get())
                .define('L', AWRegistry.AETHERIUM_LENS.get())
                .define('C', RegistryManager.ARCHAIC_CIRCUIT.get())
                .unlockedBy("has_aether_shard", has(AWRegistry.AETHER_SHARD.get()))
                .save(consumer, getResource("prism_block"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, AWRegistry.FORGE_CORE.get())
                .pattern("BSB")
                .pattern("BCB")
                .pattern("PPP")
                .define('B', RegistryManager.CAMINITE_BRICK.get())
                .define('S', RegistryManager.EMBER_CRYSTAL.get())
                .define('C', RegistryManager.WILDFIRE_CORE.get())
                .define('P', RegistryManager.DAWNSTONE_PLATE.get())
                .unlockedBy("has_ember_cluster", has(RegistryManager.EMBER_CRYSTAL_CLUSTER.get()))
                .save(consumer, getResource("forge_core_block"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, AWRegistry.FORGE_HEATER.get())
                .pattern(" S ")
                .pattern("HVH")
                .pattern("PCP")
                .define('V', RegistryManager.FLUID_VESSEL_ITEM.get())
                .define('H', RegistryManager.SUPERHEATER.get())
                .define('S', RegistryManager.WILDFIRE_CORE.get())
                .define('P', RegistryManager.DAWNSTONE_PLATE.get())
                .define('C', RegistryManager.COPPER_PLATE.get())
                .unlockedBy("has_ember_cluster", has(RegistryManager.EMBER_CRYSTAL_CLUSTER.get()))
                .save(consumer, getResource("forge_heater_block"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, AWRegistry.FORGE_COOLER.get())
                .pattern(" S ")
                .pattern("HVH")
                .pattern("PCP")
                .define('V', RegistryManager.FLUID_VESSEL_ITEM.get())
                .define('H', RegistryManager.SILVER_PLATE.get())
                .define('S', RegistryManager.WILDFIRE_CORE.get())
                .define('P', RegistryManager.DAWNSTONE_PLATE.get())
                .define('C', RegistryManager.ARCHAIC_CIRCUIT.get())
                .unlockedBy("has_ember_cluster", has(RegistryManager.EMBER_CRYSTAL_CLUSTER.get()))
                .save(consumer, getResource("forge_cooler_block"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, AWRegistry.FORGE_VENT.get())
                .pattern("PIP")
                .pattern("IBI")
                .pattern("PIP")
                .define('I', RegistryManager.IRON_PLATE.get())
                .define('B', Items.IRON_BARS)
                .define('P', RegistryManager.DAWNSTONE_PLATE.get())
                .unlockedBy("has_ember_cluster", has(RegistryManager.EMBER_CRYSTAL_CLUSTER.get()))
                .save(consumer, getResource("forge_vent_block"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, AWRegistry.FORGE_METAL_FORMER.get())
                .pattern("BAB")
                .pattern("PPP")
                .define('A', RegistryManager.ASH.get())
                .define('B', RegistryManager.CAMINITE_BRICK.get())
                .define('P', RegistryManager.CAMINITE_PLATE.get())
                .unlockedBy("has_ember_cluster", has(RegistryManager.EMBER_CRYSTAL_CLUSTER.get()))
                .save(consumer, getResource("metal_former_block"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, AWRegistry.FORGE_ANVIL.get())
                .pattern("PPP")
                .pattern("XDX")
                .define('X', AWRegistry.INGOT_AETHER.get())
                .define('P', AWRegistry.PLATE_AETHER.get())
                .define('D', RegistryManager.DAWNSTONE_BLOCK_ITEM.get())
                .unlockedBy("has_aether_ingot", has(AWRegistry.INGOT_AETHER.get()))
                .save(consumer, getResource("aetherium_anvil_block"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, AWRegistry.FORGE_TOOL_STATION.get())
                .pattern("PDP")
                .pattern("SSS")
                .define('D', AWRegistry.GEM_AETHER.get())
                .define('P', RegistryManager.DAWNSTONE_PLATE.get())
                .define('S', RegistryManager.SILVER_BLOCK.get())
                .unlockedBy("has_aether_ingot", has(AWRegistry.INGOT_AETHER.get()))
                .save(consumer, getResource("aetherium_tool_station"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, AWRegistry.HEAT_DIAL.get())
                .pattern("B")
                .pattern("P")
                .define('B', Items.PAPER)
                .define('P', RegistryManager.DAWNSTONE_PLATE.get())
                .unlockedBy("has_ember_cluster", has(RegistryManager.EMBER_CRYSTAL_CLUSTER.get()))
                .save(consumer, getResource("heat_dial_block"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, AWRegistry.TUNING_CYLINDER.get())
                .pattern(" SP")
                .pattern("SLS")
                .pattern("PS ")
                .define('P', RegistryManager.DAWNSTONE_PLATE.get())
                .define('S', AWRegistry.AETHER_SHARD.get())
                .define('L', RegistryManager.SILVER_PLATE.get())
                .unlockedBy("has_aether_shard", has(AWRegistry.AETHER_SHARD.get()))
                .save(consumer, getResource("tuning_cylinder"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BREWING, AWRegistry.POTION_GEM.get())
                .pattern(" X ")
                .pattern("GIG")
                .pattern(" X ")
                .define('G', AWRegistry.GEM_AETHER.get())
                .define('X', AWRegistry.INGOT_AETHER.get())
                .define('I', RegistryManager.INFLICTOR_GEM.get())
                .unlockedBy("has_aether_ingot", has(AWRegistry.INGOT_AETHER.get()))
                .save(consumer, getResource("potion_gem"));
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, AWRegistry.AETHERIOMETER.get())
                .pattern(" IC")
                .pattern("IGM")
                .pattern(" IC")
                .define('G', RegistryManager.ATMOSPHERIC_GAUGE_ITEM.get())
                .define('M', RegistryManager.EMBER_JAR.get())
                .define('I', Items.IRON_INGOT)
                .define('C', Items.COPPER_INGOT)
                .unlockedBy("has_redstone", has(itemTag("forge", "dusts/redstone")))
                .save(consumer, getResource("aetheriometer"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, AWRegistry.AETHERIUM_BLOCK.get())
                .pattern("XXX")
                .pattern("XXX")
                .pattern("XXX")
                .define('X', AWRegistry.INGOT_AETHER.get())
                .unlockedBy("has_aether_ingot", has(AWRegistry.INGOT_AETHER.get()))
                .save(consumer, getResource("aetherium_ingot_to_block"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, AWRegistry.INGOT_AETHER.get(), 9)
                .requires(AWRegistry.AETHERIUM_BLOCK.get())
                .unlockedBy("has_block", has(AWRegistry.AETHERIUM_BLOCK.get()))
                .save(consumer, getResource("aetherium_block_to_ingot"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, AWRegistry.AETHERIUM_SHARD_BLOCK.get())
                .pattern("XXX")
                .pattern("XXX")
                .pattern("XXX")
                .define('X', AWRegistry.AETHER_SHARD.get())
                .unlockedBy("has_aether_shard", has(AWRegistry.AETHER_SHARD.get()))
                .save(consumer, getResource("aether_shard_to_block"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, AWRegistry.AETHER_SHARD.get(), 9)
                .requires(AWRegistry.AETHERIUM_SHARD_BLOCK.get())
                .unlockedBy("has_block", has(AWRegistry.AETHERIUM_SHARD_BLOCK.get()))
                .save(consumer, getResource("raw_aetherium_block_to_shards"));
    }

    public TagKey<Item> itemTag(String modId, String name) {
        return TagKey.create(Registries.ITEM, new ResourceLocation(modId, name));
    }

    public TagKey<Fluid> fluidTag(String modId, String name) {
        return TagKey.create(Registries.FLUID, new ResourceLocation(modId, name));
    }

    public ICondition tagReal(TagKey<?> tag) {
        return new NotCondition(new TagEmptyCondition(tag.location()));
    }

    public static ResourceLocation getResource(String name) {
        return new ResourceLocation(Aetherworks.MODID, name);
    }

    public void decoRecipes(AWRegistry.StoneDecoBlocks deco, Consumer<FinishedRecipe> consumer) {
        Item item = deco.block.get().asItem();

        if (deco.stairs != null) {
            ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, deco.stairs.get(), 4)
                    .pattern("X  ")
                    .pattern("XX ")
                    .pattern("XXX")
                    .define('X', item)
                    .unlockedBy("has_" + deco.name, has(item))
                    .save(consumer, deco.stairs.getId());

            stonecutterResultFromBase(consumer, RecipeCategory.DECORATIONS, deco.stairs.get(), item);
        }

        if (deco.slab != null) {
            ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, deco.slab.get(), 6)
                    .pattern("XXX")
                    .define('X', item)
                    .unlockedBy("has_" + deco.name, has(item))
                    .save(consumer, deco.slab.getId());

            stonecutterResultFromBase(consumer, RecipeCategory.DECORATIONS, deco.slab.get(), item, 2);
        }

        if (deco.wall != null) {
            ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, deco.wall.get(), 6)
                    .pattern("XXX")
                    .pattern("XXX")
                    .define('X', item)
                    .unlockedBy("has_" + deco.name, has(item))
                    .save(consumer, deco.wall.getId());

            stonecutterResultFromBase(consumer, RecipeCategory.DECORATIONS, deco.wall.get(), item);
        }
    }
}
