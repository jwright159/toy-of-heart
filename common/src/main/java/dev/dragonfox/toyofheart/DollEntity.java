package dev.dragonfox.toyofheart;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class DollEntity extends PathfinderMob {
	private static final EntityDataAccessor<Optional<DollPart>> DOLL_PARTS = SynchedEntityData.defineId(DollEntity.class, DollPart.OPTIONAL_SERIALIZER);

	public DollEntity(EntityType<? extends PathfinderMob> entityType, Level level, DollPart parts) {
		super(entityType, level);
		entityData.set(DOLL_PARTS, Optional.of(parts));
		refreshDimensions();
	}

	public static DollEntity spawnDefault(EntityType<DollEntity> type, Level level) {
		return new DollEntity(type, level, new DollPart(ToyOfHeart.SLIM_BODY.get().getDefaultInstance()));
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder
				.define(DOLL_PARTS, Optional.empty());
	}

	@Override
	public void onSyncedDataUpdated(EntityDataAccessor<?> entityDataAccessor) {
		super.onSyncedDataUpdated(entityDataAccessor);
		if (DOLL_PARTS.equals(entityDataAccessor)) {
			refreshDimensions();
		}
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compoundTag) {
//		ToyOfHeart.LOGGER.info("Loading doll with tag: {}", compoundTag);
		super.readAdditionalSaveData(compoundTag);
		if (compoundTag.contains("DollParts", 10)) {
			DollPart.OPTIONAL_CODEC
					.parse(NbtOps.INSTANCE, compoundTag.get("DollParts"))
					.resultOrPartial(ToyOfHeart.LOGGER::error)
					.ifPresent(parts -> this.entityData.set(DOLL_PARTS, parts));
		} else {
			this.entityData.set(DOLL_PARTS, Optional.empty());
		}
		refreshDimensions();
//		ToyOfHeart.LOGGER.info("Loaded parts {}", this.getDollParts());
	}

	@Override
	public void addAdditionalSaveData(CompoundTag compoundTag) {
//		ToyOfHeart.LOGGER.info("Saving doll with parts {}", this.getDollParts());
		super.addAdditionalSaveData(compoundTag);
		DollPart.OPTIONAL_CODEC
				.encodeStart(NbtOps.INSTANCE, this.getDollParts())
				.resultOrPartial(ToyOfHeart.LOGGER::error)
				.ifPresent(tag -> compoundTag.put("DollParts", tag));
//		ToyOfHeart.LOGGER.info("Saved tag: {}", compoundTag);
	}

	public Optional<DollPart> getDollParts() {
		return entityData.get(DOLL_PARTS);
	}

	@Override
	protected void dropCustomDeathLoot(ServerLevel serverLevel, DamageSource damageSource, boolean bl) {
		super.dropCustomDeathLoot(serverLevel, damageSource, bl);
		getDollParts().ifPresent(parts -> parts.allDollParts().map(DollPart::itemStack).forEach(this::spawnAtLocation));
	}

	@Override
	protected @NotNull EntityDimensions getDefaultDimensions(Pose pose) {
		return EntityDimensions.scalable(0.75f, getDollParts().map(DollPart::height).orElse(0.75f));
	}
}
