package dev.dragonfox.toyofheart;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class Doll extends PathfinderMob {
	public final ItemStack rootPart;

	public Doll(EntityType<? extends PathfinderMob> entityType, Level level, ItemStack rootPart) {
		super(entityType, level);
		this.rootPart = rootPart.copy();
	}

	public static Doll spawnDefault(EntityType<Doll> type, Level level) {
		return new Doll(type, level, ItemStack.EMPTY);
	}
}
