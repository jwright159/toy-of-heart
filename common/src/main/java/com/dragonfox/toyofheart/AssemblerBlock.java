package com.dragonfox.toyofheart;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class AssemblerBlock extends BlockWithEntity {
	private static final VoxelShape SHAPE = VoxelShapes.union(
			Block.createCuboidShape(0, 0, 0, 16, 2, 16),
			Block.createCuboidShape(6, 2, 9, 10, 14, 13)
	);

	public AssemblerBlock(Settings settings) {
		super(settings);
	}

	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new Assembler(pos, state);
	}

	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}

	@Override
	@SuppressWarnings("deprecation")
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return SHAPE;
	}

	@Override
	@SuppressWarnings("deprecation")
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (!world.isClient) {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof Assembler assembler) {
				ItemStack stack = player.getStackInHand(hand);
				if (assembler.rootPart.isEmpty() && stack.getItem() == ToyOfHeart.SLIM_BODY.get()) {
					assembler.rootPart = stack.copyWithCount(1);
					if (!player.isCreative()) {
						stack.decrement(1);
					}
					assembler.markDirty();
					world.updateListeners(pos, state, state, 2);
				} else if (!assembler.rootPart.isEmpty() && stack.isEmpty()) {
					player.getInventory().offerOrDrop(assembler.rootPart);
					assembler.markDirty();
					world.updateListeners(pos, state, state, 2);
				}
			}
		}
		return ActionResult.SUCCESS;
	}

	@Override
	@SuppressWarnings("deprecation")
	public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
		if (!state.isOf(newState.getBlock())) {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof Assembler assembler) {
				assembler.dropItems();
			}
		}

		super.onStateReplaced(state, world, pos, newState, moved);
	}
}
