package dev.dragonfox.toyofheart;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class AssemblerBlockEntity extends BlockEntity {
	private AssemblingDollEntity doll = null;
	private UUID dollUUID = null;

	public AssemblerBlockEntity(BlockPos pos, BlockState state) {
		super(ToyOfHeart.ASSEMBLER.get(), pos, state);
	}

	@Override
	protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
//		ToyOfHeart.LOGGER.info("Saving assembler at {}  doll UUID: {}  level: {}", worldPosition, doll != null ? doll.getUUID() : dollUUID, level);
		if (doll != null) {
			tag.putUUID("dollUUID", doll.getUUID());
		} else if (dollUUID != null) {
			tag.putUUID("dollUUID", dollUUID);
		}
	}

	@Override
	public void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
//		ToyOfHeart.LOGGER.info("Loading assembler at {}  doll ID: {}  level: {}", worldPosition, tag.contains("dollUUID") ? tag.getUUID("dollUUID") : tag.contains("dollId") ? tag.getInt("dollId") : null, level);
		if (tag.contains("dollUUID")) {
			dollUUID = tag.getUUID("dollUUID");
			if (level instanceof ServerLevel serverLevel) {
				// Server level is null when this is loaded for the first time
				doll = (AssemblingDollEntity) serverLevel.getEntity(dollUUID);
			} else {
				doll = null;
			}
		} else if (tag.contains("dollId") && level != null) {
			dollUUID = null;
			doll = (AssemblingDollEntity) level.getEntity(tag.getInt("dollId"));
		} else {
			dollUUID = null;
			doll = null;
		}
	}

	@Override
	public @NotNull CompoundTag getUpdateTag(HolderLookup.Provider provider) {
		CompoundTag tag = saveWithoutMetadata(provider);
		if (getDoll() != null) {
			tag.remove("dollUUID");
			tag.putInt("dollId", getDoll().getId());
		}
		return tag;
	}

	@Override
	public Packet<ClientGamePacketListener> getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	public AssemblingDollEntity getDoll() {
		if (doll == null && dollUUID != null && level instanceof ServerLevel serverLevel) {
			doll = (AssemblingDollEntity) serverLevel.getEntity(dollUUID);
		}
		return doll;
	}

	public void dropItems() {
		if (!hasDoll() || level == null) return;

		for (ItemStack item : removeDoll()) {
			if (!item.isEmpty()) {
				ItemEntity itemEntity = new ItemEntity(level, worldPosition.getX() + 0.5, worldPosition.getY() + 0.5, worldPosition.getZ() + 0.5, item);
				itemEntity.setDefaultPickUpDelay();
				level.addFreshEntity(itemEntity);
			}
		}
	}

	public void deployDoll() {
		if (!hasDoll() || level == null) return;

		AssemblingDollEntity doll = getDoll();
		DollEntity newDoll = new DollEntity(ToyOfHeart.DOLL.get(), level, getDoll().getDollParts().orElseThrow());
		newDoll.moveTo(doll.getX(), doll.getY(), doll.getZ(), doll.getYRot(), doll.getXRot());
		level.addFreshEntity(newDoll);
		removeDoll();
	}

	public boolean hasDoll() {
//		ToyOfHeart.LOGGER.info("Checking if assembler at {} has doll: {}  doll UUID: {}", worldPosition, doll, dollUUID);
		return doll != null || dollUUID != null;
	}

	public void addDoll(ItemStack rootPart) {
//		ToyOfHeart.LOGGER.info("Adding doll to assembler at {}  existing doll: {}", worldPosition, doll != null ? doll.getUUID() : dollUUID);
		if (hasDoll() || level == null) return;

		doll = new AssemblingDollEntity(ToyOfHeart.ASSEMBLING_DOLL.get(), level, rootPart, Optional.of(worldPosition));
		BlockState state = getBlockState();
		doll.moveTo(worldPosition.getX() + 0.5, worldPosition.getY() + 0.5, worldPosition.getZ() + 0.5, state.getValue(AssemblerBlock.FACING).toYRot(), 0.0F);
		level.addFreshEntity(doll);
		dollUUID = doll.getUUID();
		setChanged();
		level.sendBlockUpdated(worldPosition, state, state, 2);
	}

	public List<ItemStack> removeDoll() {
//		ToyOfHeart.LOGGER.info("Removing doll from assembler at {}  existing doll: {}", worldPosition, doll != null ? doll.getUUID() : dollUUID);
		if (!hasDoll() || level == null) return List.of();

		List<ItemStack> items = getDoll().disassemble();
		doll = null;
		dollUUID = null;
		setChanged();
		level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 2);
		return items;
	}
}
