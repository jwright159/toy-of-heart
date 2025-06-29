package com.dragonfox.toyofheart.client;

import com.dragonfox.toyofheart.Doll;
import com.dragonfox.toyofheart.ToyOfHeart;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

public class DollRenderer extends MobEntityRenderer<Doll, DollModel> {
	public DollRenderer(EntityRendererFactory.Context context) {
		super(context, new DollModel(context.getPart(ToyOfHeartClient.DOLL_MODEL_LAYER)), 0.5f);
	}

	@Override
	public Identifier getTexture(Doll entity) {
		return Identifier.of(ToyOfHeart.MOD_ID, "textures/entity/doll.png");
	}
}
