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

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import mod.gottsch.neoforge.ddenizens.DD;
import mod.gottsch.neoforge.ddenizens.config.Config;
import mod.gottsch.neoforge.ddenizens.entity.ai.goal.SummonGoal;
import mod.gottsch.neoforge.ddenizens.entity.projectile.Harmball;
import mod.gottsch.neoforge.ddenizens.setup.Registration;

import mod.gottsch.neo.gottschcore.random.RandomHelper;
import mod.gottsch.neo.gottschcore.spatial.Coords;
import mod.gottsch.neo.gottschcore.world.WorldInfo;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BiomeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FleeSunGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RestrictSunGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

/**
 * 
 * @author Mark Gottschling on Apr 15, 2022
 *
 */
public class Shadowlord extends DDMonster {
	private static final int SUN_BURN_SECONDS = 2;
	protected static final double MELEE_DISTANCE_SQUARED = 16D;
	protected static final double SUMMON_DISTANCE_SQUARED = 1024D;
	protected static final double SHOOT_DISTANCE_SQUARED = 4096D;
	protected static final int SUMMON_CHARGE_TIME = 2400;

	private double auraOfBlindessTime;
	private int numSummonDaemons;


	/**
	 * 
	 * @param entityType
	 * @param level
	 */
	public Shadowlord(EntityType<? extends Monster> entityType, Level level) {
		super(entityType, level);
		setPersistenceRequired();
		this.xpReward = 8;
	}

