package net.sirplop.aetherworks;

import com.rekindled.embers.RegistryManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.sirplop.aetherworks.api.item.IHudFocus;

public class AWClientEvents {

    public static final IGuiOverlay INGAME_OVERLAY = AWClientEvents::renderIngameOverlay;
    public static ResourceLocation SHOVEL_SELECT = new ResourceLocation(Aetherworks.MODID, "textures/gui/shovel_overlay.png");

    public static void renderIngameOverlay(ForgeGui gui, GuiGraphics graphics, float partialTicks, int width, int height) {
        Minecraft mc = gui.getMinecraft();
        if (mc.options.hideGui)
            return;

        Player player = mc.player;

        if (!player.getMainHandItem().isEmpty() && player.getMainHandItem().getItem() instanceof IHudFocus held) {

            int x = width / 2 + 110;
            int y = height - 11;

            graphics.pose().pushPose();

            graphics.blit(SHOVEL_SELECT, x - 11, y - 11, 0, 0, 0, 22, 22, 22, 22);
            ItemStack focus = held.getFocus(player.getMainHandItem());
            if (!focus.isEmpty())
                graphics.renderFakeItem(focus, x - 8, y - 8);
        }
    }
}
