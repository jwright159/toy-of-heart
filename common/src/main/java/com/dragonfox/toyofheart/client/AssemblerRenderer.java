package com.dragonfox.toyofheart.client;

import com.dragonfox.toyofheart.Assembler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;

public class AssemblerRenderer implements BlockEntityRenderer<Assembler> {
	public AssemblerRenderer(BlockEntityRendererFactory.Context ctx) {
	}

	@Override
	public void render(Assembler entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		matrices.push();

		matrices.translate(0.5, 1.0, 0.5);
		MinecraftClient.getInstance().getItemRenderer().renderItem(entity.rootPart, ModelTransformationMode.NONE, light, overlay, matrices, vertexConsumers, entity.getWorld(), 0);

		matrices.pop();
	}
}
