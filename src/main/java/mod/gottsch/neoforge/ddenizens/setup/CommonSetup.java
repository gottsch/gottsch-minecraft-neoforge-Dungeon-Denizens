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
package mod.gottsch.neoforge.ddenizens.setup;

import mod.gottsch.neoforge.ddenizens.DD;
import mod.gottsch.neoforge.ddenizens.config.Config;
import mod.gottsch.neoforge.ddenizens.entity.monster.Boulder;
import mod.gottsch.neoforge.ddenizens.entity.monster.DDMonster;
import mod.gottsch.neoforge.ddenizens.entity.monster.Daemon;
import mod.gottsch.neoforge.ddenizens.entity.monster.Gazer;
import mod.gottsch.neoforge.ddenizens.entity.monster.Ghoul;
import mod.gottsch.neoforge.ddenizens.entity.monster.Headless;
import mod.gottsch.neoforge.ddenizens.entity.monster.Orc;
import mod.gottsch.neoforge.ddenizens.entity.monster.Shadow;
import mod.gottsch.neoforge.ddenizens.entity.monster.Shadowlord;

import mod.gottsch.neo.gottschcore.world.WorldInfo;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.ZombieVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab.TabVisibility;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

/**
 * Common event bus subscriber.
 * @author Mark Gottschling on Apr 2, 2022
 *
 */
@EventBusSubscriber(modid = DD.MODID, bus = EventBusSubscriber.Bus.MOD)
public class CommonSetup {
	public static void init(final FMLCommonSetupEvent event) {
		Config.instance.addRollingFileAppender(DD.MODID);
		DD.LOGGER.info("starting Dungeon Denizens");
	}

	/**
	 * attach defined attributes to the entity.
	 * @param event
	 */
	@SubscribeEvent
	public static void onAttributeCreate(EntityAttributeCreationEvent event) {
		event.put(Registration.HEADLESS_ENTITY_TYPE.get(), Headless.createAttributes().build());
		event.put(Registration.ORC_ENTITY_TYPE.get(), Orc.createAttributes().build());
		event.put(Registration.GHOUL_ENTITY_TYPE.get(), Ghoul.createAttributes().build());
		event.put(Registration.GAZER_ENTITY_TYPE.get(), Gazer.prepareAttributes().build());
		event.put(Registration.BOULDER_ENTITY_TYPE.get(), Boulder.createAttributes().build());
		event.put(Registration.SHADOW_ENTITY_TYPE.get(), Shadow.createAttributes().build());
		event.put(Registration.SHADOWLORD_ENTITY_TYPE.get(), Shadowlord.createAttributes().build());
		event.put(Registration.DAEMON_ENTITY_TYPE.get(), Daemon.createAttributes().build());

	}

	@SubscribeEvent
	public static void registerEntitySpawn(SpawnPlacementRegisterEvent event) {
		event.register(Registration.HEADLESS_ENTITY_TYPE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, DDMonster::checkDDMonsterSpawnRules, SpawnPlacementRegisterEvent.Operation.OR);
		event.register(Registration.ORC_ENTITY_TYPE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, DDMonster::checkDDMonsterSpawnRules, SpawnPlacementRegisterEvent.Operation.OR);
		event.register(Registration.GHOUL_ENTITY_TYPE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, DDMonster::checkDDMonsterSpawnRules, SpawnPlacementRegisterEvent.Operation.OR);
		event.register(Registration.BOULDER_ENTITY_TYPE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.WORLD_SURFACE, Boulder::checkSpawnRules, SpawnPlacementRegisterEvent.Operation.OR);

		event.register(Registration.SHADOW_ENTITY_TYPE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Shadow::checkShadowSpawnRules, SpawnPlacementRegisterEvent.Operation.OR);
		event.register(Registration.SHADOWLORD_ENTITY_TYPE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Shadowlord::checkShadowlordSpawnRules, SpawnPlacementRegisterEvent.Operation.OR);
		event.register(Registration.GAZER_ENTITY_TYPE.get(), SpawnPlacements.Type.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Gazer::checkGazerSpawnRules, SpawnPlacementRegisterEvent.Operation.OR);
		event.register(Registration.DAEMON_ENTITY_TYPE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Daemon::checkDaemonSpawnRules, SpawnPlacementRegisterEvent.Operation.OR);
	}

