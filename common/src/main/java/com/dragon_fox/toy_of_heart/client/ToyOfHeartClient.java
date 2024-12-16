package com.dragon_fox.toy_of_heart.client;

import com.dragon_fox.toy_of_heart.ToyOfHeart;
import dev.architectury.registry.client.rendering.BlockEntityRendererRegistry;

public class ToyOfHeartClient {
	public static void init() {
		BlockEntityRendererRegistry.register(ToyOfHeart.ASSEMBLER.get(), AssemblerRenderer::new);
	}
}
