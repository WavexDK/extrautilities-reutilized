package net.wavedk.extrautilitiesreutilized.procedures;

import org.apache.commons.lang3.function.FailableFunction;

import net.wavedk.extrautilitiesreutilized.network.EuruModVariables;

import net.neoforged.fml.loading.FMLPaths;

import net.minecraft.world.level.block.state.properties.BooleanProperty;
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

public class MillsUpdateHandlerProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
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
							levelOfWater = (((("" + world.getBlockState(BlockPos.containing(x, y, z - 1))).substring(("" + world.getBlockState(BlockPos.containing(x, y, z - 1))).indexOf("level=", 0))).replace("]", "")).replace("[", ""))
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
						} // x-1
						if ((world.getBlockState(BlockPos.containing(x - 1, y, z))).getBlock() == Blocks.LAVA) {
							levelOfWater = (((("" + world.getBlockState(BlockPos.containing(x - 1, y, z))).substring(("" + world.getBlockState(BlockPos.containing(x - 1, y, z))).indexOf("level=", 0))).replace("]", "")).replace("[", ""))
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
						} // x+1
						if ((world.getBlockState(BlockPos.containing(x + 1, y, z))).getBlock() == Blocks.LAVA) {
							levelOfWater = (((("" + world.getBlockState(BlockPos.containing(x + 1, y, z))).substring(("" + world.getBlockState(BlockPos.containing(x + 1, y, z))).indexOf("level=", 0))).replace("]", "")).replace("[", ""))
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
						if ((world.getBlockState(BlockPos.containing(x, y - 1, z))).getBlock() == Blocks.FIRE || (world.getBlockState(BlockPos.containing(x, y - 1, z))).getBlock() == Blocks.SOUL_FIRE) {
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
					if (mult > 0) {
						setBlockNBTNumber(world, x, y, z, "gp_generated", (mult * getBlockNBTNumber(world, BlockPos.containing(x, y, z), "nominal_generated")));
					} else {
						setBlockNBTNumber(world, x, y, z, "gp_generated", (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "nominal_generated")));
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
					setBlockNBTLogic(world, x, y, z, "generating", false);
				}
			}
			if (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "gEfficiency") == 0 || getBlockNBTNumber(world, BlockPos.containing(x, y, z), "gCutoff") == 0) {
				setBlockNBTNumber(world, x, y, z, "gEfficiency", player.getData(EuruModVariables.PLAYER_VARIABLES).group_efficiency_mills);
				setBlockNBTNumber(world, x, y, z, "gCutoff", player.getData(EuruModVariables.PLAYER_VARIABLES).group_cutoff_mills);
			}
			setBlockNBTNumber(world, x, y, z, "gRaw", player.getData(EuruModVariables.PLAYER_VARIABLES).group_raw_mills);
			setBlockNBTNumber(world, x, y, z, "gCount", player.getData(EuruModVariables.PLAYER_VARIABLES).group_count_mills);
		}
		if (0 == getBlockNBTNumber(world, BlockPos.containing(x, y, z), "configUpdate")) {
			setBlockNBTNumber(world, x, y, z, "configUpdate",
					(Mth.nextInt(RandomSource.create(), (int) getBlockNBTNumber(world, BlockPos.containing(x, y, z), "range-configUpdate-min"), (int) getBlockNBTNumber(world, BlockPos.containing(x, y, z), "range-configUpdate-max"))));
		} else if (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "configUpdate") < getBlockNBTNumber(world, BlockPos.containing(x, y, z), "configUpdateCounter")) {
			cfile = new File((FMLPaths.GAMEDIR.get().toString() + "/config/euru/"), File.separator + "euru_unified_config.json");
			if (cfile.exists()) {
				{
					try {
						BufferedReader bufferedReader = new BufferedReader(new FileReader(cfile));
						StringBuilder jsonstringbuilder = new StringBuilder();
						String line;
						while ((line = bufferedReader.readLine()) != null) {
							jsonstringbuilder.append(line);
						}
						bufferedReader.close();
						cobj = new com.google.gson.Gson().fromJson(jsonstringbuilder.toString(), com.google.gson.JsonObject.class);
						gp_gen_obj = cobj.get("gp_generation").getAsJsonObject();
						generalobj = cobj.get("general").getAsJsonObject();
						iitemobj = gp_gen_obj.get((BuiltInRegistries.BLOCK.getKey((world.getBlockState(BlockPos.containing(x, y, z))).getBlock()).toString())).getAsJsonObject();
						setBlockNBTLogic(world, x, y, z, "needs_day", iitemobj.get("needs_day").getAsBoolean());
						setBlockNBTLogic(world, x, y, z, "needs_night", iitemobj.get("needs_night").getAsBoolean());
						setBlockNBTLogic(world, x, y, z, "needs_sky", iitemobj.get("needs_sky").getAsBoolean());
						setBlockNBTNumber(world, x, y, z, "needs_water", iitemobj.get("needs_water").getAsDouble());
						setBlockNBTNumber(world, x, y, z, "needs_lava", iitemobj.get("needs_lava").getAsDouble());
						setBlockNBTNumber(world, x, y, z, "needs_fire", iitemobj.get("needs_fire").getAsDouble());
						setBlockNBTNumber(world, x, y, z, "gp_generated", iitemobj.get("gp_generated").getAsDouble());
						setBlockNBTNumber(world, x, y, z, "range-configUpdate-min", generalobj.get("range-configUpdate-min").getAsDouble());
						setBlockNBTNumber(world, x, y, z, "range-configUpdate-max", generalobj.get("range-configUpdate-max").getAsDouble());
						setBlockNBTLogic(world, x, y, z, "been_json_checked", true);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			setBlockNBTNumber(world, x, y, z, "configUpdateCounter", 1);
		} else {
			setBlockNBTNumber(world, x, y, z, "configUpdateCounter", (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "configUpdateCounter") + 1));
		}
		if (getBlockNBTLogic(world, BlockPos.containing(x, y, z), "generating")) {
			setBooleanBlockState(world, x, y, z, "anim", true);
		} else {
			setBooleanBlockState(world, x, y, z, "anim", false);
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

	private static void setBooleanBlockState(LevelAccessor world, double x, double y, double z, String property, boolean value) {
		BlockPos pos = BlockPos.containing(x, y, z);
		BlockState state = world.getBlockState(pos);
		if (state.getBlock().getStateDefinition().getProperty(property) instanceof BooleanProperty booleanProperty) {
			world.setBlock(pos, state.setValue(booleanProperty, value), 3);
		}
	}
}