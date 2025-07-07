package dev.dragonfox.toyofheart;

import net.minecraft.world.item.Item;

public class DollPart extends Item {
	private final double partWidth;
	private final double partHeight;
	private final double partDepth;

	public DollPart(Properties properties, double partWidth, double partHeight, double partDepth) {
		super(properties);
		this.partWidth = partWidth;
		this.partHeight = partHeight;
		this.partDepth = partDepth;
	}

	public double getPartWidth() {
		return partWidth;
	}
	public double getPartHeight() {
		return partHeight;
	}
	public double getPartDepth() {
		return partDepth;
	}
}
