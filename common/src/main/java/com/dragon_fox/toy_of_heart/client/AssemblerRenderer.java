package com.dragon_fox.toy_of_heart.client;

import com.dragon_fox.toy_of_heart.Assembler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;

public class AssemblerRenderer implements BlockEntityRenderer<Assembler> {
	public AssemblerRenderer(BlockEntityRendererFactory.Context ctx) {
	}

	@Override
	public void render(Assembler entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		matrices.push();

		double offset = Math.sin((entity.getWorld().getTime() + tickDelta) / 8.0) / 4.0;
		matrices.translate(0.5, 1.25 + offset, 0.5);
		matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((entity.getWorld().getTime() + tickDelta) * 4));
		int lightAbove = WorldRenderer.getLightmapCoordinates(entity.getWorld(), entity.getPos().up());
		MinecraftClient.getInstance().getItemRenderer().renderItem(entity.rootPart, ModelTransformationMode.NONE, lightAbove, overlay, matrices, vertexConsumers, entity.getWorld(), 0);

		matrices.pop();
	}
}
