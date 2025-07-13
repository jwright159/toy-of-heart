package dev.dragonfox.toyofheart.client;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.dragonfox.toyofheart.DollPart;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemDisplayContext;

public class DollPartRenderer {
	public static void render(DollPart part, Entity entity, PoseStack pose, MultiBufferSource buffers, int light)
	{
		pose.pushPose();
		pose.mulPose(part.transform());
		Minecraft.getInstance().getItemRenderer().renderStatic(part.itemStack(), ItemDisplayContext.NONE, light, OverlayTexture.NO_OVERLAY, pose, buffers, entity.level(), 0);
		pose.popPose();
		for (DollPart subParts : part.subParts())
			render(subParts, entity, pose, buffers, light);
	}
}
