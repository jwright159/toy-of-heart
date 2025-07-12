package dev.dragonfox.toyofheart;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
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

import java.util.ArrayList;
import java.util.Optional;

public class AssemblingDollEntity extends Entity {
	private static final EntityDataAccessor<Optional<DollPart>> DOLL_PARTS = SynchedEntityData.defineId(AssemblingDollEntity.class, DollPart.OPTIONAL_SERIALIZER);

	public AssemblingDollEntity(EntityType<AssemblingDollEntity> type, Level level, ItemStack rootPart) {
		super(type, level);
		entityData.set(DOLL_PARTS, Optional.of(new DollPart(rootPart)));
	}

	public static AssemblingDollEntity spawnDefault(EntityType<AssemblingDollEntity> type, Level level) {
		return new AssemblingDollEntity(type, level, ItemStack.EMPTY);
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		builder.define(DOLL_PARTS, Optional.empty());
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag compoundTag) {
		if (compoundTag.contains("DollParts", 10)) {
			DollPart.OPTIONAL_CODEC
					.parse(NbtOps.INSTANCE, compoundTag.get("DollParts"))
					.resultOrPartial(ToyOfHeart.LOGGER::error)
					.ifPresent(parts -> this.entityData.set(DOLL_PARTS, parts));
		}
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag compoundTag) {
		DollPart.OPTIONAL_CODEC
				.encodeStart(NbtOps.INSTANCE, this.getDollParts())
				.resultOrPartial(ToyOfHeart.LOGGER::error)
				.ifPresent(tag -> compoundTag.put("DollParts", tag));
	}

	public Optional<DollPart> getDollParts() {
		return entityData.get(DOLL_PARTS);
	}

	@Override
	public boolean isPickable() {
		return !this.isRemoved();
	}

	@Override
	public @NotNull InteractionResult interact(Player player, InteractionHand interactionHand) {
		ItemStack heldItemStack = player.getItemInHand(InteractionHand.MAIN_HAND).copyWithCount(1);
		if (getDollParts().isPresent() && heldItemStack.getItem() instanceof DollPartItem)
		{
			DollPart parts = getDollParts().get();
			Quaternionf rot = new Quaternionf().rotationY((float)Math.toRadians(getYRot()));
			Optional<DollPart.RaycastHit> hitOptional = parts.raycastAll(player.getEyePosition(), player.getViewVector(1), position(), rot);
			if (hitOptional.isPresent() && hitOptional.get().hitPart().itemStack().getItem() instanceof DollBodyPartItem)
			{
				DollPart.RaycastHit hit = hitOptional.get();
				Matrix4f transform = new Matrix4f(hit.hitPart().transform());
				transform.translate(hit.localHitPos());
				entityData.set(DOLL_PARTS, Optional.of(new DollPart(parts.itemStack(), transform, new ArrayList<>())));
			}
		}

		return InteractionResult.PASS;
	}
}
