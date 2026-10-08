package net.wavedk.extrautilitiesreutilized.procedures;

import org.apache.commons.lang3.function.FailableFunction;

import net.wavedk.extrautilitiesreutilized.network.EuruModVariables;
import net.wavedk.extrautilitiesreutilized.init.EuruModBlocks;

import net.neoforged.neoforge.server.ServerLifecycleHooks;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.BlockPos;

import java.util.function.Supplier;
import java.util.UUID;

public class ChunkLoadingWardOnTickUpdateProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		Entity player = null;
		double n = 0;
		com.google.gson.JsonObject uobj = new com.google.gson.JsonObject();
		com.google.gson.JsonObject general = new com.google.gson.JsonObject();
		if (ServerLifecycleHooks.getCurrentServer() != null) {
			for (ServerLevel worlditerator : ServerLifecycleHooks.getCurrentServer().getAllLevels()) {
				world = worlditerator;
				player = world instanceof ServerLevel _serverGetEntityUUID ? _serverGetEntityUUID.getEntity(tryOrDefault((getBlockNBTString(world, BlockPos.containing(x, y, z), "placedBy")), UUID::fromString, () -> new UUID(0, 0))) : null;
				if (player instanceof ServerPlayer || player instanceof Player) {
					break;
				}
			}
		}
		if (player instanceof ServerPlayer || player instanceof Player) {
			if (!EuruModVariables.WorldVariables.get(world).chunkloadingward.contains(world.getChunk(BlockPos.containing(x, y, z)))) {
				if (player.getData(EuruModVariables.PLAYER_VARIABLES).playerGP_Total >= player.getData(EuruModVariables.PLAYER_VARIABLES).playerGP_Used && player.getData(EuruModVariables.PLAYER_VARIABLES).playerGPChecking == false) {
					if (world instanceof ServerLevel _serverLevel) {
						_serverLevel.setChunkForced(world.getChunk(BlockPos.containing(x, y, z)).getPos().x, world.getChunk(BlockPos.containing(x, y, z)).getPos().z, true);
					}
					EuruModVariables.WorldVariables.get(world).chunkloadingward.add(world.getChunk(BlockPos.containing(x, y, z)));
					EuruModVariables.WorldVariables.get(world).markSyncDirty();
				}
			} else if (player.getData(EuruModVariables.PLAYER_VARIABLES).playerGP_Total < player.getData(EuruModVariables.PLAYER_VARIABLES).playerGP_Used && player.getData(EuruModVariables.PLAYER_VARIABLES).playerGPChecking == false) {
				EuruModVariables.WorldVariables.get(world).chunkloadingward.remove((int) EuruModVariables.WorldVariables.get(world).chunkloadingward.indexOf(world.getChunk(BlockPos.containing(x, y, z))));
			}
			if (player.getData(EuruModVariables.PLAYER_VARIABLES).playerGPChecking == true) {
				general = EuruModVariables.unified_config.get("general").getAsJsonObject();
				uobj = general.get((BuiltInRegistries.ITEM.getKey(EuruModBlocks.CHUNK_LOADING_WARD.get().asItem()).toString())).getAsJsonObject();
				{
					EuruModVariables.PlayerVariables _vars = player.getData(EuruModVariables.PLAYER_VARIABLES);
					_vars.playerGP_Used_Update = player.getData(EuruModVariables.PLAYER_VARIABLES).playerGP_Used_Update + uobj.get("gp_needed").getAsDouble();
					_vars.markSyncDirty();
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
}