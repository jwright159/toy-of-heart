package dev.dragonfox.toyofheart;

import net.minecraft.world.item.Item;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.Optional;

public class DollPartItem extends Item {
	private final float partWidth;
	private final float partHeight;
	private final float partDepth;

	public DollPartItem(Properties properties, float partWidth, float partHeight, float partDepth) {
		super(properties);
		this.partWidth = partWidth;
		this.partHeight = partHeight;
		this.partDepth = partDepth;
	}

	public float getPartWidth() {
		return partWidth;
	}
	public float getPartHeight() {
		return partHeight;
	}
	public float getPartDepth() {
		return partDepth;
	}

	public Optional<DollPart.RaycastHit> raycast(DollPart part, Vector3f pos, Vector3f dir, Matrix4f transform)
	{
//		ToyOfHeart.LOGGER.info("START");
//		ToyOfHeart.LOGGER.info("Before transform: pos = {}, dir = {}", pos, dir);
		Matrix4f transformInv = transform.invert(new Matrix4f());
		transformInv.transformPosition(pos);
		transformInv.transformDirection(dir);
//		ToyOfHeart.LOGGER.info("After transform: pos = {}, dir = {}", pos, dir);

		float minX = -getPartWidth() / 2;
		float maxX = getPartWidth() / 2;
		float minY = 0;
		float maxY = getPartHeight();
		float minZ = -getPartDepth() / 2;
		float maxZ = getPartDepth() / 2;

		// https://gdbooks.gitbooks.io/3dcollisions/content/Chapter3/raycast_aabb.html
		float tMinX = (minX - pos.x) / dir.x;
		float tMaxX = (maxX - pos.x) / dir.x;
		float tMinY = (minY - pos.y) / dir.y;
		float tMaxY = (maxY - pos.y) / dir.y;
		float tMinZ = (minZ - pos.z) / dir.z;
		float tMaxZ = (maxZ - pos.z) / dir.z;

		float tMin = Math.max(Math.max(Math.min(tMinX, tMaxX), Math.min(tMinY, tMaxY)), Math.min(tMinZ, tMaxZ));
		float tMax = Math.min(Math.min(Math.max(tMinX, tMaxX), Math.max(tMinY, tMaxY)), Math.max(tMinZ, tMaxZ));

		// if tMax < 0, ray (line) is intersecting AABB, but whole AABB is behing us
		if (tMax < 0) {
			return Optional.empty();
		}

		// if tMin > tMax, ray doesn't intersect AABB
		if (tMin > tMax) {
			return Optional.empty();
		}

		// if tMin < 0, ray starts inside AABB
		if (tMin < 0f) {
			return Optional.empty();
		}

		float distance = tMin;
		Vector3f localHitPos = pos.add(dir.mul(distance, new Vector3f()), new Vector3f());
		Vector3f localHitNormal = new Vector3f(
				Math.abs(localHitPos.x - minX) < 1e-6 ? -1 : (Math.abs(localHitPos.x - maxX) < 1e-6 ? 1 : 0),
				Math.abs(localHitPos.y - minY) < 1e-6 ? -1 : (Math.abs(localHitPos.y - maxY) < 1e-6 ? 1 : 0),
				Math.abs(localHitPos.z - minZ) < 1e-6 ? -1 : (Math.abs(localHitPos.z - maxZ) < 1e-6 ? 1 : 0)
		);
//		ToyOfHeart.LOGGER.info("Local hit pos: {}, local hit normal: {}", localHitPos, localHitNormal);
		transform.transformPosition(pos);
		transform.transformDirection(dir);
//		ToyOfHeart.LOGGER.info("After transform again: pos = {}, dir = {}", pos, dir);
		Vector3f worldHitPos = pos.add(dir.mul(distance, new Vector3f()), new Vector3f());
		Vector3f worldHitNormal = transform.transformDirection(localHitNormal, new Vector3f());
//		ToyOfHeart.LOGGER.info("World hit pos: {}, world hit normal: {}", worldHitPos, worldHitNormal);
//		ToyOfHeart.LOGGER.info("Local hit pos again: {}, local hit normal again: {}", localHitPos, localHitNormal);
		return Optional.of(new DollPart.RaycastHit(part, distance, worldHitPos, localHitPos, worldHitNormal, localHitNormal));
	}
}
