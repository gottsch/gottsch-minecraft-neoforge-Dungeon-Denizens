/*
* This file is part of  Dungeon Denizens.
 * Copyright (c) 2021, Mark Gottschling (gottsch)
 * 
 * All rights reserved.
 *
 * Dungeon Denizens is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Dungeon Denizens is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Dungeon Denizens.  If not, see <http://www.gnu.org/licenses/lgpl>.
 */
package mod.gottsch.neoforge.ddenizens.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mod.gottsch.neoforge.ddenizens.DD;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

/**
 * 
 * @author Mark Gottschling on Apr 18, 2022
 *
 * @param <T>
 */
public class ShadowlordModel<T extends Entity> extends EntityModel<T> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(DD.MODID, "shadowlord"), "main");

	private final ModelPart shadowlord;
	private final ModelPart head;
	private final ModelPart rightArm;
	private final ModelPart leftArm;
	
	// shadowlord4
	private final ModelPart leftFrontCloak;
	private final ModelPart rightFrontCloak;
	
	private final ModelPart leftBackCloak;
	private final ModelPart rightBackCloak;

	/**
	 * 
	 * @param root
	 */
	public ShadowlordModel(ModelPart root) {
		super(RenderType::entityTranslucentCull);
		this.shadowlord = root.getChild("shadowlord");
		this.head = shadowlord.getChild("head");
		rightArm = shadowlord.getChild("right_arm");
		leftArm = shadowlord.getChild("left_arm");
		
		// shadowlord 4
		rightFrontCloak = shadowlord.getChild("cloak").getChild("right_front_cloak");
		leftFrontCloak = shadowlord.getChild("cloak").getChild("left_front_cloak");
		
		rightBackCloak = shadowlord.getChild("cloak").getChild("right_back_cloak");
		leftBackCloak = shadowlord.getChild("cloak").getChild("left_back_cloak");
	}

	/**
	 * 
	 * @return
	 */
	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		// shadowlord
		PartDefinition shadowlord = partdefinition.addOrReplaceChild("shadowlord", CubeListBuilder.create(), PartPose.offset(5.5F, 2.0F, -3.0F));
		PartDefinition head = shadowlord.addOrReplaceChild("head", CubeListBuilder.create().texOffs(51, 44).addBox(-4.0F, -9.3757F, -4.92F, 8.0F, 1.0F, 9.0F, new CubeDeformation(0.0F))
		.texOffs(25, 37).addBox(-5.0F, -8.3757F, -2.92F, 10.0F, 8.0F, 7.0F, new CubeDeformation(0.0F))
		.texOffs(53, 38).addBox(-5.0F, -8.3757F, -4.82F, 10.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.5F, -13.6243F, 2.92F));
		PartDefinition left_hood_r1 = head.addOrReplaceChild("left_hood_r1", CubeListBuilder.create().texOffs(64, 19).addBox(0.0F, 0.0F, -4.5F, 1.0F, 9.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, -9.3757F, -0.42F, 0.0F, 0.0F, -0.0873F));
		PartDefinition right_hood_r1 = head.addOrReplaceChild("right_hood_r1", CubeListBuilder.create().texOffs(73, 0).addBox(-1.0F, 0.0F, -4.5F, 1.0F, 9.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, -9.3757F, -0.42F, 0.0F, 0.0F, 0.0873F));
		PartDefinition body = shadowlord.addOrReplaceChild("body", CubeListBuilder.create().texOffs(25, 53).addBox(-4.0F, -36.0F, 0.0F, 8.0F, 14.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(76, 19).addBox(3.5F, -36.0F, 1.0F, 5.0F, 4.0F, 4.0F, new CubeDeformation(0.1F))
		.texOffs(17, 74).addBox(-8.5F, -36.0F, 1.0F, 5.0F, 4.0F, 4.0F, new CubeDeformation(0.1F)), PartPose.offset(-5.5F, 22.0F, 0.0F));
		PartDefinition left_arm = shadowlord.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(27, 0).addBox(-6.5884F, -0.1369F, -2.0F, 9.0F, 32.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.6052F, -10.1531F, 3.0F, 0.0F, 0.0F, -0.1309F));
		PartDefinition right_arm = shadowlord.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(0, 0).addBox(-2.4116F, -0.1369F, -2.0F, 9.0F, 32.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-11.5943F, -10.3188F, 3.0F, 0.0F, 0.0F, 0.1309F));
		PartDefinition cloak = shadowlord.addOrReplaceChild("cloak", CubeListBuilder.create().texOffs(0, 37).addBox(-4.0F, -22.0F, 1.0F, 8.0F, 22.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.5F, 22.0F, 0.0F));
		PartDefinition left_front_cloak = cloak.addOrReplaceChild("left_front_cloak", CubeListBuilder.create().texOffs(71, 55).addBox(-2.0F, 0.0F, 0.0F, 4.0F, 22.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, -22.0F, 0.0F));
		PartDefinition left_back_cloak = cloak.addOrReplaceChild("left_back_cloak", CubeListBuilder.create().texOffs(0, 64).addBox(-2.0F, 0.0F, -4.0F, 4.0F, 22.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, -22.0F, 6.0F));
		PartDefinition right_front_cloak = cloak.addOrReplaceChild("right_front_cloak", CubeListBuilder.create().texOffs(54, 0).addBox(-2.0F, 0.0F, -0.5F, 4.0F, 22.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, -22.0F, 0.5F));
		PartDefinition right_back_cloak = cloak.addOrReplaceChild("right_back_cloak", CubeListBuilder.create().texOffs(54, 55).addBox(-2.0F, 0.0F, -3.5F, 4.0F, 22.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, -22.0F, 5.5F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		// head
		this.head.yRot = netHeadYaw * ((float)Math.PI / 180F);
		this.head.xRot = headPitch * ((float)Math.PI / 180F);
		
		// arms swing
		float armSpeed = 0.025F;
		float radians = 0.1F;
		this.rightArm.xRot = Mth.cos(limbSwing * armSpeed) * radians * 1.4F * limbSwingAmount;
		this.leftArm.xRot = Mth.cos(limbSwing * armSpeed + (float)Math.PI) * radians * 1.4F * limbSwingAmount;
		
		// bobs
		float speed = 0.05F; // 1/20th speed
		
		// shadowlord 4
		rightFrontCloak.xRot = Math.min(0, -1.0F * (Mth.sin(ageInTicks * speed) *  0.125F));
		leftFrontCloak.xRot = Math.min(0, -1.0F * (Mth.cos(ageInTicks * speed) *  0.125F));
		
		rightBackCloak.xRot = Math.max(0, (Mth.cos(ageInTicks * speed) *  0.125F));
		leftBackCloak.xRot = Math.max(0, (Mth.sin(ageInTicks * speed) *  0.125F));
		
		// sway arms
		leftArm.zRot =(Mth.cos(ageInTicks * armSpeed) *  0.1F);
		rightArm.zRot =(Mth.cos(ageInTicks * armSpeed) *  0.1F);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		shadowlord.render(poseStack, buffer, packedLight, packedOverlay);
	}
}