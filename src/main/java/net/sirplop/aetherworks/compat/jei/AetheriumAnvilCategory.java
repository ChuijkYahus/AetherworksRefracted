package net.sirplop.aetherworks.compat.jei;

import com.rekindled.embers.Embers;
import com.rekindled.embers.RegistryManager;
import com.rekindled.embers.gui.GuiCodex;
import com.rekindled.embers.util.Misc;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.sirplop.aetherworks.AWRegistry;
import net.sirplop.aetherworks.Aetherworks;
import net.sirplop.aetherworks.recipe.IAetheriumAnvilRecipe;
import net.sirplop.aetherworks.util.Utils;

public class AetheriumAnvilCategory implements IRecipeCategory<IAetheriumAnvilRecipe> {
    private final IDrawable background;
    private final IDrawable icon;
    public static Component title = Component.translatable(Aetherworks.MODID + ".jei.recipe.aetherium_anvil");
    public static ResourceLocation texture = new ResourceLocation(Aetherworks.MODID, "textures/gui/jei_anvil.png");

    public AetheriumAnvilCategory(IGuiHelper helper) {
        background = helper.createDrawable(texture, 0, 0, 114, 99);
        icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(AWRegistry.FORGE_ANVIL.get().asItem()));
    }

    @Override
    public RecipeType<IAetheriumAnvilRecipe> getRecipeType() {
        return JEIPlugin.AETHERIUM_ANVIL;
    }

    @Override
    public Component getTitle() {
        return title;
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, IAetheriumAnvilRecipe recipe, IFocusGroup iFocusGroup) {
        builder.addSlot(RecipeIngredientRole.INPUT, 19, 18).addIngredients(recipe.getDisplayInput());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 80, 18).addItemStack(recipe.getResultItem());
        builder.addSlot(RecipeIngredientRole.CATALYST, 19, 1).addItemStack(new ItemStack(RegistryManager.TINKER_HAMMER.get().asItem()));
    }

    @SuppressWarnings("resource")
    @Override
    public void draw(IAetheriumAnvilRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        Font fontRenderer = Minecraft.getInstance().font;

        //number of hits
        GuiCodex.drawTextGlowing(fontRenderer, guiGraphics, Component.translatable(Aetherworks.MODID + ".jei.recipe.aetherium_anvil.hits", recipe.getNumberOfHits()).getVisualOrderText(), 42, 31);
        int y = 45;
        int x = 7;

        final Vec3 min = new Vec3(255, 255, 255);
        final Vec3 mid1 = new Vec3(255, 240, 100);
        final Vec3 mid2 = new Vec3(255, 190, 0);
        final Vec3 max = new Vec3(255, 77, 77);

        final Vec3 colorLow = Utils.lerpMultiColor((recipe.getTemperatureMin() / 1000d) , min, mid1, mid2, max);
        final Vec3 colorHigh = Utils.lerpMultiColor((recipe.getTemperatureMax() / 1000d) , min, mid1, mid2, max);

        drawComponentsAsOne(fontRenderer, guiGraphics, x, y,
                Component.translatable(Aetherworks.MODID + ".jei.recipe.aetherium_anvil.difficulty.title"),
                Component.translatable(Aetherworks.MODID + ".jei.recipe.aetherium_anvil.difficulty." + Math.min(7, Math.max(1, recipe.getDifficulty()))));
        y += fontRenderer.lineHeight + 2;

        //render Ember Crystal sprite in gui
        guiGraphics.renderFakeItem(new ItemStack(RegistryManager.EMBER_SHARD.get()), x - 5, y - 5);

        Misc.drawComponents(fontRenderer, guiGraphics, x, y,
                Component.translatable(Aetherworks.MODID + ".jei.recipe.aetherium_anvil.ember", recipe.getEmberPerHit()),
                Component.translatable(Aetherworks.MODID + ".jei.recipe.aetherium_anvil.temperature"));

        y += 2 * (fontRenderer.lineHeight + 2);
        String tempText = Component.translatable(Aetherworks.MODID + ".jei.recipe.aetherium_anvil.temperature.value", recipe.getTemperatureMin(), recipe.getTemperatureMax()).getString();
        drawTextGradientCentered(fontRenderer, guiGraphics, x + 5 + (int)(fontRenderer.width(tempText) * 0.5), y, tempText, colorLow, colorHigh);
    }

    public static void drawComponentsAsOne(Font fontRenderer, GuiGraphics guiGraphics, int x, int y, Component... components) {
        StringBuilder draw = new StringBuilder();
        for (Component component : components) {
            draw.append(component.getString());
        }
        guiGraphics.drawString(fontRenderer, draw.toString(), x, y, 0xFFFFFF);
    }
    public static void drawTextGradientCentered(Font fontRenderer, GuiGraphics guiGraphics, int x, int y, String text, Vec3 colorLeft, Vec3 colorRight) {
        int adjust = (int)(-fontRenderer.width(text) * 0.5);
        for (int z = 0; z < text.length(); z++) {
            Vec3 color = colorLeft.lerp(colorRight, (double)z / text.length());
            int colorVal = (int)color.x << 16 | (int)color.y << 8 | (int)color.z;
            String c =String.valueOf(text.charAt(z));
            guiGraphics.drawString(fontRenderer, c, x + adjust, y, colorVal);
            adjust += fontRenderer.width(c);
        }
    }
}
