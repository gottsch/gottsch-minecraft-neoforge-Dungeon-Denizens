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
package mod.gottsch.neoforge.ddenizens.entity.ai.goal.target;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;

import mod.gottsch.neoforge.ddenizens.DD;

import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.phys.AABB;

/**
 * Almost identical to HurtByTargetGoal except the alertOthers() method functions in opposite
 * ie. instead of providing a list of excluded entities from the alert, the list provided is a list of entities
 * to include in the alert.
 * Remade entire class instead of extending because vanilla class hides some necessary properties.
 * @author Mark Gottschling on Apr 7, 2022
 *
 */
public class HeadlessHurtByTargetGoal extends TargetGoal {
	private static final TargetingConditions HURT_BY_TARGETING = TargetingConditions.forCombat().ignoreLineOfSight().ignoreInvisibilityTesting();
	private static final int ALERT_RANGE_Y = 10;
	private int timeStamp;
	private final Class<?>[] toIgnoreDamage;
	private List<Class<?>> othersToAlert = new ArrayList<>();

	/**
	 * 
	 * @param headless
	 * @param toIgnoreDamage
	 */
	public HeadlessHurtByTargetGoal(PathfinderMob headless, Class<?>... toIgnoreDamage) {
		super(headless, true);
		this.toIgnoreDamage = toIgnoreDamage;
		this.setFlags(EnumSet.of(Flag.TARGET));
	}

	/**
	 * from vanilla
	 */
	public boolean canUse() {
		int i = this.mob.getLastHurtByMobTimestamp();
		LivingEntity livingentity = this.mob.getLastHurtByMob();
		if (i != this.timeStamp && livingentity != null) {
			if (livingentity.getType() == EntityType.PLAYER && this.mob.level().getGameRules().getBoolean(GameRules.RULE_UNIVERSAL_ANGER)) {
				return false;
			} else {
				for(Class<?> oclass : this.toIgnoreDamage) {
					if (oclass.isAssignableFrom(livingentity.getClass())) {
						return false;
					}
				}

				return this.canAttack(livingentity, HURT_BY_TARGETING);
			}
		} else {
			return false;
		}
	}

	/**
	 * 
	 * @param toAlert
	 * @return
	 */
	public HeadlessHurtByTargetGoal setAlertOthers(Class<?>... toAlert) {
		this.othersToAlert.addAll(Arrays.asList(toAlert));
		return this;
	}

	public void start() {
		this.mob.setTarget(this.mob.getLastHurtByMob());
		this.targetMob = this.mob.getTarget();
		this.timeStamp = this.mob.getLastHurtByMobTimestamp();
		this.unseenMemoryTicks = 300;
		this.alertOthers();
		super.start();
	}

	/**
	 * 
	 */
	protected void alertOthers() {
		double distance = this.getFollowDistance();
		AABB aabb = AABB.unitCubeFromLowerCorner(this.mob.position()).inflate(distance, ALERT_RANGE_Y, distance);
		List<? extends Mob> list = this.mob.level().getEntitiesOfClass(Mob.class, aabb, EntitySelector.NO_SPECTATORS);
		Iterator<? extends Mob> iterator = list.iterator();
		while (iterator.hasNext()) {
			Mob otherMob = (Mob)iterator.next();
//			DD.LOGGER.debug("process mob alert -> {}", otherMob.getName().getString());
			if (this.mob != otherMob && otherMob.getTarget() == null) {
				if (this.othersToAlert.contains(otherMob.getClass())) {
					DD.LOGGER.debug("alerting mob of targer -> {}", otherMob.getName().getString());
					alertOther(otherMob, this.mob.getLastHurtByMob());
				}
			}
		}
	}

	protected void alertOther(Mob otherMob, LivingEntity target) {
		otherMob.setTarget(target);
	}
}
