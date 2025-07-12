package dev.dragonfox.toyofheart.neoforge;

import dev.dragonfox.toyofheart.DollPart;
import dev.dragonfox.toyofheart.ToyOfHeart;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.neoforge.registries.RegisterEvent;

@Mod(ToyOfHeart.MOD_ID)
@EventBusSubscriber(modid = ToyOfHeart.MOD_ID)
public final class ToyOfHeartNeoForge {
	public ToyOfHeartNeoForge(IEventBus eventBus) {
		// Run our common setup.
		ToyOfHeart.init();
	}

	@SubscribeEvent
	public static void register(RegisterEvent event) {
		event.register(
				NeoForgeRegistries.Keys.ENTITY_DATA_SERIALIZERS,
				registry -> {
					registry.register(ResourceLocation.fromNamespaceAndPath(ToyOfHeart.MOD_ID, "doll_parts"), DollPart.SERIALIZER);
					registry.register(ResourceLocation.fromNamespaceAndPath(ToyOfHeart.MOD_ID, "optional_doll_parts"), DollPart.OPTIONAL_SERIALIZER);
				}
		);
	}
}
