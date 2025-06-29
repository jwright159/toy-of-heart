package com.dragonfox.toyofheart.fabriclike;

import com.dragonfox.toyofheart.ToyOfHeart;
import com.dragonfox.toyofheart.client.DollModel;
import com.dragonfox.toyofheart.client.DollRenderer;
import com.dragonfox.toyofheart.client.ToyOfHeartClient;
import dev.architectury.registry.client.level.entity.EntityModelLayerRegistry;
import dev.architectury.registry.client.level.entity.EntityRendererRegistry;

public class ToyOfHeartClientFabricLike {
	public static void init() {
		ToyOfHeartClient.init();

		EntityRendererRegistry.register(ToyOfHeart.DOLL, DollRenderer::new);
		EntityModelLayerRegistry.register(ToyOfHeartClient.DOLL_MODEL_LAYER, DollModel::getTexturedModelData);
	}
}
