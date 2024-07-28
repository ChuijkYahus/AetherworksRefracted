package net.sirplop.aetherworks.blockentity.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.sirplop.aetherworks.Aetherworks;
import net.sirplop.aetherworks.blockentity.ForgeCoreBlockEntity;
import net.sirplop.aetherworks.util.FaceRendererUtil;

public class RenderForge implements BlockEntityRenderer<ForgeCoreBlockEntity> {
    public RenderForge(BlockEntityRendererProvider.Context context)  { }

    public static final ResourceLocation LOCATION_FORGE_OVERLAY = new ResourceLocation(Aetherworks.MODID, "textures/block/forge/overlay.png");
    public static final float[] COLOR = {1, 1, 1, 1};
    public static final int[] LIGHTMAP = {240, 240};

    private static final int frameTime = 20;
    private static final int frameCount = 20;
    private static final float frameUVShift = 1f / frameCount;
    private int frame = 0;
    private float time = 0;
    private boolean flip = false;

    @Override
    public void render(ForgeCoreBlockEntity blockEntity, float pPartialTick, PoseStack poseStack,
                       MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {
        if (!blockEntity.isStructureValid) {
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

        VertexConsumer overlayBuffer = pBuffer.getBuffer(RenderType.text(LOCATION_FORGE_OVERLAY));
        RenderSystem.enableDepthTest();

        float uv0 = frameUVShift * frame;
        float uv1 = frameUVShift * (frame + 1);

        //render all 12 faces of the forge
        poseStack.translate(-1, -1, -1); //north-west corner
        FaceRendererUtil.renderFace(Direction.NORTH, overlayBuffer, poseStack, COLOR, LIGHTMAP, 0, 1, uv0, uv1);
        FaceRendererUtil.renderFace(Direction.WEST, overlayBuffer, poseStack, COLOR, LIGHTMAP, 0, 1, uv0, uv1);
        poseStack.translate(1, 0, 0); //north side
        FaceRendererUtil.renderFace(Direction.NORTH, overlayBuffer, poseStack, COLOR, LIGHTMAP, 0, 1, uv0, uv1);
        poseStack.translate(1, 0, 0); //north-east corner
        FaceRendererUtil.renderFace(Direction.NORTH, overlayBuffer, poseStack, COLOR, LIGHTMAP, 0, 1, uv0, uv1);
        FaceRendererUtil.renderFace(Direction.EAST, overlayBuffer, poseStack, COLOR, LIGHTMAP, 0, 1, uv0, uv1);
        poseStack.translate(0, 0, 1); //east side
        FaceRendererUtil.renderFace(Direction.EAST, overlayBuffer, poseStack, COLOR, LIGHTMAP, 0, 1, uv0, uv1);
        poseStack.translate(0, 0, 1); //south-east corner
        FaceRendererUtil.renderFace(Direction.SOUTH, overlayBuffer, poseStack, COLOR, LIGHTMAP, 0, 1, uv0, uv1);
        FaceRendererUtil.renderFace(Direction.EAST, overlayBuffer, poseStack, COLOR, LIGHTMAP, 0, 1, uv0, uv1);
        poseStack.translate(-1, 0, 0); //south side
        FaceRendererUtil.renderFace(Direction.SOUTH, overlayBuffer, poseStack, COLOR, LIGHTMAP, 0, 1, uv0, uv1);
        poseStack.translate(-1, 0, 0); //south-west corner
        FaceRendererUtil.renderFace(Direction.SOUTH, overlayBuffer, poseStack, COLOR, LIGHTMAP, 0, 1, uv0, uv1);
        FaceRendererUtil.renderFace(Direction.WEST, overlayBuffer, poseStack, COLOR, LIGHTMAP, 0, 1, uv0, uv1);
        poseStack.translate(0, 0, -1); //west side
        FaceRendererUtil.renderFace(Direction.WEST, overlayBuffer, poseStack, COLOR, LIGHTMAP, 0, 1, uv0, uv1);
    }
}
