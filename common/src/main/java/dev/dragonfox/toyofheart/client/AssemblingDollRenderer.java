package dev.dragonfox.toyofheart.client;

import dev.dragonfox.toyofheart.*;
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

import java.util.Optional;

public class AssemblingDollRenderer extends EntityRenderer<AssemblingDollEntity> {
	public AssemblingDollRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public void render(AssemblingDollEntity entity, float yaw, float tickDelta, PoseStack pose, MultiBufferSource buffers, int light) {
		Optional<DollPart> dollParts = entity.getDollParts();
		if (dollParts.isPresent())
		{
			DollPart parts = dollParts.get();
			pose.pushPose();

			Quaternionf rot = new Quaternionf().rotationY((float)Math.toRadians(entity.getYRot()));
			pose.mulPose(rot);

			renderParts(parts, entity, pose, buffers, light);

			LocalPlayer player = Minecraft.getInstance().player;
			if (Minecraft.getInstance().crosshairPickEntity == entity && player != null)
			{
				ItemStack heldItemStack = player.getItemInHand(InteractionHand.MAIN_HAND).copyWithCount(1);
				if (heldItemStack.getItem() instanceof DollPartItem heldPart)
				{
					Optional<DollPart.RaycastHit> hitOptional = parts.raycastAll(player.getEyePosition(tickDelta), player.getViewVector(tickDelta), entity.getPosition(tickDelta), rot);
					if (hitOptional.isPresent() && hitOptional.get().hitPart().itemStack().getItem() instanceof DollBodyPartItem) {
						DollPart.RaycastHit hit = hitOptional.get();
						pose.pushPose();
						pose.mulPose(hit.newPartTransform(heldPart));
						Minecraft.getInstance().getItemRenderer().renderStatic(heldItemStack, ItemDisplayContext.NONE, light, OverlayTexture.NO_OVERLAY, pose, buffers, entity.level(), 0);
						pose.popPose();
					}
				}
			}

			pose.popPose();
		}
		super.render(entity, yaw, tickDelta, pose, buffers, light);
	}

	private static void renderParts(DollPart part, AssemblingDollEntity entity, PoseStack pose, MultiBufferSource buffers, int light)
	{
		pose.pushPose();
		pose.mulPose(part.transform());
		Minecraft.getInstance().getItemRenderer().renderStatic(part.itemStack(), ItemDisplayContext.NONE, light, OverlayTexture.NO_OVERLAY, pose, buffers, entity.level(), 0);
		pose.popPose();
		for (DollPart subParts : part.subParts())
			renderParts(subParts, entity, pose, buffers, light);
	}

	@Override
	public @NotNull ResourceLocation getTextureLocation(AssemblingDollEntity entity) {
		return ResourceLocation.fromNamespaceAndPath(ToyOfHeart.MOD_ID, "textures/entity/doll.png");
	}
}
