package com.dragon_fox.toy_of_heart;

import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;

@SuppressWarnings("UnstableApiUsage")
public final class ToyOfHeart {
	public static final String MOD_ID = "toy_of_heart";


	public static final DeferredRegister<ItemGroup> TABS = DeferredRegister.create(MOD_ID, RegistryKeys.ITEM_GROUP);

	public static final RegistrySupplier<ItemGroup> TAB = TABS.register(
			"toy_of_heart", // Tab ID
			() -> CreativeTabRegistry.create(
					Text.translatable("category.toy_of_heart"), // Tab Name
					() -> new ItemStack(ToyOfHeart.DOLL_ITEM.get()) // Icon
			)
	);


	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(MOD_ID, RegistryKeys.BLOCK);

	public static final RegistrySupplier<Block> ASSEMBLER = BLOCKS.register("assembler", () -> new Block(Block.Settings.copy(net.minecraft.block.Blocks.STONE)));


	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(MOD_ID, RegistryKeys.ITEM);

	public static final RegistrySupplier<Item> DOLL_ITEM = ITEMS.register("doll", () -> new Item(new Item.Settings()));

	public static final RegistrySupplier<Item> ASSEMBLER_ITEM = ITEMS.register(ASSEMBLER.getId(), () -> new BlockItem(ASSEMBLER.get(), new Item.Settings().arch$tab(ToyOfHeart.TAB)));


	public static void init() {
		TABS.register();
		BLOCKS.register();
		ITEMS.register();
	}
}
