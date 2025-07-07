package dev.dragonfox.toyofheart.client;

import dev.dragonfox.toyofheart.Doll;
import dev.dragonfox.toyofheart.ToyOfHeart;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class DollRenderer extends MobRenderer<Doll, DollModel> {
	public DollRenderer(EntityRendererProvider.Context context) {
		super(context, new DollModel(context.bakeLayer(ToyOfHeartClient.DOLL_MODEL_LAYER)), 0.5f);
	}

	@Override
	public @NotNull ResourceLocation getTextureLocation(Doll entity) {
		return ResourceLocation.fromNamespaceAndPath(ToyOfHeart.MOD_ID, "textures/entity/doll.png");
	}
}
