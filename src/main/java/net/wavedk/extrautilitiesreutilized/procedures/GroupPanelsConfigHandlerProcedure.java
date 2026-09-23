package net.wavedk.extrautilitiesreutilized.procedures;

import org.apache.commons.lang3.function.FailableFunction;

import net.wavedk.extrautilitiesreutilized.network.EuruModVariables;

import net.neoforged.neoforge.server.ServerLifecycleHooks;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.BlockPos;

import java.util.function.Supplier;
import java.util.UUID;

import java.io.File;

public class GroupPanelsConfigHandlerProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		double c_y = 0;
		double c_x = 0;
		double c_z = 0;
		double cN = 0;
		String group = "";
		String cS = "";
		String placedBy = "";
		File configFile = new File("");
		com.google.gson.JsonObject itemOBJ = new com.google.gson.JsonObject();
		com.google.gson.JsonObject gp_gen_obj = new com.google.gson.JsonObject();
		com.google.gson.JsonObject cOBJ = new com.google.gson.JsonObject();
		com.google.gson.JsonObject allblocks = new com.google.gson.JsonObject();
		com.google.gson.JsonObject effman = new com.google.gson.JsonObject();
		com.google.gson.JsonObject allblocksprop = new com.google.gson.JsonObject();
		com.google.gson.JsonArray vArray = new com.google.gson.JsonArray();
		Entity player = null;
		placedBy = getBlockNBTString(world, BlockPos.containing(x, y, z), "placedBy");
		if (ServerLifecycleHooks.getCurrentServer() != null) {
			for (ServerLevel worlditerator : ServerLifecycleHooks.getCurrentServer().getAllLevels()) {
				world = worlditerator;
				player = world instanceof ServerLevel _serverGetEntityUUID ? _serverGetEntityUUID.getEntity(tryOrDefault(placedBy, UUID::fromString, () -> new UUID(0, 0))) : null;
				if (player instanceof Player || player instanceof ServerPlayer) {
					break;
				}
			}
		}
		if (player instanceof Player || player instanceof ServerPlayer) {
			gp_gen_obj = EuruModVariables.unified_config.get("gp_generation").getAsJsonObject();
			effman = EuruModVariables.unified_config.get("gp_efficiency_manager").getAsJsonObject();
			itemOBJ = gp_gen_obj.get((BuiltInRegistries.BLOCK.getKey((world.getBlockState(BlockPos.containing(x, y, z))).getBlock()).toString())).getAsJsonObject();
			allblocks = effman.get("group_allblocks").getAsJsonObject();
			if (!world.isClientSide()) {
				BlockPos _bp = BlockPos.containing(x, y, z);
				BlockEntity _blockEntity = world.getBlockEntity(_bp);
				BlockState _bs = world.getBlockState(_bp);
				if (_blockEntity != null) {
					_blockEntity.getPersistentData().putBoolean("needs_sky", itemOBJ.get("needs_sky").getAsBoolean());
					_blockEntity.getPersistentData().putBoolean("needs_block", itemOBJ.get("needs_block").getAsBoolean());
				}
				if (world instanceof Level _level)
					_level.sendBlockUpdated(_bp, _bs, _bs, 3);
			}
			if (itemOBJ.get("needs_block").getAsBoolean()) {
				vArray = itemOBJ.get("needs_block_sides").getAsJsonArray();
				cN = 0;
				cS = "";
				for (int _i1 = 0; _i1 < (int) vArray.size(); _i1++) {
					if ((cS).isEmpty()) {
						cS = vArray.get((int) cN).getAsString();
					} else {
						cS = cS + "" + ("," + vArray.get((int) cN).getAsString());
					}
					cN = cN + 1;
				}
				if (!world.isClientSide()) {
					BlockPos _bp = BlockPos.containing(x, y, z);
					BlockEntity _blockEntity = world.getBlockEntity(_bp);
					BlockState _bs = world.getBlockState(_bp);
					if (_blockEntity != null) {
						_blockEntity.getPersistentData().putString("needs_block_sides", cS);
						_blockEntity.getPersistentData().putString("needs_block_id", itemOBJ.get("needs_block_id").getAsString());
					}
					if (world instanceof Level _level)
						_level.sendBlockUpdated(_bp, _bs, _bs, 3);
				}
			}
			if (!world.isClientSide()) {
				BlockPos _bp = BlockPos.containing(x, y, z);
				BlockEntity _blockEntity = world.getBlockEntity(_bp);
				BlockState _bs = world.getBlockState(_bp);
				if (_blockEntity != null) {
					_blockEntity.getPersistentData().putDouble("needs_time_min", itemOBJ.get("needs_time_min").getAsDouble());
					_blockEntity.getPersistentData().putDouble("needs_time_max", itemOBJ.get("needs_time_max").getAsDouble());
					_blockEntity.getPersistentData().putDouble("gp_generated", itemOBJ.get("gp_generated").getAsDouble());
					_blockEntity.getPersistentData().putDouble("nominal_generated", itemOBJ.get("gp_generated").getAsDouble());
				}
				if (world instanceof Level _level)
					_level.sendBlockUpdated(_bp, _bs, _bs, 3);
			}
		}
	}

	private static String getBlockNBTString(LevelAccessor world, BlockPos pos, String tag) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity != null)
			return blockEntity.getPersistentData().getString(tag);
		return "";
	}

	private static <A, B> A tryOrDefault(B funcArg, FailableFunction<B, A, Exception> func, Supplier<A> fallback) {
		try {
			return func.apply(funcArg);
		} catch (Exception e) {
			return fallback.get();
		}
	}
}