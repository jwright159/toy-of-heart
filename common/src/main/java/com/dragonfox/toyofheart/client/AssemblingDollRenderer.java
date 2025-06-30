package com.dragonfox.toyofheart.client;

import com.dragonfox.toyofheart.AssemblingDoll;
import com.dragonfox.toyofheart.ToyOfHeart;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;

public class AssemblingDollRenderer extends EntityRenderer<AssemblingDoll> {
	public AssemblingDollRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public void render(AssemblingDoll entity, float yaw, float tickDelta, PoseStack pose, MultiBufferSource buffers, int light) {
		pose.pushPose();

        pose.translate(0.0, 0.5, 0.0);
		pose.mulPose(new Quaternionf().rotationY((float)Math.toRadians(entity.getYRot())));
		Minecraft.getInstance().getItemRenderer().renderStatic(entity.getRootPart(), ItemDisplayContext.NONE, light, OverlayTexture.NO_OVERLAY, pose, buffers, entity.level(), 0);

		pose.popPose();
		super.render(entity, yaw, tickDelta, pose, buffers, light);
	}

	@Override
	public @NotNull ResourceLocation getTextureLocation(AssemblingDoll entity) {
		return ResourceLocation.fromNamespaceAndPath(ToyOfHeart.MOD_ID, "textures/entity/doll.png");
	}
}
