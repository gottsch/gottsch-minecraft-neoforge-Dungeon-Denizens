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
package mod.gottsch.neoforge.ddenizens.entity.monster;

import mod.gottsch.neoforge.ddenizens.DD;
import mod.gottsch.neoforge.ddenizens.config.Config;
import mod.gottsch.neoforge.ddenizens.config.Config.CommonSpawnConfig;
import mod.gottsch.neoforge.ddenizens.config.Config.IMobConfig;
import mod.gottsch.neoforge.ddenizens.config.Config.INetherMobConfig;
import mod.gottsch.neoforge.ddenizens.config.Config.NetherSpawnConfig;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

/**
 * @author Mark Gottschling on Apr 13, 2022
 *
 */
public abstract class DDMonster extends Monster {

	private static final int UNDERGROUND_HEIGHT = 60;
	
	protected DDMonster(EntityType<? extends Monster> mob, Level level) {
		super(mob, level);
	}

	/**
	 * 
	 * @param mob
	 * @param level
	 * @param spawnType
	 * @param pos
	 * @param random
	 * @return
	 */
//	@Deprecated
//	public static boolean checkDDSpawnRules(EntityType<? extends Mob> mob, LevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random) {
//		
//		IMobConfig mobConfig = Config.Mobs.MOBS.get(EntityType.getKey(mob));		
//		CommonSpawnConfig config = mobConfig.getSpawnConfig();
//
//		return pos.getY() > config.minHeight.get() && pos.getY() < config.maxHeight.get() && checkMobSpawnRules(mob, level, spawnType, pos, random);
//	}
	
	public static boolean checkDDMonsterSpawnRules(EntityType<? extends Mob> mob, ServerLevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random) {
		IMobConfig mobConfig = Config.Mobs.MOBS.get(EntityType.getKey(mob));		
		CommonSpawnConfig config = mobConfig.getSpawnConfig();
		return level.getDifficulty() != Difficulty.PEACEFUL && isValidHeight(pos, config) && isDarkEnoughToSpawn(level, pos, random) && checkMobSpawnRules(mob, level, spawnType, pos, random);
	}
	
	public static boolean isValidHeight(BlockPos pos, CommonSpawnConfig config) {
		return pos.getY() > config.minHeight.get() && pos.getY() < config.maxHeight.get();
	}
	
	public static boolean checkDDMonsterNetherSpawnRules(EntityType<? extends Mob> mob, ServerLevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random) {
		DD.LOGGER.info("checking nether spawn rules at -> {}", pos);
		IMobConfig mobConfig = Config.Mobs.MOBS.get(EntityType.getKey(mob));
		NetherSpawnConfig config = ((INetherMobConfig)mobConfig).getNetherSpawn();
		return level.getDifficulty() != Difficulty.PEACEFUL && isValidHeight(pos, config) && isDarkEnoughToSpawn(level, pos, random) && checkMobSpawnRules(mob, level, spawnType, pos, random);
	}
	
//	public static boolean checkDDNetherSpawnRules(EntityType<? extends Mob> mob, LevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random) {
//		DD.LOGGER.info("checking nether spawn rules at -> {}", pos);
//		IMobConfig mobConfig = Config.Mobs.MOBS.get(EntityType.getKey(mob));
//		NetherSpawnConfig config = ((INetherMobConfig)mobConfig).getNetherSpawn();
//		return pos.getY() > config.minHeight.get() && pos.getY() < config.maxHeight.get() && checkMobSpawnRules(mob, level, spawnType, pos, random);
//	}
}
