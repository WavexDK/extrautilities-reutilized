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

public class GroupMillsAddedHandlerProcedure {
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
		com.google.gson.JsonObject generalobj = new com.google.gson.JsonObject();
		if (!world.isClientSide()) {
			BlockPos _bp = BlockPos.containing(x, y, z);
			BlockEntity _blockEntity = world.getBlockEntity(_bp);
			BlockState _bs = world.getBlockState(_bp);
			if (_blockEntity != null) {
				_blockEntity.getPersistentData().putString("placedBy", (entity.getStringUUID()));
				_blockEntity.getPersistentData().putString("gp_group", "mills");
			}
			if (world instanceof Level _level)
				_level.sendBlockUpdated(_bp, _bs, _bs, 3);
		}
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
						generalobj = cOBJ.get("general").getAsJsonObject();
						itemOBJ = gp_gen_obj.get((BuiltInRegistries.BLOCK.getKey((world.getBlockState(BlockPos.containing(x, y, z))).getBlock()).toString())).getAsJsonObject();
						if (!world.isClientSide()) {
							BlockPos _bp = BlockPos.containing(x, y, z);
							BlockEntity _blockEntity = world.getBlockEntity(_bp);
							BlockState _bs = world.getBlockState(_bp);
							if (_blockEntity != null) {
								_blockEntity.getPersistentData().putBoolean("needs_day", itemOBJ.get("needs_day").getAsBoolean());
								_blockEntity.getPersistentData().putBoolean("needs_night", itemOBJ.get("needs_night").getAsBoolean());
								_blockEntity.getPersistentData().putBoolean("needs_sky", itemOBJ.get("needs_sky").getAsBoolean());
								_blockEntity.getPersistentData().putDouble("needs_water", itemOBJ.get("needs_water").getAsDouble());
								_blockEntity.getPersistentData().putDouble("needs_lava", itemOBJ.get("needs_lava").getAsDouble());
								_blockEntity.getPersistentData().putDouble("needs_fire", itemOBJ.get("needs_fire").getAsDouble());
								_blockEntity.getPersistentData().putDouble("nominal_generated", itemOBJ.get("gp_generated").getAsDouble());
								_blockEntity.getPersistentData().putDouble("gp_generated", itemOBJ.get("gp_generated").getAsDouble());
								_blockEntity.getPersistentData().putDouble("range-configUpdate-max", generalobj.get("range-configUpdate-max").getAsDouble());
								_blockEntity.getPersistentData().putDouble("range-configUpdate-min", generalobj.get("range-configUpdate-min").getAsDouble());
								_blockEntity.getPersistentData().putBoolean("been_json_checked", true);
							}
							if (world instanceof Level _level)
								_level.sendBlockUpdated(_bp, _bs, _bs, 3);
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		if (!world.isClientSide()) {
			BlockPos _bp = BlockPos.containing(x, y, z);
			BlockEntity _blockEntity = world.getBlockEntity(_bp);
			BlockState _bs = world.getBlockState(_bp);
			if (_blockEntity != null) {
				_blockEntity.getPersistentData().putDouble("old_calculated", (Math.floor((entity.getData(EuruModVariables.PLAYER_VARIABLES).group_update_mills / entity.getData(EuruModVariables.PLAYER_VARIABLES).group_count_mills) * 100) / 100));
			}
			if (world instanceof Level _level)
				_level.sendBlockUpdated(_bp, _bs, _bs, 3);
		}
	}

	private static boolean getBlockNBTLogic(LevelAccessor world, BlockPos pos, String tag) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity != null)
			return blockEntity.getPersistentData().getBoolean(tag);
		return false;
	}
}