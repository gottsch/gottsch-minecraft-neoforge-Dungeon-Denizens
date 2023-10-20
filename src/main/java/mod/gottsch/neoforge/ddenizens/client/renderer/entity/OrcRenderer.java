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

import mod.gottsch.neoforge.ddenizens.DD;
import mod.gottsch.neoforge.ddenizens.client.model.OrcModel;
import mod.gottsch.neoforge.ddenizens.entity.monster.Orc;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.resources.ResourceLocation;

/**
 * 
 * @author Mark Gottschling on Apr 28, 2022
 *
 * @param <T>
 */
public class OrcRenderer<T extends Orc> extends HumanoidMobRenderer<T, OrcModel<T>> {
	private static final ResourceLocation TEXTURE = new ResourceLocation(DD.MODID, "textures/entity/orc.png");

	/**
	 * 
	 * @param context
	 */
	public OrcRenderer(EntityRendererProvider.Context context) {
		super(context, new OrcModel<>(context.bakeLayer(OrcModel.LAYER_LOCATION)), 0.8F);
	}

	@Override
	public ResourceLocation getTextureLocation(Orc entity) {
		return TEXTURE;
	}
}
