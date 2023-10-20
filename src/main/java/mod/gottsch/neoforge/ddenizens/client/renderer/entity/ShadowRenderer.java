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
package mod.gottsch.neoforge.ddenizens.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import mod.gottsch.neoforge.ddenizens.DD;
import mod.gottsch.neoforge.ddenizens.client.model.ShadowModel;
import mod.gottsch.neoforge.ddenizens.entity.monster.Shadow;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

/**
 * 
 * @author Mark Gottschling on Apr 12, 2022
 *
 * @param <T>
 */
public class ShadowRenderer extends MobRenderer<Shadow, ShadowModel<Shadow>> {
	private static final ResourceLocation TEXTURE = new ResourceLocation(DD.MODID, "textures/entity/shadow.png");
	
	/**
	 * 
	 * @param context
	 */
	public ShadowRenderer(EntityRendererProvider.Context context) {
        super(context, new ShadowModel<>(context.bakeLayer(ShadowModel.LAYER_LOCATION)), 0F);
    }

	@Override
	public void render(Shadow shadow, float p_116532_, float p_116533_, PoseStack matrixStack, MultiBufferSource bufferSource, int p_116536_) {
//        GlStateManager.pushMatrix();
//
//        GlStateManager.enableNormalize();
//        GlStateManager.enableBlend();
//
		// color of the bound (White)
//		Color c = Color.WHITE;
//		// split up in red, green and blue and transform it to 0.0 - 1.0
//		float red = c.getRed() / 255.0f;
//		float green = c.getGreen() / 255.0f;
//		float blue = c.getBlue() / 255.0f;
		
//        GlStateManager.color(1.0F, 1.0F, 1.0F, 0.8F);
//        matrixStack.pushPose();
//        VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.entityTranslucentCull(TEXTURE));
//        vertexConsumer.color(1.0F, 1.0F, 1.0F, 0.8F);
		super.render(shadow, p_116532_, p_116533_, matrixStack, bufferSource, p_116536_);
		
//		matrixStack.popPose();
		
	}
	
     @Override
    public ResourceLocation getTextureLocation(Shadow entity) {
        return TEXTURE;
    }
}
