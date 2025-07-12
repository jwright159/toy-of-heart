package dev.dragonfox.toyofheart;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.Level;

public class DollEntity extends PathfinderMob {
	public final DollPart parts;

	public DollEntity(EntityType<? extends PathfinderMob> entityType, Level level, DollPart parts) {
		super(entityType, level);
		this.parts = parts;
	}

	public static DollEntity spawnDefault(EntityType<DollEntity> type, Level level) {
		return new DollEntity(type, level, new DollPart(ToyOfHeart.SLIM_BODY.get().getDefaultInstance()));
	}
}
