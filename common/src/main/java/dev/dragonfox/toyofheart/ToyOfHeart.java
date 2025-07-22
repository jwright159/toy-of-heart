package dev.dragonfox.toyofheart;

import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.level.entity.EntityAttributeRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("UnstableApiUsage")
public final class ToyOfHeart {
	public static final String MOD_ID = "toy_of_heart";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(MOD_ID, Registries.CREATIVE_MODE_TAB);
	public static final RegistrySupplier<CreativeModeTab> TAB = TABS.register(
			"toy_of_heart", // Tab ID
			() -> CreativeTabRegistry.create(
					Component.translatable("itemGroup.toy_of_heart"), // Tab Name
					() -> new ItemStack(ToyOfHeart.DOLL_ITEM.get()) // Icon
			)
	);

	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(MOD_ID, Registries.BLOCK);
	public static final RegistrySupplier<Block> ASSEMBLER_BLOCK = BLOCKS.register("assembler", () -> new AssemblerBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE).noOcclusion()));

	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(MOD_ID, Registries.ITEM);
	public static final RegistrySupplier<Item> DOLL_ITEM = ITEMS.register("doll", () -> new Item(new Item.Properties()));
	public static final RegistrySupplier<Item> ASSEMBLER_ITEM = ITEMS.register(ASSEMBLER_BLOCK.getId(), () -> new BlockItem(ASSEMBLER_BLOCK.get(), new Item.Properties().arch$tab(ToyOfHeart.TAB)));
	public static final RegistrySupplier<Item> SLIM_BODY = ITEMS.register("slim_body", () -> new DollBodyPartItem(new Item.Properties().arch$tab(ToyOfHeart.TAB), 4f / 16f, 6f / 16f, 2f / 16f));
	public static final RegistrySupplier<Item> STOUT_BODY = ITEMS.register("stout_body", () -> new DollBodyPartItem(new Item.Properties().arch$tab(ToyOfHeart.TAB), 4f / 16f, 4f / 16f, 4f / 16f));
	public static final RegistrySupplier<Item> ARM = ITEMS.register("arm", () -> new DollArmPartItem(new Item.Properties().arch$tab(ToyOfHeart.TAB), 2f / 16f, 6f / 16f, 2f / 16f));
	public static final RegistrySupplier<Item> LEG = ITEMS.register("leg", () -> new DollLegPartItem(new Item.Properties().arch$tab(ToyOfHeart.TAB), 2f / 16f, 6f / 16f, 2f / 16f));

	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(MOD_ID, Registries.BLOCK_ENTITY_TYPE);
	public static final RegistrySupplier<BlockEntityType<AssemblerBlockEntity>> ASSEMBLER = BLOCK_ENTITIES.register("assembler", () -> BlockEntityType.Builder.of(AssemblerBlockEntity::new, ASSEMBLER_BLOCK.get()).build(null));

	public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(MOD_ID, Registries.ENTITY_TYPE);
	public static final RegistrySupplier<EntityType<DollEntity>> DOLL = ENTITIES.register("doll", () -> EntityType.Builder.of(DollEntity::spawnDefault, MobCategory.MISC).sized(4f / 16f, 6f / 16f).build("doll"));
	public static final RegistrySupplier<EntityType<AssemblingDollEntity>> ASSEMBLING_DOLL = ENTITIES.register("assembling_doll", () -> EntityType.Builder.of(AssemblingDollEntity::spawnDefault, MobCategory.MISC).sized(4f / 16f, 6f / 16f).build("assembling_doll"));

	public static void init() {
		TABS.register();
		BLOCKS.register();
		ITEMS.register();
		BLOCK_ENTITIES.register();
		ENTITIES.register();

		EntityAttributeRegistry.register(DOLL, DollEntity::createMobAttributes);
	}
}
