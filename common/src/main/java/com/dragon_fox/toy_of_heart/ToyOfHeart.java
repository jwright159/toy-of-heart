package com.dragon_fox.toy_of_heart;

import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;

public final class ToyOfHeart {
	public static final String MOD_ID = "toy_of_heart";

	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(MOD_ID, RegistryKeys.ITEM);

	@SuppressWarnings("UnstableApiUsage")
	public static final RegistrySupplier<Item> EXAMPLE_ITEM = ITEMS.register("example_item", () -> new Item(new Item.Settings().arch$tab(ToyOfHeart.TAB)));

	public static final DeferredRegister<ItemGroup> TABS = DeferredRegister.create(MOD_ID, RegistryKeys.ITEM_GROUP);
	public static final RegistrySupplier<ItemGroup> TAB = TABS.register(
			"toy_of_heart", // Tab ID
			() -> CreativeTabRegistry.create(
					Text.translatable("category.toy_of_heart"), // Tab Name
					() -> new ItemStack(ToyOfHeart.EXAMPLE_ITEM.get()) // Icon
			)
	);

	public static void init() {
		TABS.register();
		ITEMS.register();
	}
}
