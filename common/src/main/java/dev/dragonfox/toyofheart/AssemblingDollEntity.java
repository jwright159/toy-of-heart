package dev.dragonfox.toyofheart;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Tuple;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

import java.util.List;
import java.util.Optional;

public class AssemblingDollEntity extends Entity {
	private static final EntityDataAccessor<Optional<DollPart>> DOLL_PARTS = SynchedEntityData.defineId(AssemblingDollEntity.class, DollPart.OPTIONAL_SERIALIZER);
	private static final EntityDataAccessor<Optional<BlockPos>> ASSEMBLER_POS = SynchedEntityData.defineId(AssemblingDollEntity.class, EntityDataSerializers.OPTIONAL_BLOCK_POS);

	public AssemblingDollEntity(EntityType<AssemblingDollEntity> type, Level level, ItemStack rootPart, Optional<BlockPos> assemblerPos) {
		super(type, level);
		entityData.set(DOLL_PARTS, Optional.of(new DollPart(rootPart)));
		entityData.set(ASSEMBLER_POS, assemblerPos);
	}

	public static AssemblingDollEntity spawnDefault(EntityType<AssemblingDollEntity> type, Level level) {
		return new AssemblingDollEntity(type, level, ToyOfHeart.SLIM_BODY.get().getDefaultInstance(), Optional.empty());
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		builder
				.define(DOLL_PARTS, Optional.empty())
				.define(ASSEMBLER_POS, Optional.empty());
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag compoundTag) {
//		ToyOfHeart.LOGGER.info("Loading doll with tag: {}", compoundTag);
		if (compoundTag.contains("DollParts", 10)) {
			DollPart.OPTIONAL_CODEC
					.parse(NbtOps.INSTANCE, compoundTag.get("DollParts"))
					.resultOrPartial(ToyOfHeart.LOGGER::error)
					.ifPresent(parts -> this.entityData.set(DOLL_PARTS, parts));
		} else {
			this.entityData.set(DOLL_PARTS, Optional.empty());
		}

		if (compoundTag.contains("AssemblerPos", 11)) {
			BlockPos pos = BlockPos.CODEC.parse(NbtOps.INSTANCE, compoundTag.get("AssemblerPos"))
					.resultOrPartial(ToyOfHeart.LOGGER::error)
					.orElse(BlockPos.ZERO);
			this.entityData.set(ASSEMBLER_POS, Optional.of(pos));
		} else {
			this.entityData.set(ASSEMBLER_POS, Optional.empty());
		}
//		ToyOfHeart.LOGGER.info("Loaded parts {} and pos {}", this.getDollParts(), this.getAssemblerPos());
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag compoundTag) {
//		ToyOfHeart.LOGGER.info("Saving doll with parts {} and pos {}", this.getDollParts(), this.getAssemblerPos());
		DollPart.OPTIONAL_CODEC
				.encodeStart(NbtOps.INSTANCE, this.getDollParts())
				.resultOrPartial(ToyOfHeart.LOGGER::error)
				.ifPresent(tag -> compoundTag.put("DollParts", tag));
		this.getAssemblerPos().flatMap(pos -> BlockPos.CODEC.encodeStart(NbtOps.INSTANCE, pos)
				.resultOrPartial(ToyOfHeart.LOGGER::error))
				.ifPresent(tag -> compoundTag.put("AssemblerPos", tag));
//		ToyOfHeart.LOGGER.info("Saved tag: {}", compoundTag);
	}

	public Optional<DollPart> getDollParts() {
		return entityData.get(DOLL_PARTS);
	}

	public Optional<BlockPos> getAssemblerPos() {
		return entityData.get(ASSEMBLER_POS);
	}

	@Override
	public boolean isPickable() {
		return !this.isRemoved();
	}

	@Override
	public @NotNull InteractionResult interact(Player player, InteractionHand interactionHand) {
//		ToyOfHeart.LOGGER.info("Interacting with doll");
		ItemStack heldItemStack = player.getItemInHand(InteractionHand.MAIN_HAND);
		if (getDollParts().isPresent() && (heldItemStack.getItem() instanceof DollPartItem || heldItemStack.isEmpty()))
		{
			DollPart parts = getDollParts().get();
			Quaternionf rot = new Quaternionf().rotationY((float)Math.toRadians(getYRot()));
			Optional<DollPart.RaycastHit> hitOptional = parts.raycastAll(player.getEyePosition(), player.getViewVector(1), position(), rot);
			if (hitOptional.isPresent())
			{
				DollPart.RaycastHit hit = hitOptional.get();

				if (hitOptional.get().hitPart().itemStack().getItem() instanceof DollBodyPartItem && heldItemStack.getItem() instanceof DollPartItem)
				{
//					ToyOfHeart.LOGGER.info("Adding onto doll");
					Matrix4f transform = new Matrix4f(hit.hitPart().transform());
					transform.translate(hit.localHitPos());

					Optional<DollPart> newParts = parts.withChildPart(hit.hitPart(), new DollPart(heldItemStack.copyWithCount(1), transform));
					if (newParts.isPresent())
					{
						if (!player.level().isClientSide)
						{
							entityData.set(DOLL_PARTS, newParts);
							if (!player.isCreative())
								heldItemStack.shrink(1);
						}
						return InteractionResult.SUCCESS;
					}
				}
				else if (heldItemStack.isEmpty())
				{
//					ToyOfHeart.LOGGER.info("Removing doll");
					Optional<Tuple<Optional<DollPart>, List<ItemStack>>> newParts = parts.withRemovedPart(hit.hitPart());
					if (newParts.isPresent())
					{
						if (!player.level().isClientSide)
						{
							Optional<DollPart> updatedParts = newParts.get().getA();
							entityData.set(DOLL_PARTS, updatedParts);
							if (updatedParts.isEmpty()) {
								if (getAssemblerPos().isPresent() && level().getBlockEntity(getAssemblerPos().get()) instanceof AssemblerBlockEntity assembler)
								{
									assembler.removeDoll();
								}
								else {
									this.discard();
								}
							}

							if (!player.isCreative()) {
								List<ItemStack> removedParts = newParts.get().getB();
								for (ItemStack removedPart : removedParts) {
									if (!removedPart.isEmpty()) {
										player.getInventory().placeItemBackInInventory(removedPart);
									}
								}
							}
						}
						return InteractionResult.SUCCESS;
					}
				}
			}
		}

		return InteractionResult.FAIL;
	}

	public List<ItemStack> disassemble() {
		List<ItemStack> items = getDollParts()
				.map(parts -> parts.allDollParts()
						.map(DollPart::itemStack)
						.toList())
				.orElse(List.of());
		entityData.set(DOLL_PARTS, Optional.empty());
		this.discard();
		return items;
	}
}
