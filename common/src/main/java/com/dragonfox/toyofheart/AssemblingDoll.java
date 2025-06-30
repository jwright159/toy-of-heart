package com.dragonfox.toyofheart;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class AssemblingDoll extends Entity {
	private static final EntityDataAccessor<ItemStack> ROOT_PART = SynchedEntityData.defineId(AssemblingDoll.class, EntityDataSerializers.ITEM_STACK);

	public AssemblingDoll(EntityType<AssemblingDoll> type, Level level, ItemStack rootPart) {
		super(type, level);
		setRootPart(rootPart);
	}

	public static AssemblingDoll spawnDefault(EntityType<AssemblingDoll> type, Level level) {
		return new AssemblingDoll(type, level, ItemStack.EMPTY);
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		builder.define(ROOT_PART, ItemStack.EMPTY);
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag compoundTag) {
		setRootPart(ItemStack.parseOptional(registryAccess(), compoundTag.getCompound("rootPart")));
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag compoundTag) {
		compoundTag.put("rootPart", getRootPart().save(registryAccess()));
	}

	public ItemStack getRootPart() {
		return entityData.get(ROOT_PART);
	}

	public void setRootPart(ItemStack rootPart) {
		entityData.set(ROOT_PART, rootPart.copy());
	}
}
