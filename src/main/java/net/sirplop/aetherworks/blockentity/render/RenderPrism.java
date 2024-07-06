package net.sirplop.aetherworks.blockentity.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.sirplop.aetherworks.Aetherworks;
import net.sirplop.aetherworks.blockentity.PrismBlockEntity;
import net.sirplop.aetherworks.util.FaceRendererUtil;
import org.joml.Vector3d;

import java.util.Random;

public class RenderPrism implements BlockEntityRenderer<PrismBlockEntity>
{
    public static final ResourceLocation LOCATION_PRISM_OVERLAY = new ResourceLocation(Aetherworks.MODID, "textures/block/prism_active_overlay.png");
    public static final ResourceLocation LOCATION_RUNES = new ResourceLocation(Aetherworks.MODID, "textures/block/prism_runes.png");

    public static final Random RANDOM = new Random();
    public static final float[] COLOR = {1, 1, 1, 1};
    public static final int[] LIGHTMAP = {240, 240};

    private static final int runeCount = 5;
    private static final float runeUV = 1f / runeCount;
    private static final int frameTime = 100;
    private static final int frameCount = 20;
    private static final float frameUVShift = 1f / frameCount;
    private int frame = 0;
    private float time = 0;
    private boolean flip = false;

    public RenderPrism(BlockEntityRendererProvider.Context context)  { }

    @Override
    public void render(PrismBlockEntity blockEntity, float pPartialTick, PoseStack poseStack,
                       MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {
        if (!blockEntity.isStructureValid()) {
            return;
        }
        time += pPartialTick;
        if (time >= frameTime)
        {
            time = 0;
            if (flip)
            {
                if (--frame <= -1)
                {
                    flip = false;
                    frame += 2;
                }
            }
            else
            {
                if (++frame >= frameCount)
                {
                    flip = true;
                    frame -= 2;
                }
            }
        }
        VertexConsumer overlayBuffer = pBuffer.getBuffer(RenderType.text(LOCATION_PRISM_OVERLAY));
        RenderSystem.enableDepthTest();

        float uv0 = frameUVShift * frame;
        float uv1 = frameUVShift * (frame + 1);


        poseStack.translate(3, -1, 0); //3, -1, 0
        FaceRendererUtil.renderHorizontals(overlayBuffer, poseStack, COLOR, LIGHTMAP, 0, 1, uv0, uv1);
        poseStack.translate(-6, 0, 0); //-3, -1, 0
        FaceRendererUtil.renderHorizontals(overlayBuffer, poseStack, COLOR, LIGHTMAP,0, 1, uv0, uv1);
        poseStack.translate(3, 0, 3);  //0, -1, 3
        FaceRendererUtil.renderHorizontals(overlayBuffer, poseStack, COLOR, LIGHTMAP,0, 1, uv0, uv1);
        poseStack.translate(0, 0, -6); //0, -1, -3
        FaceRendererUtil.renderHorizontals(overlayBuffer, poseStack, COLOR, LIGHTMAP,0, 1, uv0, uv1);
        poseStack.translate(2, -1, 5); //2, -2, 2
        FaceRendererUtil.renderHorizontals(overlayBuffer, poseStack, COLOR, LIGHTMAP,0, 1, uv0, uv1);
        poseStack.translate(0, 0, -4); //2, -2, -2
        FaceRendererUtil.renderHorizontals(overlayBuffer, poseStack, COLOR, LIGHTMAP,0, 1, uv0, uv1);
        poseStack.translate(-4, 0, 4); //-2, -2, 2
        FaceRendererUtil.renderHorizontals(overlayBuffer, poseStack, COLOR, LIGHTMAP,0, 1, uv0, uv1);
        poseStack.translate(0, 0, -4); //-2, -2, -2
        FaceRendererUtil.renderHorizontals(overlayBuffer, poseStack, COLOR, LIGHTMAP,0, 1, uv0, uv1);
        poseStack.translate(2, 2, 2);

        VertexConsumer runeBuffer = pBuffer.getBuffer(RenderType.text(LOCATION_RUNES));
        RenderSystem.enableDepthTest();

        //random rune seed - noisy enough?
        RANDOM.setSeed(((long) blockEntity.getBlockPos().getX() <<16) + ((long) blockEntity.getBlockPos().getY() <<8)+ blockEntity.getBlockPos().getZ());

        renderRune(runeBuffer, poseStack, new Vector3d(3, -2, 0));
        renderRune(runeBuffer, poseStack, new Vector3d(-3, -2, 0));
        renderRune(runeBuffer, poseStack, new Vector3d(0, -2, 3));
        renderRune(runeBuffer, poseStack, new Vector3d(0, -2, -3));
        renderRune(runeBuffer, poseStack, new Vector3d(2, -3, 2));
        renderRune(runeBuffer, poseStack, new Vector3d(2, -3, -2));
        renderRune(runeBuffer, poseStack, new Vector3d(-2, -3, 2));
        renderRune(runeBuffer, poseStack, new Vector3d(-2, -3, -2));
    }
    private static final Float[] xUVArr = new Float[] {0f, 1f};
    private void renderRune(VertexConsumer buffer, PoseStack pose, Vector3d at)
    {
        pose.translate(at.x, at.y, at.z);
        FaceRendererUtil.renderHorizontals(buffer, pose, COLOR, LIGHTMAP, () -> xUVArr, () -> {
            int val = RANDOM.nextInt(runeCount);
            return new Float[] {val * runeUV, (val + 1) * runeUV};
        });
        pose.translate(-at.x, -at.y, -at.z);
    }
}
