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
import net.minecraftforge.common.data.LanguageProvider;

/**
 * 
 * @author Mark Gottschling on Apr 6, 2022
 *
 */
public class DDLanguageProvider extends LanguageProvider {

    public DDLanguageProvider(PackOutput pack, String locale) {
        super(pack, DD.MODID, locale);
    }

    @Override
    protected void addTranslations() {
//        add("itemGroup." + TAB_NAME, "Tutorial");
    	add(Registration.HEADLESS_ENTITY_TYPE.get(), "Headless");
        add(Registration.HEADLESS_EGG.get(), "Headless Spawn Egg");
    	add(Registration.ORC_ENTITY_TYPE.get(), "Orc");
        add(Registration.ORC_EGG.get(), "Orc Spawn Egg");
        add(Registration.GHOUL_ENTITY_TYPE.get(), "Ghoul");
        add(Registration.GHOUL_EGG.get(), "Ghoul Spawn Egg");
        
        add(Registration.GAZER_ENTITY_TYPE.get(), "Gazer");
        add(Registration.GAZER_EGG.get(), "Gazer Spawn Egg");
        
        add(Registration.BOULDER_ENTITY_TYPE.get(), "Boulder");
        add(Registration.BOULDER_EGG.get(), "Boulder Spawn Egg");
        
        add(Registration.SHADOW_ENTITY_TYPE.get(), "Shadow");
        add(Registration.SHADOW_EGG.get(), "Shadow Spawn Egg");
        
        add(Registration.SHADOWLORD_ENTITY_TYPE.get(), "Shadowlord");
        add(Registration.SHADOWLORD_EGG.get(), "Shadowlord Spawn Egg");
        
        add(Registration.DAEMON_ENTITY_TYPE.get(), "Daemon");
        add(Registration.DAEMON_EGG.get(), "Daemon Spawn Egg");
        
        add(Registration.SLOWBALL_ITEM.get(), "Slowball");
        add(Registration.HARMBALL_ITEM.get(), "Harmball");
        add(Registration.ROCK_ITEM.get(), "Rock");
        
        add(Registration.CLUB.get(), "Club");
        add(Registration.SPIKED_CLUB.get(), "Spiked Club");
    }
}
