package com.dragonfox.toyofheart.quilt.client;

import com.dragonfox.toyofheart.fabriclike.ToyOfHeartClientFabricLike;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;

public final class ToyOfHeartFabricQuilt implements ClientModInitializer {
	@Override
	public void onInitializeClient(ModContainer modContainer) {
		ToyOfHeartClientFabricLike.init();
	}
}
