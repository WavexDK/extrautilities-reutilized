package net.wavedk.extrautilitiesreutilized.procedures;

import net.wavedk.extrautilitiesreutilized.network.EuruModVariables;
import net.wavedk.extrautilitiesreutilized.init.EuruModItems;
import net.wavedk.extrautilitiesreutilized.EuruMod;

import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.registries.BuiltInRegistries;

import javax.annotation.Nullable;

import java.io.IOException;
import java.io.FileReader;
import java.io.File;
import java.io.BufferedReader;

@EventBusSubscriber
public class DOEDeathFromProcedureProcedure {
	@SubscribeEvent
	public static void onEntityDeath(LivingDeathEvent event) {
		if (event.getEntity() != null) {
			execute(event, event.getEntity().level(), event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ(), event.getEntity(), event.getSource().getEntity());
		}
	}

	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity, Entity sourceentity) {
		execute(null, world, x, y, z, entity, sourceentity);
	}

	private static void execute(@Nullable Event event, LevelAccessor world, double x, double y, double z, Entity entity, Entity sourceentity) {
		if (entity == null || sourceentity == null)
			return;
		File cfil = new File("");
		com.google.gson.JsonObject opbj = new com.google.gson.JsonObject();
		com.google.gson.JsonObject catobj = new com.google.gson.JsonObject();
		com.google.gson.JsonObject iobj = new com.google.gson.JsonObject();
		com.google.gson.JsonArray doelist = new com.google.gson.JsonArray();
		boolean multiply = false;
		double num = 0;
		double drop = 0;
		cfil = new File((FMLPaths.GAMEDIR.get().toString() + "/config/euru/"), File.separator + "euru_unified_config.json");
		if (EuruModVariables.WorldVariables.get(world).doe_drops_from.isEmpty() && !EuruModVariables.WorldVariables.get(world).doeList_empty) {
			{
				try {
					BufferedReader bufferedReader = new BufferedReader(new FileReader(cfil));
					StringBuilder jsonstringbuilder = new StringBuilder();
					String line;
					while ((line = bufferedReader.readLine()) != null) {
						jsonstringbuilder.append(line);
					}
					bufferedReader.close();
					opbj = new com.google.gson.Gson().fromJson(jsonstringbuilder.toString(), com.google.gson.JsonObject.class);
					catobj = opbj.get("general").getAsJsonObject();
					iobj = catobj.get((BuiltInRegistries.ITEM.getKey(EuruModItems.DROP_OF_EVIL.get()).toString())).getAsJsonObject();
					doelist = iobj.get("drops_from").getAsJsonArray();
					drop = iobj.get("dropchance").getAsDouble();
					multiply = iobj.get("dropchance_multiplywithlooting").getAsBoolean();
					num = 0;
					for (int index1819 = 0; index1819 < (int) doelist.size(); index1819++) {
						EuruModVariables.WorldVariables.get(world).doe_drops_from.add(doelist.get((int) num).getAsString());
						EuruModVariables.WorldVariables.get(world).markSyncDirty();
						num = num + 1;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else {
			EuruMod.LOGGER.warn("Drop of Evil, \"drops_from\" list is empty, and will therefore no longer be checked. Restart your game to re-check it.");
		}
		if (!EuruModVariables.WorldVariables.get(world).doe_drops_from.isEmpty()) {
			if (EuruModVariables.WorldVariables.get(world).doe_drops_from.contains((BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).toString()))) {
				if (drop == 0) {
					{
						try {
							BufferedReader bufferedReader = new BufferedReader(new FileReader(cfil));
							StringBuilder jsonstringbuilder = new StringBuilder();
							String line;
							while ((line = bufferedReader.readLine()) != null) {
								jsonstringbuilder.append(line);
							}
							bufferedReader.close();
							opbj = new com.google.gson.Gson().fromJson(jsonstringbuilder.toString(), com.google.gson.JsonObject.class);
							catobj = opbj.get("general").getAsJsonObject();
							iobj = catobj.get((BuiltInRegistries.ITEM.getKey(EuruModItems.DROP_OF_EVIL.get()).toString())).getAsJsonObject();
							doelist = iobj.get("drops_from").getAsJsonArray();
							drop = iobj.get("dropchance").getAsDouble();
							multiply = iobj.get("dropchance_multiplywithlooting").getAsBoolean();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
				if (multiply) {
					if ((sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getEnchantmentLevel(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.LOOTING)) != 0) {
						if (3 * ((sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getEnchantmentLevel(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.LOOTING))
								+ 1) >= Mth.nextInt(RandomSource.create(), 1, 100)) {
							if (world instanceof ServerLevel _level) {
								ItemEntity entityToSpawn = new ItemEntity(_level, x, y, z, new ItemStack(EuruModItems.DROP_OF_EVIL.get()));
								entityToSpawn.setPickUpDelay(10);
								_level.addFreshEntity(entityToSpawn);
							}
						}
					} else if (3 >= Mth.nextInt(RandomSource.create(), 1, 100)) {
						if (world instanceof ServerLevel _level) {
							ItemEntity entityToSpawn = new ItemEntity(_level, x, y, z, new ItemStack(EuruModItems.DROP_OF_EVIL.get()));
							entityToSpawn.setPickUpDelay(10);
							_level.addFreshEntity(entityToSpawn);
						}
					}
				} else {
					if (3 >= Mth.nextInt(RandomSource.create(), 1, 100)) {
						if (world instanceof ServerLevel _level) {
							ItemEntity entityToSpawn = new ItemEntity(_level, x, y, z, new ItemStack(EuruModItems.DROP_OF_EVIL.get()));
							entityToSpawn.setPickUpDelay(10);
							_level.addFreshEntity(entityToSpawn);
						}
					}
				}
			}
		}
		if (EuruModVariables.WorldVariables.get(world).doeUpdateCounter >= 80) {
			if (!EuruModVariables.WorldVariables.get(world).doeList_empty) {
				EuruModVariables.WorldVariables.get(world).doe_drops_from.clear();
			}
			EuruModVariables.WorldVariables.get(world).doeUpdateCounter = 0;
			EuruModVariables.WorldVariables.get(world).markSyncDirty();
		} else {
			EuruModVariables.WorldVariables.get(world).doeUpdateCounter = EuruModVariables.WorldVariables.get(world).doeUpdateCounter + 1;
			EuruModVariables.WorldVariables.get(world).markSyncDirty();
		}
	}
}