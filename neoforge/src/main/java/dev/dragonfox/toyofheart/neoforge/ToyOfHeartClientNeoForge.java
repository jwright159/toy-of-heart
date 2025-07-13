package dev.dragonfox.toyofheart.neoforge;

import dev.dragonfox.toyofheart.ToyOfHeart;
import dev.dragonfox.toyofheart.client.AssemblingDollRenderer;
import dev.dragonfox.toyofheart.client.DollRenderer;
import dev.dragonfox.toyofheart.client.ToyOfHeartClient;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(modid = ToyOfHeart.MOD_ID, value = Dist.CLIENT)
public class ToyOfHeartClientNeoForge {
	@SubscribeEvent
	public static void onClientSetup(FMLClientSetupEvent event) {
		ToyOfHeartClient.init();
		EntityRenderers.register(ToyOfHeart.DOLL.get(), DollRenderer::new);
		EntityRenderers.register(ToyOfHeart.ASSEMBLING_DOLL.get(), AssemblingDollRenderer::new);
	}
}
