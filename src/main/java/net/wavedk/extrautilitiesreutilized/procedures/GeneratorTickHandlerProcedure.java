package net.wavedk.extrautilitiesreutilized.procedures;

import org.apache.commons.lang3.function.FailableFunction;

import net.wavedk.extrautilitiesreutilized.network.EuruModVariables;
import net.wavedk.extrautilitiesreutilized.init.EuruModItems;
import net.wavedk.extrautilitiesreutilized.block.entity.IEnergyReceiver;

import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.common.extensions.ILevelExtension;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.fml.loading.FMLPaths;

import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.tags.ItemTags;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;

import java.util.function.Supplier;
import java.util.UUID;

import java.io.IOException;
import java.io.FileReader;
import java.io.File;
import java.io.BufferedReader;

public class GeneratorTickHandlerProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, BlockState blockstate) {
		Entity player = null;
		File feConfig = new File("");
		com.google.gson.JsonArray fuelList = new com.google.gson.JsonArray();
		String currentItem = "";
		com.google.gson.JsonObject configOBJ = new com.google.gson.JsonObject();
		com.google.gson.JsonObject fuelPropertiesOBJ = new com.google.gson.JsonObject();
		com.google.gson.JsonObject itemOBJ = new com.google.gson.JsonObject();
		com.google.gson.JsonObject blockOBJ = new com.google.gson.JsonObject();
		double feGen = 0;
		double feSpeed = 0;
		double sentSouth = 0;
		double sentNorth = 0;
		double sentWest = 0;
		double sentEast = 0;
		double mult = 0;
		double cNum = 0;
		double cSendNum = 0;
		double sentUp = 0;
		double oX = 0;
		double oY = 0;
		double oZ = 0;
		boolean canGenerate = false;
		boolean canSlowBurn = false;
		boolean canPass = false;
		boolean loopdiloop = false;
		boolean canSendEnergy = false;
		if (world.getServer() != null) {
			LevelAccessor _origWorld = world;
			for (ServerLevel worlditerator : world.getServer().getAllLevels()) {
				world = worlditerator;
				player = world instanceof ServerLevel _serverGetEntityUUID ? _serverGetEntityUUID.getEntity(tryOrDefault((getBlockNBTString(world, BlockPos.containing(x, y, z), "placedBy")), UUID::fromString, () -> new UUID(0, 0))) : null;
				if (player instanceof ServerPlayer || player instanceof Player) {
					break;
				}
			}
			world = _origWorld;
		}
		if (player instanceof ServerPlayer || player instanceof Player) {// redstoneMode
			if (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "redstoneMode") == 0) {
				setBlockNBTLogic(world, x, y, z, "redstoneModeOn", true);
			} else if (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "redstoneMode") == 1) {
				if (world instanceof Level _level11 && _level11.hasNeighborSignal(BlockPos.containing(x, y, z))) {
					setBlockNBTLogic(world, x, y, z, "redstoneModeOn", true);
				} else {
					setBlockNBTLogic(world, x, y, z, "redstoneModeOn", false);
				}
			} else if (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "redstoneMode") == 2) {
				if (world instanceof Level _level15 && _level15.hasNeighborSignal(BlockPos.containing(x, y, z))) {
					setBlockNBTLogic(world, x, y, z, "redstoneModeOn", false);
				} else {
					setBlockNBTLogic(world, x, y, z, "redstoneModeOn", true);
				}
			} else if (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "redstoneMode") == 3) {
				setBlockNBTLogic(world, x, y, z, "redstoneModeOn", false);
			} else {
				setBlockNBTLogic(world, x, y, z, "redstoneModeOn", true);
			} // multiplier
			mult = 1;
			if ((itemFromBlockInventory(world, BlockPos.containing(x, y, z), 1).copy()).getItem() == EuruModItems.SPEED_UPGRADE.get()) {
				setBlockNBTNumber(world, x, y, z, "updateMult", (mult + itemFromBlockInventory(world, BlockPos.containing(x, y, z), 1).getCount() / 4d));
			} else if ((itemFromBlockInventory(world, BlockPos.containing(x, y, z), 1).copy()).getItem() == EuruModItems.MAGICAL_SPEED_UPGRADE.get()) {
				setBlockNBTNumber(world, x, y, z, "updateMult", (mult + itemFromBlockInventory(world, BlockPos.containing(x, y, z), 1).getCount() / 2d));
			} else if ((itemFromBlockInventory(world, BlockPos.containing(x, y, z), 1).copy()).getItem() == EuruModItems.ULTIMATE_SPEED_UPGRADE.get()) {
				setBlockNBTNumber(world, x, y, z, "updateMult", (mult + itemFromBlockInventory(world, BlockPos.containing(x, y, z), 1).getCount() / 1.25));
			} else {
				setBlockNBTNumber(world, x, y, z, "updateMult", 1);
			}
			if (player.getData(EuruModVariables.PLAYER_VARIABLES).updateMultipliers) {
				mult = getBlockNBTNumber(world, BlockPos.containing(x, y, z), "updateMult");
				setBlockNBTNumber(world, x, y, z, "oldMult", (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "updateMult")));
			} else {
				mult = getBlockNBTNumber(world, BlockPos.containing(x, y, z), "oldMult");
			} // fe gen
			if (getBlockNBTLogic(world, BlockPos.containing(x, y, z), "redstoneModeOn")) {
				feConfig = new File((FMLPaths.GAMEDIR.get().toString() + "/config/euru/"), File.separator + "euru_fe_config.json");
				if ((getBlockNBTString(world, BlockPos.containing(x, y, z), "currentItem")).equals("")) {
					canGenerate = false;
					canSlowBurn = false;
				}
				if ((getBlockNBTString(world, BlockPos.containing(x, y, z), "currentItem")).equals("") && itemFromBlockInventory(world, BlockPos.containing(x, y, z), 0).getCount() != 0) {
					if (!(getBlockNBTString(world, BlockPos.containing(x, y, z), "lastCheckedItem")).equals(BuiltInRegistries.ITEM.getKey((itemFromBlockInventory(world, BlockPos.containing(x, y, z), 0).copy()).getItem()).toString())) {
						setBlockNBTText(world, x, y, z, "lastCheckedItem", (BuiltInRegistries.ITEM.getKey((itemFromBlockInventory(world, BlockPos.containing(x, y, z), 0).copy()).getItem()).toString()));
						{
							try {
								BufferedReader bufferedReader = new BufferedReader(new FileReader(feConfig));
								StringBuilder jsonstringbuilder = new StringBuilder();
								String line;
								while ((line = bufferedReader.readLine()) != null) {
									jsonstringbuilder.append(line);
								}
								bufferedReader.close();
								configOBJ = new com.google.gson.Gson().fromJson(jsonstringbuilder.toString(), com.google.gson.JsonObject.class);
								blockOBJ = configOBJ.get((BuiltInRegistries.ITEM.getKey((new ItemStack((world.getBlockState(BlockPos.containing(x, y, z))).getBlock())).getItem()).toString())).getAsJsonObject();
								fuelList = blockOBJ.get("listFuel").getAsJsonArray();
								cNum = 0;
								if (!fuelList.isEmpty()) {
									for (int index3397 = 0; index3397 < (int) fuelList.size(); index3397++) {
										if ((fuelList.get((int) cNum).getAsString()).equals(BuiltInRegistries.ITEM.getKey((itemFromBlockInventory(world, BlockPos.containing(x, y, z), 0).copy()).getItem()).toString())
												|| (itemFromBlockInventory(world, BlockPos.containing(x, y, z), 0).copy()).is(ItemTags.create(ResourceLocation.parse((fuelList.get((int) cNum).getAsString()).toLowerCase(java.util.Locale.ENGLISH))))) {
											if (player.getData(EuruModVariables.PLAYER_VARIABLES).playerGP_Total >= player.getData(EuruModVariables.PLAYER_VARIABLES).playerGP_Used
													&& !player.getData(EuruModVariables.PLAYER_VARIABLES).playerGPChecking) {
												canPass = true;
											} else if (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "oldGPTotal") >= getBlockNBTNumber(world, BlockPos.containing(x, y, z), "oldGPUsed")
													&& player.getData(EuruModVariables.PLAYER_VARIABLES).playerGPChecking) {
												canPass = true;
											}
											setBlockNBTText(world, x, y, z, "lastCheckedItem", "");
											if (canPass) {
												fuelPropertiesOBJ = blockOBJ.get("fuelProperties").getAsJsonObject();
												itemOBJ = fuelPropertiesOBJ.get(fuelList.get((int) cNum).getAsString()).getAsJsonObject();
												setBlockNBTText(world, x, y, z, "currentItem", (BuiltInRegistries.ITEM.getKey((itemFromBlockInventory(world, BlockPos.containing(x, y, z), 0).copy()).getItem()).toString()));
												setBlockNBTNumber(world, x, y, z, "wait_time", itemOBJ.get("feGenerated").getAsDouble());
												setBlockNBTNumber(world, x, y, z, "feSpeed", itemOBJ.get("feSpeed").getAsDouble());
												setBlockNBTNumber(world, x, y, z, "cProgress", 0);
												canGenerate = true;
												if (world instanceof ILevelExtension _ext && _ext.getCapability(Capabilities.ItemHandler.BLOCK, BlockPos.containing(x, y, z), null) instanceof IItemHandlerModifiable _itemHandlerModifiable) {
													int _slotid = 0;
													ItemStack _stk = _itemHandlerModifiable.getStackInSlot(_slotid).copy();
													_stk.shrink(1);
													_itemHandlerModifiable.setStackInSlot(_slotid, _stk);
												}
												setBooleanBlockState(world, x, y, z, "on", true);
											}
											break;
										}
										cNum = cNum + 1;
									}
								}
								if ((getBlockNBTString(world, BlockPos.containing(x, y, z), "currentItem")).equals("")) {
									setBooleanBlockState(world, x, y, z, "on", false);
								}
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					} else {
						setBooleanBlockState(world, x, y, z, "on", false);
					}
				} else {
					canGenerate = true;
				}
				if (!(getBlockNBTNumber(world, BlockPos.containing(x, y, z), "wait_time") != 0 && getBlockNBTNumber(world, BlockPos.containing(x, y, z), "feSpeed") != 0)) {
					canGenerate = false;
				}
				if (player.getData(EuruModVariables.PLAYER_VARIABLES).playerGP_Total < player.getData(EuruModVariables.PLAYER_VARIABLES).playerGP_Used && !player.getData(EuruModVariables.PLAYER_VARIABLES).playerGPChecking) {
					setBlockNBTLogic(world, x, y, z, "tmGP", true);
					setBlockNBTNumber(world, x, y, z, "oldGPTotal", player.getData(EuruModVariables.PLAYER_VARIABLES).playerGP_Total);
					setBlockNBTNumber(world, x, y, z, "oldGPUsed", player.getData(EuruModVariables.PLAYER_VARIABLES).playerGP_Used);
					canGenerate = false;
				} else if (!player.getData(EuruModVariables.PLAYER_VARIABLES).playerGPChecking) {
					setBlockNBTNumber(world, x, y, z, "oldGPUsed", player.getData(EuruModVariables.PLAYER_VARIABLES).playerGP_Used);
					setBlockNBTNumber(world, x, y, z, "oldGPTotal", player.getData(EuruModVariables.PLAYER_VARIABLES).playerGP_Total);
				} else if (player.getData(EuruModVariables.PLAYER_VARIABLES).playerGP_Total >= player.getData(EuruModVariables.PLAYER_VARIABLES).playerGP_Used && !player.getData(EuruModVariables.PLAYER_VARIABLES).playerGPChecking) {
					setBlockNBTLogic(world, x, y, z, "tmGP", false);
				}
				if (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "oldGPTotal") < getBlockNBTNumber(world, BlockPos.containing(x, y, z), "oldGPUsed") && player.getData(EuruModVariables.PLAYER_VARIABLES).playerGPChecking) {
					setBlockNBTLogic(world, x, y, z, "tmGP", true);
					canGenerate = false;
				} else if (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "oldGPTotal") >= getBlockNBTNumber(world, BlockPos.containing(x, y, z), "oldGPUsed") && player.getData(EuruModVariables.PLAYER_VARIABLES).playerGPChecking) {
					setBlockNBTLogic(world, x, y, z, "tmGP", false);
				}
				if (canGenerate) {
					feSpeed = getBlockNBTNumber(world, BlockPos.containing(x, y, z), "feSpeed") * mult;
					if (feSpeed + getEnergyStored(world, BlockPos.containing(x, y, z), null) <= getMaxEnergyStored(world, BlockPos.containing(x, y, z), null)) {
						canGenerate = true;
						canSlowBurn = false;
					} else if (feSpeed + getEnergyStored(world, BlockPos.containing(x, y, z), null) > getMaxEnergyStored(world, BlockPos.containing(x, y, z), null)
							&& getEnergyStored(world, BlockPos.containing(x, y, z), null) < getMaxEnergyStored(world, BlockPos.containing(x, y, z), null)) {
						feSpeed = getMaxEnergyStored(world, BlockPos.containing(x, y, z), null) - getEnergyStored(world, BlockPos.containing(x, y, z), null);
						canGenerate = true;
						canSlowBurn = false;
					} else {
						canGenerate = false;
						canSlowBurn = true;
					}
				}
				if (canGenerate) {
					if (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "wait_time") > getBlockNBTNumber(world, BlockPos.containing(x, y, z), "cProgress")) {
						if (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "cProgress") + feSpeed > getBlockNBTNumber(world, BlockPos.containing(x, y, z), "wait_time")) {
							feSpeed = getBlockNBTNumber(world, BlockPos.containing(x, y, z), "wait_time") - getBlockNBTNumber(world, BlockPos.containing(x, y, z), "cProgress");
							setBlockNBTNumber(world, x, y, z, "cProgress", (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "wait_time")));
						} else {
							setBlockNBTNumber(world, x, y, z, "cProgress", (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "cProgress") + feSpeed));
						}
						SpecialGenFeatureProcedure.execute(world, x, y, z, BuiltInRegistries.ITEM.getKey((new ItemStack((world.getBlockState(BlockPos.containing(x, y, z))).getBlock())).getItem()).toString());
						if (world.getBlockEntity(new BlockPos((int) x, (int) y, (int) z)) instanceof IEnergyReceiver be) {
							be.addEnergy((int) feSpeed);
						}
					}
				} else if (canSlowBurn) {
					if (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "wait_time") > getBlockNBTNumber(world, BlockPos.containing(x, y, z), "cProgress")) {
						setBlockNBTNumber(world, x, y, z, "cProgress", (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "cProgress") + feSpeed / 32));
					}
				}
				if (canGenerate || canSlowBurn) {
					if (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "wait_time") <= getBlockNBTNumber(world, BlockPos.containing(x, y, z), "cProgress")) {
						setBlockNBTNumber(world, x, y, z, "cProgress", 0);
						setBlockNBTNumber(world, x, y, z, "wait_time", 0);
						setBlockNBTNumber(world, x, y, z, "feSpeed", 0);
						setBlockNBTText(world, x, y, z, "currentItem", "");
					}
				}
			}
			if (player.getData(EuruModVariables.PLAYER_VARIABLES).playerGPChecking) {
				{
					EuruModVariables.PlayerVariables _vars = player.getData(EuruModVariables.PLAYER_VARIABLES);
					_vars.playerGP_Used_Update = player.getData(EuruModVariables.PLAYER_VARIABLES).playerGP_Used_Update + itemFromBlockInventory(world, BlockPos.containing(x, y, z), 1).getCount();
					_vars.markSyncDirty();
				}
			}
			if (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "wait_time") == 0) {
				if (getBooleanFromBlockState(blockstate, "on")) {
					if (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "offCounter") > 2) {
						setBlockNBTNumber(world, x, y, z, "offCounter", 0);
						setBooleanBlockState(world, x, y, z, "on", false);
					} else {
						setBlockNBTNumber(world, x, y, z, "offCounter", (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "offCounter") + 1));
					}
				}
			} else {
				if (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "offCounter") > 0) {
					setBlockNBTNumber(world, x, y, z, "offCounter", 0);
				}
			} // energyHandling
			//
			for (Direction directioniterator : Direction.values()) {
				oX = x + directioniterator.getStepX();
				oY = y + directioniterator.getStepY();
				oZ = z + directioniterator.getStepZ();
				sentSouth = receiveEnergySimulate(world, BlockPos.containing(oX, oY, oZ), (int) (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "sendEnergyCapability") * mult), (directioniterator.getOpposite()));
				if (getEnergyStored(world, BlockPos.containing(x, y, z), directioniterator) > 0) {
					canSendEnergy = false;
					if (sentSouth > 0) {
						if (getEnergyStored(world, BlockPos.containing(x, y, z), directioniterator) >= sentSouth) {
							if (world instanceof ILevelExtension _ext) {
								IEnergyStorage _entityStorage = _ext.getCapability(Capabilities.EnergyStorage.BLOCK, BlockPos.containing(x, y, z), directioniterator);
								if (_entityStorage != null)
									_entityStorage.extractEnergy((int) sentSouth, false);
							}
							if (world instanceof ILevelExtension _ext) {
								IEnergyStorage _entityStorage = _ext.getCapability(Capabilities.EnergyStorage.BLOCK, BlockPos.containing(oX, oY, oZ), (directioniterator.getOpposite()));
								if (_entityStorage != null)
									_entityStorage.receiveEnergy((int) sentSouth, false);
							}
						} else {
							canSendEnergy = true;
						}
					} else {
						canSendEnergy = true;
					}
					if (canSendEnergy) {// group
						loopdiloop = true;
						cSendNum = sentSouth;
						while (loopdiloop) {
							if (cSendNum > 1) {
								if (getEnergyStored(world, BlockPos.containing(x, y, z), directioniterator) >= cSendNum && receiveEnergySimulate(world, BlockPos.containing(oX, oY, oZ), (int) cSendNum, (directioniterator.getOpposite())) == cSendNum) {
									if (world instanceof ILevelExtension _ext) {
										IEnergyStorage _entityStorage = _ext.getCapability(Capabilities.EnergyStorage.BLOCK, BlockPos.containing(x, y, z), directioniterator);
										if (_entityStorage != null)
											_entityStorage.extractEnergy((int) cSendNum, false);
									}
									if (world instanceof ILevelExtension _ext) {
										IEnergyStorage _entityStorage = _ext.getCapability(Capabilities.EnergyStorage.BLOCK, BlockPos.containing(oX, oY, oZ), (directioniterator.getOpposite()));
										if (_entityStorage != null)
											_entityStorage.receiveEnergy((int) cSendNum, false);
									}
									loopdiloop = false;
								}
							} else {
								loopdiloop = false;
							}
							cSendNum = Math.round(cSendNum / 2);
						}
					}
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

	private static ItemStack itemFromBlockInventory(LevelAccessor world, BlockPos pos, int slot) {
		if (world instanceof ILevelExtension ext) {
			IItemHandler itemHandler = ext.getCapability(Capabilities.ItemHandler.BLOCK, pos, null);
			if (itemHandler != null)
				return itemHandler.getStackInSlot(slot);
		}
		return ItemStack.EMPTY;
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

	private static void setBlockNBTText(LevelAccessor world, double x, double y, double z, String tag, String value) {
		if (!world.isClientSide()) {
			BlockPos pos = BlockPos.containing(x, y, z);
			BlockEntity blockEntity = world.getBlockEntity(pos);
			BlockState blockState = world.getBlockState(pos);
			if (blockEntity != null) {
				blockEntity.getPersistentData().putString(tag, value);
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

	public static int getEnergyStored(LevelAccessor level, BlockPos pos, Direction direction) {
		if (level instanceof ILevelExtension levelExtension) {
			IEnergyStorage energyStorage = levelExtension.getCapability(Capabilities.EnergyStorage.BLOCK, pos, direction);
			if (energyStorage != null)
				return energyStorage.getEnergyStored();
		}
		return 0;
	}

	public static int getMaxEnergyStored(LevelAccessor level, BlockPos pos, Direction direction) {
		if (level instanceof ILevelExtension levelExtension) {
			IEnergyStorage energyStorage = levelExtension.getCapability(Capabilities.EnergyStorage.BLOCK, pos, direction);
			if (energyStorage != null)
				return energyStorage.getMaxEnergyStored();
		}
		return 0;
	}

	private static boolean getBooleanFromBlockState(BlockState blockState, String property) {
		Property<?> prop = blockState.getBlock().getStateDefinition().getProperty(property);
		return prop instanceof BooleanProperty bp && blockState.getValue(bp);
	}

	private static int receiveEnergySimulate(LevelAccessor level, BlockPos pos, int amount, Direction direction) {
		if (level instanceof ILevelExtension levelExtension) {
			IEnergyStorage energyStorage = levelExtension.getCapability(Capabilities.EnergyStorage.BLOCK, pos, direction);
			if (energyStorage != null)
				return energyStorage.receiveEnergy(amount, true);
		}
		return 0;
	}
}