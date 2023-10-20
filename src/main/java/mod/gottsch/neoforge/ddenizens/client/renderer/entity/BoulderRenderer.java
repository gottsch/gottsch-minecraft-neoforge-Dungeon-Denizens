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
import mod.gottsch.neoforge.ddenizens.client.model.BoulderModel;
import mod.gottsch.neoforge.ddenizens.entity.monster.Boulder;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

/**
 * 
 * @author Mark Gottschling on Apr 8, 2022
 *
 * @param <T>
 */
public class BoulderRenderer extends MobRenderer<Boulder, BoulderModel<Boulder>> {
	private static final ResourceLocation TEXTURE = new ResourceLocation(DD.MODID, "textures/entity/boulder.png");

	/**
	 * 
	 * @param context
	 */
	public BoulderRenderer(EntityRendererProvider.Context context) {
		super(context, new BoulderModel<>(context.bakeLayer(BoulderModel.LAYER_LOCATION)), 0.4F);
	}

	@Override
	public void render(Boulder boulder, float p_116532_, float p_116533_, PoseStack matrixStack, MultiBufferSource bufferSource, int p_116536_) {
		super.render(boulder, p_116532_, p_116533_, matrixStack, bufferSource, p_116536_);
	}

	@Override
	public ResourceLocation getTextureLocation(Boulder entity) {
		return TEXTURE;
	}
}
