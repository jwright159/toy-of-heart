package dev.dragonfox.toyofheart;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.AxisAngle4f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public record DollPart(ItemStack itemStack, Matrix4f transform, List<DollPart> subParts) {
	public static final Codec<DollPart> CODEC = Codec.recursive(DollPart.class.getSimpleName(), recursed ->
			RecordCodecBuilder.create(
					instance -> instance.group(
							ItemStack.CODEC.fieldOf("itemStack").forGetter(part -> part.itemStack),
							ExtraCodecs.MATRIX4F.fieldOf("transform").forGetter(part -> part.transform),
							recursed.listOf().fieldOf("subParts").forGetter(part -> part.subParts)
					).apply(instance, DollPart::new)
			));
	public static final Codec<Optional<DollPart>> OPTIONAL_CODEC = ExtraCodecs.optionalEmptyMap(CODEC);

	public static final StreamCodec<ByteBuf, Matrix4f> TRANSFORM_STREAM_CODEC = ByteBufCodecs.FLOAT.apply(ByteBufCodecs.list(16)).map(
			list -> new Matrix4f(
					list.get(0), list.get(1), list.get(2), list.get(3),
					list.get(4), list.get(5), list.get(6), list.get(7),
					list.get(8), list.get(9), list.get(10), list.get(11),
					list.get(12), list.get(13), list.get(14), list.get(15)),
			matrix -> List.of(
					matrix.m00(), matrix.m01(), matrix.m02(), matrix.m03(),
					matrix.m10(), matrix.m11(), matrix.m12(), matrix.m13(),
					matrix.m20(), matrix.m21(), matrix.m22(), matrix.m23(),
					matrix.m30(), matrix.m31(), matrix.m32(), matrix.m33())
	);
	public static final StreamCodec<RegistryFriendlyByteBuf, DollPart> STREAM_CODEC = StreamCodec.recursive(recursed ->
			StreamCodec.composite(
					ItemStack.STREAM_CODEC,
					part -> part.itemStack,
					TRANSFORM_STREAM_CODEC,
					part -> part.transform,
					recursed.apply(ByteBufCodecs.list()),
					parts -> parts.subParts,
					DollPart::new
			));

	public static final EntityDataSerializer<DollPart> SERIALIZER = EntityDataSerializer.forValueType(STREAM_CODEC);
	public static final EntityDataSerializer<Optional<DollPart>> OPTIONAL_SERIALIZER = EntityDataSerializer.forValueType(ByteBufCodecs.optional(STREAM_CODEC));

	public DollPart(ItemStack itemStack) {
		this(itemStack, new Matrix4f().translate(0, 3f / 16f, 0), List.of());
	}

	public DollPart(ItemStack itemStack, Matrix4f transform) {
		this(itemStack, transform, List.of());
	}

	public Stream<DollPart> allDollParts() {
		return subParts.stream().map(DollPart::allDollParts).reduce(Stream.of(this), Stream::concat);
	}

	public Optional<RaycastHit> raycast(Vec3 rayPos, Vec3 rayDir, Vec3 entityPos, Quaternionf entityRot) {
		DollPartItem partItem = (DollPartItem) itemStack.getItem();
		Matrix4f partTransform = new Matrix4f();
		partTransform.translate(entityPos.toVector3f());
		partTransform.rotate(entityRot);
		partTransform.mul(transform);
		return partItem.raycast(this, rayPos.toVector3f(), rayDir.toVector3f(), partTransform);
	}

	public Optional<RaycastHit> raycastAll(Vec3 rayPos, Vec3 rayDir, Vec3 entityPos, Quaternionf entityRot) {
		return allDollParts()
				.flatMap(part -> part.raycast(rayPos, rayDir, entityPos, entityRot).stream())
				.min(Comparator.comparingDouble(RaycastHit::distance));
	}

	public Optional<DollPart> withChildPart(DollPart parent, DollPart child) {
		if (parent == this) {
			return Optional.of(new DollPart(itemStack, transform, Stream.concat(subParts.stream(), Stream.of(child)).collect(Collectors.toList())));
		}
		for (int i = 0; i < subParts.size(); i++) {
			DollPart subPart = subParts.get(i);
			Optional<DollPart> updatedSubPart = subPart.withChildPart(parent, child);
			if (updatedSubPart.isPresent()) {
				return Optional.of(new DollPart(itemStack, transform,
						Stream.concat(
								subParts.stream().limit(i),
								Stream.concat(Stream.of(updatedSubPart.get()), subParts.stream().skip(i + 1))
						).collect(Collectors.toList())));
			}
		}
		return Optional.empty();
	}

	public Optional<Tuple<Optional<DollPart>, List<ItemStack>>> withRemovedPart(DollPart part) {
		if (part == this) {
			return Optional.of(new Tuple<>(Optional.empty(), allDollParts().map(DollPart::itemStack).collect(Collectors.toList())));
		}
		for (int i = 0; i < subParts.size(); i++) {
			DollPart subPart = subParts.get(i);
			Optional<Tuple<Optional<DollPart>, List<ItemStack>>> updatedSubPart = subPart.withRemovedPart(part);
			if (updatedSubPart.isPresent()) {
				Optional<DollPart> newSubPart = updatedSubPart.get().getA();
				List<ItemStack> removedParts = updatedSubPart.get().getB();
				if (newSubPart.isEmpty())
				{
					return Optional.of(new Tuple<>(Optional.of(new DollPart(itemStack, transform, Stream.concat(subParts.stream().limit(i), subParts.stream().skip(i + 1)).collect(Collectors.toList()))), removedParts));
				} else {
					return Optional.of(new Tuple<>(Optional.of(new DollPart(itemStack, transform, Stream.concat(
							subParts.stream().limit(i),
							Stream.concat(Stream.of(newSubPart.get()), subParts.stream().skip(i + 1))
					).collect(Collectors.toList()))), removedParts));
				}
			}
		}
		return Optional.empty();
	}

	@Override
	public @NotNull String toString() {
		String toString = itemStack.getItem().toString();
		if (!subParts.isEmpty())
			toString += " -> {" + subParts.stream().map(DollPart::toString)
					.collect(Collectors.joining(", ")) + "}";
		return toString;
	}

	public record RaycastHit(DollPart hitPart, double distance, Vector3f worldHitPos, Vector3f localHitPos, Vector3f worldHitNormal, Vector3f localHitNormal) {
		public Vector3f roundedLocalHitPos() {
			return new Vector3f(
					Math.round(localHitPos.x * 16f) / 16f,
					Math.round(localHitPos.y * 16f) / 16f,
					Math.round(localHitPos.z * 16f) / 16f
			);
		}

		public Quaternionf orientation() {
			Vector3f up = new Vector3f(0, 1, 0);
			Vector3f forward = new Vector3f(0, 0, 1); // might be -1 idk it doesn't really matter here
			double rotDot = up.dot(localHitNormal);
			double rotDotThreshold = 0.95;
			Vector3f rotAxis = Math.abs(rotDot) > rotDotThreshold ? forward : up.cross(localHitNormal).normalize();
			double rotAngle = Math.acos(rotDot);
			AxisAngle4f axisAngle = new AxisAngle4f((float)rotAngle, rotAxis);
			return new Quaternionf(axisAngle);
		}

		public Matrix4f newPartTransform(DollPartItem newPart) {
			Matrix4f transform = new Matrix4f(hitPart.transform);
			transform.translate(roundedLocalHitPos());
			transform.rotate(orientation());
			transform.translate(0, newPart.getPartHeight() / 2.0f, 0);
			return transform;
		}
	}
}
