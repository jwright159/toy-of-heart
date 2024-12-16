package com.dragon_fox.toy_of_heart.quilt.client;

import com.dragon_fox.toy_of_heart.fabriclike.ToyOfHeartClientFabricLike;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;

public final class ToyOfHeartFabricQuilt implements ClientModInitializer {
	@Override
	public void onInitializeClient(ModContainer modContainer) {
		ToyOfHeartClientFabricLike.init();
	}
}
