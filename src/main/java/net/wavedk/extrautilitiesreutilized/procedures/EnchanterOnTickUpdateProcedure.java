package net.wavedk.extrautilitiesreutilized.procedures;

import org.apache.commons.lang3.function.FailableFunction;

import net.wavedk.extrautilitiesreutilized.network.EuruModVariables;
import net.wavedk.extrautilitiesreutilized.init.EuruModItems;
import net.wavedk.extrautilitiesreutilized.block.entity.EnchanterBlockEntity;

import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.common.extensions.ILevelExtension;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.fml.loading.FMLPaths;

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

public class EnchanterOnTickUpdateProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		File recipeList = new File("");
		File configFile = new File("");
		Entity player = null;
		com.google.gson.JsonArray rlArray = new com.google.gson.JsonArray();
		com.google.gson.JsonObject configOBJ = new com.google.gson.JsonObject();
		com.google.gson.JsonObject catOBJ = new com.google.gson.JsonObject();
		com.google.gson.JsonObject blockOBJ = new com.google.gson.JsonObject();
		com.google.gson.JsonObject recipeOBJ = new com.google.gson.JsonObject();
		String rl_cItem = "";
		double mult = 0;
		double cost = 0;
		double rlNum = 0;
		double feStep = 0;
		double energyRemove = 0;
		boolean canPass = false;
		boolean on = false;
		if (world.getServer() != null) {
			LevelAccessor _origWorld = world;
			for (ServerLevel worlditerator : world.getServer().getAllLevels()) {
				world = worlditerator;
				if (!(player instanceof Player || player instanceof ServerPlayer)) {
					player = world instanceof ServerLevel _serverGetEntityUUID ? _serverGetEntityUUID.getEntity(tryOrDefault((getBlockNBTString(world, BlockPos.containing(x, y, z), "placedBy")), UUID::fromString, () -> new UUID(0, 0))) : null;
					if (player instanceof Player || player instanceof ServerPlayer) {
						break;
					}
				}
			}
			world = _origWorld;
		}
		if (player instanceof Player || player instanceof ServerPlayer) {
			if (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "redstoneMode") == 0) {
				if (!world.isClientSide()) {
					BlockPos _bp = BlockPos.containing(x, y, z);
					BlockEntity _blockEntity = world.getBlockEntity(_bp);
					BlockState _bs = world.getBlockState(_bp);
					if (_blockEntity != null) {
						_blockEntity.getPersistentData().putBoolean("redstoneModeOn", true);
					}
					if (world instanceof Level _level)
						_level.sendBlockUpdated(_bp, _bs, _bs, 3);
				}
			} else if (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "redstoneMode") == 1) {
				if (world instanceof Level _level13 && _level13.hasNeighborSignal(BlockPos.containing(x, y, z))) {
					if (!world.isClientSide()) {
						BlockPos _bp = BlockPos.containing(x, y, z);
						BlockEntity _blockEntity = world.getBlockEntity(_bp);
						BlockState _bs = world.getBlockState(_bp);
						if (_blockEntity != null) {
							_blockEntity.getPersistentData().putBoolean("redstoneModeOn", true);
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
							_blockEntity.getPersistentData().putBoolean("redstoneModeOn", false);
						}
						if (world instanceof Level _level)
							_level.sendBlockUpdated(_bp, _bs, _bs, 3);
					}
				}
			} else if (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "redstoneMode") == 2) {
				if (world instanceof Level _level17 && _level17.hasNeighborSignal(BlockPos.containing(x, y, z))) {
					if (!world.isClientSide()) {
						BlockPos _bp = BlockPos.containing(x, y, z);
						BlockEntity _blockEntity = world.getBlockEntity(_bp);
						BlockState _bs = world.getBlockState(_bp);
						if (_blockEntity != null) {
							_blockEntity.getPersistentData().putBoolean("redstoneModeOn", false);
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
							_blockEntity.getPersistentData().putBoolean("redstoneModeOn", true);
						}
						if (world instanceof Level _level)
							_level.sendBlockUpdated(_bp, _bs, _bs, 3);
					}
				}
			} else if (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "redstoneMode") == 3) {
				if (!world.isClientSide()) {
					BlockPos _bp = BlockPos.containing(x, y, z);
					BlockEntity _blockEntity = world.getBlockEntity(_bp);
					BlockState _bs = world.getBlockState(_bp);
					if (_blockEntity != null) {
						_blockEntity.getPersistentData().putBoolean("redstoneModeOn", false);
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
						_blockEntity.getPersistentData().putBoolean("redstoneModeOn", true);
					}
					if (world instanceof Level _level)
						_level.sendBlockUpdated(_bp, _bs, _bs, 3);
				}
			}
			if (getBlockNBTLogic(world, BlockPos.containing(x, y, z), "redstoneModeOn")) {
				mult = 1;
				if ((itemFromBlockInventory(world, BlockPos.containing(x, y, z), 3).copy()).getItem() == EuruModItems.SPEED_UPGRADE.get()) {
					mult = 1 + itemFromBlockInventory(world, BlockPos.containing(x, y, z), 3).getCount() / 4d;
					cost = itemFromBlockInventory(world, BlockPos.containing(x, y, z), 3).getCount();
				} else if ((itemFromBlockInventory(world, BlockPos.containing(x, y, z), 3).copy()).getItem() == EuruModItems.MAGICAL_SPEED_UPGRADE.get()) {
					mult = 1 + itemFromBlockInventory(world, BlockPos.containing(x, y, z), 3).getCount() / 2d;
					cost = itemFromBlockInventory(world, BlockPos.containing(x, y, z), 3).getCount();
				} else if ((itemFromBlockInventory(world, BlockPos.containing(x, y, z), 3).copy()).getItem() == EuruModItems.ULTIMATE_SPEED_UPGRADE.get()) {
					mult = 1 + itemFromBlockInventory(world, BlockPos.containing(x, y, z), 3).getCount() / 1.25;
					cost = itemFromBlockInventory(world, BlockPos.containing(x, y, z), 3).getCount();
				}
				on = false;
				if (itemFromBlockInventory(world, BlockPos.containing(x, y, z), 0).getCount() > 0) {
					if ((getBlockNBTString(world, BlockPos.containing(x, y, z), "output")).equals("") || (getBlockNBTString(world, BlockPos.containing(x, y, z), "cItem")).equals("")
							|| getBlockNBTNumber(world, BlockPos.containing(x, y, z), "wait_time") == 0 || getBlockNBTNumber(world, BlockPos.containing(x, y, z), "lapis_required") == 0
							|| getBlockNBTNumber(world, BlockPos.containing(x, y, z), "fe_required") == 0 || getBlockNBTNumber(world, BlockPos.containing(x, y, z), "gp_required") == 0) {
						configFile = new File((FMLPaths.GAMEDIR.get().toString() + "/config/euru/"), File.separator + "euru_unified_config.json");
						{
							try {
								BufferedReader bufferedReader = new BufferedReader(new FileReader(configFile));
								StringBuilder jsonstringbuilder = new StringBuilder();
								String line;
								while ((line = bufferedReader.readLine()) != null) {
									jsonstringbuilder.append(line);
								}
								bufferedReader.close();
								configOBJ = new com.google.gson.Gson().fromJson(jsonstringbuilder.toString(), com.google.gson.JsonObject.class);
								catOBJ = configOBJ.get("recipes").getAsJsonObject();
								blockOBJ = catOBJ.get((BuiltInRegistries.BLOCK.getKey((world.getBlockState(BlockPos.containing(x, y, z))).getBlock()).toString())).getAsJsonObject();
								rlArray = blockOBJ.get("recipeList").getAsJsonArray();
								rlNum = 0;
								for (int index1814 = 0; index1814 < (int) rlArray.size(); index1814++) {
									rl_cItem = rlArray.get((int) rlNum).getAsString();
									if ((BuiltInRegistries.ITEM.getKey((itemFromBlockInventory(world, BlockPos.containing(x, y, z), 0).copy()).getItem()).toString()).equals(rl_cItem)
											|| (itemFromBlockInventory(world, BlockPos.containing(x, y, z), 0).copy()).is(ItemTags.create(ResourceLocation.parse((rl_cItem).toLowerCase(java.util.Locale.ENGLISH))))) {
										recipeOBJ = blockOBJ.get(rl_cItem).getAsJsonObject();
										if (!world.isClientSide()) {
											BlockPos _bp = BlockPos.containing(x, y, z);
											BlockEntity _blockEntity = world.getBlockEntity(_bp);
											BlockState _bs = world.getBlockState(_bp);
											if (_blockEntity != null) {
												_blockEntity.getPersistentData().putString("output", recipeOBJ.get("output").getAsString());
												_blockEntity.getPersistentData().putString("lapis_input", recipeOBJ.get("lapis_input").getAsString());
												_blockEntity.getPersistentData().putString("cItem", rl_cItem);
												_blockEntity.getPersistentData().putDouble("gp_required", recipeOBJ.get("gp_required").getAsDouble());
												_blockEntity.getPersistentData().putDouble("wait_time", recipeOBJ.get("wait_time").getAsDouble());
												_blockEntity.getPersistentData().putDouble("lapis_required", recipeOBJ.get("lapis_required").getAsDouble());
												_blockEntity.getPersistentData().putDouble("fe_required", recipeOBJ.get("fe_required").getAsDouble());
											}
											if (world instanceof Level _level)
												_level.sendBlockUpdated(_bp, _bs, _bs, 3);
										}
										break;
									}
									rlNum = rlNum + 1;
								}
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
					if (!(getBlockNBTString(world, BlockPos.containing(x, y, z), "output")).equals("") && !(getBlockNBTString(world, BlockPos.containing(x, y, z), "cItem")).equals("")
							&& getBlockNBTNumber(world, BlockPos.containing(x, y, z), "wait_time") != 0 && getBlockNBTNumber(world, BlockPos.containing(x, y, z), "lapis_required") != 0
							&& getBlockNBTNumber(world, BlockPos.containing(x, y, z), "fe_required") != 0 && getBlockNBTNumber(world, BlockPos.containing(x, y, z), "gp_required") != 0) {
						if ((itemFromBlockInventory(world, BlockPos.containing(x, y, z), 0).copy()).is(ItemTags.create(ResourceLocation.parse(((getBlockNBTString(world, BlockPos.containing(x, y, z), "cItem"))).toLowerCase(java.util.Locale.ENGLISH))))
								|| (BuiltInRegistries.ITEM.getKey((itemFromBlockInventory(world, BlockPos.containing(x, y, z), 0).copy()).getItem()).toString()).equals(getBlockNBTString(world, BlockPos.containing(x, y, z), "cItem"))) {
							if (itemFromBlockInventory(world, BlockPos.containing(x, y, z), 2).getCount() == 0
									|| itemFromBlockInventory(world, BlockPos.containing(x, y, z), 2).getCount() < (itemFromBlockInventory(world, BlockPos.containing(x, y, z), 2).copy()).getMaxStackSize()
											&& (BuiltInRegistries.ITEM.getKey((itemFromBlockInventory(world, BlockPos.containing(x, y, z), 2).copy()).getItem()).toString()).equals(getBlockNBTString(world, BlockPos.containing(x, y, z), "output"))) {
								if ((BuiltInRegistries.ITEM.getKey((itemFromBlockInventory(world, BlockPos.containing(x, y, z), 1).copy()).getItem()).toString()).equals(getBlockNBTString(world, BlockPos.containing(x, y, z), "lapis_input"))
										&& itemFromBlockInventory(world, BlockPos.containing(x, y, z), 1).getCount() >= getBlockNBTNumber(world, BlockPos.containing(x, y, z), "lapis_required")) {
									if (player.getData(EuruModVariables.PLAYER_VARIABLES).playerGPChecking) {
										{
											EuruModVariables.PlayerVariables _vars = player.getData(EuruModVariables.PLAYER_VARIABLES);
											_vars.playerGP_Used_Update = player.getData(EuruModVariables.PLAYER_VARIABLES).playerGP_Used_Update + getBlockNBTNumber(world, BlockPos.containing(x, y, z), "gp_required") + cost;
											_vars.markSyncDirty();
										}
									}
									if (player.getData(EuruModVariables.PLAYER_VARIABLES).playerGP_Total >= player.getData(EuruModVariables.PLAYER_VARIABLES).playerGP_Used) {
										feStep = getBlockNBTNumber(world, BlockPos.containing(x, y, z), "fe_required") / getBlockNBTNumber(world, BlockPos.containing(x, y, z), "wait_time");
										if (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "cProgress") == 0) {
											if (getEnergyStored(world, BlockPos.containing(x, y, z), null) >= getBlockNBTNumber(world, BlockPos.containing(x, y, z), "fe_required")) {
												if (!world.isClientSide()) {
													BlockPos _bp = BlockPos.containing(x, y, z);
													BlockEntity _blockEntity = world.getBlockEntity(_bp);
													BlockState _bs = world.getBlockState(_bp);
													if (_blockEntity != null) {
														_blockEntity.getPersistentData().putDouble("cProgress", (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "cProgress") + 1 * mult));
													}
													if (world instanceof Level _level)
														_level.sendBlockUpdated(_bp, _bs, _bs, 3);
												}
												energyRemove = feStep * mult;
												on = true;
												if (world.getBlockEntity(new BlockPos((int) x, (int) y, (int) z)) instanceof EnchanterBlockEntity be) {
													be.removeEnergy((int) energyRemove);
												}
												if (!world.isClientSide()) {
													BlockPos _bp = BlockPos.containing(x, y, z);
													BlockEntity _blockEntity = world.getBlockEntity(_bp);
													BlockState _bs = world.getBlockState(_bp);
													if (_blockEntity != null) {
														_blockEntity.getPersistentData().putString("errorMessage", "");
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
														_blockEntity.getPersistentData().putString("errorMessage", "notEnoughFE");
													}
													if (world instanceof Level _level)
														_level.sendBlockUpdated(_bp, _bs, _bs, 3);
												}
											}
										} else if (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "cProgress") < getBlockNBTNumber(world, BlockPos.containing(x, y, z), "wait_time")) {
											if (getEnergyStored(world, BlockPos.containing(x, y, z), null) >= getBlockNBTNumber(world, BlockPos.containing(x, y, z), "fe_required")
													- feStep * getBlockNBTNumber(world, BlockPos.containing(x, y, z), "cProgress")) {
												if (!world.isClientSide()) {
													BlockPos _bp = BlockPos.containing(x, y, z);
													BlockEntity _blockEntity = world.getBlockEntity(_bp);
													BlockState _bs = world.getBlockState(_bp);
													if (_blockEntity != null) {
														_blockEntity.getPersistentData().putDouble("cProgress", (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "cProgress") + 1 * mult));
													}
													if (world instanceof Level _level)
														_level.sendBlockUpdated(_bp, _bs, _bs, 3);
												}
												on = true;
												energyRemove = feStep * mult;
												if (world.getBlockEntity(new BlockPos((int) x, (int) y, (int) z)) instanceof EnchanterBlockEntity be) {
													be.removeEnergy((int) energyRemove);
												}
												if (!world.isClientSide()) {
													BlockPos _bp = BlockPos.containing(x, y, z);
													BlockEntity _blockEntity = world.getBlockEntity(_bp);
													BlockState _bs = world.getBlockState(_bp);
													if (_blockEntity != null) {
														_blockEntity.getPersistentData().putString("errorMessage", "");
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
														_blockEntity.getPersistentData().putString("errorMessage", "notEnoughFE");
													}
													if (world instanceof Level _level)
														_level.sendBlockUpdated(_bp, _bs, _bs, 3);
												}
											}
										} else {
											if (world instanceof ILevelExtension _ext && _ext.getCapability(Capabilities.ItemHandler.BLOCK, BlockPos.containing(x, y, z), null) instanceof IItemHandlerModifiable _itemHandlerModifiable) {
												ItemStack _setstack = new ItemStack(BuiltInRegistries.ITEM.get(ResourceLocation.parse(((getBlockNBTString(world, BlockPos.containing(x, y, z), "output"))).toLowerCase(java.util.Locale.ENGLISH))))
														.copy();
												_setstack.setCount(itemFromBlockInventory(world, BlockPos.containing(x, y, z), 2).getCount() + 1);
												_itemHandlerModifiable.setStackInSlot(2, _setstack);
											}
											if (world instanceof ILevelExtension _ext && _ext.getCapability(Capabilities.ItemHandler.BLOCK, BlockPos.containing(x, y, z), null) instanceof IItemHandlerModifiable _itemHandlerModifiable) {
												ItemStack _setstack = new ItemStack(BuiltInRegistries.ITEM.get(ResourceLocation.parse(((getBlockNBTString(world, BlockPos.containing(x, y, z), "lapis_input"))).toLowerCase(java.util.Locale.ENGLISH))))
														.copy();
												_setstack.setCount((int) (itemFromBlockInventory(world, BlockPos.containing(x, y, z), 1).getCount() - getBlockNBTNumber(world, BlockPos.containing(x, y, z), "lapis_required")));
												_itemHandlerModifiable.setStackInSlot(1, _setstack);
											}
											if (world instanceof ILevelExtension _ext && _ext.getCapability(Capabilities.ItemHandler.BLOCK, BlockPos.containing(x, y, z), null) instanceof IItemHandlerModifiable _itemHandlerModifiable) {
												ItemStack _setstack = (itemFromBlockInventory(world, BlockPos.containing(x, y, z), 0).copy()).copy();
												_setstack.setCount(itemFromBlockInventory(world, BlockPos.containing(x, y, z), 0).getCount() - 1);
												_itemHandlerModifiable.setStackInSlot(0, _setstack);
											}
											if (!world.isClientSide()) {
												BlockPos _bp = BlockPos.containing(x, y, z);
												BlockEntity _blockEntity = world.getBlockEntity(_bp);
												BlockState _bs = world.getBlockState(_bp);
												if (_blockEntity != null) {
													_blockEntity.getPersistentData().putDouble("cProgress", 0);
													_blockEntity.getPersistentData().putString("output", "");
												}
												if (world instanceof Level _level)
													_level.sendBlockUpdated(_bp, _bs, _bs, 3);
											}
										}
									} else {
										if (!world.isClientSide()) {
											BlockPos _bp = BlockPos.containing(x, y, z);
											BlockEntity _blockEntity = world.getBlockEntity(_bp);
											BlockState _bs = world.getBlockState(_bp);
											if (_blockEntity != null) {
												_blockEntity.getPersistentData().putString("errorMessage", "tmGP");
											}
											if (world instanceof Level _level)
												_level.sendBlockUpdated(_bp, _bs, _bs, 3);
										}
									}
								} else {
									if (!world.isClientSide()) {
										BlockPos _bp = BlockPos.containing(x, y, z);
										BlockEntity _blockEntity = world.getBlockEntity(_bp);
										BlockState _bs = world.getBlockState(_bp);
										if (_blockEntity != null) {
											_blockEntity.getPersistentData().putString("output", "");
											_blockEntity.getPersistentData().putString("errorMessage", "notEnoughLapis");
										}
										if (world instanceof Level _level)
											_level.sendBlockUpdated(_bp, _bs, _bs, 3);
									}
								}
							} else {
								if (!world.isClientSide()) {
									BlockPos _bp = BlockPos.containing(x, y, z);
									BlockEntity _blockEntity = world.getBlockEntity(_bp);
									BlockState _bs = world.getBlockState(_bp);
									if (_blockEntity != null) {
										_blockEntity.getPersistentData().putString("output", "");
										_blockEntity.getPersistentData().putString("errorMessage", "outputFilled");
									}
									if (world instanceof Level _level)
										_level.sendBlockUpdated(_bp, _bs, _bs, 3);
								}
							}
						} else {
							if (!world.isClientSide()) {
								BlockPos _bp = BlockPos.containing(x, y, z);
								BlockEntity _blockEntity = world.getBlockEntity(_bp);
								BlockState _bs = world.getBlockState(_bp);
								if (_blockEntity != null) {
									_blockEntity.getPersistentData().putString("output", "");
									_blockEntity.getPersistentData().putString("errorMessage", "noInput");
								}
								if (world instanceof Level _level)
									_level.sendBlockUpdated(_bp, _bs, _bs, 3);
							}
						}
						if (!(getBlockNBTString(world, BlockPos.containing(x, y, z), "errorMessage")).equals("") && !(getBlockNBTString(world, BlockPos.containing(x, y, z), "errorMessage")).equals("notEnoughFE")) {
							if (!world.isClientSide()) {
								BlockPos _bp = BlockPos.containing(x, y, z);
								BlockEntity _blockEntity = world.getBlockEntity(_bp);
								BlockState _bs = world.getBlockState(_bp);
								if (_blockEntity != null) {
									_blockEntity.getPersistentData().putDouble("cProgress", 0);
								}
								if (world instanceof Level _level)
									_level.sendBlockUpdated(_bp, _bs, _bs, 3);
							}
						}
					}
				} else {
					if (!world.isClientSide()) {
						BlockPos _bp = BlockPos.containing(x, y, z);
						BlockEntity _blockEntity = world.getBlockEntity(_bp);
						BlockState _bs = world.getBlockState(_bp);
						if (_blockEntity != null) {
							_blockEntity.getPersistentData().putDouble("cProgress", 0);
							_blockEntity.getPersistentData().putString("output", "");
							_blockEntity.getPersistentData().putString("errorMessage", "noInput");
						}
						if (world instanceof Level _level)
							_level.sendBlockUpdated(_bp, _bs, _bs, 3);
					}
				}
				if (!on) {
					if (3 < getBlockNBTNumber(world, BlockPos.containing(x, y, z), "onCounter")) {
						if (!world.isClientSide()) {
							BlockPos _bp = BlockPos.containing(x, y, z);
							BlockEntity _blockEntity = world.getBlockEntity(_bp);
							BlockState _bs = world.getBlockState(_bp);
							if (_blockEntity != null) {
								_blockEntity.getPersistentData().putDouble("onCounter", 0);
							}
							if (world instanceof Level _level)
								_level.sendBlockUpdated(_bp, _bs, _bs, 3);
						}
						{
							BlockPos _pos = BlockPos.containing(x, y, z);
							BlockState _bs = world.getBlockState(_pos);
							if (_bs.getBlock().getStateDefinition().getProperty("on") instanceof BooleanProperty _booleanProp)
								world.setBlock(_pos, _bs.setValue(_booleanProp, on), 3);
						}
					} else {
						if (!world.isClientSide()) {
							BlockPos _bp = BlockPos.containing(x, y, z);
							BlockEntity _blockEntity = world.getBlockEntity(_bp);
							BlockState _bs = world.getBlockState(_bp);
							if (_blockEntity != null) {
								_blockEntity.getPersistentData().putDouble("onCounter", (1 + getBlockNBTNumber(world, BlockPos.containing(x, y, z), "onCounter")));
							}
							if (world instanceof Level _level)
								_level.sendBlockUpdated(_bp, _bs, _bs, 3);
						}
					}
				} else {
					{
						BlockPos _pos = BlockPos.containing(x, y, z);
						BlockState _bs = world.getBlockState(_pos);
						if (_bs.getBlock().getStateDefinition().getProperty("on") instanceof BooleanProperty _booleanProp)
							world.setBlock(_pos, _bs.setValue(_booleanProp, true), 3);
					}
					if (!world.isClientSide()) {
						BlockPos _bp = BlockPos.containing(x, y, z);
						BlockEntity _blockEntity = world.getBlockEntity(_bp);
						BlockState _bs = world.getBlockState(_bp);
						if (_blockEntity != null) {
							_blockEntity.getPersistentData().putDouble("onCounter", 0);
						}
						if (world instanceof Level _level)
							_level.sendBlockUpdated(_bp, _bs, _bs, 3);
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

	private static boolean getBlockNBTLogic(LevelAccessor world, BlockPos pos, String tag) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity != null)
			return blockEntity.getPersistentData().getBoolean(tag);
		return false;
	}

	private static ItemStack itemFromBlockInventory(LevelAccessor world, BlockPos pos, int slot) {
		if (world instanceof ILevelExtension ext) {
			IItemHandler itemHandler = ext.getCapability(Capabilities.ItemHandler.BLOCK, pos, null);
			if (itemHandler != null)
				return itemHandler.getStackInSlot(slot);
		}
		return ItemStack.EMPTY;
	}

	public static int getEnergyStored(LevelAccessor level, BlockPos pos, Direction direction) {
		if (level instanceof ILevelExtension levelExtension) {
			IEnergyStorage energyStorage = levelExtension.getCapability(Capabilities.EnergyStorage.BLOCK, pos, direction);
			if (energyStorage != null)
				return energyStorage.getEnergyStored();
		}
		return 0;
	}
}