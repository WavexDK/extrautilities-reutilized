package net.wavedk.extrautilitiesreutilized.procedures;

import org.apache.commons.lang3.function.FailableFunction;

import net.wavedk.extrautilitiesreutilized.network.EuruModVariables;

import net.neoforged.neoforge.server.ServerLifecycleHooks;

import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;
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

public class MillsUpdateHandlerProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, BlockState blockstate) {
		Entity player = null;
		boolean canGenerate = false;
		double mult = 0;
		String placedBy = "";
		String levelOfWater = "";
		com.google.gson.JsonObject itemOBJ = new com.google.gson.JsonObject();
		com.google.gson.JsonObject generalobj = new com.google.gson.JsonObject();
		com.google.gson.JsonObject gp_gen_obj = new com.google.gson.JsonObject();
		com.google.gson.JsonObject cobj = new com.google.gson.JsonObject();
		com.google.gson.JsonObject iitemobj = new com.google.gson.JsonObject();
		File configFile = new File("");
		File cfile = new File("");
		Direction cD = Direction.NORTH;
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
					if (mult > 0) {
						if (!world.isClientSide()) {
							BlockPos _bp = BlockPos.containing(x, y, z);
							BlockEntity _blockEntity = world.getBlockEntity(_bp);
							BlockState _bs = world.getBlockState(_bp);
							if (_blockEntity != null) {
								_blockEntity.getPersistentData().putDouble("gp_generated", (mult * getBlockNBTNumber(world, BlockPos.containing(x, y, z), "nominal_generated")));
							}
							if (world instanceof Level _level)
								_level.sendBlockUpdated(_bp, _bs, _bs, 3);
						}
					} else {
						if (!world.isClientSide()) {
							BlockPos _bp = BlockPos.containing(x, y, z);
							BlockEntity _blockEntity = world.getBlockEntity(_bp);
							BlockState _bs = world.getBlockState(_bp);
							if (_blockEntity != null) {
								_blockEntity.getPersistentData().putDouble("gp_generated", (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "nominal_generated")));
							}
							if (world instanceof Level _level)
								_level.sendBlockUpdated(_bp, _bs, _bs, 3);
						}
					}
					if (player.getData(EuruModVariables.PLAYER_VARIABLES).playerGPChecking) {
						{
							EuruModVariables.PlayerVariables _vars = player.getData(EuruModVariables.PLAYER_VARIABLES);
							_vars.group_count_mills = player.getData(EuruModVariables.PLAYER_VARIABLES).group_count_mills + 1;
							_vars.group_raw_mills = player.getData(EuruModVariables.PLAYER_VARIABLES).group_raw_mills + getBlockNBTNumber(world, BlockPos.containing(x, y, z), "gp_generated");
							_vars.markSyncDirty();
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
			if (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "gEfficiency") == 0 || getBlockNBTNumber(world, BlockPos.containing(x, y, z), "gCutoff") == 0) {
				if (!world.isClientSide()) {
					BlockPos _bp = BlockPos.containing(x, y, z);
					BlockEntity _blockEntity = world.getBlockEntity(_bp);
					BlockState _bs = world.getBlockState(_bp);
					if (_blockEntity != null) {
						_blockEntity.getPersistentData().putDouble("gEfficiency", player.getData(EuruModVariables.PLAYER_VARIABLES).group_efficiency_mills);
						_blockEntity.getPersistentData().putDouble("gCutoff", player.getData(EuruModVariables.PLAYER_VARIABLES).group_cutoff_mills);
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
					_blockEntity.getPersistentData().putDouble("gRaw", player.getData(EuruModVariables.PLAYER_VARIABLES).group_raw_mills);
					_blockEntity.getPersistentData().putDouble("gCount", player.getData(EuruModVariables.PLAYER_VARIABLES).group_count_mills);
				}
				if (world instanceof Level _level)
					_level.sendBlockUpdated(_bp, _bs, _bs, 3);
			}
		}
		if (0 == getBlockNBTNumber(world, BlockPos.containing(x, y, z), "configUpdate")) {
			if (!world.isClientSide()) {
				BlockPos _bp = BlockPos.containing(x, y, z);
				BlockEntity _blockEntity = world.getBlockEntity(_bp);
				BlockState _bs = world.getBlockState(_bp);
				if (_blockEntity != null) {
					_blockEntity.getPersistentData().putDouble("configUpdate",
							(Mth.nextInt(RandomSource.create(), (int) getBlockNBTNumber(world, BlockPos.containing(x, y, z), "range-configUpdate-min"), (int) getBlockNBTNumber(world, BlockPos.containing(x, y, z), "range-configUpdate-max"))));
				}
				if (world instanceof Level _level)
					_level.sendBlockUpdated(_bp, _bs, _bs, 3);
			}
		} else if (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "configUpdate") < getBlockNBTNumber(world, BlockPos.containing(x, y, z), "configUpdateCounter")) {
			if (!world.isClientSide()) {
				BlockPos _bp = BlockPos.containing(x, y, z);
				BlockEntity _blockEntity = world.getBlockEntity(_bp);
				BlockState _bs = world.getBlockState(_bp);
				if (_blockEntity != null) {
					_blockEntity.getPersistentData().putDouble("configUpdateCounter", 1);
				}
				if (world instanceof Level _level)
					_level.sendBlockUpdated(_bp, _bs, _bs, 3);
			}
			GroupPanelsConfigHandlerProcedure.execute(world, x, y, z);
		} else {
			if (!world.isClientSide()) {
				BlockPos _bp = BlockPos.containing(x, y, z);
				BlockEntity _blockEntity = world.getBlockEntity(_bp);
				BlockState _bs = world.getBlockState(_bp);
				if (_blockEntity != null) {
					_blockEntity.getPersistentData().putDouble("configUpdateCounter", (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "configUpdateCounter") + 1));
				}
				if (world instanceof Level _level)
					_level.sendBlockUpdated(_bp, _bs, _bs, 3);
			}
		}
		if (getBlockNBTLogic(world, BlockPos.containing(x, y, z), "generating")) {
			{
				BlockPos _pos = BlockPos.containing(x, y, z);
				BlockState _bs = world.getBlockState(_pos);
				if (_bs.getBlock().getStateDefinition().getProperty("anim") instanceof BooleanProperty _booleanProp)
					world.setBlock(_pos, _bs.setValue(_booleanProp, true), 3);
			}
			if (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "animWait") == 1) {
				if (!world.isClientSide()) {
					BlockPos _bp = BlockPos.containing(x, y, z);
					BlockEntity _blockEntity = world.getBlockEntity(_bp);
					BlockState _bs = world.getBlockState(_bp);
					if (_blockEntity != null) {
						_blockEntity.getPersistentData().putDouble("animWait", 0);
					}
					if (world instanceof Level _level)
						_level.sendBlockUpdated(_bp, _bs, _bs, 3);
				}
				if ((getPropertyByName((world.getBlockState(BlockPos.containing(x, y, z))), "animation") instanceof IntegerProperty _getip54 ? (world.getBlockState(BlockPos.containing(x, y, z))).getValue(_getip54) : -1) == 3) {
					{
						int _value = 0;
						BlockPos _pos = BlockPos.containing(x, y, z);
						BlockState _bs = world.getBlockState(_pos);
						if (_bs.getBlock().getStateDefinition().getProperty("animation") instanceof IntegerProperty _integerProp && _integerProp.getPossibleValues().contains(_value))
							world.setBlock(_pos, _bs.setValue(_integerProp, _value), 3);
					}
				} else {
					{
						int _value = (getPropertyByName((world.getBlockState(BlockPos.containing(x, y, z))), "animation") instanceof IntegerProperty _getip57 ? (world.getBlockState(BlockPos.containing(x, y, z))).getValue(_getip57) : -1) + 1;
						BlockPos _pos = BlockPos.containing(x, y, z);
						BlockState _bs = world.getBlockState(_pos);
						if (_bs.getBlock().getStateDefinition().getProperty("animation") instanceof IntegerProperty _integerProp && _integerProp.getPossibleValues().contains(_value))
							world.setBlock(_pos, _bs.setValue(_integerProp, _value), 3);
					}
				}
			} else {
				if (!world.isClientSide()) {
					BlockPos _bp = BlockPos.containing(x, y, z);
					BlockEntity _blockEntity = world.getBlockEntity(_bp);
					BlockState _bs = world.getBlockState(_bp);
					if (_blockEntity != null) {
						_blockEntity.getPersistentData().putDouble("animWait", 1);
					}
					if (world instanceof Level _level)
						_level.sendBlockUpdated(_bp, _bs, _bs, 3);
				}
			}
		} else {
			{
				BlockPos _pos = BlockPos.containing(x, y, z);
				BlockState _bs = world.getBlockState(_pos);
				if (_bs.getBlock().getStateDefinition().getProperty("anim") instanceof BooleanProperty _booleanProp)
					world.setBlock(_pos, _bs.setValue(_booleanProp, false), 3);
			}
			{
				int _value = 0;
				BlockPos _pos = BlockPos.containing(x, y, z);
				BlockState _bs = world.getBlockState(_pos);
				if (_bs.getBlock().getStateDefinition().getProperty("animation") instanceof IntegerProperty _integerProp && _integerProp.getPossibleValues().contains(_value))
					world.setBlock(_pos, _bs.setValue(_integerProp, _value), 3);
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

	private static Property<?> getPropertyByName(BlockState state, String name) {
		for (Property<?> property : state.getProperties()) {
			if (property.getName().equals(name)) {
				return property;
			}
		}
		return null;
	}
}