package com.dragonfox.toyofheart;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class Assembler extends BlockEntity {
	public ItemStack rootPart = ItemStack.EMPTY;

	public Assembler(BlockPos pos, BlockState state) {
		super(ToyOfHeart.ASSEMBLER.get(), pos, state);
	}

	@Override
	protected void writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);
		NbtCompound rootPartNbt = new NbtCompound();
		rootPart.writeNbt(rootPartNbt);
		nbt.put("rootPart", rootPartNbt);
	}

	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		if (nbt.contains("rootPart")) {
			rootPart = ItemStack.fromNbt(nbt.getCompound("rootPart"));
		}
	}

	@Nullable
	@Override
	public Packet<ClientPlayPacketListener> toUpdatePacket() {
		return BlockEntityUpdateS2CPacket.create(this);
	}

	@Override
	public NbtCompound toInitialChunkDataNbt() {
		return createNbt();
	}

	public void dropItems() {
		if (!rootPart.isEmpty() && world != null) {
			world.spawnEntity(new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, rootPart.copyAndEmpty()));
		}
	}
}
