package dev.dragonfox.toyofheart;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class DollEntity extends PathfinderMob {
	public final ItemStack rootPart;

	public DollEntity(EntityType<? extends PathfinderMob> entityType, Level level, ItemStack rootPart) {
		super(entityType, level);
		this.rootPart = rootPart.copy();
	}

	public static DollEntity spawnDefault(EntityType<DollEntity> type, Level level) {
		return new DollEntity(type, level, ItemStack.EMPTY);
	}
}
