/**
 * 
 */
package mod.gottsch.neoforge.ddenizens.entity.monster;

import java.util.EnumSet;
import java.util.Random;

import mod.gottsch.neoforge.ddenizens.config.Config;
import mod.gottsch.neoforge.ddenizens.entity.ai.goal.SummonGoal;
import mod.gottsch.neoforge.ddenizens.entity.projectile.Slowball;
import mod.gottsch.neoforge.ddenizens.setup.Registration;

import mod.gottsch.neo.gottschcore.random.RandomHelper;
import mod.gottsch.neo.gottschcore.spatial.Coords;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BiomeTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.FlyingMob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

/**
 * @author Mark Gottschling on Apr 6, 2022
 *
 */
public class Gazer extends FlyingMob {
	/**
	 * 
	 * @param entityType
	 * @param level
	 */
	public Gazer(EntityType<? extends FlyingMob> entityType, Level level) {
		super(entityType, level);
		this.moveControl = new GazerMoveControl(this);
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(4, new GazerBiteGoal(this, Config.Mobs.GAZER.biteCooldownTime.get()));
		this.goalSelector.addGoal(5, new GazerRandomFloatAroundGoal(this));
		this.goalSelector.addGoal(7, new GazerLookGoal(this));
		this.goalSelector.addGoal(7, new GazerShootParalysisGoal(this, Config.Mobs.GAZER.paralysisChargeTime.get()));
		this.goalSelector.addGoal(7, new GazerSummon(this, Config.Mobs.GAZER.summonCooldownTime.get()));
		
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Boulder.class, true, (entity) -> {
			if (entity instanceof Boulder) {
				 return ((Boulder)entity).isActive();
			}
			return false;
		}));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
	}

	/**
	 * this method needs to be assigned to the EntityType during EntityAttributeCreationEvent event.
	 * see ModSetup.onAttributeCreate()
	 * @return
	 */
	public static AttributeSupplier.Builder prepareAttributes() {
		return Mob.createMobAttributes()
				.add(Attributes.ATTACK_DAMAGE, 4.0D)
				.add(Attributes.ATTACK_KNOCKBACK, 4.0D)
				.add(Attributes.ARMOR, 3.0D)
				.add(Attributes.ARMOR_TOUGHNESS, 1.0D)
				.add(Attributes.MAX_HEALTH, 18.0)
				.add(Attributes.FOLLOW_RANGE, 100.0)
				.add(Attributes.MOVEMENT_SPEED, 0.18F);
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
	public static boolean checkGazerSpawnRules(EntityType<Gazer> mob, ServerLevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random) {
		if (level.getBiome(pos).is(BiomeTags.IS_NETHER) ) {
			return DDMonster.checkDDMonsterNetherSpawnRules(mob, level, spawnType, pos, random);
		}
		else {
			return DDMonster.checkDDMonsterSpawnRules(mob, level, spawnType, pos, random);
		}
	}
	
	/**
	 * 
	 * @author Mark Gottschling on Apr 15, 2022
	 *
	 */
	static class GazerSummon extends SummonGoal {
		private static final int DEFAULT_COOLDOWN_TIME = 2400;
		private final Gazer gazer;
		private int cooldownTime;
		private Random random;

		public GazerSummon(Gazer gazer) {
			this(gazer, DEFAULT_COOLDOWN_TIME);
		}
		
		public GazerSummon(Gazer gazer, int maxCooldownTime) {
			super(maxCooldownTime);
			this.gazer = gazer;
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
			LivingEntity target = this.gazer.getTarget();
			if (target != null) {
				if (target.distanceToSqr(this.gazer) < 1024.0D && this.gazer.hasLineOfSight(target)) {
					Level level = this.gazer.level();
					++this.cooldownTime;

					if (this.cooldownTime >= summonCooldownTime) {

						int y = 0;
						int height = level.getHeight(Heightmap.Types.WORLD_SURFACE_WG, gazer.blockPosition().getX(), gazer.blockPosition().getZ());
						if (gazer.blockPosition().getY() > height) {
							// gazer is above ground
							y = height;
						}
						else {
							// find ground below gazer
							y = gazer.blockPosition().below().getY();
							while (level.getBlockState(gazer.blockPosition().atY(y)).getBlock() == Blocks.AIR) {
								y--;
								if (Math.abs(gazer.blockPosition().getY() - y) > 15) {
									return;
								}
							}
						}
						int numSpawns = random.nextInt(Config.Mobs.GAZER.minSummonSpawns.get(), Config.Mobs.GAZER.maxSummonSpawns.get() + 1);
						for (int i = 0; i < numSpawns; i++) {
							EntityType<? extends Mob> mob; 
							if (RandomHelper.checkProbability(random, 90)) {
								if (random.nextInt(2) == 0) {
									mob = Registration.HEADLESS_ENTITY_TYPE.get();
								}
								else {
									mob = Registration.ORC_ENTITY_TYPE.get();
								}
							}
							else {
								mob = Registration.DAEMON_ENTITY_TYPE.get();
							}
							boolean spawnSuccess = super.spawn((ServerLevel)level, level.random, gazer, mob, new Coords(gazer.blockPosition().getX(), y + 1, gazer.blockPosition().getZ()), target);
							if (!level.isClientSide() && spawnSuccess) {
								for (int p = 0; p < 20; p++) {
									double xSpeed = random.nextGaussian() * 0.02D;
									double ySpeed = random.nextGaussian() * 0.02D;
									double zSpeed = random.nextGaussian() * 0.02D;
									((ServerLevel)level).sendParticles(ParticleTypes.POOF, gazer.blockPosition().getX() + 0.5D, gazer.blockPosition().getY(), gazer.blockPosition().getZ() + 0.5D, 1, xSpeed, ySpeed, zSpeed, (double)0.15F);
								}
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
	 * @author Mark Gottschling on Apr 14, 2022
	 *
	 */
	static class GazerShootParalysisGoal extends Goal {
		private static final int DEFAULT_CHARGE_TIME = 80;
		private final Gazer gazer;
		public int maxChargeTime;
		public int chargeTime;

		public GazerShootParalysisGoal(Gazer gazer) {
			this(gazer, DEFAULT_CHARGE_TIME);
		}
		
		public GazerShootParalysisGoal(Gazer gazer, int maxChargeTime) {
			this.gazer = gazer;
			this.maxChargeTime =maxChargeTime;
		}

		@Override
		public boolean canUse() {
			return this.gazer.getTarget() != null && !(this.getAttackReachSqr(gazer.getTarget()) >= this.gazer.distanceToSqr(gazer.getTarget().getX(), gazer.getTarget().getY(), gazer.getTarget().getZ()));
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
			LivingEntity livingentity = this.gazer.getTarget();
			if (livingentity != null) {
				if (livingentity.distanceToSqr(this.gazer) < 4096.0D && this.gazer.hasLineOfSight(livingentity)) {
					Level level = this.gazer.level();
					++this.chargeTime;

					if (this.chargeTime >= maxChargeTime) {
						Vec3 vec3 = this.gazer.getViewVector(1.0F);
						double x = livingentity.getX() - (this.gazer.getX() + vec3.x * 2.0D);
						double y = livingentity.getY(0.5D) - (this.gazer.getY(0.5D));
						double z = livingentity.getZ() - (this.gazer.getZ() + vec3.z * 2.0D);

						Slowball slowball = new Slowball(Registration.SLOWBALL_ENTITY_TYPE.get(), level);
						slowball.init(this.gazer, x, y, z);
						slowball.setPos(this.gazer.getX() + vec3.x * 2.0D, this.gazer.getY(0.5D), slowball.getZ() + vec3.z * 2.0);
						level.addFreshEntity(slowball);
						this.chargeTime = 0;
					}
				} else if (this.chargeTime > 0) {
					--this.chargeTime;
				}
			}
		}

		protected double getAttackReachSqr(LivingEntity entity) {
			return (double)(this.gazer.getBbWidth() * 2.0F * this.gazer.getBbWidth() * 2.0F + entity.getBbWidth());
		}
	}

	/**
	 * 
	 * @author Mark Gottschling on Apr 14, 2022
	 *
	 */
	static class GazerBiteGoal extends Goal {
		private static final int DEFAULT_COOLDOWN_TIME = 80;
		private Gazer gazer;
		private int cooldown;
		private int maxCooldownTime;

		public GazerBiteGoal(Gazer gazer) {
			this(gazer, DEFAULT_COOLDOWN_TIME);
		}
		
		public GazerBiteGoal(Gazer gazer, int maxCooldownTime) {
			this.gazer = gazer;
			this.maxCooldownTime = maxCooldownTime;
		}

		@Override
		public boolean canUse() {
			if (gazer.getTarget() != null) {
				if (this.getAttackReachSqr(gazer.getTarget()) >= this.gazer.distanceToSqr(gazer.getTarget().getX(), gazer.getTarget().getY(), gazer.getTarget().getZ())) {
					return true;
				}
			}
			return false;
		}

		@Override
		public void start() {
			this.cooldown = 0;
		}

		@Override
		public boolean requiresUpdateEveryTick() {
			return true;
		}

		@Override
		public void tick() {
			cooldown++;
			if (cooldown > maxCooldownTime) {
				if (this.getAttackReachSqr(gazer.getTarget()) >= this.gazer.distanceToSqr(gazer.getTarget().getX(), gazer.getTarget().getY(), gazer.getTarget().getZ())) {
					this.gazer.doHurtTarget(gazer.getTarget());
					this.cooldown = 0;
				}
			}
		}

		protected double getAttackReachSqr(LivingEntity entity) {
			return (double)(this.gazer.getBbWidth() * 2.0F * this.gazer.getBbWidth() * 2.0F + entity.getBbWidth());
		}
	}

	/**
	 * 
	 * @author Mark Gottschling on Apr 6, 2022
	 *
	 */
	static class GazerRandomFloatAroundGoal extends Goal {
		private final Gazer gazer;
		private Random random = new Random();
		
		public GazerRandomFloatAroundGoal(Gazer gazer) {
			this.gazer = gazer;
			this.setFlags(EnumSet.of(Flag.MOVE));
		}

		@Override
		public boolean canUse() {
			MoveControl moveControl = this.gazer.getMoveControl();
			if (!moveControl.hasWanted()) {
				return true;
			} else {
				double deltaX = moveControl.getWantedX() - this.gazer.getX();
				double deltaY = moveControl.getWantedY() - this.gazer.getY();
				double detlaZ = moveControl.getWantedZ() - this.gazer.getZ();
				double delta = deltaX * deltaX + deltaY * deltaY + detlaZ * detlaZ;
				return delta < 1.0D || delta > 3600.0D;
			}
		}

		public boolean canContinueToUse() {
			return false;
		}

		/**
		 * 
		 */
		public void start() {
			double x = this.gazer.getX() + (double)((random.nextFloat() * 2.0F - 1.0F) * 8.0F);
			double y = this.gazer.getY() + (double)((random.nextFloat() * 2.0F - 1.0F) * 8.0F);
			double z = this.gazer.getZ() + (double)((random.nextFloat() * 2.0F - 1.0F) * 8.0F);

			int height = gazer.level().getHeight(Heightmap.Types.WORLD_SURFACE_WG, gazer.blockPosition().getX(), gazer.blockPosition().getZ());
			// ie above ground (anywhere)
			if (gazer.blockPosition().getY() > height) {
				y = Math.min(y, height + 12);
			}
			this.gazer.getMoveControl().setWantedPosition(x, y, z, 1.0D);
		}
	}

	/**
	 * 
	 * @author Mark Gottschling on Apr 6, 2022
	 *
	 */
	static class GazerLookGoal extends Goal {
		private final Gazer gazer;

		public GazerLookGoal(Gazer gazer) {
			this.gazer = gazer;
			this.setFlags(EnumSet.of(Flag.LOOK));
		}

		public boolean canUse() {
			return true;
		}

		public boolean requiresUpdateEveryTick() {
			return true;
		}

		@Override
		public void tick() {
			if (gazer.getTarget() == null) {
				Vec3 vec3 = gazer.getDeltaMovement();
				gazer.setYRot(-((float)Mth.atan2(vec3.x, vec3.z)) * (180F / (float)Math.PI));
				gazer.yBodyRot = gazer.getYRot();
			} else {
				LivingEntity livingEntity = this.gazer.getTarget();
				if (livingEntity.distanceToSqr(this.gazer) < 4096.0D) {
					double deltaX = livingEntity.getX() - gazer.getX();
					double deltaY = livingEntity.getZ() - gazer.getZ();
					gazer.setYRot(-((float)Mth.atan2(deltaX, deltaY)) * (180F / (float)Math.PI));
					gazer.yBodyRot = gazer.getYRot();
				}
			}
		}
	}

	/**
	 * 
	 * @author Mark Gottschling on Apr 6, 2022
	 *
	 */
	static class GazerMoveControl extends MoveControl {
		private final Gazer gazer;
		private int floatDuration;

		public GazerMoveControl(Gazer gazer) {
			super(gazer);
			this.gazer = gazer;
		}

		public void tick() {
			if (operation == Operation.MOVE_TO) {
				if (floatDuration-- <= 0) {
					floatDuration += gazer.getRandom().nextInt(5) + 2;
					Vec3 vec3 = new Vec3(wantedX - gazer.getX(), wantedY - gazer.getY(), wantedZ - gazer.getZ());
					double distance = vec3.length();
					vec3 = vec3.normalize();
					if (canReach(vec3, Mth.ceil(distance))) {
						gazer.setDeltaMovement(gazer.getDeltaMovement().add(vec3.scale(0.1D)));
					} else {
						operation = Operation.WAIT;
					}
				}

			}
		}

		private boolean canReach(Vec3 vec3, int p_32772_) {
			AABB aabb = gazer.getBoundingBox();

			for(int i = 1; i < p_32772_; ++i) {
				aabb = aabb.move(vec3);
				if (!gazer.level().noCollision(gazer, aabb)) {
					return false;
				}
			}

			return true;
		}
	}
}
