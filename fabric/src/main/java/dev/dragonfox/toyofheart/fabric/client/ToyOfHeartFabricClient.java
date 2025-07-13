package dev.dragonfox.toyofheart.fabric.client;

import dev.dragonfox.toyofheart.ToyOfHeart;
import dev.dragonfox.toyofheart.client.AssemblingDollRenderer;
import dev.dragonfox.toyofheart.client.DollModel;
import dev.dragonfox.toyofheart.client.DollRenderer;
import dev.dragonfox.toyofheart.client.ToyOfHeartClient;
import dev.architectury.registry.client.level.entity.EntityModelLayerRegistry;
import dev.architectury.registry.client.level.entity.EntityRendererRegistry;
import net.fabricmc.api.ClientModInitializer;

public final class ToyOfHeartFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ToyOfHeartClient.init();

        EntityRendererRegistry.register(ToyOfHeart.DOLL, DollRenderer::new);
        EntityRendererRegistry.register(ToyOfHeart.ASSEMBLING_DOLL, AssemblingDollRenderer::new);
    }
}
