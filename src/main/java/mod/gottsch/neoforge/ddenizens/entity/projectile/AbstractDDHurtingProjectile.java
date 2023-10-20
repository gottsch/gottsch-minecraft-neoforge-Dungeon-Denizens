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
package mod.gottsch.neoforge.ddenizens.entity.projectile;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

/**
 * This is an almost exact duplicate of vanilla AbstractHurtingProjectile except
 * that tick() separates out the execution code after the call to super.tick().
 * This is done so that this class can be extended with different motion behaviours,
 * while still being able to call super.tick().
 * 
 * @author Mark Gottschling on Apr 22, 2022
 *
 */
public abstract class AbstractDDHurtingProjectile extends Projectile {
	private static final String POWER_TAG = "power";

	public double xPower;
	public double yPower;
	public double zPower;

	/**
	 * 
	 * @param entityType
	 * @param level
	 */
	public AbstractDDHurtingProjectile(EntityType<? extends AbstractDDHurtingProjectile> entityType, Level level) {
		super(entityType, level);
	}

	/**
	 * This method is separated out from the original vanilla constructor that includes the vector xyz values.
	 * @param owner
	 * @param x
	 * @param y
	 * @param z
	 */
	public void init(LivingEntity owner, double x, double y, double z, double x2, double y2, double z2) {
		this.moveTo(x, y, z, this.getYRot(), this.getXRot());
		this.reapplyPosition();
		double d0 = Math.sqrt(x2 * x2 + y2 * y2 + z2 * z2);
		if (d0 != 0.0D) {
			this.xPower = x2 / d0 * 0.1D;
			this.yPower = y2 / d0 * 0.1D;
			this.zPower = z2 / d0 * 0.1D;
		}
		this.setOwner(owner);
		this.setRot(owner.getYRot(), owner.getXRot());
	}

	@Override
	abstract protected void defineSynchedData();

	public boolean shouldRenderAtSqrDistance(double distanceSq) {
		double d0 = this.getBoundingBox().getSize() * 4.0D;
		if (Double.isNaN(d0)) {
			d0 = 4.0D;
		}

		d0 *= 64.0D;
		return distanceSq < d0 * d0;
	}

	@Override
	public void tick() {
		Entity entity = this.getOwner();
		if (this.level().isClientSide || (entity == null || !entity.isRemoved()) && this.level().hasChunkAt(this.blockPosition())) {
			super.tick();
			clientSideTick();
		} else {
			serverSideTick();
		}
	}

	/**
	 *  where custom code goes.
	 *  default implementation is vanilla projectile.
	 */
	public void clientSideTick() {
		if (this.shouldBurn()) {
			this.setSecondsOnFire(1);
		}

		HitResult hitresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
		if (hitresult.getType() != HitResult.Type.MISS && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, hitresult)) {
			this.onHit(hitresult);
		}

		this.checkInsideBlocks();
		Vec3 vec3 = this.getDeltaMovement();
		double d0 = this.getX() + vec3.x;
		double d1 = this.getY() + vec3.y;
		double d2 = this.getZ() + vec3.z;
		ProjectileUtil.rotateTowardsMovement(this, 0.2F);
		float f = this.getInertia();
		if (this.isInWater()) {
			for(int i = 0; i < 4; ++i) {
				float f1 = 0.25F;
				this.level().addParticle(ParticleTypes.BUBBLE, d0 - vec3.x * 0.25D, d1 - vec3.y * 0.25D, d2 - vec3.z * 0.25D, vec3.x, vec3.y, vec3.z);
			}

			f = 0.8F;
		}

		this.setDeltaMovement(vec3.add(this.xPower, this.yPower, this.zPower).scale((double)f));
		this.level().addParticle(this.getTrailParticle(), d0, d1 + 0.5D, d2, 0.0D, 0.0D, 0.0D);
		this.setPos(d0, d1, d2);
	}

	public void serverSideTick() {
		// remove entity from server side
		this.discard();
	}

	protected boolean canHitEntity(Entity entity) {
		return super.canHitEntity(entity) && !entity.noPhysics;
	}

	protected boolean shouldBurn() {
		return true;
	}

	protected ParticleOptions getTrailParticle() {
		return ParticleTypes.SMOKE;
	}

	protected float getInertia() {
		return 0.95F;
	}

	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		tag.put(POWER_TAG, this.newDoubleList(new double[]{
				this.xPower, 
				this.yPower, 
				this.zPower}));
	}

	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		if (tag.contains(POWER_TAG, 9)) {
			ListTag listtag = tag.getList(POWER_TAG, 6);
			if (listtag.size() == 3) {
				this.xPower = listtag.getDouble(0);
				this.yPower = listtag.getDouble(1);
				this.zPower = listtag.getDouble(2);
			}
		}

	}
	@Override
	public boolean isPickable() {
		return true;
	}
	@Override
	public float getPickRadius() {
		return 1.0F;
	}

	@Override
	public boolean hurt(DamageSource damageSource, float amount) {
		if (this.isInvulnerableTo(damageSource)) {
			return false;
		} else {
			this.markHurt();
			Entity entity = damageSource.getEntity();
			if (entity != null) {
				Vec3 vec3 = entity.getLookAngle();
				this.setDeltaMovement(vec3);
				this.xPower = vec3.x * 0.1D;
				this.yPower = vec3.y * 0.1D;
				this.zPower = vec3.z * 0.1D;
				this.setOwner(entity);
				return true;
			} else {
				return false;
			}
		}
	}
	
//	@Override
//	public float getBrightness() {
//		return 1.0F;
//	}

	@Override
	public Packet<ClientGamePacketListener> getAddEntityPacket() {
		Entity entity = this.getOwner();
		int i = entity == null ? 0 : entity.getId();
		return new ClientboundAddEntityPacket(this.getId(), this.getUUID(), this.getX(), this.getY(), this.getZ(), this.getXRot(), this.getYRot(), this.getType(), i, new Vec3(this.xPower, this.yPower, this.zPower), 0D);
	}

	@Override
	public void recreateFromPacket(ClientboundAddEntityPacket packet) {
		super.recreateFromPacket(packet);
		double d0 = packet.getXa();
		double d1 = packet.getYa();
		double d2 = packet.getZa();
		double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
		if (d3 != 0.0D) {
			this.xPower = d0 / d3 * 0.1D;
			this.yPower = d1 / d3 * 0.1D;
			this.zPower = d2 / d3 * 0.1D;
		}
	}
}
