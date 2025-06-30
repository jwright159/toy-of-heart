package com.dragonfox.toyofheart.neoforge;

import com.dragonfox.toyofheart.ToyOfHeart;
import com.dragonfox.toyofheart.client.AssemblingDollRenderer;
import com.dragonfox.toyofheart.client.DollModel;
import com.dragonfox.toyofheart.client.DollRenderer;
import com.dragonfox.toyofheart.client.ToyOfHeartClient;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(modid = ToyOfHeart.MOD_ID, value = Dist.CLIENT)
public class ToyOfHeartClientNeoForge {
	@SubscribeEvent
	public static void onClientSetup(FMLClientSetupEvent event) {
		ToyOfHeartClient.init();
		EntityRenderers.register(ToyOfHeart.DOLL.get(), DollRenderer::new);
		EntityRenderers.register(ToyOfHeart.ASSEMBLING_DOLL.get(), AssemblingDollRenderer::new);
	}

	@SubscribeEvent
	public static void onRegisterLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(ToyOfHeartClient.DOLL_MODEL_LAYER, DollModel::createBodyLayer);
	}
}
