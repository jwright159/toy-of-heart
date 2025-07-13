package dev.dragonfox.toyofheart.client;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.dragonfox.toyofheart.DollEntity;
import dev.dragonfox.toyofheart.DollPart;
import dev.dragonfox.toyofheart.ToyOfHeart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;

public class DollRenderer extends MobRenderer<DollEntity, DollModel> {
	public DollRenderer(EntityRendererProvider.Context context) {
		super(context, new DollModel(), 0.5f);
	}

	@Override
	public void render(DollEntity entity, float yaw, float tickDelta, PoseStack pose, MultiBufferSource buffers, int light) {
		if (entity.getDollParts().isPresent())
		{
			DollPart parts = entity.getDollParts().get();
			pose.pushPose();

			Quaternionf rot = new Quaternionf().rotationY((float) Math.toRadians(entity.getYRot()));
			pose.mulPose(rot);
			pose.translate(0, -parts.minY(), 0);

			DollPartRenderer.render(parts, entity, pose, buffers, light);

			pose.popPose();
		}
		super.render(entity, yaw, tickDelta, pose, buffers, light);
	}

	@Override
	public @NotNull ResourceLocation getTextureLocation(DollEntity entity) {
		return ResourceLocation.fromNamespaceAndPath(ToyOfHeart.MOD_ID, "textures/entity/doll.png");
	}
}
