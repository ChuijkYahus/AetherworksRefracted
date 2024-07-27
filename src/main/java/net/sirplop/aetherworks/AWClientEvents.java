package net.sirplop.aetherworks;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import com.rekindled.embers.RegistryManager;
import com.rekindled.embers.datagen.EmbersItemTags;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.sirplop.aetherworks.api.item.IHudFocus;
import net.sirplop.aetherworks.util.AetheriometerUtil;
import net.sirplop.aetherworks.util.Utils;
import org.joml.Matrix4f;
import org.joml.Random;

public class AWClientEvents {

    public static final IGuiOverlay INGAME_OVERLAY = AWClientEvents::renderIngameOverlay;
    public static ResourceLocation SHOVEL_SELECT = new ResourceLocation(Aetherworks.MODID, "textures/gui/shovel_overlay.png");

    public static ResourceLocation GAUGE = new ResourceLocation(Aetherworks.MODID, "textures/gui/aetheriometer_overlay.png");
    public static ResourceLocation GAUGE_COLOR = new ResourceLocation(Aetherworks.MODID, "textures/gui/aetheriometer_underlay.png");
    public static ResourceLocation GAUGE_POINTER = new ResourceLocation(Aetherworks.MODID, "textures/gui/aetheriometer_pointer.png");

    public static double gaugeAngle = 0;
    public static int ticks = 0;
    public static double prevT = 0;

    public static Random random = new Random();

    public static void renderIngameOverlay(ForgeGui gui, GuiGraphics graphics, float partialTicks, int width, int height) {
        Minecraft mc = gui.getMinecraft();
        if (mc.options.hideGui)
            return;

        Player player = mc.player;
        ticks++;

        if (!player.getMainHandItem().isEmpty() && player.getMainHandItem().getItem() instanceof IHudFocus held) {

            int x = width / 2 + 110;
            int y = height - 11;

            graphics.pose().pushPose();

            graphics.blit(SHOVEL_SELECT, x - 11, y - 11, 0, 0, 0, 22, 22, 22, 22);
            ItemStack focus = held.getFocus(player.getMainHandItem());
            if (focus != null && !focus.isEmpty()) {
                graphics.renderFakeItem(focus, x - 8, y - 8);
                if (held.showAmount()) {
                    Font fontRenderer = Minecraft.getInstance().font;
                    graphics.drawCenteredString(fontRenderer, Integer.toString(focus.getCount()), x, y-20, 0xFFFFFF);
                }
            }
        }

        if (player.getMainHandItem().getItem() == AWRegistry.AETHERIOMETER.get() || (player.getOffhandItem().getItem() == AWRegistry.AETHERIOMETER.get() && !player.getMainHandItem().is(EmbersItemTags.GAUGE_OVERLAY))) {
            renderAetheriometer(gui, graphics, player, partialTicks, width, height);
        }
    }

    public static void renderAetheriometer(ForgeGui gui, GuiGraphics graphics, Player player, float partialTicks, int width, int height) {
        if (player == null)
            return;

        int x = width / 2;
        int y = height / 2;

        double ratioRaw = AetheriometerUtil.getAverageInSurroundings(player.level(), player.getOnPos(), 4) / 120d;
        double ratio = Math.min(1, ratioRaw);
        if (gaugeAngle == 0) {
            gaugeAngle = 75.0 + 210.0 * ratio;
        } else {
            gaugeAngle = gaugeAngle * 0.99 + 0.01 * (75.0 + 210.0 * ratio);
            if (ratioRaw > 1.25d && gaugeAngle > 275) { //shake-a shake-a
                gaugeAngle += (random.nextFloat() - 0.4) * (ratio - 0.25) * 2.5;
                gaugeAngle = Math.max(275, Math.min(300, gaugeAngle));
            }
        }

        graphics.pose().pushPose();
        if (gaugeAngle > 0) { //90% of the time we don't even need to render the color background.
            Vec3 startColor = new Vec3(33f / 255f, 178 / 255f, 1f);
            Vec3 endColor = new Vec3(135f / 255f, 223 / 255f, 1f);

            double colorRatio = gaugeAngle / 285;
            double t = Math.abs((((ticks % 120) * 2) / 120d) - 1);
            double tChange = (t - prevT) * Utils.mix(0.01, 1, colorRatio);
            double tReal = Math.max(0, Math.min(1, prevT + tChange));
            Vec3 color = startColor.lerp(endColor, tReal);
            prevT = tReal;

            colorBlit(graphics, GAUGE_COLOR, x - 16, y - 16,0, 32, 32, 0, 0, 32, 32, color, (float)colorRatio);
        }

        graphics.blit(GAUGE, x - 16, y - 16, 0, 0, 0, 32, 32, 32, 32);

        graphics.pose().translate(x - 2, y, 0);
        graphics.pose().mulPose(Axis.ZP.rotationDegrees((float) gaugeAngle));
        graphics.pose().translate(-2.5, -2.5, 0);

        graphics.blit(GAUGE_POINTER, 0, 0, 0, 0, 0, 12, 5, 16, 16);

        graphics.pose().popPose();
    }

    private static void colorBlit(GuiGraphics graphics, ResourceLocation pAtlasLocation, int pX, int pY, int pBlitOffset, int pUWidth, int pVHeight, float pUOffset, float pVOffset, int pTextureWidth, int pTextureHeight, Vec3 color, float alpha) {
        innerColorBlit(graphics, pAtlasLocation, pX, pX + pUWidth, pY, pY + pVHeight, pBlitOffset, (pUOffset + 0.0F) / (float)pTextureWidth, (pUOffset + (float)pUWidth) / (float)pTextureWidth, (pVOffset + 0.0F) / (float)pTextureHeight, (pVOffset + (float)pVHeight) / (float)pTextureHeight,
                (float)color.x, (float)color.y, (float)color.z, alpha);
    }
    private static void innerColorBlit(GuiGraphics graphics, ResourceLocation pAtlasLocation, int pX1, int pX2, int pY1, int pY2, int pBlitOffset, float pMinU, float pMaxU, float pMinV, float pMaxV, float r, float g, float b, float a) {
        RenderSystem.setShaderTexture(0, pAtlasLocation);
        RenderSystem.setShaderColor(r, g, b, a);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        Matrix4f matrix4f = graphics.pose().last().pose();
        BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferbuilder.vertex(matrix4f, (float)pX1, (float)pY1, (float)pBlitOffset).uv(pMinU, pMinV).endVertex();
        bufferbuilder.vertex(matrix4f, (float)pX1, (float)pY2, (float)pBlitOffset).uv(pMinU, pMaxV).endVertex();
        bufferbuilder.vertex(matrix4f, (float)pX2, (float)pY2, (float)pBlitOffset).uv(pMaxU, pMaxV).endVertex();
        bufferbuilder.vertex(matrix4f, (float)pX2, (float)pY1, (float)pBlitOffset).uv(pMaxU, pMinV).endVertex();
        BufferUploader.drawWithShader(bufferbuilder.end());
        RenderSystem.setShaderColor(1, 1, 1, 1);
    }
}
