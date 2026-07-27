package net.wavedk.extrautilitiesreutilized.procedures;

import org.apache.commons.lang3.function.FailableFunction;

import net.wavedk.extrautilitiesreutilized.network.EuruModVariables;

import net.neoforged.fml.loading.FMLPaths;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.BlockPos;

import java.util.function.Supplier;
import java.util.UUID;

import java.io.IOException;
import java.io.FileReader;
import java.io.File;
import java.io.BufferedReader;

public class PanelsTickUpdateHandlerProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		com.google.gson.JsonObject itemOBJ = new com.google.gson.JsonObject();
		com.google.gson.JsonObject gp_gen_obj = new com.google.gson.JsonObject();
		com.google.gson.JsonObject cOBJ = new com.google.gson.JsonObject();
		File configFile = new File("");
		Entity player = null;
		boolean canGenerate = false;
		double mult = 0;
		String placedBy = "";
		String levelOfWater = "";
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
				if ((getBlockNBTLogic(world, BlockPos.containing(x, y, z), "needs_day") == true && world.dayTime() <= 12000 || getBlockNBTLogic(world, BlockPos.containing(x, y, z), "needs_night") == true && world.dayTime() > 12000
						|| getBlockNBTLogic(world, BlockPos.containing(x, y, z), "needs_night") == false && getBlockNBTLogic(world, BlockPos.containing(x, y, z), "needs_day") == false)
						&& (getBlockNBTLogic(world, BlockPos.containing(x, y, z), "needs_sky") == true && world.canSeeSkyFromBelowWater(BlockPos.containing(x, y + 1, z))
								|| getBlockNBTLogic(world, BlockPos.containing(x, y, z), "needs_sky") == false)) {
					canGenerate = true;
				} else {
					canGenerate = false;
				}
				if (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "needs_water") == 1) {
					canGenerate = false;
					mult = 0;
					if ((world.getBlockState(BlockPos.containing(x, y, z + 1))).getBlock() == Blocks.WATER) {
						levelOfWater = (((("" + world.getBlockState(BlockPos.containing(x, y, z + 1))).substring(("" + world.getBlockState(BlockPos.containing(x, y, z + 1))).indexOf("level=", 0))).replace("]", "")).replace("[", "")).replace("level=",
								"");
						if (new Object() {
							double convert(String s) {
								try {
									return Double.parseDouble(s.trim());
								} catch (Exception e) {
								}
								return 0;
							}
						}.convert(levelOfWater) > 0) {
							canGenerate = true;
							mult = mult + 1;
						}
					}
					if ((world.getBlockState(BlockPos.containing(x, y, z - 1))).getBlock() == Blocks.WATER) {
						levelOfWater = (((("" + world.getBlockState(BlockPos.containing(x, y, z - 1))).substring(("" + world.getBlockState(BlockPos.containing(x, y, z - 1))).indexOf("level=", 0))).replace("]", "")).replace("[", "")).replace("level=",
								"");
						if (new Object() {
							double convert(String s) {
								try {
									return Double.parseDouble(s.trim());
								} catch (Exception e) {
								}
								return 0;
							}
						}.convert(levelOfWater) > 0) {
							canGenerate = true;
							mult = mult + 1;
						}
					}
					if ((world.getBlockState(BlockPos.containing(x + 1, y, z))).getBlock() == Blocks.WATER) {
						levelOfWater = (((("" + world.getBlockState(BlockPos.containing(x + 1, y, z))).substring(("" + world.getBlockState(BlockPos.containing(x + 1, y, z))).indexOf("level=", 0))).replace("]", "")).replace("[", "")).replace("level=",
								"");
						if (new Object() {
							double convert(String s) {
								try {
									return Double.parseDouble(s.trim());
								} catch (Exception e) {
								}
								return 0;
							}
						}.convert(levelOfWater) > 0) {
							canGenerate = true;
							mult = mult + 1;
						}
					}
					if ((world.getBlockState(BlockPos.containing(x - 1, y, z))).getBlock() == Blocks.WATER) {
						levelOfWater = (((("" + world.getBlockState(BlockPos.containing(x - 1, y, z))).substring(("" + world.getBlockState(BlockPos.containing(x - 1, y, z))).indexOf("level=", 0))).replace("]", "")).replace("[", "")).replace("level=",
								"");
						if (new Object() {
							double convert(String s) {
								try {
									return Double.parseDouble(s.trim());
								} catch (Exception e) {
								}
								return 0;
							}
						}.convert(levelOfWater) > 0) {
							canGenerate = true;
							mult = mult + 1;
						}
					}
				} else if (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "needs_water") == 2) {
					canGenerate = false;
					mult = 0;
					if ((world.getBlockState(BlockPos.containing(x, y, z + 1))).getBlock() == Blocks.WATER) {
						canGenerate = true;
						mult = mult + 1;
					}
					if ((world.getBlockState(BlockPos.containing(x, y, z - 1))).getBlock() == Blocks.WATER) {
						canGenerate = true;
						mult = mult + 1;
					}
					if ((world.getBlockState(BlockPos.containing(x - 1, y, z))).getBlock() == Blocks.WATER) {
						canGenerate = true;
						mult = mult + 1;
					}
					if ((world.getBlockState(BlockPos.containing(x + 1, y, z))).getBlock() == Blocks.WATER) {
						canGenerate = true;
						mult = mult + 1;
					}
				}
				if (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "needs_lava") == 1) {
					if (canGenerate) {
						canGenerate = false;
						mult = 0;
						if ((world.getBlockState(BlockPos.containing(x, y, z + 1))).getBlock() == Blocks.LAVA) {
							levelOfWater = (((("" + world.getBlockState(BlockPos.containing(x, y, z + 1))).substring(("" + world.getBlockState(BlockPos.containing(x, y, z + 1))).indexOf("level=", 0))).replace("]", "")).replace("[", ""))
									.replace("level=", "");
							if (new Object() {
								double convert(String s) {
									try {
										return Double.parseDouble(s.trim());
									} catch (Exception e) {
									}
									return 0;
								}
							}.convert(levelOfWater) > 0) {
								canGenerate = true;
								mult = mult + 1;
							}
						}
						if ((world.getBlockState(BlockPos.containing(x, y, z - 1))).getBlock() == Blocks.LAVA) {
							canGenerate = true;
							mult = mult + 1;
						}
						if ((world.getBlockState(BlockPos.containing(x - 1, y, z))).getBlock() == Blocks.LAVA) {
							canGenerate = true;
							mult = mult + 1;
						}
						if ((world.getBlockState(BlockPos.containing(x + 1, y, z))).getBlock() == Blocks.LAVA) {
							canGenerate = true;
							mult = mult + 1;
						}
					}
				} else if (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "needs_lava") == 2) {
					if (canGenerate) {
						canGenerate = false;
						mult = 0;
						if ((world.getBlockState(BlockPos.containing(x, y, z + 1))).getBlock() == Blocks.LAVA) {
							canGenerate = true;
							mult = mult + 1;
						}
						if ((world.getBlockState(BlockPos.containing(x, y, z - 1))).getBlock() == Blocks.LAVA) {
							canGenerate = true;
							mult = mult + 1;
						}
						if ((world.getBlockState(BlockPos.containing(x - 1, y, z))).getBlock() == Blocks.LAVA) {
							canGenerate = true;
							mult = mult + 1;
						}
						if ((world.getBlockState(BlockPos.containing(x + 1, y, z))).getBlock() == Blocks.LAVA) {
							canGenerate = true;
							mult = mult + 1;
						}
					}
				}
				if (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "needs_fire") == 1) {
					if (canGenerate) {
						canGenerate = false;
						mult = 0;
						if ((world.getBlockState(BlockPos.containing(x, y + 1, z))).getBlock() == Blocks.FIRE || (world.getBlockState(BlockPos.containing(x, y + 1, z))).getBlock() == Blocks.SOUL_FIRE) {
							canGenerate = true;
							mult = mult + 1;
						}
					}
				} else if (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "needs_fire") == 2) {
					if (canGenerate) {
						canGenerate = false;
						mult = 0;
						if ((world.getBlockState(BlockPos.containing(x, y, z + 1))).getBlock() == Blocks.FIRE || (world.getBlockState(BlockPos.containing(x, y, z + 1))).getBlock() == Blocks.SOUL_FIRE) {
							canGenerate = true;
							mult = mult + 1;
						}
						if ((world.getBlockState(BlockPos.containing(x, y, z - 1))).getBlock() == Blocks.FIRE || (world.getBlockState(BlockPos.containing(x, y, z - 1))).getBlock() == Blocks.SOUL_FIRE) {
							canGenerate = true;
							mult = mult + 1;
						}
						if ((world.getBlockState(BlockPos.containing(x - 1, y, z))).getBlock() == Blocks.FIRE || (world.getBlockState(BlockPos.containing(x - 1, y, z))).getBlock() == Blocks.SOUL_FIRE) {
							canGenerate = true;
							mult = mult + 1;
						}
						if ((world.getBlockState(BlockPos.containing(x + 1, y, z))).getBlock() == Blocks.FIRE || (world.getBlockState(BlockPos.containing(x + 1, y, z))).getBlock() == Blocks.SOUL_FIRE) {
							canGenerate = true;
							mult = mult + 1;
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
									_blockEntity.getPersistentData().putDouble("gp_generated", itemOBJ.get("gp_generated").getAsDouble());
									_blockEntity.getPersistentData().putDouble("range-configUpdate-min", cOBJ.get("range-configUpdate-min").getAsDouble());
									_blockEntity.getPersistentData().putDouble("range-configUpdate-max", cOBJ.get("range-configUpdate-max").getAsDouble());
									_blockEntity.getPersistentData().putDouble("range-configUpdate",
											(Mth.nextInt(RandomSource.create(), (int) cOBJ.get("range-configUpdate-min").getAsDouble(), (int) cOBJ.get("range-configUpdate-max").getAsDouble())));
									_blockEntity.getPersistentData().putDouble("range-configUpdate-counter", 0);
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

	private static double getBlockNBTNumber(LevelAccessor world, BlockPos pos, String tag) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity != null)
			return blockEntity.getPersistentData().getDouble(tag);
		return -1;
	}
}