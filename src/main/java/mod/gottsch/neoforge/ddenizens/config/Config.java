/*
 * This file is part of  Dungeon Denizens.
 * Copyright (c) 2021 Mark Gottschling (gottsch)
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
package mod.gottsch.neoforge.ddenizens.config;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;
import mod.gottsch.neoforge.ddenizens.DD;
import mod.gottsch.neoforge.ddenizens.setup.Registration;

import mod.gottsch.neo.gottschcore.config.AbstractConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.Builder;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.common.ForgeConfigSpec.DoubleValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.config.ModConfig;

/**
 * 
 * @author Mark Gottschling on Apr 25, 2022
 *
 */
@EventBusSubscriber(modid = DD.MODID, bus = EventBusSubscriber.Bus.MOD)
public final class Config extends AbstractConfig {
	protected static final Builder COMMON_BUILDER = new Builder();

	public static final String CATEGORY_DIV = "##############################";
	public static final String UNDERLINE_DIV = "------------------------------";

	public static final double MIN_HEALTH = 1D;
	public static final double MAX_HEALTH = 1024D;
	public static final int UNDERGROUND_HEIGHT = 60;
	public static final int MIN_HEIGHT = -64;
	public static final int MAX_HEIGHT = 319;

	public static ForgeConfigSpec COMMON_CONFIG;
	public static Logging LOGGING;
	
	public static Config instance = new Config();
	
	/**
	 * 
	 */
	public static void register() {
		registerServerConfigs();
	}

	/**
	 * TODO change this to SERVER SPEC
	 */
	private static void registerServerConfigs() {
		Builder COMMON_BUILDER = new Builder();
		LOGGING = new Logging(COMMON_BUILDER);
		Mobs.register(COMMON_BUILDER);
		Spells.register(COMMON_BUILDER);
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, COMMON_BUILDER.build());
	}

	public static class CommonSpawnConfig {
//		public IntValue minSpawn;
//		public IntValue maxSpawn;
//		public ConfigValue<Integer> spawnWeight;

		public IntValue minHeight;
		public IntValue maxHeight;
		
		public CommonSpawnConfig() {}
		
		public CommonSpawnConfig(Builder builder, int i, int j, int k, int minHeight2, int maxHeight2) {
			configure(builder, maxHeight2, i, k, minHeight2, maxHeight2);
		}

		/*
		 * sets the property values with a push()/pop()
		 */
		public void configure(Builder builder, int weight, int minSpawn, int maxSpawn,
                              int minHeight, int maxHeight) {
			
//			this.minSpawn = builder
//					.comment(" Minimum spawn group size.")
//					.defineInRange("minSpawnSize: ", minSpawn, 1, Integer.MAX_VALUE);
//
//			this.maxSpawn = builder
//					.comment(" Maximum spawn group size.")
//					.defineInRange("maxSpawnSize", maxSpawn, 1, Integer.MAX_VALUE);
//
//			this.spawnWeight = builder
//					.comment(" Weight of the spawn. A higher number represents a greater probability.", 
//							" A zombie in the overworld is 95.", 
//							" Use 0 for default, which uses a calculation based on zombie's weight.")
//					.defineInRange("spawnWeight: ", weight, 0, Integer.MAX_VALUE);

			this.minHeight = builder
					.comment(" Minimum world height for spawning.")
					.defineInRange("minHeight: ", minHeight, MIN_HEIGHT, MAX_HEIGHT);

			this.maxHeight = builder
					.comment(" Maximum world height for spawning.")
					.defineInRange("maxHeight: ", maxHeight, MIN_HEIGHT, MAX_HEIGHT);
		}
	}
	
	/*
	 * 
	 */
	public static class NetherSpawnConfig extends CommonSpawnConfig {
		public NetherSpawnConfig(Builder builder, int weight, int minSpawn, int maxSpawn,
                                 int minHeight, int maxHeight) {
			builder.push("nether_spawning");
			configure(builder, weight, minSpawn, maxSpawn, minHeight, maxHeight);
			builder.pop();
		}
	}

	/**
	 * 
	 * @author Mark Gottschling on Apr 26, 2022
	 *
	 */
