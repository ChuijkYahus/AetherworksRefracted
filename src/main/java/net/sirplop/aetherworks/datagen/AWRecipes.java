package net.sirplop.aetherworks.datagen;

import com.rekindled.embers.datagen.EmbersItemTags;
import com.rekindled.embers.recipe.AnvilAugmentRecipeBuilder;
import com.rekindled.embers.recipe.HeatIngredient;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import net.sirplop.aetherworks.AWRegistry;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class AWRecipes extends RecipeProvider implements IConditionBuilder {

    public static String boringFolder = "boring";
    public static String activationFolder = "ember_activation";
    public static String meltingFolder = "melting";
    public static String stampingFolder = "stamping";
    public static String mixingFolder = "mixing";
    public static String coefficientFolder = "metal_coefficient";
    public static String alchemyFolder = "alchemy";
    public static String boilingFolder = "boiling";
    public static String gaseousFuelFolder = "gas_fuel";
    public static String catalysisFolder = "catalysis";
    public static String combustionFolder = "combustion";
    public static String anvilFolder = "dawnstone_anvil";
    public static String forgeFolder = "aetherium_forge";

    public AWRecipes(PackOutput pOutput) {
        super(pOutput);
    }

    @Override
    protected void buildRecipes(@NotNull Consumer<FinishedRecipe> consumer) {


        //dawnstone anvil
        AnvilAugmentRecipeBuilder.create(AWRegistry.TUNING_CYLINDER_AUGMENT).folder(anvilFolder).tool(HeatIngredient.of(Ingredient.of(EmbersItemTags.AUGMENTABLE_TOOLS))).input(AWRegistry.TUNING_CYLINDER.get()).save(consumer);

    }
}
