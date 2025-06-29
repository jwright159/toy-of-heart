package com.dragonfox.toyofheart.fabric;

import com.dragonfox.toyofheart.fabriclike.ToyOfHeartFabricLike;
import net.fabricmc.api.ModInitializer;

public final class ToyOfHeartFabric implements ModInitializer {
	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		// Run the Fabric-like setup.
		ToyOfHeartFabricLike.init();
	}
}
