package net.sirplop.aetherworks.recipe;

import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Either;
import com.rekindled.embers.recipe.FluidIngredient;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;
import net.sirplop.aetherworks.AWRegistry;

import java.util.function.Consumer;

public class AetheriumAnvilRecipeBuilder {
    public ResourceLocation id;
    public Ingredient input = Ingredient.EMPTY;
    public int temperatureMin;
    public int temperatureMax;
    public int difficulty;
    public int emberPerHit;
    public int numberOfHits;
    public Either<ItemStack, TagKey<Item>> output;

    public static AetheriumAnvilRecipeBuilder create(ItemStack itemStack) {
        AetheriumAnvilRecipeBuilder builder = new AetheriumAnvilRecipeBuilder();
        builder.output = Either.left(itemStack);
        builder.id = ForgeRegistries.ITEMS.getKey(itemStack.getItem());
        return builder;
    }
    public static AetheriumAnvilRecipeBuilder create(Item item) {
        return create(new ItemStack(item));
    }
    public static AetheriumAnvilRecipeBuilder create(TagKey<Item> tag) {
        AetheriumAnvilRecipeBuilder builder = new AetheriumAnvilRecipeBuilder();
        builder.output = Either.right(tag);
        builder.id = tag.location();
        return builder;
    }
    public AetheriumAnvilRecipeBuilder id(ResourceLocation id) {
        this.id = id;
        return this;
    }

    public AetheriumAnvilRecipeBuilder domain(String domain) {
        this.id = new ResourceLocation(domain, this.id.getPath());
        return this;
    }

    public AetheriumAnvilRecipeBuilder id(String domain, String id) {
        this.id = new ResourceLocation(domain, id);
        return this;
    }
    public AetheriumAnvilRecipeBuilder folder(String folder) {
        this.id = new ResourceLocation(id.getNamespace(), folder + "/" + id.getPath());
        return this;
    }
    public AetheriumAnvilRecipeBuilder input(Ingredient input) {
        this.input = input;
        return this;
    }

    public AetheriumAnvilRecipeBuilder input(ItemLike... input) {
        input(Ingredient.of(input));
        return this;
    }

    public AetheriumAnvilRecipeBuilder input(TagKey<Item> tag) {
        input(Ingredient.of(tag));
        return this;
    }

    public AetheriumAnvilRecipeBuilder temperatureRange(int min, int max) {
        this.temperatureMin = min;
        this.temperatureMax = max;
        return this;
    }
    public AetheriumAnvilRecipeBuilder hitInfo(int numberOfHits, int emberPerHit) {
        this.numberOfHits = numberOfHits;
        this.emberPerHit = emberPerHit;
        return this;
    }
    public AetheriumAnvilRecipeBuilder difficulty(int difficulty) {
        this.difficulty = difficulty;
        return this;
    }

    public AetheriumAnvilRecipe build() {
        return new AetheriumAnvilRecipe(id, input, temperatureMin, temperatureMax, difficulty, emberPerHit, numberOfHits, output);
    }

    public void save(Consumer<FinishedRecipe> consumer) {
        consumer.accept(new Finished(build()));
    }

    public static class Finished implements FinishedRecipe {

        public final AetheriumAnvilRecipe recipe;

        public Finished(AetheriumAnvilRecipe recipe) {
            this.recipe = recipe;
        }

        @Override
        public void serializeRecipeData(JsonObject json) {
            JsonObject outputJson = new JsonObject();
            if (recipe.output.right().isPresent()) {
                outputJson.addProperty("tag", recipe.output.right().get().location().toString());
            } else {
                outputJson.addProperty("item", ForgeRegistries.ITEMS.getKey(recipe.output.left().get().getItem()).toString());
            }
            json.add("output", outputJson);

            if (!recipe.input.isEmpty())
                json.add("input", recipe.input.toJson());

            json.addProperty("temperatureMin", recipe.temperatureMin);
            json.addProperty("temperatureMax", recipe.temperatureMax);
            json.addProperty("difficulty", recipe.difficulty);
            json.addProperty("emberPerHit", recipe.emberPerHit);
            json.addProperty("numberOfHits", recipe.numberOfHits);
        }

        @Override
        public ResourceLocation getId() {
            return recipe.getId();
        }

        @Override
        public RecipeSerializer<?> getType() {
            return AWRegistry.AETHERIUM_ANVIL_SERIALIZER.get();
        }

        @Override
        public JsonObject serializeAdvancement() {
            return null;
        }

        @Override
        public ResourceLocation getAdvancementId() {
            return null;
        }
    }
}
