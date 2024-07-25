package net.sirplop.aetherworks.recipe;

import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.sirplop.aetherworks.AWRegistry;
import net.sirplop.aetherworks.item.PotionGemItem;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class PotionGemImbueRecipe implements CraftingRecipe {

    public static final Serializer SERIALIZER = new Serializer();

    public final ResourceLocation id;

    public PotionGemImbueRecipe(ResourceLocation id) {
        this.id = id;
    }

    @Override
    public boolean matches(CraftingContainer container, @NotNull Level level) {
        ItemStack gem = ItemStack.EMPTY;
        ItemStack potion = ItemStack.EMPTY;
        for (int i = 0; i < container.getContainerSize(); i++) {
            ItemStack stack = container.getItem(i);
            if (!stack.isEmpty()) {
                if (stack.getItem() instanceof PotionGemItem) {
                    if (!gem.isEmpty())
                        return false; //too many gems
                    gem = stack;
                } else if (stack.getItem() instanceof PotionItem) {
                    if (!potion.isEmpty())
                        return false; //too many potions
                    potion = stack;
                } else {
                    return false;
                }
            }
        }
        return !gem.isEmpty() && !potion.isEmpty();
    }

    @Override
    public @NotNull ItemStack assemble(CraftingContainer container, @NotNull RegistryAccess registryAccess) {
        ItemStack gem = ItemStack.EMPTY;

        for (int i = 0; i < container.getContainerSize(); i++) {
            if (!container.getItem(i).isEmpty() && container.getItem(i).getItem() instanceof PotionGemItem) {
                gem = container.getItem(i).copyWithCount(1);
            }
        }
        if (!gem.isEmpty()) {
            for (int i = 0; i < container.getContainerSize(); i++) {
                ItemStack stack = container.getItem(i);
                if (!stack.isEmpty() && stack.getItem() instanceof PotionItem) {
                    ((PotionGemItem)gem.getItem()).setEffects(stack, gem);
                }
            }
            return gem;
        }
        return ItemStack.EMPTY;
    }

    @Override
    public @NotNull NonNullList<ItemStack> getRemainingItems(CraftingContainer container) {
        NonNullList<ItemStack> bottles = NonNullList.withSize(container.getContainerSize(), ItemStack.EMPTY);
        for (int i = 0; i < container.getContainerSize(); i++) {
            ItemStack stack = container.getItem(i);
            if (!stack.isEmpty() && stack.getItem() instanceof PotionItem potion) {
                bottles.set(i, new ItemStack(Items.GLASS_BOTTLE.asItem(), 1));
                break;
            }
        }
        return bottles;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public @NotNull ItemStack getResultItem(@NotNull RegistryAccess registryAccess) {
        return new ItemStack(AWRegistry.AETHER_CROWN.get());
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public boolean showNotification() {
        return false;
    }

    @Override
    public @NotNull ResourceLocation getId() {
        return id;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public @NotNull CraftingBookCategory category() {
        return CraftingBookCategory.EQUIPMENT;
    }

    public static class Serializer implements RecipeSerializer<PotionGemImbueRecipe> {

        @Override
        public @NotNull PotionGemImbueRecipe fromJson(@NotNull ResourceLocation recipeId, @NotNull JsonObject json) {
            return new PotionGemImbueRecipe(recipeId);
        }

        @Override
        public @Nullable PotionGemImbueRecipe fromNetwork(@NotNull ResourceLocation recipeId, @NotNull FriendlyByteBuf buffer) {
            return new PotionGemImbueRecipe(recipeId);
        }

        @Override
        public void toNetwork(@NotNull FriendlyByteBuf buffer, @NotNull PotionGemImbueRecipe recipe) { }
    }
}
