package com.dragonfox.toyofheart;

import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;

import static net.minecraft.util.math.Direction.Axis.*;

public class HorizontalVoxelShape {
	private final VoxelShape north;
	private final VoxelShape east;
	private final VoxelShape south;
	private final VoxelShape west;

	public HorizontalVoxelShape(VoxelShape... shapes) {
		VoxelShape north = VoxelShapes.cuboid(0, 0, 0, 0, 0, 0);
		VoxelShape east = VoxelShapes.cuboid(0, 0, 0, 0, 0, 0);
		VoxelShape south = VoxelShapes.cuboid(0, 0, 0, 0, 0, 0);
		VoxelShape west = VoxelShapes.cuboid(0, 0, 0, 0, 0, 0);

		for (VoxelShape shape : shapes) {
			north = VoxelShapes.union(north, shape);
			east = VoxelShapes.union(east, VoxelShapes.cuboid(
					1 - shape.getMax(Z), shape.getMin(Y), shape.getMin(X),
					1 - shape.getMin(Z), shape.getMax(Y), shape.getMax(X)));
			south = VoxelShapes.union(south, VoxelShapes.cuboid(
					1 - shape.getMax(X), shape.getMin(Y), 1 - shape.getMax(Z),
					1 - shape.getMin(X), shape.getMax(Y), 1 - shape.getMin(Z)));
			west = VoxelShapes.union(west, VoxelShapes.cuboid(
					shape.getMin(Z), shape.getMin(Y), 1 - shape.getMax(X),
					shape.getMax(Z), shape.getMax(Y), 1 - shape.getMin(X)));
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
