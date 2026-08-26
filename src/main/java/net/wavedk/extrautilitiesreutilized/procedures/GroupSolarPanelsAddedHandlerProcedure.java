package net.wavedk.extrautilitiesreutilized.procedures;

import net.wavedk.extrautilitiesreutilized.network.EuruModVariables;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.Entity;
import net.minecraft.core.BlockPos;

import java.io.File;

public class GroupSolarPanelsAddedHandlerProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		double c_y = 0;
		double c_x = 0;
		double c_z = 0;
		double cN = 0;
		String group = "";
		String cS = "";
		File configFile = new File("");
		com.google.gson.JsonObject itemOBJ = new com.google.gson.JsonObject();
		com.google.gson.JsonObject gp_gen_obj = new com.google.gson.JsonObject();
		com.google.gson.JsonObject cOBJ = new com.google.gson.JsonObject();
		com.google.gson.JsonArray vArray = new com.google.gson.JsonArray();
		if ((getBlockNBTString(world, BlockPos.containing(x, y, z), "placedBy")).equals("")) {
			if (!world.isClientSide()) {
				BlockPos _bp = BlockPos.containing(x, y, z);
				BlockEntity _blockEntity = world.getBlockEntity(_bp);
				BlockState _bs = world.getBlockState(_bp);
				if (_blockEntity != null) {
					_blockEntity.getPersistentData().putString("placedBy", (entity.getStringUUID()));
					_blockEntity.getPersistentData().putString("gp_group", "solarpanels");
					_blockEntity.getPersistentData().putDouble("old_calculated",
							(Math.floor((entity.getData(EuruModVariables.PLAYER_VARIABLES).group_update_solarpanels / entity.getData(EuruModVariables.PLAYER_VARIABLES).group_count_solarpanels) * 100) / 100));
				}
				if (world instanceof Level _level)
					_level.sendBlockUpdated(_bp, _bs, _bs, 3);
			}
		}
		GroupPanelsConfigHandlerProcedure.execute(world, x, y, z);
	}

	private static String getBlockNBTString(LevelAccessor world, BlockPos pos, String tag) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity != null)
			return blockEntity.getPersistentData().getString(tag);
		return "";
	}
}