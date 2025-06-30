package com.dragonfox.toyofheart.client;

import com.dragonfox.toyofheart.AssemblingDoll;
import com.dragonfox.toyofheart.DollBodyPart;
import com.dragonfox.toyofheart.ToyOfHeart;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.AxisAngle4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class AssemblingDollRenderer extends EntityRenderer<AssemblingDoll> {
	public AssemblingDollRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public void render(AssemblingDoll entity, float yaw, float tickDelta, PoseStack pose, MultiBufferSource buffers, int light) {
		pose.pushPose();

		Quaternionf rot = new Quaternionf().rotationY((float)Math.toRadians(entity.getYRot()));
		pose.mulPose(rot);

		pose.pushPose();
		pose.translate(0.0, entity.getBbHeight() * 0.5, 0.0);
		Minecraft.getInstance().getItemRenderer().renderStatic(entity.getRootPart(), ItemDisplayContext.NONE, light, OverlayTexture.NO_OVERLAY, pose, buffers, entity.level(), 0);
		pose.popPose();

		LocalPlayer player = Minecraft.getInstance().player;
		if (player != null) {
			ItemStack heldItemStack = player.getItemInHand(InteractionHand.MAIN_HAND).copyWithCount(1);
			if (entity.getRootPart().getItem() instanceof DollBodyPart bodyPart && heldItemStack.getItem() instanceof DollBodyPart heldBodyPart)
			{
				DollBodyPart.RaycastResult result = bodyPart.raycast(player.getEyePosition(tickDelta), player.getViewVector(tickDelta), entity.getPosition(tickDelta), rot);
				if (result.hit) {
					pose.pushPose();

					Vec3 localHitPos = new Vec3(
							Math.round(result.localHitPos.x * 16) / 16.0,
							Math.round(result.localHitPos.y * 16) / 16.0,
							Math.round(result.localHitPos.z * 16) / 16.0
					);
					pose.translate(localHitPos.x, localHitPos.y, localHitPos.z);

					Vec3 up = new Vec3(0, 1, 0);
					Vec3 forward = new Vec3(0, 0, 1); // might be -1 idk it doesn't really matter here
					double rotDot = up.dot(result.localHitNormal);
					double rotDotThreshold = 0.95;
					Vector3f rotAxis = Math.abs(rotDot) > rotDotThreshold ? forward.toVector3f() : up.cross(result.localHitNormal).normalize().toVector3f();
					double rotAngle = Math.acos(rotDot);
					AxisAngle4f axisAngle = new AxisAngle4f((float)rotAngle, rotAxis);
					Quaternionf orientation = new Quaternionf(axisAngle);
					pose.mulPose(orientation);

					pose.translate(0, heldBodyPart.getPartHeight() / 2.0, 0);

					Minecraft.getInstance().getItemRenderer().renderStatic(heldItemStack, ItemDisplayContext.NONE, light, OverlayTexture.NO_OVERLAY, pose, buffers, entity.level(), 0);
					pose.popPose();
				}
			}
		}

		pose.popPose();
		super.render(entity, yaw, tickDelta, pose, buffers, light);
	}

	@Override
	public @NotNull ResourceLocation getTextureLocation(AssemblingDoll entity) {
		return ResourceLocation.fromNamespaceAndPath(ToyOfHeart.MOD_ID, "textures/entity/doll.png");
	}
}
