package com.dragonfox.toyofheart.client;

import com.dragonfox.toyofheart.ToyOfHeart;
import dev.architectury.registry.client.rendering.BlockEntityRendererRegistry;

public class ToyOfHeartClient {
	public static void init() {
		BlockEntityRendererRegistry.register(ToyOfHeart.ASSEMBLER.get(), AssemblerRenderer::new);
	}
}
