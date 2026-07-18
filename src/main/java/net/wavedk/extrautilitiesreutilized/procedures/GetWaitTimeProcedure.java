package net.wavedk.extrautilitiesreutilized.procedures;

import net.neoforged.fml.loading.FMLPaths;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.BlockPos;

import java.io.IOException;
import java.io.FileReader;
import java.io.File;
import java.io.BufferedReader;

public class GetWaitTimeProcedure {
	public static double execute(LevelAccessor world, double x, double y, double z) {
		File file = new File("");
		com.google.gson.JsonObject obj1 = new com.google.gson.JsonObject();
		com.google.gson.JsonObject obj2 = new com.google.gson.JsonObject();
		com.google.gson.JsonObject ob3 = new com.google.gson.JsonObject();
		double returnNum = 0;
		file = new File((FMLPaths.GAMEDIR.get().toString() + "/config/euru/"), File.separator + "euru_recipes.json");
		returnNum = 160;
		{
			try {
				BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
				StringBuilder jsonstringbuilder = new StringBuilder();
				String line;
				while ((line = bufferedReader.readLine()) != null) {
					jsonstringbuilder.append(line);
				}
				bufferedReader.close();
				obj1 = new com.google.gson.Gson().fromJson(jsonstringbuilder.toString(), com.google.gson.JsonObject.class);
				obj2 = obj1.get((BuiltInRegistries.BLOCK.getKey((world.getBlockState(BlockPos.containing(x, y, z))).getBlock()).toString())).getAsJsonObject();
				ob3 = obj2.get((getBlockNBTString(world, BlockPos.containing(x, y, z), "currentItem"))).getAsJsonObject();
				returnNum = ob3.get("wait_time").getAsDouble();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return returnNum;
	}

	private static String getBlockNBTString(LevelAccessor world, BlockPos pos, String tag) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity != null)
			return blockEntity.getPersistentData().getString(tag);
		return "";
	}
}