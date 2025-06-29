package com.dragonfox.toyofheart.client;

import com.dragonfox.toyofheart.ToyOfHeart;
import dev.architectury.registry.client.rendering.BlockEntityRendererRegistry;
import net.minecraft.client.render.entity.model.EntityModelLayer;

public class ToyOfHeartClient {
	public static final EntityModelLayer DOLL_MODEL_LAYER = new EntityModelLayer(ToyOfHeart.DOLL.getId(), "main");

	public static void init() {
		BlockEntityRendererRegistry.register(ToyOfHeart.ASSEMBLER.get(), AssemblerRenderer::new);
	}
}
