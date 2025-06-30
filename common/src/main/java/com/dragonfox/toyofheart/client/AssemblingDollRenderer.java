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
		Quaternionf rot = new Quaternionf().rotationY((float)Math.toRadians(entity.getYRot()));
		pose.mulPose(rot);
		Minecraft.getInstance().getItemRenderer().renderStatic(entity.getRootPart(), ItemDisplayContext.NONE, light, OverlayTexture.NO_OVERLAY, pose, buffers, entity.level(), 0);

		LocalPlayer player = Minecraft.getInstance().player;
		if (player != null) {
			ItemStack heldItemStack = player.getItemInHand(InteractionHand.MAIN_HAND).copyWithCount(1);
			if (entity.getRootPart().getItem() instanceof DollBodyPart bodyPart)
			{
				DollBodyPart.RaycastResult result = bodyPart.raycast(player.getEyePosition(tickDelta), player.getViewVector(tickDelta), entity.getPosition(tickDelta), rot);
				if (result.hit) {
					pose.pushPose();
					pose.translate(result.localHitPos.x, result.localHitPos.y, result.localHitPos.z);
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
