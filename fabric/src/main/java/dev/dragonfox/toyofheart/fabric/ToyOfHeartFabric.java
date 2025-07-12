package dev.dragonfox.toyofheart.fabric;

import dev.dragonfox.toyofheart.DollPart;
import dev.dragonfox.toyofheart.ToyOfHeart;
import net.fabricmc.api.ModInitializer;
import net.minecraft.network.syncher.EntityDataSerializers;

public final class ToyOfHeartFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        // Run our common setup.
        ToyOfHeart.init();

        EntityDataSerializers.registerSerializer(DollPart.SERIALIZER);
        EntityDataSerializers.registerSerializer(DollPart.OPTIONAL_SERIALIZER);
    }
}
