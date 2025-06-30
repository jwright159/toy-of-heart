package com.dragonfox.toyofheart;

import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;

public class DollBodyPart extends DollPart {
	public DollBodyPart(Properties properties, double partWidth, double partHeight, double partDepth) {
		super(properties, partWidth, partHeight, partDepth);
	}

	public RaycastResult raycast(Vec3 pos, Vec3 dir, Vec3 partPos, Quaternionf partRot)
	{
//		ToyOfHeart.LOGGER.info("Before transform: pos = {}, dir = {}, partPos = {}, partRot = {}", pos, dir, partPos, partRot);
		Quaternionf partRotInv = partRot.conjugate(new Quaternionf());
		pos = pos.subtract(partPos);
		pos = new Vec3(partRotInv.transform(pos.toVector3f()));
		dir = new Vec3(partRotInv.transform(dir.toVector3f()));
//		ToyOfHeart.LOGGER.info("After transform: pos = {}, dir = {}", pos, dir);

		double minX = -partWidth / 2;
		double maxX = partWidth / 2;
		double minY = 0;
		double maxY = partHeight;
		double minZ = -partDepth / 2;
		double maxZ = partDepth / 2;

		// https://gdbooks.gitbooks.io/3dcollisions/content/Chapter3/raycast_aabb.html
		double tMinX = (minX - pos.x) / dir.x;
		double tMaxX = (maxX - pos.x) / dir.x;
		double tMinY = (minY - pos.y) / dir.y;
		double tMaxY = (maxY - pos.y) / dir.y;
		double tMinZ = (minZ - pos.z) / dir.z;
		double tMaxZ = (maxZ - pos.z) / dir.z;

		double tMin = Math.max(Math.max(Math.min(tMinX, tMaxX), Math.min(tMinY, tMaxY)), Math.min(tMinZ, tMaxZ));
		double tMax = Math.min(Math.min(Math.max(tMinX, tMaxX), Math.max(tMinY, tMaxY)), Math.max(tMinZ, tMaxZ));

		// if tMax < 0, ray (line) is intersecting AABB, but whole AABB is behing us
		if (tMax < 0) {
			return RaycastResult.miss();
		}

		// if tMin > tMax, ray doesn't intersect AABB
		if (tMin > tMax) {
			return RaycastResult.miss();
		}

		// if tMin < 0, ray starts inside AABB
		if (tMin < 0f) {
			return RaycastResult.miss();
		}

		double distance = tMin;
		Vec3 localHitPos = pos.add(dir.scale(distance));
		Vec3 localHitNormal = new Vec3(
				Math.abs(localHitPos.x - minX) < 1e-6 ? -1 : (Math.abs(localHitPos.x - maxX) < 1e-6 ? 1 : 0),
				Math.abs(localHitPos.y - minY) < 1e-6 ? -1 : (Math.abs(localHitPos.y - maxY) < 1e-6 ? 1 : 0),
				Math.abs(localHitPos.z - minZ) < 1e-6 ? -1 : (Math.abs(localHitPos.z - maxZ) < 1e-6 ? 1 : 0)
		);
		dir = new Vec3(partRot.transform(localHitPos.toVector3f()));
		pos = new Vec3(partRot.transform(pos.toVector3f()));
		pos = pos.add(partPos);
		Vec3 worldHitPos = pos.add(dir.scale(distance));
		Vec3 worldHitNormal = new Vec3(partRot.transform(localHitNormal.toVector3f()));
//		ToyOfHeart.LOGGER.info("HIT: distance = {}, worldHitPos = {}, localHitPos = {}", distance, worldHitPos, localHitPos);
		return RaycastResult.hit(distance, worldHitPos, localHitPos, worldHitNormal, localHitNormal);
	}

	public static class RaycastResult {
		public final boolean hit;
		public final double distance;
		public final Vec3 worldHitPos;
		public final Vec3 localHitPos;
		public final Vec3 worldHitNormal;
		public final Vec3 localHitNormal;

		private RaycastResult(boolean hit, double distance, Vec3 worldHitPos, Vec3 localHitPos, Vec3 worldHitNormal, Vec3 localHitNormal) {
			this.hit = hit;
			this.distance = distance;
			this.worldHitPos = worldHitPos;
			this.localHitPos = localHitPos;
			this.worldHitNormal = worldHitNormal;
			this.localHitNormal = localHitNormal;
		}

		public static RaycastResult hit(double distance, Vec3 worldHitPos, Vec3 localHitPos, Vec3 worldHitNormal, Vec3 localHitNormal) {
			return new RaycastResult(true, distance, worldHitPos, localHitPos, worldHitNormal, localHitNormal);
		}
		public static RaycastResult miss() {
			return new RaycastResult(false, 0, Vec3.ZERO, Vec3.ZERO, Vec3.ZERO, Vec3.ZERO);
		}
	}
}
