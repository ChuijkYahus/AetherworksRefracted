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

public class MetalFormerRecipe implements IMetalFormerRecipe{
    public static final Serializer SERIALIZER = new Serializer();

    public final ResourceLocation id;

    public final Ingredient input;
    public final FluidIngredient fluid;
    public final int temperature;

    public final Either<ItemStack, TagAmount> output;

    public MetalFormerRecipe(ResourceLocation id, Ingredient input, FluidIngredient fluid, int temperature, TagAmount output) {
        this(id, input, fluid, temperature, Either.right(output));
    }
    public MetalFormerRecipe(ResourceLocation id, Ingredient input, FluidIngredient fluid, int temperature, ItemStack output) {
        this(id, input, fluid, temperature, Either.left(output));
    }
    public MetalFormerRecipe(ResourceLocation id, Ingredient input, FluidIngredient fluid, int temperature, Either<ItemStack, TagAmount> output) {
        this.id = id;
        this.input = input;
        this.fluid = fluid;
        this.temperature = temperature;
        this.output = output;
    }

    @Override
    public boolean matches(MetalFormerContext context, Level pLevel) {
        for (int i = 0; i < context.getContainerSize(); i++) {
            if (input.test(context.getItem(i))) {
                if (context.temperature >= this.temperature) {
                    if (fluid.test(context.fluids.getFluidInTank(0)))
                        return true;
                }
                return false;
            }
        }
        return false;
    }

    @Override
    public ItemStack getOutput(RecipeWrapper context) {
        return getResultItem();
    }

    @Override
    public ItemStack assemble(MetalFormerContext context, RegistryAccess registry) {
        for (int i = 0; i < context.getContainerSize(); i++) {
            if (input.test(context.getItem(i))) {
                context.removeItem(i, 1);
                break;
            }
        }
        for (FluidStack stack : fluid.getFluids()) {
            if (fluid.test(context.fluids.drain(stack, IFluidHandler.FluidAction.SIMULATE))) {
                context.fluids.drain(stack, IFluidHandler.FluidAction.EXECUTE);
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
    @Override
    public ItemStack getResultItem() {
        if (output.left().isPresent())
            return output.left().get().copy();
        return new ItemStack(Misc.getTaggedItem(output.right().get().tag), output.right().get().amount);
    }
    @Override
    public FluidIngredient getDisplayInputFluid() {
        return fluid;
    }

    @Override
    public Ingredient getDisplayInput() {
        return input;
    }

    @Override
    public int getTemperature() {
        return temperature;
    }

    public static class TagAmount {
        public TagKey<Item> tag;
        public int amount;

        public TagAmount(TagKey<Item> tag, int amount) {
            this.tag = tag;
            this.amount = amount;
        }
    }
    public static class Serializer implements RecipeSerializer<MetalFormerRecipe> {

        @Override
        public MetalFormerRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            Ingredient input = Ingredient.EMPTY;
            FluidIngredient fluid = FluidIngredient.EMPTY;
            if (json.has("input"))
                input = Ingredient.fromJson(json.get("input"));
            if (json.has("fluid"))
                fluid = FluidIngredient.deserialize(json, "fluid");

            int temperature = json.get("temperature").getAsInt();

            JsonObject outputJson = GsonHelper.getAsJsonObject(json, "output");
            if (outputJson.has("tag")) {
                TagAmount output = new TagAmount(ItemTags.create(new ResourceLocation(GsonHelper.getAsString(outputJson, "tag"))), GsonHelper.getAsInt(outputJson, "count", 1));
                return new MetalFormerRecipe(recipeId, input, fluid, temperature, output);
            } else {
                ItemStack output = ShapedRecipe.itemStackFromJson(outputJson);
                return new MetalFormerRecipe(recipeId, input, fluid, temperature, output);
            }
        }

        @Override
        public @Nullable MetalFormerRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            Ingredient input = Ingredient.fromNetwork(buffer);
            FluidIngredient fluid = FluidIngredient.read(buffer);
            int temperature = buffer.readInt();
            if (buffer.readBoolean()) {
                TagAmount output = new TagAmount(ItemTags.create(buffer.readResourceLocation()), buffer.readInt());
                return new MetalFormerRecipe(recipeId, input, fluid, temperature, output);
            }
            ItemStack output = buffer.readItem();

            return new MetalFormerRecipe(recipeId, input, fluid,temperature, output);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, MetalFormerRecipe recipe) {
            recipe.input.toNetwork(buffer);
            recipe.fluid.write(buffer);
            buffer.writeInt(recipe.temperature);
            if (recipe.output.right().isPresent()) {
                buffer.writeBoolean(true);
                buffer.writeResourceLocation(recipe.output.right().get().tag.location());
                buffer.writeInt(recipe.output.right().get().amount);
            } else {
                buffer.writeBoolean(false);
                buffer.writeItemStack(recipe.output.left().get(), false);
            }
        }
    }
}
