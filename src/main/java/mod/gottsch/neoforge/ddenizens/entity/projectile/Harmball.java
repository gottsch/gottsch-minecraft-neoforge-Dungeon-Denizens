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

import mod.gottsch.neoforge.ddenizens.config.Config;
import mod.gottsch.neoforge.ddenizens.damagesource.ModDamageTypes;
import mod.gottsch.neoforge.ddenizens.setup.Registration;

import net.minecraft.Util;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

/**
 * 
 * @author Mark Gottschling on Apr 19, 2022
 *
 */
public class Harmball extends AbstractHurtingProjectile implements ItemSupplier {
	private static final EntityDataAccessor<ItemStack> DATA_ITEM_STACK = SynchedEntityData.defineId(Harmball.class, EntityDataSerializers.ITEM_STACK);

	/**
	 * 
	 * @param entityType
	 * @param level
	 */
	public Harmball(EntityType<Harmball> entityType, Level level) {
		super(entityType, level);
	}

	/**
	 * 
	 * @param owner
	 * @param x
	 * @param y
	 * @param z
	 */
	public void init(LivingEntity owner, double x, double y, double z) {
		this.moveTo(owner.getX(), owner.getY(), owner.getZ(), this.getYRot(), this.getXRot());
		this.reapplyPosition();
		double d0 = Math.sqrt(x * x + y * y + z * z);
		if (d0 != 0.0D) {
			this.xPower = x / d0 * 0.1D;
			this.yPower = y / d0 * 0.1D;
			this.zPower = z / d0 * 0.1D;
		}
		this.setOwner(owner);
		this.setRot(owner.getYRot(), owner.getXRot());
	}

	@Override
	public boolean isOnFire() {
		return false;
	}

	@Override
	protected void onHit(HitResult hitResult) {
		super.onHit(hitResult);
		if (!this.level().isClientSide) {
			this.discard();
		}
	}

	@Override
	protected void onHitEntity(EntityHitResult hitResult) {
		super.onHitEntity(hitResult);
		if (!this.level().isClientSide) {
			Entity target = hitResult.getEntity();
			Entity ownerEntity = this.getOwner();
//			DamageSource damageSource = new IndirectEntityDamageSource("harmball", this, ownerEntity).setProjectile();
			target.hurt(level().damageSources().source(ModDamageTypes.HARMBALL), Config.Spells.HARMBALL.damage.get());
//			target.hurt(damageSource, Config.Spells.HARMBALL.damage.get());
			if (target instanceof LivingEntity) {
				this.doEnchantDamageEffects((LivingEntity)ownerEntity, target);
			}
		}
	}

	public void setItem(ItemStack stack) {
		if (!stack.is(Registration.HARMBALL_ITEM.get()) || stack.hasTag()) {
			this.getEntityData().set(DATA_ITEM_STACK, Util.make(stack.copy(), (itemStack) -> {
				itemStack.setCount(1);
			}));
		}
	}

	protected ItemStack getItemRaw() {
		return this.getEntityData().get(DATA_ITEM_STACK);
	}

	@Override
	protected ParticleOptions getTrailParticle() {
		return ParticleTypes.SMOKE;
	}

	@Override
	public ItemStack getItem() {
		ItemStack stack = this.getItemRaw();
		return stack.isEmpty() ? new ItemStack(Registration.HARMBALL_ITEM.get()) : stack;
	}

	@Override
	protected void defineSynchedData() {
		this.getEntityData().define(DATA_ITEM_STACK, ItemStack.EMPTY);
	}

	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		ItemStack itemstack = this.getItemRaw();
		if (!itemstack.isEmpty()) {
			tag.put("Item", itemstack.save(new CompoundTag()));
		}

	}

	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		ItemStack itemStack = ItemStack.of(tag.getCompound("Item"));
		this.setItem(itemStack);
	}
}