//	@Deprecated
//	public static class SpawnConfig extends CommonSpawnConfig {
//		@Deprecated
//		public static final int IGNORE_HEIGHT = -65;
//
//		public final BooleanValue  enable;
//
//		public ConfigValue<List<? extends String>> biomeWhitelist;
//		public ConfigValue<List<? extends String>> biomeBlacklist;
//
//		public ConfigValue<List<? extends String>> biomeCategoryWhitelist;
//		public ConfigValue<List<? extends String>> biomeCategoryBlacklist;
//
//		private static final Predicate<Object> STRING_PREDICATE = s -> s instanceof String;
//
//		public SpawnConfig(ForgeConfigSpec.Builder builder, int weight, int minSpawn, int maxSpawn, 
//				int minHeight, int maxHeight,
//				List<String> biomeWhitelist, List<String> biomeBlacklist,
//				List<String> categoryWhitelist, List<String> categoryBlacklist) {
//
//			builder.push("spawning");
//
//			this.enable = builder
//					.comment(" Enable / disable spawning.")
//					.define("enable", true);
//
//			// configure the common properties
//			configure(builder, weight, minSpawn, maxSpawn, minHeight, maxHeight);
//
//			this.biomeWhitelist = builder.comment(" Allowed biomes for spawning. Must match the Biome Registry Name(s). ex. minecraft:plains", " Supercedes blacklist.",
//					" Biome white/black lists superced biome category white/black lists.")
//					.defineList("biomeWhitelist", biomeWhitelist, STRING_PREDICATE);
//
//			this.biomeBlacklist = builder.comment(" Disallowed biomes for spawning. Must match the Biome Registry Name(s). ex. minecraft:plains")
//					.defineList("biomeBlacklist", biomeBlacklist, STRING_PREDICATE);
//
//			this.biomeCategoryWhitelist = builder.comment(" Allowed biome categories for spawning. Must match the Biome Category names. ex. underground, nether", " Supercedes blacklist.")
//					.defineList("biomeCategoryWhitelist", categoryWhitelist, STRING_PREDICATE);
//
//			this.biomeCategoryBlacklist = builder.comment(" Disallowed biome categories for spawning. Must match the Biome Category names. ex. underground, nether")
//					.defineList("biomeCategoryBlacklist", categoryBlacklist, STRING_PREDICATE);
//
//			builder.pop();
//		}
//	}

	/*
	 * 
	 */
	public static class ParalysisConfig {
		public IntValue damage;
		public IntValue duration;

		public ParalysisConfig(Builder builder) {
			builder.comment(CATEGORY_DIV, " Paralysis spell properties.", CATEGORY_DIV).push("paralysis");				

			damage = builder
					.comment(" The amount of damage the spell inflicts (this is in addition to the slowness/paralysis).")
					.defineInRange("damage", 2, 1, Integer.MAX_VALUE);

			duration = builder
					.comment(" The length of time in ticks that the spell lasts for.")
					.defineInRange("duration", 200, 1, Integer.MAX_VALUE);

			builder.pop();
		}		
	}

	public static class HarmballConfig {
		public IntValue damage;

		public HarmballConfig(Builder builder) {
			builder.comment(CATEGORY_DIV, " Shadowlord spell properties.", CATEGORY_DIV).push("shadowlord_spell");				

			damage = builder
					.comment(" The amount of damage the spell inflicts.")
					.defineInRange("damage", 6, 1, Integer.MAX_VALUE);

			builder.pop();
		}
	}

	public static class FirespoutConfig {
		public IntValue explosionRadius;
		public IntValue damage;
		public IntValue maxHeight;

		public FirespoutConfig(Builder builder) {
			builder.comment(CATEGORY_DIV, " Firespout spell properties.", CATEGORY_DIV).push("firespout");				

			explosionRadius = builder
					.comment(" The radius of the explosion.")
					.defineInRange("explosionRadius", 1, 1, 10);

			damage = builder
					.comment(" The amount of damage the spell inflicts (this is fire damage, not explosion damage).")
					.defineInRange("damage", 6, 1, Integer.MAX_VALUE);

			maxHeight = builder
					.comment(" Maximum height in blocks that a firespout can reach.")
					.defineInRange("damage", 5, 1, 20);

			builder.pop();
		}
	}

	public static class Spells {
		public static ParalysisConfig PARALYSIS;
		public static HarmballConfig HARMBALL;
		public static FirespoutConfig FIRESPOUT;

		public static void register(Builder builder) {
			PARALYSIS = new ParalysisConfig(builder);
			HARMBALL = new HarmballConfig(builder);
			FIRESPOUT = new FirespoutConfig(builder);
		}
	}

	public static class Mobs {
		public static HeadlessConfig HEADLESS;
		public static OrcConfig ORC;
		public static GhoulConfig GHOUL;
		public static BoulderConfig BOULDER;
		public static ShadowConfig SHADOW;
		public static GazerConfig GAZER;
		public static ShadowlordConfig SHADOWLORD;
		public static DaemonConfig DAEMON;

		public static Map<ResourceLocation, IMobConfig> MOBS = Maps.newHashMap();

		public static void register(Builder builder) {
			HEADLESS = new HeadlessConfig(builder);
			ORC = new OrcConfig(builder);
			GHOUL = new GhoulConfig(builder);
			BOULDER = new BoulderConfig(builder);
			SHADOW = new ShadowConfig(builder);
			GAZER = new GazerConfig(builder);
			SHADOWLORD = new ShadowlordConfig(builder);
			DAEMON = new DaemonConfig(builder);

			MOBS.put(new ResourceLocation(DD.MODID, Registration.HEADLESS), HEADLESS);
			MOBS.put(new ResourceLocation(DD.MODID, Registration.ORC), ORC);
			MOBS.put(new ResourceLocation(DD.MODID, Registration.GHOUL), GHOUL);
			MOBS.put(new ResourceLocation(DD.MODID, Registration.BOULDER), BOULDER);
			MOBS.put(new ResourceLocation(DD.MODID, Registration.SHADOW), SHADOW);
			MOBS.put(new ResourceLocation(DD.MODID, Registration.GAZER), GAZER);
			MOBS.put(new ResourceLocation(DD.MODID, Registration.SHADOWLORD), SHADOWLORD);
			MOBS.put(new ResourceLocation(DD.MODID, Registration.DAEMON), DAEMON);
		}
	}

	public static interface IMobConfig {
		public CommonSpawnConfig getSpawnConfig();
	}

	public static interface INetherMobConfig {
		public NetherSpawnConfig getNetherSpawn();		
	}

	public static abstract class MobConfig implements IMobConfig {
		public CommonSpawnConfig spawnConfig;
		public CommonSpawnConfig getSpawnConfig() {
			return spawnConfig;
		}
	}

	public static abstract class NetherMobConfig implements IMobConfig, INetherMobConfig {
		public CommonSpawnConfig spawnConfig;
		public NetherSpawnConfig netherSpawnConfig;
		public CommonSpawnConfig getSpawnConfig() {
			return spawnConfig;
		}
		public NetherSpawnConfig getNetherSpawn() {
			return netherSpawnConfig;
		}
	}


	/*
	 *
	 */
	public static class HeadlessConfig extends MobConfig {
		// headless specific
		public ConfigValue<List<? extends String>> injuredAlertOthersList;
		public ConfigValue<List<? extends String>> targetsAlertOthersList;

		public HeadlessConfig(Builder builder) {
			builder.comment(CATEGORY_DIV, " Headless properties.", CATEGORY_DIV).push(Registration.HEADLESS);				

			spawnConfig = new CommonSpawnConfig(builder, 40, 1, 2, MIN_HEIGHT, MAX_HEIGHT); //,
			//		new ArrayList<>(), new ArrayList<>(), new ArrayList<>(),  Arrays.asList(BiomeCategory.NETHER.getName(), BiomeCategory.THEEND.getName()));

			builder.pop();
		}
	}

	/*
	 *
	 */
	public static class OrcConfig extends MobConfig {
		public OrcConfig(Builder builder) {
			builder.comment(CATEGORY_DIV, " Orc properties.", CATEGORY_DIV).push(Registration.ORC);
			spawnConfig = new CommonSpawnConfig(builder, 35, 1, 2, MIN_HEIGHT, MAX_HEIGHT);//,
//					new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), Arrays.asList(BiomeCategory.NETHER.getName(), BiomeCategory.THEEND.getName()));
			builder.pop();
		}
	}
	
	/*
	 * 
	 */
	public static class GhoulConfig extends MobConfig {
		// ghoul specific
		public DoubleValue healAmount;
		public BooleanValue canOpenDoors;

		public GhoulConfig(Builder builder) {
			builder.comment(CATEGORY_DIV, " Ghoul properties.", CATEGORY_DIV).push(Registration.GHOUL);				

			spawnConfig = new CommonSpawnConfig(builder, 25, 1, 1,  MIN_HEIGHT, MAX_HEIGHT);//,
//					new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), Arrays.asList(BiomeCategory.NETHER.getName(), BiomeCategory.THEEND.getName()));

			healAmount = builder
					.comment(" The amount a ghoul can heal themselves when eating meat.")
					.defineInRange("healAmount", 4D, MIN_HEALTH, MAX_HEALTH);

			canOpenDoors = builder
					.comment(" Determines whether a ghoul open doors.")
					.define("canOpenDoors", true);

			builder.pop();
		}
	}

	/*
	 * 
	 */
	public static class BoulderConfig extends MobConfig {
		// boulder specific		

		public BoulderConfig(Builder builder) {
			builder.comment(CATEGORY_DIV, " Boulder properties.", CATEGORY_DIV).push("boulder");				

			spawnConfig = new CommonSpawnConfig(builder, 35, 1, 1,  MIN_HEIGHT, 60);//,
//					new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), Arrays.asList(BiomeCategory.NETHER.getName(), BiomeCategory.THEEND.getName()));

			builder.pop();
		}
	}

	/*
	 * 
	 */
	public static class ShadowConfig extends NetherMobConfig {
		// shadow specific		

		public ShadowConfig(Builder builder) {
			builder.comment(CATEGORY_DIV, " Shadow properties.", CATEGORY_DIV).push(Registration.SHADOW);				

			spawnConfig = new CommonSpawnConfig(builder, 30, 1, 2,  MIN_HEIGHT, 60);//,
//					new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), Arrays.asList(BiomeCategory.THEEND.getName()));

			netherSpawnConfig = new NetherSpawnConfig(builder, 5, 1, 2, MIN_HEIGHT, MAX_HEIGHT);

			builder.pop();
		}
	}

	/*
	 * 
	 */
	public static class GazerConfig extends NetherMobConfig {
		// gazer specific		
		public IntValue biteCooldownTime;
		public IntValue paralysisChargeTime;
		public IntValue summonCooldownTime;
		public IntValue minSummonSpawns;
		public IntValue maxSummonSpawns;

		public GazerConfig(Builder builder) {
			builder.comment(CATEGORY_DIV, " Gazer properties.", CATEGORY_DIV).push(Registration.GAZER);				

			spawnConfig = new CommonSpawnConfig(builder, 25, 1, 1,  MIN_HEIGHT, UNDERGROUND_HEIGHT);//,
//					new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), Arrays.asList(BiomeCategory.THEEND.getName()));

			netherSpawnConfig = new NetherSpawnConfig(builder, 10, 1, 1, MIN_HEIGHT, MAX_HEIGHT);

			biteCooldownTime = builder
					.comment(" The cooldown time of a bite attack (measured in ticks).")
					.defineInRange("biteCooldownTime", 20, 1, Integer.MAX_VALUE);

			paralysisChargeTime = builder
					.comment(" The charge time of a paraylsis spell attack (measured in ticks).")
					.defineInRange("paralysisChargeTime", 80, 1, Integer.MAX_VALUE);

			summonCooldownTime = builder
					.comment(" The cooldown time of a summon spell (measured in ticks).")
					.defineInRange("summonCooldownTime", 2400, 1, Integer.MAX_VALUE);

			minSummonSpawns = builder
					.comment(" Minimum spawn group size for summon spell.")
					.defineInRange("minSummonSpawns", 1, 0, Integer.MAX_VALUE);

			maxSummonSpawns = builder
					.comment(" Maximum spawn group size for summon spell.")
					.defineInRange("maxSummonSpawns", 3, 1, Integer.MAX_VALUE);

			builder.pop();
		}
	}

	/*
	 * 
	 */
	public static class ShadowlordConfig extends NetherMobConfig {		
		// shadowlord specific		
		public IntValue harmChargeTime;
		public IntValue summonCooldownTime;
		public IntValue minSummonSpawns;
		public IntValue maxSummonSpawns;

		public ShadowlordConfig(Builder builder) {
			builder.comment(CATEGORY_DIV, " Shadowlord properties.", CATEGORY_DIV).push(Registration.SHADOWLORD);				

			spawnConfig = new CommonSpawnConfig(builder, 15, 1, 1,  MIN_HEIGHT, 20);//,
//					new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), Arrays.asList(BiomeCategory.THEEND.getName()));

			netherSpawnConfig = new NetherSpawnConfig(builder, 15, 1, 1,  MIN_HEIGHT, MAX_HEIGHT);

			harmChargeTime = builder
					.comment(" The charge time of a harm spell attack (measured in ticks).")
					.defineInRange("harmChargeTime", 50, 1, Integer.MAX_VALUE);

			summonCooldownTime = builder
					.comment(" The cooldown time of a summon spell (measured in ticks).")
					.defineInRange("summonCooldownTime", 2400, 1, Integer.MAX_VALUE);

			minSummonSpawns = builder
					.comment(" Minimum spawn group size for summon spell.")
					.defineInRange("minSummonSpawns", 1, 0, Integer.MAX_VALUE);

			maxSummonSpawns = builder
					.comment(" Maximum spawn group size for summon spell.")
					.defineInRange("maxSummonSpawns", 2, 1, Integer.MAX_VALUE);

			builder.pop();
		}
	}

	/*
	 * 
	 */
	public static class DaemonConfig extends NetherMobConfig {		
		// daemon specific		
		public IntValue firespoutCooldownTime;
		public IntValue firespoutMaxDistance;

		public DaemonConfig(Builder builder) {
			builder.comment(CATEGORY_DIV, " Daemon properties.", CATEGORY_DIV).push(Registration.DAEMON);				

			spawnConfig = new CommonSpawnConfig(builder, 1, 1, 1,  MIN_HEIGHT, 0);//,
//					new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), Arrays.asList(BiomeCategory.THEEND.getName()));

			netherSpawnConfig = new NetherSpawnConfig(builder, 10, 1, 1,  MIN_HEIGHT, MAX_HEIGHT);

			firespoutCooldownTime = builder
					.comment(" The cooldown time of a firespout spell (measured in ticks).")
					.defineInRange("firespoutCooldownTime", 200, 1, Integer.MAX_VALUE);

			firespoutMaxDistance = builder
					.comment(" The max distance (in blocks) that firespout spell can travel.")
					.defineInRange("firespoutMaxDistance", 10, 3, 20);

			builder.pop();
		}
	}
	
	@Override
	public String getLogsFolder() {
		return Config.LOGGING.folder.get();
	}
	
	public void setLogsFolder(String folder) {
		Config.LOGGING.folder.set(folder);
	}
	
	@Override
	public String getLoggingLevel() {
		return Config.LOGGING.level.get();
	}
}
