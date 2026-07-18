package net.wavedk.extrautilitiesreutilized.procedures;

import org.apache.commons.lang3.function.FailableFunction;

import net.wavedk.extrautilitiesreutilized.network.EuruModVariables;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;

import java.util.function.Supplier;
import java.util.UUID;

import java.io.File;

public class PanelsTickUpdateHandlerProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		com.google.gson.JsonObject itemOBJ = new com.google.gson.JsonObject();
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
					mult = 0;// z+1
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
					} // z-1
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
					} // x+1
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
					} // x-1
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
					mult = 0;// z+1
					if ((world.getBlockState(BlockPos.containing(x, y, z + 1))).getBlock() == Blocks.WATER) {
						canGenerate = true;
						mult = mult + 1;
					} // z-1
					if ((world.getBlockState(BlockPos.containing(x, y, z - 1))).getBlock() == Blocks.WATER) {
						canGenerate = true;
						mult = mult + 1;
					} // x-1
					if ((world.getBlockState(BlockPos.containing(x - 1, y, z))).getBlock() == Blocks.WATER) {
						canGenerate = true;
						mult = mult + 1;
					} // x+1
					if ((world.getBlockState(BlockPos.containing(x + 1, y, z))).getBlock() == Blocks.WATER) {
						canGenerate = true;
						mult = mult + 1;
					}
				}
				if (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "needs_lava") == 1) {
					if (canGenerate) {
						canGenerate = false;
						mult = 0;// z+1
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
						} // z-1
						if ((world.getBlockState(BlockPos.containing(x, y, z - 1))).getBlock() == Blocks.LAVA) {
							canGenerate = true;
							mult = mult + 1;
						} // x-1
						if ((world.getBlockState(BlockPos.containing(x - 1, y, z))).getBlock() == Blocks.LAVA) {
							canGenerate = true;
							mult = mult + 1;
						} // x+1
						if ((world.getBlockState(BlockPos.containing(x + 1, y, z))).getBlock() == Blocks.LAVA) {
							canGenerate = true;
							mult = mult + 1;
						}
					}
				} else if (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "needs_lava") == 2) {
					if (canGenerate) {
						canGenerate = false;
						mult = 0;// z+1
						if ((world.getBlockState(BlockPos.containing(x, y, z + 1))).getBlock() == Blocks.LAVA) {
							canGenerate = true;
							mult = mult + 1;
						} // z-1
						if ((world.getBlockState(BlockPos.containing(x, y, z - 1))).getBlock() == Blocks.LAVA) {
							canGenerate = true;
							mult = mult + 1;
						} // x-1
						if ((world.getBlockState(BlockPos.containing(x - 1, y, z))).getBlock() == Blocks.LAVA) {
							canGenerate = true;
							mult = mult + 1;
						} // x+1
						if ((world.getBlockState(BlockPos.containing(x + 1, y, z))).getBlock() == Blocks.LAVA) {
							canGenerate = true;
							mult = mult + 1;
						}
					}
				}
				if (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "needs_fire") == 1) {
					if (canGenerate) {
						canGenerate = false;
						mult = 0;// firecheck
						if ((world.getBlockState(BlockPos.containing(x, y + 1, z))).getBlock() == Blocks.FIRE || (world.getBlockState(BlockPos.containing(x, y + 1, z))).getBlock() == Blocks.SOUL_FIRE) {
							canGenerate = true;
							mult = mult + 1;
						}
					}
				} else if (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "needs_fire") == 2) {
					if (canGenerate) {
						canGenerate = false;
						mult = 0;// z+1
						if ((world.getBlockState(BlockPos.containing(x, y, z + 1))).getBlock() == Blocks.FIRE || (world.getBlockState(BlockPos.containing(x, y, z + 1))).getBlock() == Blocks.SOUL_FIRE) {
							canGenerate = true;
							mult = mult + 1;
						} // z-1
						if ((world.getBlockState(BlockPos.containing(x, y, z - 1))).getBlock() == Blocks.FIRE || (world.getBlockState(BlockPos.containing(x, y, z - 1))).getBlock() == Blocks.SOUL_FIRE) {
							canGenerate = true;
							mult = mult + 1;
						} // x-1
						if ((world.getBlockState(BlockPos.containing(x - 1, y, z))).getBlock() == Blocks.FIRE || (world.getBlockState(BlockPos.containing(x - 1, y, z))).getBlock() == Blocks.SOUL_FIRE) {
							canGenerate = true;
							mult = mult + 1;
						} // x+1
						if ((world.getBlockState(BlockPos.containing(x + 1, y, z))).getBlock() == Blocks.FIRE || (world.getBlockState(BlockPos.containing(x + 1, y, z))).getBlock() == Blocks.SOUL_FIRE) {
							canGenerate = true;
							mult = mult + 1;
						}
					}
				}
				if (canGenerate) {
					setBlockNBTLogic(world, x, y, z, "generating", true);
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
					setBlockNBTLogic(world, x, y, z, "generating", false);
				}
			}
			if (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "gEfficiency") == 0 || getBlockNBTNumber(world, BlockPos.containing(x, y, z), "gCutoff") == 0) {
				setBlockNBTNumber(world, x, y, z, "gEfficiency", player.getData(EuruModVariables.PLAYER_VARIABLES).group_efficiency_solarpanels);
				setBlockNBTNumber(world, x, y, z, "gCutoff", player.getData(EuruModVariables.PLAYER_VARIABLES).group_cutoff_solarpanels);
			}
			setBlockNBTNumber(world, x, y, z, "gRaw", player.getData(EuruModVariables.PLAYER_VARIABLES).group_raw_solarpanels);
			setBlockNBTNumber(world, x, y, z, "gCount", player.getData(EuruModVariables.PLAYER_VARIABLES).group_count_solarpanels);
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
}