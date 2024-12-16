package com.dragon_fox.toy_of_heart.forge.client;

import com.dragon_fox.toy_of_heart.ToyOfHeart;
import com.dragon_fox.toy_of_heart.client.ToyOfHeartClient;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = ToyOfHeart.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ToyOfHeartClientForge {
	@SubscribeEvent
	public static void onClientSetup(FMLClientSetupEvent event) {
		ToyOfHeartClient.init();
	}
}