	@SubscribeEvent
	public static void registemItemsToTab(BuildCreativeModeTabContentsEvent event) {
		if (event.getTabKey() == CreativeModeTabs.SPAWN_EGGS) {
			event.accept(Registration.HEADLESS_EGG.get(), TabVisibility.PARENT_AND_SEARCH_TABS);
			event.accept(Registration.ORC_EGG.get(), TabVisibility.PARENT_AND_SEARCH_TABS);
			event.accept(Registration.GHOUL_EGG.get(), TabVisibility.PARENT_AND_SEARCH_TABS);
			event.accept(Registration.BOULDER_EGG.get(), TabVisibility.PARENT_AND_SEARCH_TABS);

			event.accept(Registration.SHADOW_EGG.get(), TabVisibility.PARENT_AND_SEARCH_TABS);
			event.accept(Registration.SHADOWLORD_EGG.get(), TabVisibility.PARENT_AND_SEARCH_TABS);
			event.accept(Registration.GAZER_EGG.get(), TabVisibility.PARENT_AND_SEARCH_TABS);
			event.accept(Registration.DAEMON_EGG.get(), TabVisibility.PARENT_AND_SEARCH_TABS);
		}
		else if (event.getTabKey() == CreativeModeTabs.COMBAT) {
			event.accept(Registration.CLUB.get(), TabVisibility.PARENT_AND_SEARCH_TABS);
			event.accept(Registration.SPIKED_CLUB.get(), TabVisibility.PARENT_AND_SEARCH_TABS);
		}
	}
	
	@EventBusSubscriber(modid = DD.MODID, bus = EventBusSubscriber.Bus.FORGE)
	public static class ForgeBusSucscriber {
		/*
		 * Register the Features with Biomes
		 */
//		@SubscribeEvent
//		public static <BiomeLoadingEvent> void onBiomeLoading(final BiomeLoadingEvent event) {
//			DD.LOGGER.debug("event for biome -> {}, category -> {}", event.getName(), event.getCategory().getName());
//			/* 
//			 * register mob spawns to biomes
//			 */
//			ResourceLocation biome = event.getName();
//
//			Registration.ALL_MOBS.forEach(entityType -> {				
//				IMobConfig config = Config.Mobs.MOBS.get(((EntityType<?>)entityType.get()).getRegistryName());
//
//				if (config.getSpawnConfig().enable.get()) {
//					Result result = isBiomeAllowed(biome, event.getCategory(), config.getSpawnConfig());
//					if (result == Result.OK || result == Result.WHITE_LISTED) {
//						DD.LOGGER.debug("registering spawner data -> {}", ((EntityType<?>)entityType.get()).getRegistryName());
//
//						if (event.getCategory() == BiomeCategory.NETHER && config instanceof INetherMobConfig) {
//							event.getSpawns().getSpawner(MobCategory.MONSTER)
//							.add(new MobSpawnSettings.SpawnerData(
//									(EntityType<?>)entityType.get(), 
//									((INetherMobConfig)config).getNetherSpawn().spawnWeight.get(), 
//									((INetherMobConfig)config).getNetherSpawn().minSpawn.get(), 
//									((INetherMobConfig)config).getNetherSpawn().maxSpawn.get()));
//						}
//						else {
//							event.getSpawns().getSpawner(MobCategory.MONSTER)
//							.add(new MobSpawnSettings.SpawnerData(
//									(EntityType<?>)entityType.get(), 
//									config.getSpawnConfig().spawnWeight.get(), 
//									config.getSpawnConfig().minSpawn.get(), 
//									config.getSpawnConfig().maxSpawn.get()));
//						}
//					}						
//				}
//			});
//		}

