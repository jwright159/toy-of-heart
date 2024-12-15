package com.dragon_fox.toy_of_heart.quilt;

import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;

import com.dragon_fox.toy_of_heart.fabriclike.ToyOfHeartFabricLike;

public final class ToyOfHeartQuilt implements ModInitializer {
    @Override
    public void onInitialize(ModContainer mod) {
        // Run the Fabric-like setup.
        ToyOfHeartFabricLike.init();
    }
}
