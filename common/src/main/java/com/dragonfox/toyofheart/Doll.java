package com.dragonfox.toyofheart;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.world.World;

public class Doll extends PathAwareEntity {
	public Doll(EntityType<? extends PathAwareEntity> entityType, World world) {
		super(entityType, world);
	}
}
