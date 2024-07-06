package net.sirplop.aetherworks.recipe;

import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Either;
import com.rekindled.embers.recipe.FluidIngredient;
import com.rekindled.embers.util.Misc;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;

import javax.annotation.Nullable;

public class AetheriumAnvilRecipe implements IAetheriumAnvilRecipe {
    public static final Serializer SERIALIZER = new Serializer();

    public final ResourceLocation id;

    public final Ingredient input;
    public final int temperatureMin;
    public final int temperatureMax;
    public final int difficulty;
    public final int emberPerHit;
    public final int numberOfHits;

    public final Either<ItemStack, TagKey<Item>> output;

    public AetheriumAnvilRecipe(ResourceLocation id, Ingredient input, int temperatureMin, int temperatureMax,
                                int difficulty, int emberPerHit, int numberOfHits, TagKey<Item> output) {
        this(id, input, temperatureMin, temperatureMax, difficulty, emberPerHit, numberOfHits, Either.right(output));
    }
    public AetheriumAnvilRecipe(ResourceLocation id, Ingredient input, int temperatureMin, int temperatureMax,
                                int difficulty, int emberPerHit, int numberOfHits, ItemStack output) {
        this(id, input, temperatureMin, temperatureMax, difficulty, emberPerHit, numberOfHits, Either.left(output));
    }
    public AetheriumAnvilRecipe(ResourceLocation id, Ingredient input, int temperatureMin, int temperatureMax,
                                int difficulty, int emberPerHit, int numberOfHits, Either<ItemStack, TagKey<Item>> output) {
        this.id = id;
        this.input = input;
        this.output = output;

        this.temperatureMin = temperatureMin;
        this.temperatureMax = temperatureMax;
        this.difficulty = difficulty;
        this.emberPerHit = emberPerHit;
        this.numberOfHits = numberOfHits;
    }

    @Override
    public ItemStack getOutput(RecipeWrapper context) {
        return getResultItem();
    }

    @Override
    public ItemStack getResultItem() {
        if (output.left().isPresent())
            return output.left().get().copy();
        return new ItemStack(Misc.getTaggedItem(output.right().get()), 1);
    }

    @Override
    public Ingredient getDisplayInput() {
        return input;
    }

    @Override
    public int getTemperatureMin() {
        return temperatureMin;
    }

    @Override
    public int getTemperatureMax() {
        return temperatureMax;
    }

    @Override
    public int getDifficulty() {
        return difficulty;
    }

    @Override
    public int getEmberPerHit() {
        return emberPerHit;
    }

    @Override
    public int getNumberOfHits() {
        return numberOfHits;
    }

    @Override
    public boolean matches(AetheriumAnvilContext context, Level level) {
        for (int i = 0; i < context.getContainerSize(); i++) {
            if (input.test(context.getItem(i))) {
                if (context.temperature >= this.temperatureMin &&
                    context.temperature <= this.temperatureMax) {
                        return true;
                }
            }
        }
        return false;
    }

    @Override
    public ItemStack assemble(AetheriumAnvilContext context, RegistryAccess registryAccess) {
        for (int i = 0; i < context.getContainerSize(); i++) {
            if (input.test(context.getItem(i))) {
                context.removeItem(i, 1);
                break;
            }
        }
        return this.getOutput(context);
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return SERIALIZER;
    }
    public static class Serializer implements RecipeSerializer<AetheriumAnvilRecipe> {

        @Override
        public AetheriumAnvilRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            Ingredient input = Ingredient.EMPTY;
            if (json.has("input"))
                input = Ingredient.fromJson(json.get("input"));

            int temperatureMin = json.get("temperatureMin").getAsInt();
            int temperatureMax = json.get("temperatureMax").getAsInt();
            int difficulty = json.get("difficulty").getAsInt();
            int emberPerHit = json.get("emberPerHit").getAsInt();
            int numberOfHits =  json.get("numberOfHits").getAsInt();

            JsonObject outputJson = GsonHelper.getAsJsonObject(json, "output");
            if (outputJson.has("tag")) {

                TagKey<Item> output = ItemTags.create(new ResourceLocation(GsonHelper.getAsString(outputJson, "tag")));
                return new AetheriumAnvilRecipe(recipeId, input, temperatureMin, temperatureMax, difficulty, emberPerHit, numberOfHits, output);
            } else {
                ItemStack output = ShapedRecipe.itemStackFromJson(outputJson);
                return new AetheriumAnvilRecipe(recipeId, input, temperatureMin, temperatureMax, difficulty, emberPerHit, numberOfHits, output);
            }
        }

        @Override
        public @Nullable AetheriumAnvilRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            Ingredient input = Ingredient.fromNetwork(buffer);
            int temperatureMin = buffer.readInt();
            int temperatureMax = buffer.readInt();
            int difficulty = buffer.readInt();
            int emberPerHit = buffer.readInt();
            int numberOfHits = buffer.readInt();

            if (buffer.readBoolean()) {
                TagKey<Item> output = ItemTags.create(buffer.readResourceLocation());
                return new AetheriumAnvilRecipe(recipeId, input, temperatureMin, temperatureMax, difficulty, emberPerHit, numberOfHits, output);
            }
            ItemStack output = buffer.readItem();

            return new AetheriumAnvilRecipe(recipeId, input, temperatureMin, temperatureMax, difficulty, emberPerHit, numberOfHits, output);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, AetheriumAnvilRecipe recipe) {
            recipe.input.toNetwork(buffer);
            buffer.writeInt(recipe.temperatureMin);
            buffer.writeInt(recipe.temperatureMax);
            buffer.writeInt(recipe.difficulty);
            buffer.writeInt(recipe.emberPerHit);
            buffer.writeInt(recipe.numberOfHits);

            if (recipe.output.right().isPresent()) {
                buffer.writeBoolean(true);
                buffer.writeResourceLocation(recipe.output.right().get().location());
            } else {
                buffer.writeBoolean(false);
                buffer.writeItemStack(recipe.output.left().get(), false);
            }
        }
    }
}