	/**
	 * 
	 */
	protected void registerGoals() {
		this.goalSelector.addGoal(2, new RestrictSunGoal(this));
		this.goalSelector.addGoal(3, new FleeSunGoal(this, 1.1D));
				
		this.goalSelector.addGoal(4, new ShadowlordShootHarmGoal(this, Config.Mobs.SHADOWLORD.harmChargeTime.get()));
		this.goalSelector.addGoal(4, new ShadowlordSummonGoal(this, Config.Mobs.SHADOWLORD.summonCooldownTime.get(), true));
		// TODO update with strafing movement - see RangedBowAttackGoal
		//		this.goalSelector.addGoal(5, new ShadowlordMeleeAttackGoal(this, 1.0D, false));
		this.goalSelector.addGoal(5, new MeleeAttackGoal(this, 1.1D, false));
		
		this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 1.0D));
		this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 16.0F));
		this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));


		this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Boulder.class, true, (entity) -> {
			if (entity instanceof Boulder) {
				return ((Boulder)entity).isActive();
			}
			return false;
		}));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
	}

	/**
	 * 
	 * @return
	 */
	public static AttributeSupplier.Builder createAttributes() {
		return createMonsterAttributes()
				.add(Attributes.ATTACK_DAMAGE, 8.0D)
				.add(Attributes.MAX_HEALTH, 50.0D)
				.add(Attributes.ARMOR, 10.0D)
				.add(Attributes.ARMOR_TOUGHNESS, 1.0D)
				.add(Attributes.KNOCKBACK_RESISTANCE, 0.8)
				.add(Attributes.MOVEMENT_SPEED, 0.2F)
				.add(Attributes.FOLLOW_RANGE, 80D);
	}

	@Override
	public MobType getMobType() {
		return MobType.UNDEAD;
	}

	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
			MobSpawnType spawnType, SpawnGroupData groupData, CompoundTag tag) {

		if (difficulty.getDifficulty() == Difficulty.NORMAL) {
			numSummonDaemons = 1;
		}
		else if (difficulty.getDifficulty() == Difficulty.HARD) {
			numSummonDaemons = 2;
		}
		else {
			numSummonDaemons = 0;
		}
		return super.finalizeSpawn(level, difficulty, spawnType, groupData, tag);		
	}

	public static boolean checkShadowlordSpawnRules(EntityType<Shadowlord> mob, ServerLevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random) {
		if (level.getBiome(pos).is(BiomeTags.IS_NETHER)) {
			return checkDDMonsterNetherSpawnRules(mob, level, spawnType, pos, random);
		}
		else {
			return checkDDMonsterSpawnRules(mob, level, spawnType, pos, random);
		}
	}

	/**
	 * Only executed on server side.
	 * @param entity
	 * @param amount
	 */
	public void drain(LivingEntity entity, float amount) {
		// add damage to Shadowlord's health
		DD.LOGGER.debug("draining {} hp from player", amount);
		setHealth(Math.min(getMaxHealth(), getHealth() + amount));
		if (!WorldInfo.isClientSide(this.level())) {
			for (int p = 0; p < 20; p++) {
				double xSpeed = random.nextGaussian() * 0.02D;
				double ySpeed = random.nextGaussian() * 0.02D;
				double zSpeed = random.nextGaussian() * 0.02D;
				((ServerLevel)level()).sendParticles(ParticleTypes.SOUL, blockPosition().getX() + 0.5D, blockPosition().getY(), blockPosition().getZ() + 0.5D, 1, xSpeed, ySpeed, zSpeed, (double)0.15F);
			}
		}
	}

	@Override
	public void aiStep() {
		/*
		 * Create a ring of smoke particles to delineate the boundary of the Aura of Blindness 
		 */
		if (WorldInfo.isClientSide(this.level())) {
			double x = 2D * Math.sin(auraOfBlindessTime);
			double z = 2D * Math.cos(auraOfBlindessTime);
			this.level().addParticle(ParticleTypes.SMOKE, this.position().x + x, position().y, position().z + z, 0, 0, 0);
			auraOfBlindessTime++;
			auraOfBlindessTime = auraOfBlindessTime % 360;
		}

		/*
		 * Apply Aura of Blindness to Players
		 */
		// get all entities with radius
		double distance = 2;
		AABB aabb = AABB.unitCubeFromLowerCorner(this.position()).inflate(distance, distance, distance);
		List<? extends Player> list = this.level().getEntitiesOfClass(Player.class, aabb, EntitySelector.NO_SPECTATORS);
		Iterator<? extends Player> iterator = list.iterator();
		while (iterator.hasNext()) {
			Player target = (Player)iterator.next();
			// test if player is wearing golden helmet
			ItemStack helmetStack = target.getItemBySlot(EquipmentSlot.HEAD);
			if (helmetStack.isEmpty() || helmetStack.getItem() != Items.GOLDEN_HELMET) {
				// inflict blindness
				target.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 10 * 20, 0), this);
			}
		}

		// set on fire if in sun
		if (this.isSunBurnTick()) {
			this.setSecondsOnFire(SUN_BURN_SECONDS);
		}
		super.aiStep();
	}

	/**
	 * 
	 */
	@Override
	public boolean doHurtTarget(Entity target) {
		if (super.doHurtTarget(target)) {
			if (target instanceof Player) {
				int seconds = 10;
				// inflict poison
				((LivingEntity)target).addEffect(new MobEffectInstance(MobEffects.POISON, seconds * 20, 0), this);
			}
			return true;
		} 
		return false;
	}

	/**
	 * 
	 */
	@Override
	public boolean hurt(DamageSource damageSource, float amount) {
		if (WorldInfo.isClientSide(this.level())) {
			return false;
		}

		if (damageSource.getEntity() != null && damageSource.getEntity() instanceof Player) {
			Player player = (Player)damageSource.getEntity();
			ItemStack heldStack = ((Player)damageSource.getEntity()).getItemInHand(InteractionHand.MAIN_HAND);

			if (!heldStack.isEmpty() && heldStack.getItem() == Items.GOLDEN_SWORD) {
				// increase damage to that of iron sword
				amount += 2.0F;
				// negate the weakness from the strike power of the sword
				if (player.hasEffect(MobEffects.WEAKNESS)) {
					amount += MobEffects.WEAKNESS.getAttributeModifierValue(0, null);
				}
			}
			// TODO add shadow sword condition
			else {
				amount = 1.0F;
			}
			DD.LOGGER.debug("new gold sword strike amount -> {}", amount);
		}

		return super.hurt(damageSource, amount);
	}

	/**
	 * Fires harmball at target every 4 secs.
	 * @author Mark Gottschling on Apr 19, 2022
	 *
	 */
	static class ShadowlordShootHarmGoal extends Goal {
		private static final int DEFAULT_CHARGE_TIME = 50;
		private final Shadowlord shadowlord;
		public int maxChargeTime;
		public int chargeTime;

		public ShadowlordShootHarmGoal(Shadowlord mob) {
			this(mob, DEFAULT_CHARGE_TIME);			
		}

		public ShadowlordShootHarmGoal(Shadowlord mob, int maxChargeTime) {
			this.shadowlord = mob;
			this.maxChargeTime = maxChargeTime;
		}

		@Override
		public boolean canUse() {
			return this.shadowlord.getTarget() != null; // && !(Shadowlord.MELEE_DISTANCE_SQUARED >= this.shadowlord.distanceToSqr(shadowlord.getTarget().getX(), shadowlord.getTarget().getY(), shadowlord.getTarget().getZ()));
		}

		@Override
		public void start() {
			this.chargeTime = 0;
		}

		@Override
		public void stop() {
		}

		@Override
		public boolean requiresUpdateEveryTick() {
			return true;
		}

		@Override
		public void tick() {
			LivingEntity livingentity = this.shadowlord.getTarget();
			if (livingentity != null) {
				if (livingentity.distanceToSqr(this.shadowlord) < SHOOT_DISTANCE_SQUARED && this.shadowlord.hasLineOfSight(livingentity) 
						&& livingentity.distanceToSqr(this.shadowlord) > Shadowlord.MELEE_DISTANCE_SQUARED) {

					Level level = this.shadowlord.level();
					++this.chargeTime;

					if (this.chargeTime >= maxChargeTime) {
						Vec3 vec3 = this.shadowlord.getViewVector(1.0F);
						double x = livingentity.getX() - (this.shadowlord.getX() + vec3.x * 2.0D);
						double y = livingentity.getY(0.5D) - (this.shadowlord.getY(0.5D));
						double z = livingentity.getZ() - (this.shadowlord.getZ() + vec3.z * 2.0D);

						Harmball spell = new Harmball(Registration.HARMBALL_ENTITY_TYPE.get(), level);
						spell.init(this.shadowlord, x, y, z);
						spell.setPos(this.shadowlord.getX() + vec3.x * 2.0D, this.shadowlord.getY(0.5D), spell.getZ() + vec3.z * 2.0);
						level.addFreshEntity(spell);
						this.chargeTime = 0;
					}
				} else if (this.chargeTime > 0) {
					--this.chargeTime;
				}

			}
		}

		protected double getAttackReachSqr(LivingEntity entity) {
			return (double)(this.shadowlord.getBbWidth() * 2.0F * this.shadowlord.getBbWidth() * 2.0F + entity.getBbWidth() + 0.5F);
		}
	}

	/**
	 * 
	 * @author Mark Gottschling on Apr 19, 2022
	 *
	 */
	static class ShadowlordSummonGoal extends SummonGoal {
		private final Shadowlord shadowlord;
		private Random random;

		public ShadowlordSummonGoal(Shadowlord shadowlord) {
			this(shadowlord, SUMMON_CHARGE_TIME, true);
		}

		/**
		 * 
		 * @param mob
		 * @param summonCooldownTime
		 * @param canSummonDaemon
		 */
		public ShadowlordSummonGoal(Shadowlord mob, int summonCooldownTime, boolean canSummonDaemon) {
			super(summonCooldownTime);
			this.shadowlord = mob;			
			this.random = new Random();
		}

		@Override
		public void start() {
			this.cooldownTime = summonCooldownTime / 2;
		}

		@Override
		public void stop() {
		}

		@Override
		public void tick() {
			LivingEntity target = this.shadowlord.getTarget();
			if (target != null) {
				if (target.distanceToSqr(this.shadowlord) < SUMMON_DISTANCE_SQUARED && this.shadowlord.hasLineOfSight(target)) {
					Level level = this.shadowlord.level();
					++this.cooldownTime;

					if (this.cooldownTime >= summonCooldownTime) {

						int y = shadowlord.blockPosition().getY();
						boolean spawnSuccess = false;
						// summon daemon is health < max/3
						if (shadowlord.getHealth() < shadowlord.getMaxHealth() / 3 && shadowlord.numSummonDaemons > 0) {
							spawnSuccess |=spawn((ServerLevel)level, level.getRandom(), shadowlord, Registration.DAEMON_ENTITY_TYPE.get(), new Coords(shadowlord.blockPosition().getX(), y + 1, shadowlord.blockPosition().getZ()), target);
							if (spawnSuccess) {
								shadowlord.numSummonDaemons--;
							}
						}
						else {
							int numSpawns = random.nextInt(Config.Mobs.SHADOWLORD.minSummonSpawns.get(), Config.Mobs.SHADOWLORD.maxSummonSpawns.get() + 1);
							for (int i = 0; i < numSpawns; i++) {
								EntityType<? extends Mob> mob; 
								if (RandomHelper.checkProbability(random, 50)) {
									mob = Registration.SHADOW_ENTITY_TYPE.get();
								}
								else {
									mob = Registration.GHOUL_ENTITY_TYPE.get();
								}
								spawnSuccess |=spawn((ServerLevel)level, level.getRandom(), shadowlord, mob, new Coords(shadowlord.blockPosition().getX(), y + 1, shadowlord.blockPosition().getZ()), target);
							}
						}
						if (!WorldInfo.isClientSide(level) && spawnSuccess) {
							for (int p = 0; p < 20; p++) {
								double xSpeed = random.nextGaussian() * 0.02D;
								double ySpeed = random.nextGaussian() * 0.02D;
								double zSpeed = random.nextGaussian() * 0.02D;
								((ServerLevel)level).sendParticles(ParticleTypes.POOF, shadowlord.blockPosition().getX() + 0.5D, shadowlord.blockPosition().getY(), shadowlord.blockPosition().getZ() + 0.5D, 1, xSpeed, ySpeed, zSpeed, (double)0.15F);
							}
						}
						this.cooldownTime = 0;
					}
				}
			}
		}

		@Override
		public boolean canUse() {
			return true;
		}
	}

	/**
	 * 
	 * @author Mark Gottschling on Apr 19, 2022
	 *
	 */
	public static class ShadowlordMeleeAttackGoal extends MeleeAttackGoal {

		public ShadowlordMeleeAttackGoal(PathfinderMob mob, double walkSpeedModifier, boolean sprintSpeedModifier) {
			super(mob, walkSpeedModifier, sprintSpeedModifier);
		}

		/**
		 * 
		 */
		@Override
		public boolean canUse() {
			if (mob.getTarget() != null) {
				if (mob.distanceToSqr(mob.getTarget().getX(), mob.getTarget().getY(), mob.getTarget().getZ()) <= 100D) {
					boolean x = super.canUse();
					DD.LOGGER.debug("can use melee at distance {} -> {}", mob.distanceToSqr(mob.getTarget().getX(), mob.getTarget().getY(), mob.getTarget().getZ()), x);
					return x;
				}
			}
			return false;
		}

		@Override
		public boolean canContinueToUse() {
			if (mob.getTarget() != null) {
				if (mob.distanceToSqr(mob.getTarget().getX(), mob.getTarget().getY(), mob.getTarget().getZ()) <= 100D) {
					boolean x = super.canContinueToUse();
					DD.LOGGER.debug("can use melee at distance {} -> {}", mob.distanceToSqr(mob.getTarget().getX(), mob.getTarget().getY(), mob.getTarget().getZ()), x);
					return x;
				}
			}
			return false;
		}
	}
}