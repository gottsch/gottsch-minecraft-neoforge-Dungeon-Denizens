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
package mod.gottsch.neoforge.ddenizens.item;

import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;

/**
 * 
 * @author Mark Gottschling on Jul 21, 2022
 *
 */
@Deprecated
public class Club extends SwordItem {

	public Club(Tier tier, int damage, float speedModifier, Properties properties) {
		super(tier, damage, speedModifier, properties);
	}

	// NOTE if this weapon isn't going to do anything special other than change damage properties, do we really need a new class? - no.
}
