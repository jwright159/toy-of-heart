package com.dragonfox.toyofheart.quilt;

import com.dragonfox.toyofheart.fabriclike.ToyOfHeartFabricLike;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;

public final class ToyOfHeartQuilt implements ModInitializer {
	@Override
	public void onInitialize(ModContainer mod) {
		// Run the Fabric-like setup.
		ToyOfHeartFabricLike.init();
	}
}
