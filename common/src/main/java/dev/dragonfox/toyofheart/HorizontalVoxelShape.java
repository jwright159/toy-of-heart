package dev.dragonfox.toyofheart;

import net.minecraft.core.Direction;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import static net.minecraft.core.Direction.Axis.X;
import static net.minecraft.core.Direction.Axis.Y;
import static net.minecraft.core.Direction.Axis.Z;

public class HorizontalVoxelShape {
	private final VoxelShape north;
	private final VoxelShape east;
	private final VoxelShape south;
	private final VoxelShape west;

	public HorizontalVoxelShape(VoxelShape... shapes) {
		VoxelShape north = Shapes.box(0, 0, 0, 0, 0, 0);
		VoxelShape east = Shapes.box(0, 0, 0, 0, 0, 0);
		VoxelShape south = Shapes.box(0, 0, 0, 0, 0, 0);
		VoxelShape west = Shapes.box(0, 0, 0, 0, 0, 0);

		for (VoxelShape shape : shapes) {
			north = Shapes.or(north, shape);
			east = Shapes.or(east, Shapes.box(
					1 - shape.max(Z), shape.min(Y), shape.min(X),
					1 - shape.min(Z), shape.max(Y), shape.max(X)));
			south = Shapes.or(south, Shapes.box(
					1 - shape.max(X), shape.min(Y), 1 - shape.max(Z),
					1 - shape.min(X), shape.max(Y), 1 - shape.min(Z)));
			west = Shapes.or(west, Shapes.box(
					shape.min(Z), shape.min(Y), 1 - shape.max(X),
					shape.max(Z), shape.max(Y), 1 - shape.min(X)));
		}

		this.north = north;
		this.east = east;
		this.south = south;
		this.west = west;
	}

	public VoxelShape get(Direction direction) {
		return switch (direction) {
			case NORTH -> north;
			case EAST -> east;
			case SOUTH -> south;
			case WEST -> west;
			default ->
					throw new IllegalArgumentException("Can't get direction " + direction + " for HorizontalVoxelShape");
		};
	}
}
