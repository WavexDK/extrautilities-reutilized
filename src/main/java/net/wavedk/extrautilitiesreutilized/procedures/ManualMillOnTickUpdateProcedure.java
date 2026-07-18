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
import net.minecraft.core.BlockPos;

import java.util.function.Supplier;
import java.util.UUID;

public class ManualMillOnTickUpdateProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		String placedBy = "";
		Entity player = null;
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
			if (getBlockNBTLogic(world, BlockPos.containing(x, y, z), "clicked")) {
				setBlockNBTLogic(world, x, y, z, "generating", true);
				setBlockNBTNumber(world, x, y, z, "generatingLogicCounter", 0);
				if (player.getData(EuruModVariables.PLAYER_VARIABLES).playerGPChecking) {
					setBlockNBTLogic(world, x, y, z, "clicked", false);
					{
						EuruModVariables.PlayerVariables _vars = player.getData(EuruModVariables.PLAYER_VARIABLES);
						_vars.group_count_mills = player.getData(EuruModVariables.PLAYER_VARIABLES).group_count_mills + 1;
						_vars.group_raw_mills = player.getData(EuruModVariables.PLAYER_VARIABLES).group_raw_mills + getBlockNBTNumber(world, BlockPos.containing(x, y, z), "gp_generated");
						_vars.markSyncDirty();
					}
				}
			} else {
				if (getBlockNBTLogic(world, BlockPos.containing(x, y, z), "generating")) {
					if (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "generatingLogicCounter") > 1 && !player.getData(EuruModVariables.PLAYER_VARIABLES).playerGPChecking) {
						setBlockNBTLogic(world, x, y, z, "generating", false);
						setBlockNBTNumber(world, x, y, z, "generatingLogicCounter", 0);
					} else {
						setBlockNBTNumber(world, x, y, z, "generatingLogicCounter", (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "generatingLogicCounter") + 1));
					}
				} else {
					setBlockNBTNumber(world, x, y, z, "generatingLogicCounter", 0);
				}
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

	private static double getBlockNBTNumber(LevelAccessor world, BlockPos pos, String tag) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity != null)
			return blockEntity.getPersistentData().getDouble(tag);
		return -1;
	}
}