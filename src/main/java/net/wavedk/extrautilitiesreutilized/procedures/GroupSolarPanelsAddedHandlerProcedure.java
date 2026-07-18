package net.wavedk.extrautilitiesreutilized.procedures;

import net.wavedk.extrautilitiesreutilized.network.EuruModVariables;

import net.neoforged.fml.loading.FMLPaths;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.Entity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.BlockPos;

import java.io.IOException;
import java.io.FileReader;
import java.io.File;
import java.io.BufferedReader;

public class GroupSolarPanelsAddedHandlerProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		double c_y = 0;
		double c_x = 0;
		double c_z = 0;
		String group = "";
		File currentFile = new File("");
		File cfile = new File("");
		File configFile = new File("");
		com.google.gson.JsonObject cOBJ = new com.google.gson.JsonObject();
		com.google.gson.JsonObject ccobjk = new com.google.gson.JsonObject();
		com.google.gson.JsonObject itemOBJ = new com.google.gson.JsonObject();
		com.google.gson.JsonObject gp_gen_obj = new com.google.gson.JsonObject();
		setBlockNBTText(world, x, y, z, "placedBy", (entity.getStringUUID()));
		setBlockNBTText(world, x, y, z, "gp_group", "solarpanels");
		if (!getBlockNBTLogic(world, BlockPos.containing(x, y, z), "been_json_checked")) {
			configFile = new File((FMLPaths.GAMEDIR.get().toString() + "/config/euru/"), File.separator + "euru_unified_config.json");
			if (configFile.exists()) {
				{
					try {
						BufferedReader bufferedReader = new BufferedReader(new FileReader(configFile));
						StringBuilder jsonstringbuilder = new StringBuilder();
						String line;
						while ((line = bufferedReader.readLine()) != null) {
							jsonstringbuilder.append(line);
						}
						bufferedReader.close();
						cOBJ = new com.google.gson.Gson().fromJson(jsonstringbuilder.toString(), com.google.gson.JsonObject.class);
						gp_gen_obj = cOBJ.get("gp_generation").getAsJsonObject();
						itemOBJ = gp_gen_obj.get((BuiltInRegistries.BLOCK.getKey((world.getBlockState(BlockPos.containing(x, y, z))).getBlock()).toString())).getAsJsonObject();
						setBlockNBTLogic(world, x, y, z, "needs_day", itemOBJ.get("needs_day").getAsBoolean());
						setBlockNBTLogic(world, x, y, z, "needs_night", itemOBJ.get("needs_night").getAsBoolean());
						setBlockNBTLogic(world, x, y, z, "needs_sky", itemOBJ.get("needs_sky").getAsBoolean());
						setBlockNBTNumber(world, x, y, z, "needs_water", itemOBJ.get("needs_water").getAsDouble());
						setBlockNBTNumber(world, x, y, z, "needs_lava", itemOBJ.get("needs_lava").getAsDouble());
						setBlockNBTNumber(world, x, y, z, "needs_fire", itemOBJ.get("needs_fire").getAsDouble());
						setBlockNBTNumber(world, x, y, z, "gp_generated", itemOBJ.get("gp_generated").getAsDouble());
						setBlockNBTLogic(world, x, y, z, "been_json_checked", true);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		setBlockNBTNumber(world, x, y, z, "old_calculated", (Math.floor((entity.getData(EuruModVariables.PLAYER_VARIABLES).group_update_solarpanels / entity.getData(EuruModVariables.PLAYER_VARIABLES).group_count_solarpanels) * 100) / 100));
	}

	private static void setBlockNBTText(LevelAccessor world, double x, double y, double z, String tag, String value) {
		if (!world.isClientSide()) {
			BlockPos pos = BlockPos.containing(x, y, z);
			BlockEntity blockEntity = world.getBlockEntity(pos);
			BlockState blockState = world.getBlockState(pos);
			if (blockEntity != null) {
				blockEntity.getPersistentData().putString(tag, value);
			}
			if (world instanceof Level level) {
				level.sendBlockUpdated(pos, blockState, blockState, 3);
			}
		}
	}

	private static boolean getBlockNBTLogic(LevelAccessor world, BlockPos pos, String tag) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity != null)
			return blockEntity.getPersistentData().getBoolean(tag);
		return false;
	}

	private static void setBlockNBTLogic(LevelAccessor world, double x, double y, double z, String tag, boolean value) {
		if (!world.isClientSide()) {
			BlockPos pos = BlockPos.containing(x, y, z);
			BlockEntity blockEntity = world.getBlockEntity(pos);
			BlockState blockState = world.getBlockState(pos);
			if (blockEntity != null) {
				blockEntity.getPersistentData().putBoolean(tag, value);
			}
			if (world instanceof Level level) {
				level.sendBlockUpdated(pos, blockState, blockState, 3);
			}
		}
	}

	private static void setBlockNBTNumber(LevelAccessor world, double x, double y, double z, String tag, double value) {
		if (!world.isClientSide()) {
			BlockPos pos = BlockPos.containing(x, y, z);
			BlockEntity blockEntity = world.getBlockEntity(pos);
			BlockState blockState = world.getBlockState(pos);
			if (blockEntity != null) {
				blockEntity.getPersistentData().putDouble(tag, value);
			}
			if (world instanceof Level level) {
				level.sendBlockUpdated(pos, blockState, blockState, 3);
			}
		}
	}
}