		//	    @SubscribeEvent
		//	    public static void registerParticle(ParticleFactoryRegisterEvent event){
		//	        Minecraft.getInstance().particleEngine.register(Registration.SHADOW_PARTICLE.get(), ModGlowParticle.BlockOffSideProvider::new);
		//	    }

		/**
		 * Helper method
		 * @param biome
		 * @param category
		 * @param config
		 * @return
		 */
//		public static Result isBiomeAllowed(ResourceLocation biome, BiomeCategory category, SpawnConfig config) {
//			return BiomeHelper.isBiomeAllowed(biome, category, config.biomeWhitelist.get(), config.biomeBlacklist.get(),
//					config.biomeCategoryWhitelist.get(), config.biomeCategoryBlacklist.get());
//		}

		@SubscribeEvent
		public static void addGoals(final EntityJoinLevelEvent event) {
			if (event.getEntity() instanceof Zombie) {
				((Zombie)event.getEntity()).goalSelector.addGoal(3, new AvoidEntityGoal<>(((Zombie)event.getEntity()), Boulder.class, 6.0F, 1.0D, 1.2D, (entity) -> {
					if (entity instanceof Boulder) {
						 return ((Boulder)entity).isActive();
					}
					return false;
				}));
			}
			else if (event.getEntity() instanceof Skeleton) {
				((Skeleton)event.getEntity()).goalSelector.addGoal(3, new AvoidEntityGoal<>(((Skeleton)event.getEntity()), Boulder.class, 6.0F, 1.0D, 1.2D, (entity) -> {
					if (entity instanceof Boulder) {
						 return ((Boulder)entity).isActive();
					}
					return false;
				}));
			}
			else if (event.getEntity() instanceof ZombieVillager) {
				((ZombieVillager)event.getEntity()).goalSelector.addGoal(3, new AvoidEntityGoal<>(((ZombieVillager)event.getEntity()), Boulder.class, 6.0F, 1.0D, 1.2D, (entity) -> {
					if (entity instanceof Boulder) {
						 return ((Boulder)entity).isActive();
					}
					return false;
				}));
			}
		}

		@SubscribeEvent
		public static void onFeedBoulder(final PlayerInteractEvent.EntityInteract event) {
			// get item in hand
			ItemStack heldItem = event.getItemStack();

			if (!heldItem.isEmpty() && (heldItem.getItem() == Items.IRON_INGOT 
					|| heldItem.getItem() == Items.IRON_ORE 
					|| heldItem.getItem() == Items.DEEPSLATE_IRON_ORE)) {

				if (event.getTarget() instanceof Boulder) {
					Boulder boulder = (Boulder)event.getTarget();
					boulder.feed(event.getEntity().getUUID());
				}
			}
		}

		@SubscribeEvent
		public static void hitFromShadowlord(LivingDamageEvent event) {
			if (WorldInfo.isClientSide(event.getEntity().level())) {
				return;
			}

			if (event.getEntity() instanceof Player && event.getSource().getEntity() instanceof Shadowlord) {
				// get the player
				ServerPlayer player = (ServerPlayer) event.getEntity();
				((Shadowlord)event.getSource().getEntity()).drain(player, event.getAmount());
			}
			else if (event.getEntity() instanceof Shadowlord && event.getSource().getEntity() instanceof Player) {

			}
		}

		//		@SubscribeEvent
		public static void hitFromShadowlord(LivingHurtEvent event) {
			if (WorldInfo.isClientSide(event.getEntity().level())) {
				return;
			}

			// do something to player every update tick:
			if (event.getEntity() instanceof Player && event.getSource().getEntity() instanceof Shadowlord) {
				DD.LOGGER.debug("event: hurting player from shadowlord -> {}", event.getAmount());
			}
		}

	}
}
