package net.sirplop.aetherworks.util;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.core.Direction;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Matrix4f;
import java.util.function.Supplier;

@OnlyIn(Dist.CLIENT)
public class FaceRendererUtil
{
    public static void renderFaceNorthSouth(VertexConsumer buffer, PoseStack pose,
                                       float[] color, int[] lightmap,
                                       float minUVx, float maxUVx, float minUVy, float maxUVy) {
        pose.translate(0, 0, -0.001f);
        Matrix4f matrix4f = pose.last().pose();

        buffer.vertex(matrix4f, -0.5f, -0.5f, 0).color(color[0], color[1], color[2], color[3]).uv(maxUVx, maxUVy).uv2(lightmap[0], lightmap[1]).endVertex();
        buffer.vertex(matrix4f, -0.5f, 0.5f, 0).color(color[0], color[1], color[2], color[3]).uv(maxUVx, minUVy).uv2(lightmap[0], lightmap[1]).endVertex();
        buffer.vertex(matrix4f,  0.5f, 0.5f, 0).color(color[0], color[1], color[2], color[3]).uv(minUVx, minUVy).uv2(lightmap[0], lightmap[1]).endVertex();
        buffer.vertex(matrix4f, 0.5f, -0.5f, 0).color(color[0], color[1], color[2], color[3]).uv(minUVx, maxUVy).uv2(lightmap[0], lightmap[1]).endVertex();

        pose.translate(0, 0, 0.001f);
    }

    public static void renderFaceEastWest(VertexConsumer buffer, PoseStack pose,
                                      float[] color, int[] lightmap,
                                      float minUVx, float maxUVx, float minUVy, float maxUVy) {
        pose.translate(-0.001f, 0, 0);
        Matrix4f matrix4f = pose.last().pose();

        buffer.vertex(matrix4f, 0, -0.5f, -0.5f).color(color[0], color[1], color[2], color[3]).uv(minUVx, maxUVy).uv2(lightmap[0], lightmap[1]).endVertex();
        buffer.vertex(matrix4f, 0, -0.5f, 0.5f).color(color[0], color[1], color[2], color[3]).uv(maxUVx, maxUVy).uv2(lightmap[0], lightmap[1]).endVertex();
        buffer.vertex(matrix4f, 0, 0.5f, 0.5f).color(color[0], color[1], color[2], color[3]).uv(maxUVx, minUVy).uv2(lightmap[0], lightmap[1]).endVertex();
        buffer.vertex(matrix4f, 0, 0.5f, -0.5f).color(color[0], color[1], color[2], color[3]).uv(minUVx, minUVy).uv2(lightmap[0], lightmap[1]).endVertex();

        pose.translate(0.001f, 0, 0);
    }
    public static void renderHorizontals(VertexConsumer buffer, PoseStack pose,
                                         float[] color, int[] lightmap,
                                         Supplier<Float[]> minMaxUVx, Supplier<Float[]> minMaxUVy)
    {
        Float[] uvX = minMaxUVx.get();
        Float[] uvY = minMaxUVy.get();
        //we assume the posestack is already at the desired block's int coordinates.
        pose.translate(0.5f, 0.5f, 0);          //.5, .5, 0
        renderFaceNorthSouth(buffer, pose, color, lightmap, uvX[0], uvX[1], uvY[0], uvY[1]);
        pose.translate(0, 0, 1);                //.5, .5, 1
        pose.mulPose(Axis.YP.rotationDegrees(180f));

        uvX = minMaxUVx.get();
        uvY = minMaxUVy.get();
        renderFaceNorthSouth(buffer, pose, color, lightmap, uvX[0], uvX[1], uvY[0], uvY[1]);
        pose.translate(-0.5f, 0, 0.5f);        //1, .5, .5

        uvX = minMaxUVx.get();
        uvY = minMaxUVy.get();
        renderFaceEastWest(buffer, pose, color, lightmap, uvX[0], uvX[1], uvY[0], uvY[1]);
        pose.mulPose(Axis.YP.rotationDegrees(180f));
        pose.translate(-1, 0, 0);

        uvX = minMaxUVx.get();
        uvY = minMaxUVy.get();//0, .5, .5
        renderFaceEastWest(buffer, pose, color, lightmap, uvX[0], uvX[1], uvY[0], uvY[1]);
        pose.translate(0, -0.5f, -0.5f);       //0, 0, 0
    }

    public static void renderHorizontals(VertexConsumer buffer, PoseStack pose,
                                         float[] color, int[] lightmap,
                                         float minUVx, float maxUVx, float minUVy, float maxUVy)
    {
        //we assume the posestack is already at the desired block's int coordinates.
        pose.translate(0.5f, 0.5f, 0);          //.5, .5, 0
        renderFaceNorthSouth(buffer, pose, color, lightmap, minUVx, maxUVx, minUVy, maxUVy);
        pose.translate(0, 0, 1);                //.5, .5, 1
        pose.mulPose(Axis.YP.rotationDegrees(180f));
        renderFaceNorthSouth(buffer, pose, color, lightmap, minUVx, maxUVx, minUVy, maxUVy);
        pose.translate(-0.5f, 0, 0.5f);        //1, .5, .5
        renderFaceEastWest(buffer, pose, color, lightmap, minUVx, maxUVx, minUVy, maxUVy);
        pose.mulPose(Axis.YP.rotationDegrees(180f));
        pose.translate(-1, 0, 0);              //0, .5, .5
        renderFaceEastWest(buffer, pose, color, lightmap, minUVx, maxUVx, minUVy, maxUVy);
        pose.translate(0, -0.5f, -0.5f);       //0, 0, 0
    }

    public static void renderFace(Direction direction, VertexConsumer buffer, PoseStack pose,
                                  float[] color, int[] lightmap,
                                  float minUVx, float maxUVx, float minUVy, float maxUVy)
    {
        switch (direction) {
            case NORTH -> {
                pose.translate(0.5f, 0.5f, 0);
                renderFaceNorthSouth(buffer, pose, color, lightmap, minUVx, maxUVx, minUVy, maxUVy);
                pose.translate(-0.5f, -0.5f, 0);
            }
            case SOUTH -> {
                pose.translate(0.5f, 0.5f, 1);
                pose.mulPose(Axis.YP.rotationDegrees(180f));
                renderFaceNorthSouth(buffer, pose, color, lightmap, minUVx, maxUVx, minUVy, maxUVy);
                pose.mulPose(Axis.YP.rotationDegrees(180f));
                pose.translate(-0.5f, -0.5f, -1);
            }
            case WEST -> {
                pose.translate(0, 0.5f, 0.5f);
                renderFaceEastWest(buffer, pose, color, lightmap, minUVx, maxUVx, minUVy, maxUVy);
                pose.translate(0, -0.5f, -0.5f);
            }
            case EAST -> {
                pose.translate(1, 0.5f, 0.5f);
                pose.mulPose(Axis.YP.rotationDegrees(180f));
                renderFaceEastWest(buffer, pose, color, lightmap, minUVx, maxUVx, minUVy, maxUVy);
                pose.mulPose(Axis.YP.rotationDegrees(180f));
                pose.translate(-1, -0.5f, -0.5f);
            }
            default -> {
                return;
            }
        }
    }
}
