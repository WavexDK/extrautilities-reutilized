package net.wavedk.extrautilitiesreutilized.procedures;

import org.apache.commons.lang3.function.FailableFunction;

import net.wavedk.extrautilitiesreutilized.network.EuruModVariables;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;

import java.util.regex.Pattern;
import java.util.function.Supplier;
import java.util.UUID;

import java.io.File;

public class PanelsTickUpdateHandlerProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, BlockState blockstate) {
		com.google.gson.JsonObject itemOBJ = new com.google.gson.JsonObject();
		com.google.gson.JsonObject gp_gen_obj = new com.google.gson.JsonObject();
		com.google.gson.JsonObject cOBJ = new com.google.gson.JsonObject();
		File configFile = new File("");
		Entity player = null;
		boolean canGenerate = false;
		double mult = 0;
		String placedBy = "";
		String levelOfWater = "";
		Direction cD = Direction.NORTH;
		placedBy = getBlockNBTString(world, BlockPos.containing(x, y, z), "placedBy");
		if (world.getServer() != null) {
			LevelAccessor _origWorld = world;
			for (ServerLevel worlditerator : world.getServer().getAllLevels()) {
				world = worlditerator;
				player = world instanceof ServerLevel _serverGetEntityUUID ? _serverGetEntityUUID.getEntity(tryOrDefault(placedBy, UUID::fromString, () -> new UUID(0, 0))) : null;
				if (player instanceof Player || player instanceof ServerPlayer) {
					break;
				}
			}
			world = _origWorld;
		}
		if (player instanceof Player || player instanceof ServerPlayer) {
			if (player.getData(EuruModVariables.PLAYER_VARIABLES).playerGPChecking) {
				canGenerate = true;
				if (world.dayTime() >= getBlockNBTNumber(world, BlockPos.containing(x, y, z), "needs_time_min") && world.dayTime() <= getBlockNBTNumber(world, BlockPos.containing(x, y, z), "needs_time_max")
						&& (getBlockNBTLogic(world, BlockPos.containing(x, y, z), "needs_sky") == true && world.canSeeSkyFromBelowWater(BlockPos.containing(x, y + 1, z))
								|| getBlockNBTLogic(world, BlockPos.containing(x, y, z), "needs_sky") == false)) {
					if (getBlockNBTLogic(world, BlockPos.containing(x, y, z), "needs_block")) {
						canGenerate = false;
						String _splitContent25 = Pattern.quote(",");
						String _toSplit25 = (getBlockNBTString(world, BlockPos.containing(x, y, z), "needs_block_sides"));
						String[] _array25 = _toSplit25.split(_splitContent25);
						if (_array25.length != 0) {
							for (String stringiterator : _array25) {
								cD = GetDirectionFromTextProcedure.execute(blockstate, stringiterator);
								if ((world.getBlockState(BlockPos.containing(x + cD.getStepX(), y + cD.getStepY(), z + cD.getStepZ()))).getBlock() == BuiltInRegistries.BLOCK
										.get(ResourceLocation.parse(((getBlockNBTString(world, BlockPos.containing(x, y, z), "needs_block_id"))).toLowerCase(java.util.Locale.ENGLISH)))) {
									canGenerate = true;
									mult = mult + 1;
								}
							}
						} else {
							String stringiterator = _toSplit25;
							for (int _yourmother25 = 0; _yourmother25 < 1; _yourmother25++) {
								cD = GetDirectionFromTextProcedure.execute(blockstate, stringiterator);
								if ((world.getBlockState(BlockPos.containing(x + cD.getStepX(), y + cD.getStepY(), z + cD.getStepZ()))).getBlock() == BuiltInRegistries.BLOCK
										.get(ResourceLocation.parse(((getBlockNBTString(world, BlockPos.containing(x, y, z), "needs_block_id"))).toLowerCase(java.util.Locale.ENGLISH)))) {
									canGenerate = true;
									mult = mult + 1;
								}
							}
						}
					}
				} else {
					canGenerate = false;
				}
			}
			if (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "gEfficiency") == 0 || getBlockNBTNumber(world, BlockPos.containing(x, y, z), "gCutoff") == 0) {
				if (!world.isClientSide()) {
					BlockPos _bp = BlockPos.containing(x, y, z);
					BlockEntity _blockEntity = world.getBlockEntity(_bp);
					BlockState _bs = world.getBlockState(_bp);
					if (_blockEntity != null) {
						_blockEntity.getPersistentData().putDouble("gEfficiency", player.getData(EuruModVariables.PLAYER_VARIABLES).group_efficiency_solarpanels);
						_blockEntity.getPersistentData().putDouble("gCutoff", player.getData(EuruModVariables.PLAYER_VARIABLES).group_cutoff_solarpanels);
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
					_blockEntity.getPersistentData().putDouble("gRaw", player.getData(EuruModVariables.PLAYER_VARIABLES).group_raw_solarpanels);
					_blockEntity.getPersistentData().putDouble("gCount", player.getData(EuruModVariables.PLAYER_VARIABLES).group_count_solarpanels);
				}
				if (world instanceof Level _level)
					_level.sendBlockUpdated(_bp, _bs, _bs, 3);
			}
			if (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "range-configUpdate-counter") >= getBlockNBTNumber(world, BlockPos.containing(x, y, z), "range-configUpdate")) {
				GroupPanelsConfigHandlerProcedure.execute(world, x, y, z);
			} else {
				if (!world.isClientSide()) {
					BlockPos _bp = BlockPos.containing(x, y, z);
					BlockEntity _blockEntity = world.getBlockEntity(_bp);
					BlockState _bs = world.getBlockState(_bp);
					if (_blockEntity != null) {
						_blockEntity.getPersistentData().putDouble("range-configUpdate-counter", (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "range-configUpdate-counter") + 1));
					}
					if (world instanceof Level _level)
						_level.sendBlockUpdated(_bp, _bs, _bs, 3);
				}
			}
		}
		if (canGenerate) {
			if (!world.isClientSide()) {
				BlockPos _bp = BlockPos.containing(x, y, z);
				BlockEntity _blockEntity = world.getBlockEntity(_bp);
				BlockState _bs = world.getBlockState(_bp);
				if (_blockEntity != null) {
					_blockEntity.getPersistentData().putBoolean("generating", true);
				}
				if (world instanceof Level _level)
					_level.sendBlockUpdated(_bp, _bs, _bs, 3);
			}
			if (player.getData(EuruModVariables.PLAYER_VARIABLES).playerGPChecking) {
				{
					EuruModVariables.PlayerVariables _vars = player.getData(EuruModVariables.PLAYER_VARIABLES);
					_vars.group_count_solarpanels = player.getData(EuruModVariables.PLAYER_VARIABLES).group_count_solarpanels + 1;
					_vars.markSyncDirty();
				}
				if (mult > 0) {
					{
						EuruModVariables.PlayerVariables _vars = player.getData(EuruModVariables.PLAYER_VARIABLES);
						_vars.group_raw_solarpanels = player.getData(EuruModVariables.PLAYER_VARIABLES).group_raw_solarpanels + mult * getBlockNBTNumber(world, BlockPos.containing(x, y, z), "gp_generated");
						_vars.markSyncDirty();
					}
				} else {
					{
						EuruModVariables.PlayerVariables _vars = player.getData(EuruModVariables.PLAYER_VARIABLES);
						_vars.group_raw_solarpanels = player.getData(EuruModVariables.PLAYER_VARIABLES).group_raw_solarpanels + getBlockNBTNumber(world, BlockPos.containing(x, y, z), "gp_generated");
						_vars.markSyncDirty();
					}
				}
			}
		} else {
			if (!world.isClientSide()) {
				BlockPos _bp = BlockPos.containing(x, y, z);
				BlockEntity _blockEntity = world.getBlockEntity(_bp);
				BlockState _bs = world.getBlockState(_bp);
				if (_blockEntity != null) {
					_blockEntity.getPersistentData().putBoolean("generating", false);
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

	private static double getBlockNBTNumber(LevelAccessor world, BlockPos pos, String tag) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity != null)
			return blockEntity.getPersistentData().getDouble(tag);
		return -1;
	}

	private static boolean getBlockNBTLogic(LevelAccessor world, BlockPos pos, String tag) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity != null)
			return blockEntity.getPersistentData().getBoolean(tag);
		return false;
	}
}