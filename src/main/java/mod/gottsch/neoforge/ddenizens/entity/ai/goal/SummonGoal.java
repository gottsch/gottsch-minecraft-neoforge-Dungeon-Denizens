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
package mod.gottsch.neoforge.ddenizens.entity.ai.goal;

import mod.gottsch.neo.gottschcore.spatial.Coords;
import mod.gottsch.neo.gottschcore.spatial.ICoords;
import mod.gottsch.neo.gottschcore.world.WorldInfo;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.NaturalSpawner;

/**
 * 
 * @author Mark Gottschling on Apr 19, 2022
 *
 */
public abstract class SummonGoal extends Goal {
	protected int summonCooldownTime;
	protected int cooldownTime;
	
	/**
	 * 
	 * @param summonCoolDownTime
	 */
	public SummonGoal(int summonCoolDownTime) {
		this.summonCooldownTime = summonCoolDownTime;
	}
	
	/**
	 * 
	 * @param level
	 * @param random
	 * @param owner
	 * @param entityType
	 * @param coords
	 * @param target
	 * @return
	 */
	protected boolean spawn(ServerLevel level, RandomSource random, LivingEntity owner, EntityType<? extends Mob> entityType, ICoords coords, LivingEntity target) {
		for (int i = 0; i < 20; i++) { // 20 tries
			int spawnX = coords.getX() + Mth.nextInt(random, 1, 2) * Mth.nextInt(random, -1, 1);
			int spawnY = coords.getY() + Mth.nextInt(random, 1, 2) * Mth.nextInt(random, -1, 1);
			int spawnZ = coords.getZ() + Mth.nextInt(random, 1, 2) * Mth.nextInt(random, -1, 1);

			ICoords spawnCoords = new Coords(spawnX, spawnY, spawnZ);

			boolean isSpawned = false;
			if (!WorldInfo.isClientSide(level)) {
				SpawnPlacements.Type placement = SpawnPlacements.getPlacementType(entityType);
				if (NaturalSpawner.isSpawnPositionOk(placement, level, spawnCoords.toPos(), entityType)) {
					Mob mob = entityType.create(level);
					mob.setPos((double)spawnX, (double)spawnY, (double)spawnZ);
					mob.setTarget(target);
					level.addFreshEntityWithPassengers(mob);
					isSpawned = true;
				}
				
				if (isSpawned) {
					for (int p = 0; p < 20; p++) {
						double xSpeed = random.nextGaussian() * 0.02D;
						double ySpeed = random.nextGaussian() * 0.02D;
						double zSpeed = random.nextGaussian() * 0.02D;
						level.sendParticles(ParticleTypes.POOF, owner.blockPosition().getX() + 0.5D, owner.blockPosition().getY(), owner.blockPosition().getZ() + 0.5D, 1, xSpeed, ySpeed, zSpeed, (double)0.15F);
					}
					return true;
				}
			}

		}
		return false;
	}
}
