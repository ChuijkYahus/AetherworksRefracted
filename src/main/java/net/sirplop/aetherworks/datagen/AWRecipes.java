package net.sirplop.aetherworks.datagen;

import com.rekindled.embers.RegistryManager;
import com.rekindled.embers.datagen.EmbersItemTags;
import com.rekindled.embers.datagen.EmbersRecipes;
import com.rekindled.embers.recipe.*;
import com.rekindled.embers.util.ConsumerWrapperBuilder;
import com.rekindled.embers.util.FluidAmounts;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
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
import net.sirplop.aetherworks.recipe.*;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class AWRecipes extends RecipeProvider implements IConditionBuilder {


    public static String metalFormerFolder = "metal_former";
    public static String anvilFolder = "aetherium_anvil";

    public AWRecipes(PackOutput pOutput) {
        super(pOutput);
    }

    @Override
    protected void buildRecipes(@NotNull Consumer<FinishedRecipe> consumer) {
        //dawnstone anvil
        AnvilAugmentRecipeBuilder.create(AWRegistry.TUNING_CYLINDER_AUGMENT).folder(EmbersRecipes.anvilFolder).tool(HeatIngredient.of(Ingredient.of(EmbersItemTags.AUGMENTABLE_TOOLS))).input(AWRegistry.TUNING_CYLINDER.get()).save(consumer);

        //metal former
        MetalFormerRecipeBuilder.create(AWRegistry.INGOT_AETHER.get()).domain(Aetherworks.MODID).folder(metalFormerFolder).temperature(2100).fluid(new FluidStack(AWRegistry.AETHERIUM_GAS.FLUID.get(), FluidAmounts.INGOT_AMOUNT)).input(Ingredient.of(RegistryManager.DAWNSTONE_INGOT.get())).save(consumer);
        MetalFormerRecipeBuilder.create(AWRegistry.GEM_AETHER.get()).domain(Aetherworks.MODID).folder(metalFormerFolder).temperature(2600).fluid(new FluidStack(AWRegistry.AETHERIUM_GAS.FLUID.get(), FluidAmounts.INGOT_AMOUNT * 4)).input(Ingredient.of(Items.DIAMOND)).save(consumer);
        MetalFormerRecipeBuilder.create(AWRegistry.GEM_AETHER.get()).id(Aetherworks.MODID, "gem_aether_alt").folder(metalFormerFolder).temperature(2800).fluid(new FluidStack(AWRegistry.AETHERIUM_GAS.FLUID.get(), FluidAmounts.INGOT_AMOUNT * 4)).input(Ingredient.of(Items.EMERALD)).save(consumer);
        MetalFormerRecipeBuilder.create(AWRegistry.PLATE_AETHER.get()).domain(Aetherworks.MODID).folder(metalFormerFolder).temperature(2400).fluid(new FluidStack(AWRegistry.AETHERIUM_GAS.FLUID.get(), FluidAmounts.INGOT_AMOUNT * 2)).input(Ingredient.of(RegistryManager.DAWNSTONE_PLATE.get())).save(consumer);

        //aetherium anvil
        AetheriumAnvilRecipeBuilder.create(AWRegistry.TOOL_ROD.get()).domain(Aetherworks.MODID).folder(anvilFolder).temperatureRange(2100, 3000).difficulty(2).hitInfo(15, 50).input(AWRegistry.TOOL_ROD_CRUDE.get()).save(consumer);
        AetheriumAnvilRecipeBuilder.create(AWRegistry.PICKAXE_HEAD.get()).domain(Aetherworks.MODID).folder(anvilFolder).temperatureRange(2500, 2900).difficulty(4).hitInfo(30, 60).input(AWRegistry.PICKAXE_HEAD_CRUDE.get()).save(consumer);
        AetheriumAnvilRecipeBuilder.create(AWRegistry.AXE_HEAD.get()).domain(Aetherworks.MODID).folder(anvilFolder).temperatureRange(2500, 2900).difficulty(4).hitInfo(30, 60).input(AWRegistry.AXE_HEAD_CRUDE.get()).save(consumer);
        AetheriumAnvilRecipeBuilder.create(AWRegistry.SHOVEL_HEAD.get()).domain(Aetherworks.MODID).folder(anvilFolder).temperatureRange(2500, 2900).difficulty(4).hitInfo(30, 60).input(AWRegistry.SHOVEL_HEAD_CRUDE.get()).save(consumer);
        AetheriumAnvilRecipeBuilder.create(AWRegistry.CROSSBOW_FRAME.get()).domain(Aetherworks.MODID).folder(anvilFolder).temperatureRange(2650, 3000).difficulty(5).hitInfo(35, 80).input(AWRegistry.CROSSBOW_FRAME_CRUDE.get()).save(consumer);
        AetheriumAnvilRecipeBuilder.create(AWRegistry.CROSSBOW_LIMBS.get()).domain(Aetherworks.MODID).folder(anvilFolder).temperatureRange(2650, 3000).difficulty(5).hitInfo(35, 80).input(AWRegistry.CROSSBOW_FRAME_CRUDE.get()).save(consumer);
        AetheriumAnvilRecipeBuilder.create(AWRegistry.AETHER_CROWN_MUNDANE.get()).domain(Aetherworks.MODID).folder(anvilFolder).temperatureRange(2600, 2800).difficulty(6).hitInfo(40, 90).input(AWRegistry.AETHER_CROWN_CRUDE.get()).save(consumer);

        //stamping
        StampingRecipeBuilder.create(AWRegistry.AETHER_SHARD.get()).domain(Aetherworks.MODID).folder(EmbersRecipes.stampingFolder).stamp(RegistryManager.FLAT_STAMP.get()).fluid(AWRegistry.AETHERIUM_GAS_IMPURE.FLUID.get(), FluidAmounts.NUGGET_AMOUNT).save(ConsumerWrapperBuilder.wrap().build(consumer));
        StampingRecipeBuilder.create(AWRegistry.FOCUS_CRYSTAL.get()).domain(Aetherworks.MODID).folder(EmbersRecipes.stampingFolder).stamp(RegistryManager.FLAT_STAMP.get()).input(RegistryManager.EMBER_SHARD.get()).fluid(Fluids.WATER, 1000).save(ConsumerWrapperBuilder.wrap().build(consumer));
        StampingRecipeBuilder.create(AWRegistry.AETHER_ASPECTUS.get()).domain(Aetherworks.MODID).folder(EmbersRecipes.stampingFolder).stamp(RegistryManager.INGOT_STAMP.get()).input(RegistryManager.EMBER_SHARD.get()).fluid(AWRegistry.AETHERIUM_GAS.FLUID.get(), FluidAmounts.INGOT_AMOUNT).save(ConsumerWrapperBuilder.wrap().build(consumer));

        //mixing

        //special recipes
        GenericRecipeBuilder.create(new PotionGemSocketRecipe(new ResourceLocation(Aetherworks.MODID, "crown_socket"))).save(consumer);
        GenericRecipeBuilder.create(new PotionGemUnsocketRecipe(new ResourceLocation(Aetherworks.MODID, "crown_unsocket"))).save(consumer);
        GenericRecipeBuilder.create(new PotionGemImbueRecipe(new ResourceLocation(Aetherworks.MODID, "potion_gem_imbue"))).save(consumer);

        //alchemy
        AlchemyRecipeBuilder.create(AWRegistry.AETHER_AMALGAM.get()).tablet(RegistryManager.EMBER_CRYSTAL_CLUSTER.get()).folder(EmbersRecipes.alchemyFolder)
                .inputs(AWRegistry.AETHER_SHARD.get(), AWRegistry.AETHER_SHARD.get(), AWRegistry.AETHER_SHARD.get(), AWRegistry.AETHER_SHARD.get(), AWRegistry.AETHER_SHARD.get())
                .aspects(EmbersItemTags.COPPER_ASPECTUS, EmbersItemTags.SILVER_ASPECTUS, EmbersItemTags.DAWNSTONE_ASPECTUS).save(consumer);

        AlchemyRecipeBuilder.create(AWRegistry.PICKAXE_HEAD_AETHER.get()).tablet(AWRegistry.PICKAXE_HEAD.get()).folder(EmbersRecipes.alchemyFolder)
                .inputs(AWRegistry.AETHER_AMALGAM.get(), AWRegistry.AETHER_SHARD.get(), AWRegistry.AETHER_AMALGAM.get(), AWRegistry.AETHER_SHARD.get())
                .aspects(EmbersItemTags.SILVER_ASPECTUS, AWItemTags.AETHERIUM_ASPECTUS, EmbersItemTags.DAWNSTONE_ASPECTUS).save(consumer);
        AlchemyRecipeBuilder.create(AWRegistry.PICKAXE_HEAD_EMBER.get()).tablet(AWRegistry.PICKAXE_HEAD.get()).folder(EmbersRecipes.alchemyFolder)
                .inputs(RegistryManager.EMBER_CRYSTAL_CLUSTER.get(), RegistryManager.EMBER_CRYSTAL.get(), RegistryManager.EMBER_CRYSTAL_CLUSTER.get(), RegistryManager.EMBER_CRYSTAL.get())
                .aspects(EmbersItemTags.LEAD_ASPECTUS, AWItemTags.AETHERIUM_ASPECTUS, EmbersItemTags.COPPER_ASPECTUS).save(consumer);
        AlchemyRecipeBuilder.create(AWRegistry.AXE_HEAD_ENDER.get()).tablet(AWRegistry.AXE_HEAD.get()).folder(EmbersRecipes.alchemyFolder)
                .inputs(Ingredient.of(Tags.Items.OBSIDIAN), Ingredient.of(Tags.Items.OBSIDIAN))
                .inputs(Items.ENDER_EYE, Items.ENDER_EYE, Items.ENDER_EYE)
                .aspects(EmbersItemTags.SILVER_ASPECTUS, EmbersItemTags.COPPER_ASPECTUS, AWItemTags.AETHERIUM_ASPECTUS).save(consumer);
        //need to add shovel heads, axe head, and crown

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
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, AWRegistry.AETHERIUM_BLOCK.get())
                .pattern("XXX")
                .pattern("XXX")
                .pattern("XXX")
                .define('X', AWRegistry.INGOT_AETHER.get())
                .unlockedBy("has_aether_ingot", has(AWRegistry.INGOT_AETHER.get()))
                .save(consumer, getResource("aetherium_ingot_to_block"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, AWRegistry.FORGE_CORE.get())
                .pattern("BSB")
                .pattern("BCB")
                .pattern("PPP")
                .define('B', RegistryManager.CAMINITE_BRICK.get())
                .define('S', RegistryManager.EMBER_CRYSTAL.get())
                .define('C', RegistryManager.EMBER_CRYSTAL_CLUSTER.get())
                .define('P', RegistryManager.DAWNSTONE_PLATE.get())
                .unlockedBy("has_ember_cluster", has(RegistryManager.EMBER_CRYSTAL_CLUSTER.get()))
                .save(consumer, getResource("forge_core_block"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, AWRegistry.FORGE_HEATER.get())
                .pattern(" S ")
                .pattern("HVH")
                .pattern("PCP")
                .define('V', RegistryManager.FLUID_VESSEL_ITEM.get())
                .define('H', RegistryManager.SUPERHEATER.get())
                .define('S', RegistryManager.EMBER_CRYSTAL_CLUSTER.get())
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
                .define('S', RegistryManager.EMBER_CRYSTAL_CLUSTER.get())
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
                .pattern("X")
                .pattern("G")
                .pattern("X")
                .define('G', AWRegistry.GEM_AETHER.get())
                .define('X', AWRegistry.INGOT_AETHER.get())
                .unlockedBy("has_aether_ingot", has(AWRegistry.INGOT_AETHER.get()))
                .save(consumer, getResource("potion_gem"));
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
