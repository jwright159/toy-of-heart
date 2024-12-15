package com.dragon_fox.toy_of_heart;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;

public class Assembler extends BlockEntity {
	public ItemStack rootPart = ItemStack.EMPTY;

	public Assembler(BlockPos pos, BlockState state) {
		super(ToyOfHeart.ASSEMBLER.get(), pos, state);
	}

	@Override
	protected void writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);
		if (!rootPart.isEmpty()) {
			NbtCompound rootPartNbt = new NbtCompound();
			rootPart.writeNbt(rootPartNbt);
			nbt.put("rootPart", rootPartNbt);
		}
	}

	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		if (nbt.contains("rootPart")) {
			rootPart = ItemStack.fromNbt(nbt.getCompound("rootPart"));
		}
	}
}
