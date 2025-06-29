package com.dragonfox.toyofheart.fabric.client;

import com.dragonfox.toyofheart.fabriclike.ToyOfHeartClientFabricLike;
import net.fabricmc.api.ClientModInitializer;

public final class ToyOfHeartFabricClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ToyOfHeartClientFabricLike.init();
	}
}
