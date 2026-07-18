package net.wavedk.extrautilitiesreutilized.procedures;

import org.apache.commons.lang3.function.FailableFunction;

import net.wavedk.extrautilitiesreutilized.network.EuruModVariables;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.core.BlockPos;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

import java.util.function.Supplier;
import java.util.UUID;

public class ChunkLoadingWardOnTickUpdateProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		String placedBy = "";
		Entity player = null;
		boolean canPass = false;
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
		canPass = false;
		if (!player.getData(EuruModVariables.PLAYER_VARIABLES).playerGPChecking) {
			if (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "tickCounter") > 29) {
				canPass = true;
				setBlockNBTNumber(world, x, y, z, "tickCounter", 0);
			} else {
				setBlockNBTNumber(world, x, y, z, "tickCounter", (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "tickCounter") + 1));
			}
		}
		if (canPass) {
			if (!getBlockNBTLogic(world, BlockPos.containing(x, y, z), "chunkLoaded")) {
				if (!player.getData(EuruModVariables.PLAYER_VARIABLES).playerGPChecking && player.getData(EuruModVariables.PLAYER_VARIABLES).playerGP_Total >= player.getData(EuruModVariables.PLAYER_VARIABLES).playerGP_Used) {
					if (world instanceof ServerLevel _level)
						_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
								("forceload add " + (("" + x).substring(0, ("" + x).indexOf(".", 0)) + " ") + ("" + z).substring(0, ("" + z).indexOf(".", 0))));
					setBlockNBTLogic(world, x, y, z, "chunkLoaded", true);
				}
			} else {
				if (!player.getData(EuruModVariables.PLAYER_VARIABLES).playerGPChecking) {
					if (player.getData(EuruModVariables.PLAYER_VARIABLES).playerGP_Total < player.getData(EuruModVariables.PLAYER_VARIABLES).playerGP_Used) {
						if (world instanceof ServerLevel _level)
							_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
									("forceload remove " + (("" + x).substring(0, ("" + x).indexOf(".", 0)) + " ") + ("" + z).substring(0, ("" + z).indexOf(".", 0))));
						setBlockNBTLogic(world, x, y, z, "chunkLoaded", false);
					} else if ((executeCommandGetResult(world, new Vec3(x, y, z), ("forceload query " + (("" + x).substring(0, ("" + x).indexOf(".", 0)) + " ") + ("" + z).substring(0, ("" + z).indexOf(".", 0)))))
							.contains("is not marked for force loading")) {
						if (world instanceof ServerLevel _level)
							_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
									("forceload add " + (("" + x).substring(0, ("" + x).indexOf(".", 0)) + " ") + ("" + z).substring(0, ("" + z).indexOf(".", 0))));
					}
				}
			}
		}
		if (player.getData(EuruModVariables.PLAYER_VARIABLES).playerGPChecking) {
			{
				EuruModVariables.PlayerVariables _vars = player.getData(EuruModVariables.PLAYER_VARIABLES);
				_vars.playerGP_Used_Update = player.getData(EuruModVariables.PLAYER_VARIABLES).playerGP_Used_Update + getBlockNBTNumber(world, BlockPos.containing(x, y, z), "gp_needed");
				_vars.markSyncDirty();
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

	private static String executeCommandGetResult(LevelAccessor world, Vec3 pos, String command) {
		StringBuilder result = new StringBuilder();
		if (world instanceof ServerLevel level) {
			CommandSource dataConsumer = new CommandSource() {
				@Override
				public void sendSystemMessage(Component message) {
					result.append(message.getString());
				}

				@Override
				public boolean acceptsSuccess() {
					return true;
				}

				@Override
				public boolean acceptsFailure() {
					return true;
				}

				@Override
				public boolean shouldInformAdmins() {
					return false;
				}
			};
			level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(dataConsumer, pos, Vec2.ZERO, level, 4, "", Component.literal(""), level.getServer(), null), command);
		}
		return result.toString();
	}
}