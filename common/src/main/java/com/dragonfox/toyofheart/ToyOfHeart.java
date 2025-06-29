package com.dragonfox.toyofheart;

import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.level.entity.EntityAttributeRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
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
					Text.translatable("itemGroup.toy_of_heart"), // Tab Name
					() -> new ItemStack(ToyOfHeart.DOLL_ITEM.get()) // Icon
			)
	);

	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(MOD_ID, RegistryKeys.BLOCK);
	public static final RegistrySupplier<Block> ASSEMBLER_BLOCK = BLOCKS.register("assembler", () -> new AssemblerBlock(Block.Settings.copy(net.minecraft.block.Blocks.STONE).nonOpaque()));

	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(MOD_ID, RegistryKeys.ITEM);
	public static final RegistrySupplier<Item> DOLL_ITEM = ITEMS.register("doll", () -> new Item(new Item.Settings()));
	public static final RegistrySupplier<Item> ASSEMBLER_ITEM = ITEMS.register(ASSEMBLER_BLOCK.getId(), () -> new BlockItem(ASSEMBLER_BLOCK.get(), new Item.Settings().arch$tab(ToyOfHeart.TAB)));
	public static final RegistrySupplier<Item> SLIM_BODY = ITEMS.register("slim_body", () -> new Item(new Item.Settings().arch$tab(ToyOfHeart.TAB)));

	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(MOD_ID, RegistryKeys.BLOCK_ENTITY_TYPE);
	public static final RegistrySupplier<BlockEntityType<Assembler>> ASSEMBLER = BLOCK_ENTITIES.register("assembler", () -> BlockEntityType.Builder.create(Assembler::new, ASSEMBLER_BLOCK.get()).build(null));

	public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(MOD_ID, RegistryKeys.ENTITY_TYPE);
	public static final RegistrySupplier<EntityType<Doll>> DOLL = ENTITIES.register("doll", () -> EntityType.Builder.create(Doll::new, SpawnGroup.MISC).setDimensions(0.75f, 0.75f).build("doll"));

	public static void init() {
		TABS.register();
		BLOCKS.register();
		ITEMS.register();
		BLOCK_ENTITIES.register();
		ENTITIES.register();

		EntityAttributeRegistry.register(DOLL, Doll::createMobAttributes);
	}
}
