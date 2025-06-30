package com.dragonfox.toyofheart.fabric.client;

import com.dragonfox.toyofheart.ToyOfHeart;
import com.dragonfox.toyofheart.client.AssemblingDollRenderer;
import com.dragonfox.toyofheart.client.DollModel;
import com.dragonfox.toyofheart.client.DollRenderer;
import com.dragonfox.toyofheart.client.ToyOfHeartClient;
import dev.architectury.registry.client.level.entity.EntityModelLayerRegistry;
import dev.architectury.registry.client.level.entity.EntityRendererRegistry;
import net.fabricmc.api.ClientModInitializer;

public final class ToyOfHeartFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ToyOfHeartClient.init();

        EntityRendererRegistry.register(ToyOfHeart.DOLL, DollRenderer::new);
        EntityModelLayerRegistry.register(ToyOfHeartClient.DOLL_MODEL_LAYER, DollModel::createBodyLayer);
        EntityRendererRegistry.register(ToyOfHeart.ASSEMBLING_DOLL, AssemblingDollRenderer::new);
    }
}
