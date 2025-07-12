package dev.dragonfox.toyofheart.client;

import dev.dragonfox.toyofheart.DollEntity;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartNames;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class DollModel extends EntityModel<DollEntity> {
	private final ModelPart base;

	public DollModel(ModelPart root) {
		this.base = root.getChild(PartNames.CUBE);
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition part = mesh.getRoot();
		part.addOrReplaceChild(PartNames.CUBE, CubeListBuilder.create().texOffs(0, 0).addBox(-6F, 12F, -6F, 12F, 12F, 12F), PartPose.offset(0F, 0F, 0F));
		return LayerDefinition.create(mesh, 64, 64);
	}

	@Override
	public void setupAnim(DollEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
	}

	@Override
	public void renderToBuffer(PoseStack pose, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int packedColor) {
		ImmutableList.of(this.base).forEach((modelRenderer) -> modelRenderer.render(pose, vertexConsumer, packedLight, packedOverlay, packedColor));
	}
}
