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
package mod.gottsch.neoforge.ddenizens.datagen;

import mod.gottsch.neoforge.ddenizens.DD;
import mod.gottsch.neoforge.ddenizens.setup.Registration;

import net.minecraft.data.PackOutput;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

/**
 * 
 * @author Mark Gottschling on Apr 6, 2022
 *
 */
public class DDItemModelsProvider extends ItemModelProvider {

    public DDItemModelsProvider(PackOutput pack, ExistingFileHelper existingFileHelper) {
        super(pack, DD.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
    	// projectiles
        singleTexture("slowball",
                mcLoc("item/generated"), "layer0", modLoc("item/slowball"));
        
        singleTexture("harmball",
                mcLoc("item/generated"), "layer0", modLoc("item/harmball"));
        
        singleTexture("rock",
                mcLoc("item/generated"), "layer0", modLoc("item/rock"));
    	
        // weapons
        singleTexture("club",
        		mcLoc("item/generated"), "layer0", modLoc("item/club"));
        
        singleTexture("spiked_club",
        		mcLoc("item/generated"), "layer0", modLoc("item/spiked_club"));
        
        // eggs
    	withExistingParent(Registration.HEADLESS, mcLoc("item/template_spawn_egg"));
    	withExistingParent(Registration.ORC, mcLoc("item/template_spawn_egg"));
    	withExistingParent(Registration.GHOUL, mcLoc("item/template_spawn_egg"));
    	withExistingParent(Registration.GAZER, mcLoc("item/template_spawn_egg"));
    	withExistingParent(Registration.BOULDER, mcLoc("item/template_spawn_egg"));
    	withExistingParent(Registration.SHADOW, mcLoc("item/template_spawn_egg"));
    	withExistingParent(Registration.SHADOWLORD, mcLoc("item/template_spawn_egg"));
    	withExistingParent(Registration.DAEMON, mcLoc("item/template_spawn_egg"));
    }
}
