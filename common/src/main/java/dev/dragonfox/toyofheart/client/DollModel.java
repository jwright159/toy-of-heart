package dev.dragonfox.toyofheart.client;

import dev.dragonfox.toyofheart.DollEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.*;

public class DollModel extends EntityModel<DollEntity> {
	public DollModel() {
	}

	@Override
	public void setupAnim(DollEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
	}

	@Override
	public void renderToBuffer(PoseStack pose, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int packedColor) {
	}
}
