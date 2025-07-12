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

import java.util.UUID;

public class AssemblerBlockEntity extends BlockEntity {
	private ItemStack rootPart = ItemStack.EMPTY;
	private AssemblingDollEntity doll = null;
	private UUID dollUUID = null;

	public AssemblerBlockEntity(BlockPos pos, BlockState state) {
		super(ToyOfHeart.ASSEMBLER.get(), pos, state);
	}

	@Override
	protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
//		ToyOfHeart.LOGGER.info("Saving assembler at {} with root part: {}  doll UUID: {}  level: {}", worldPosition, rootPart, doll != null ? doll.getUUID() : null, level);
		if (!rootPart.isEmpty()) {
			tag.put("rootPart", rootPart.save(provider));
		}

		if (getDoll() != null) {
			tag.putUUID("dollUUID", getDoll().getUUID());
		} else if (dollUUID != null) {
			tag.putUUID("dollUUID", dollUUID);
		}
	}

	@Override
	public void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
//		ToyOfHeart.LOGGER.info("Loading assembler at {} with root part: {}  doll UUID: {}  level: {}", worldPosition, tag.contains("rootPart") ? tag.get("rootPart") : ItemStack.EMPTY, tag.contains("dollUUID") ? tag.getUUID("dollUUID") : null, level);
		if (tag.contains("rootPart")) {
			rootPart = ItemStack.parseOptional(provider, tag.getCompound("rootPart"));
		} else {
			rootPart = ItemStack.EMPTY;
		}

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
		tag.putInt("dollId", getDoll() != null ? getDoll().getId() : -1);
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

		level.addFreshEntity(new ItemEntity(level, worldPosition.getX() + 0.5, worldPosition.getY() + 0.5, worldPosition.getZ() + 0.5, removeDoll()));
	}

	public void deployDoll() {
		if (!hasDoll() || level == null) return;

		AssemblingDollEntity doll = getDoll();
		DollEntity newDoll = new DollEntity(ToyOfHeart.DOLL.get(), level, this.rootPart);
		newDoll.moveTo(doll.getX(), doll.getY(), doll.getZ(), doll.getYRot(), doll.getXRot());
		level.addFreshEntity(newDoll);
		removeDoll();
	}

	public boolean hasDoll() {
		return !rootPart.isEmpty();
	}

	public void addDoll(ItemStack rootPart) {
		if (hasDoll() || level == null) return;

		this.rootPart = rootPart.copy();
		doll = new AssemblingDollEntity(ToyOfHeart.ASSEMBLING_DOLL.get(), level, this.rootPart);
		BlockState state = getBlockState();
		doll.moveTo(worldPosition.getX() + 0.5, worldPosition.getY() + 0.5, worldPosition.getZ() + 0.5, state.getValue(AssemblerBlock.FACING).toYRot(), 0.0F);
		level.addFreshEntity(doll);
		dollUUID = doll.getUUID();
		setChanged();
		level.sendBlockUpdated(worldPosition, state, state, 2);
	}

	public ItemStack removeDoll() {
		if (!hasDoll() || level == null) return ItemStack.EMPTY;

		if (getDoll() != null) {
			getDoll().discard();
		}
		doll = null;
		dollUUID = null;
		setChanged();
		level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 2);
		return rootPart.copyAndClear();
	}
}
