package net.wavedk.extrautilitiesreutilized.procedures;

import org.apache.commons.lang3.function.FailableFunction;

import net.wavedk.extrautilitiesreutilized.network.EuruModVariables;
import net.wavedk.extrautilitiesreutilized.init.EuruModItems;
import net.wavedk.extrautilitiesreutilized.init.EuruModBlocks;
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
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.tags.ItemTags;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.ChatFormatting;

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
		String getDependencies = "";
		String eNumString = "";
		String eMaxNumString = "";
		com.google.gson.JsonObject configOBJ = new com.google.gson.JsonObject();
		com.google.gson.JsonObject fuelPropertiesOBJ = new com.google.gson.JsonObject();
		com.google.gson.JsonObject itemOBJ = new com.google.gson.JsonObject();
		com.google.gson.JsonObject blockOBJ = new com.google.gson.JsonObject();
		com.google.gson.JsonObject deobj = new com.google.gson.JsonObject();
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
		double eNum = 0;
		double eMaxNum = 0;
		double soFL = 0;
		boolean canGenerate = false;
		boolean canSlowBurn = false;
		boolean canPass = false;
		boolean loopdiloop = false;
		boolean canSendEnergy = false;
		ItemStack eItem = ItemStack.EMPTY;
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
		if (player instanceof ServerPlayer || player instanceof Player) {
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
				if (world instanceof Level _level11 && _level11.hasNeighborSignal(BlockPos.containing(x, y, z))) {
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
				if (world instanceof Level _level15 && _level15.hasNeighborSignal(BlockPos.containing(x, y, z))) {
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
			mult = 1;
			if ((itemFromBlockInventory(world, BlockPos.containing(x, y, z), 1).copy()).getItem() == EuruModItems.SPEED_UPGRADE.get()) {
				if (!world.isClientSide()) {
					BlockPos _bp = BlockPos.containing(x, y, z);
					BlockEntity _blockEntity = world.getBlockEntity(_bp);
					BlockState _bs = world.getBlockState(_bp);
					if (_blockEntity != null) {
						_blockEntity.getPersistentData().putDouble("updateMult", (mult + itemFromBlockInventory(world, BlockPos.containing(x, y, z), 1).getCount() / 4d));
					}
					if (world instanceof Level _level)
						_level.sendBlockUpdated(_bp, _bs, _bs, 3);
				}
			} else if ((itemFromBlockInventory(world, BlockPos.containing(x, y, z), 1).copy()).getItem() == EuruModItems.MAGICAL_SPEED_UPGRADE.get()) {
				if (!world.isClientSide()) {
					BlockPos _bp = BlockPos.containing(x, y, z);
					BlockEntity _blockEntity = world.getBlockEntity(_bp);
					BlockState _bs = world.getBlockState(_bp);
					if (_blockEntity != null) {
						_blockEntity.getPersistentData().putDouble("updateMult", (mult + itemFromBlockInventory(world, BlockPos.containing(x, y, z), 1).getCount() / 2d));
					}
					if (world instanceof Level _level)
						_level.sendBlockUpdated(_bp, _bs, _bs, 3);
				}
			} else if ((itemFromBlockInventory(world, BlockPos.containing(x, y, z), 1).copy()).getItem() == EuruModItems.ULTIMATE_SPEED_UPGRADE.get()) {
				if (!world.isClientSide()) {
					BlockPos _bp = BlockPos.containing(x, y, z);
					BlockEntity _blockEntity = world.getBlockEntity(_bp);
					BlockState _bs = world.getBlockState(_bp);
					if (_blockEntity != null) {
						_blockEntity.getPersistentData().putDouble("updateMult", (mult + itemFromBlockInventory(world, BlockPos.containing(x, y, z), 1).getCount() / 1.25));
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
						_blockEntity.getPersistentData().putDouble("updateMult", 1);
					}
					if (world instanceof Level _level)
						_level.sendBlockUpdated(_bp, _bs, _bs, 3);
				}
			}
			if (player.getData(EuruModVariables.PLAYER_VARIABLES).updateMultipliers) {
				mult = getBlockNBTNumber(world, BlockPos.containing(x, y, z), "updateMult");
				if (!world.isClientSide()) {
					BlockPos _bp = BlockPos.containing(x, y, z);
					BlockEntity _blockEntity = world.getBlockEntity(_bp);
					BlockState _bs = world.getBlockState(_bp);
					if (_blockEntity != null) {
						_blockEntity.getPersistentData().putDouble("oldMult", (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "updateMult")));
					}
					if (world instanceof Level _level)
						_level.sendBlockUpdated(_bp, _bs, _bs, 3);
				}
			} else {
				mult = getBlockNBTNumber(world, BlockPos.containing(x, y, z), "oldMult");
			}
			if (getBlockNBTLogic(world, BlockPos.containing(x, y, z), "redstoneModeOn")) {
				feConfig = new File((FMLPaths.GAMEDIR.get().toString() + "/config/euru/"), File.separator + "euru_fe_config.json");
				if ((getBlockNBTString(world, BlockPos.containing(x, y, z), "currentItem")).equals("")) {
					canGenerate = false;
					canSlowBurn = false;
				}
				if ((getBlockNBTString(world, BlockPos.containing(x, y, z), "currentItem")).equals("") && itemFromBlockInventory(world, BlockPos.containing(x, y, z), 0).getCount() != 0) {
					if (!(getBlockNBTString(world, BlockPos.containing(x, y, z), "lastCheckedItem")).equals(BuiltInRegistries.ITEM.getKey((itemFromBlockInventory(world, BlockPos.containing(x, y, z), 0).copy()).getItem()).toString())) {
						if (!world.isClientSide()) {
							BlockPos _bp = BlockPos.containing(x, y, z);
							BlockEntity _blockEntity = world.getBlockEntity(_bp);
							BlockState _bs = world.getBlockState(_bp);
							if (_blockEntity != null) {
								_blockEntity.getPersistentData().putString("lastCheckedItem", (BuiltInRegistries.ITEM.getKey((itemFromBlockInventory(world, BlockPos.containing(x, y, z), 0).copy()).getItem()).toString()));
							}
							if (world instanceof Level _level)
								_level.sendBlockUpdated(_bp, _bs, _bs, 3);
						}
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
								if (blockOBJ.has("listFuel") && blockOBJ.get("listFuel").isJsonArray()) {
									fuelList = blockOBJ.getAsJsonArray("listFuel");
								}
								cNum = 0;
								for (int index2088 = 0; index2088 < (int) fuelList.size(); index2088++) {
									if ((fuelList.get((int) cNum).getAsString()).equals(BuiltInRegistries.ITEM.getKey((itemFromBlockInventory(world, BlockPos.containing(x, y, z), 0).copy()).getItem()).toString())
											|| (itemFromBlockInventory(world, BlockPos.containing(x, y, z), 0).copy()).is(ItemTags.create(ResourceLocation.parse((fuelList.get((int) cNum).getAsString()).toLowerCase(java.util.Locale.ENGLISH))))
											|| (world.getBlockState(BlockPos.containing(x, y, z))).getBlock() == EuruModBlocks.DISENCHANTMENT_GENERATOR.get()
													&& (itemFromBlockInventory(world, BlockPos.containing(x, y, z), 0).copy()).getItem() == Items.ENCHANTED_BOOK) {
										if (player.getData(EuruModVariables.PLAYER_VARIABLES).playerGP_Total >= player.getData(EuruModVariables.PLAYER_VARIABLES).playerGP_Used && !player.getData(EuruModVariables.PLAYER_VARIABLES).playerGPChecking) {
											canPass = true;
										} else if (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "oldGPTotal") >= getBlockNBTNumber(world, BlockPos.containing(x, y, z), "oldGPUsed")
												&& player.getData(EuruModVariables.PLAYER_VARIABLES).playerGPChecking) {
											canPass = true;
										}
										if (!world.isClientSide()) {
											BlockPos _bp = BlockPos.containing(x, y, z);
											BlockEntity _blockEntity = world.getBlockEntity(_bp);
											BlockState _bs = world.getBlockState(_bp);
											if (_blockEntity != null) {
												_blockEntity.getPersistentData().putString("lastCheckedItem", "");
											}
											if (world instanceof Level _level)
												_level.sendBlockUpdated(_bp, _bs, _bs, 3);
										}
										if (canPass) {
											if ((world.getBlockState(BlockPos.containing(x, y, z))).getBlock() == EuruModBlocks.DISENCHANTMENT_GENERATOR.get()) {
												fuelPropertiesOBJ = blockOBJ.get("fuelProperties").getAsJsonObject();
												eItem = (itemFromBlockInventory(world, BlockPos.containing(x, y, z), 0).copy()).copy();
												eMaxNum = (net.minecraft.world.item.enchantment.EnchantmentHelper.getEnchantmentsForCrafting(eItem)).keySet().stream().findFirst().map(enchantment -> enchantment.value().getMaxLevel()).orElse(0);
												eMaxNumString = ("" + eNum).substring(0, ("" + eNum).indexOf(".", 0));
												if (eMaxNum != 0) {
													deobj = fuelPropertiesOBJ.get("math_-Dont_touch_this_if_you_dont_know_what_youre_doing").getAsJsonObject();
													if (deobj.get("currentWeight").getAsDouble() > 0) {
														if (!world.isClientSide()) {
															BlockPos _bp = BlockPos.containing(x, y, z);
															BlockEntity _blockEntity = world.getBlockEntity(_bp);
															BlockState _bs = world.getBlockState(_bp);
															if (_blockEntity != null) {
																_blockEntity.getPersistentData().putString("currentItem", (BuiltInRegistries.ITEM.getKey((itemFromBlockInventory(world, BlockPos.containing(x, y, z), 0).copy()).getItem()).toString()));
															}
															if (world instanceof Level _level)
																_level.sendBlockUpdated(_bp, _bs, _bs, 3);
														}
														eNum = (net.minecraft.world.item.enchantment.EnchantmentHelper.getEnchantmentsForCrafting(eItem)).entrySet().stream().findFirst().map(entry -> entry.getIntValue()).orElse(0);
														eNumString = ("" + eNum).substring(0, ("" + eNum).indexOf(".", 0));
														if (!world.isClientSide()) {
															BlockPos _bp = BlockPos.containing(x, y, z);
															BlockEntity _blockEntity = world.getBlockEntity(_bp);
															BlockState _bs = world.getBlockState(_bp);
															if (_blockEntity != null) {
																_blockEntity.getPersistentData().putDouble("wait_time", (Math.round(deobj.get("totalFEGenerated").getAsDouble() * Math.pow(eNum / eMaxNum, deobj.get("currentWeight").getAsDouble()))
																		- Math.round(deobj.get("totalFEGenerated").getAsDouble() * Math.pow((eNum - 1) / eMaxNum, deobj.get("currentWeight").getAsDouble()))));
																_blockEntity.getPersistentData().putDouble("feSpeed", (fuelPropertiesOBJ.get("feSpeed").getAsDouble() + fuelPropertiesOBJ.get("feIncrement").getAsDouble() * eMaxNum));
																_blockEntity.getPersistentData().putDouble("cProgress", 0);
															}
															if (world instanceof Level _level)
																_level.sendBlockUpdated(_bp, _bs, _bs, 3);
														}
														canGenerate = true;
														if (world instanceof ILevelExtension _ext && _ext.getCapability(Capabilities.ItemHandler.BLOCK, BlockPos.containing(x, y, z), null) instanceof IItemHandlerModifiable _itemHandlerModifiable) {
															int _slotid = 0;
															ItemStack _stk = _itemHandlerModifiable.getStackInSlot(_slotid).copy();
															_stk.shrink(1);
															_itemHandlerModifiable.setStackInSlot(_slotid, _stk);
														}
														{
															BlockPos _pos = BlockPos.containing(x, y, z);
															BlockState _bs = world.getBlockState(_pos);
															if (_bs.getBlock().getStateDefinition().getProperty("on") instanceof BooleanProperty _booleanProp)
																world.setBlock(_pos, _bs.setValue(_booleanProp, true), 3);
														}
													} else {
														if (world instanceof ServerLevel _level) {
															_level.getServer().getPlayerList().broadcastSystemMessage(Component.literal(
																	"A mod-breaking error has occurred. The 'currentWeight' value in the \"euru_fe_config.json\" file under \"euru:disenchantment_generator\" is not higher than 0, this causes all math related to the Disenchantment Generator to fail. Go into your config and set this value to a number above 0, or contact your server admins for help.")
																	.withColor(0xff0000).withStyle(ChatFormatting.BOLD), false);
														}
													}
												} else {
													if (!world.isClientSide()) {
														BlockPos _bp = BlockPos.containing(x, y, z);
														BlockEntity _blockEntity = world.getBlockEntity(_bp);
														BlockState _bs = world.getBlockState(_bp);
														if (_blockEntity != null) {
															_blockEntity.getPersistentData().putString("lastCheckedItem", (BuiltInRegistries.ITEM.getKey((itemFromBlockInventory(world, BlockPos.containing(x, y, z), 0).copy()).getItem()).toString()));
														}
														if (world instanceof Level _level)
															_level.sendBlockUpdated(_bp, _bs, _bs, 3);
													}
												}
											} else {
												itemOBJ = fuelPropertiesOBJ.get(fuelList.get((int) cNum).getAsString()).getAsJsonObject();
												if (!world.isClientSide()) {
													BlockPos _bp = BlockPos.containing(x, y, z);
													BlockEntity _blockEntity = world.getBlockEntity(_bp);
													BlockState _bs = world.getBlockState(_bp);
													if (_blockEntity != null) {
														_blockEntity.getPersistentData().putString("currentItem", (BuiltInRegistries.ITEM.getKey((itemFromBlockInventory(world, BlockPos.containing(x, y, z), 0).copy()).getItem()).toString()));
														_blockEntity.getPersistentData().putDouble("wait_time", itemOBJ.get("feGenerated").getAsDouble());
														_blockEntity.getPersistentData().putDouble("feSpeed", itemOBJ.get("feSpeed").getAsDouble());
														_blockEntity.getPersistentData().putDouble("cProgress", 0);
													}
													if (world instanceof Level _level)
														_level.sendBlockUpdated(_bp, _bs, _bs, 3);
												}
												canGenerate = true;
												if (world instanceof ILevelExtension _ext && _ext.getCapability(Capabilities.ItemHandler.BLOCK, BlockPos.containing(x, y, z), null) instanceof IItemHandlerModifiable _itemHandlerModifiable) {
													int _slotid = 0;
													ItemStack _stk = _itemHandlerModifiable.getStackInSlot(_slotid).copy();
													_stk.shrink(1);
													_itemHandlerModifiable.setStackInSlot(_slotid, _stk);
												}
												{
													BlockPos _pos = BlockPos.containing(x, y, z);
													BlockState _bs = world.getBlockState(_pos);
													if (_bs.getBlock().getStateDefinition().getProperty("on") instanceof BooleanProperty _booleanProp)
														world.setBlock(_pos, _bs.setValue(_booleanProp, true), 3);
												}
											}
										}
										break;
									}
									cNum = cNum + 1;
								}
								if ((getBlockNBTString(world, BlockPos.containing(x, y, z), "currentItem")).equals("")) {
									{
										BlockPos _pos = BlockPos.containing(x, y, z);
										BlockState _bs = world.getBlockState(_pos);
										if (_bs.getBlock().getStateDefinition().getProperty("on") instanceof BooleanProperty _booleanProp)
											world.setBlock(_pos, _bs.setValue(_booleanProp, false), 3);
									}
								}
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					} else {
						{
							BlockPos _pos = BlockPos.containing(x, y, z);
							BlockState _bs = world.getBlockState(_pos);
							if (_bs.getBlock().getStateDefinition().getProperty("on") instanceof BooleanProperty _booleanProp)
								world.setBlock(_pos, _bs.setValue(_booleanProp, false), 3);
						}
					}
				} else {
					canGenerate = true;
				}
				if (!(getBlockNBTNumber(world, BlockPos.containing(x, y, z), "wait_time") != 0 && getBlockNBTNumber(world, BlockPos.containing(x, y, z), "feSpeed") != 0)) {
					canGenerate = false;
				}
				if (player.getData(EuruModVariables.PLAYER_VARIABLES).playerGP_Total < player.getData(EuruModVariables.PLAYER_VARIABLES).playerGP_Used && !player.getData(EuruModVariables.PLAYER_VARIABLES).playerGPChecking) {
					if (!world.isClientSide()) {
						BlockPos _bp = BlockPos.containing(x, y, z);
						BlockEntity _blockEntity = world.getBlockEntity(_bp);
						BlockState _bs = world.getBlockState(_bp);
						if (_blockEntity != null) {
							_blockEntity.getPersistentData().putBoolean("tmGP", true);
							_blockEntity.getPersistentData().putDouble("oldGPTotal", player.getData(EuruModVariables.PLAYER_VARIABLES).playerGP_Total);
							_blockEntity.getPersistentData().putDouble("oldGPUsed", player.getData(EuruModVariables.PLAYER_VARIABLES).playerGP_Used);
						}
						if (world instanceof Level _level)
							_level.sendBlockUpdated(_bp, _bs, _bs, 3);
					}
					canGenerate = false;
				} else if (!player.getData(EuruModVariables.PLAYER_VARIABLES).playerGPChecking) {
					if (!world.isClientSide()) {
						BlockPos _bp = BlockPos.containing(x, y, z);
						BlockEntity _blockEntity = world.getBlockEntity(_bp);
						BlockState _bs = world.getBlockState(_bp);
						if (_blockEntity != null) {
							_blockEntity.getPersistentData().putDouble("oldGPUsed", player.getData(EuruModVariables.PLAYER_VARIABLES).playerGP_Used);
							_blockEntity.getPersistentData().putDouble("oldGPTotal", player.getData(EuruModVariables.PLAYER_VARIABLES).playerGP_Total);
						}
						if (world instanceof Level _level)
							_level.sendBlockUpdated(_bp, _bs, _bs, 3);
					}
				} else if (player.getData(EuruModVariables.PLAYER_VARIABLES).playerGP_Total >= player.getData(EuruModVariables.PLAYER_VARIABLES).playerGP_Used && !player.getData(EuruModVariables.PLAYER_VARIABLES).playerGPChecking) {
					if (!world.isClientSide()) {
						BlockPos _bp = BlockPos.containing(x, y, z);
						BlockEntity _blockEntity = world.getBlockEntity(_bp);
						BlockState _bs = world.getBlockState(_bp);
						if (_blockEntity != null) {
							_blockEntity.getPersistentData().putBoolean("tmGP", false);
						}
						if (world instanceof Level _level)
							_level.sendBlockUpdated(_bp, _bs, _bs, 3);
					}
				}
				if (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "oldGPTotal") < getBlockNBTNumber(world, BlockPos.containing(x, y, z), "oldGPUsed") && player.getData(EuruModVariables.PLAYER_VARIABLES).playerGPChecking) {
					if (!world.isClientSide()) {
						BlockPos _bp = BlockPos.containing(x, y, z);
						BlockEntity _blockEntity = world.getBlockEntity(_bp);
						BlockState _bs = world.getBlockState(_bp);
						if (_blockEntity != null) {
							_blockEntity.getPersistentData().putBoolean("tmGP", true);
						}
						if (world instanceof Level _level)
							_level.sendBlockUpdated(_bp, _bs, _bs, 3);
					}
					canGenerate = false;
				} else if (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "oldGPTotal") >= getBlockNBTNumber(world, BlockPos.containing(x, y, z), "oldGPUsed") && player.getData(EuruModVariables.PLAYER_VARIABLES).playerGPChecking) {
					if (!world.isClientSide()) {
						BlockPos _bp = BlockPos.containing(x, y, z);
						BlockEntity _blockEntity = world.getBlockEntity(_bp);
						BlockState _bs = world.getBlockState(_bp);
						if (_blockEntity != null) {
							_blockEntity.getPersistentData().putBoolean("tmGP", false);
						}
						if (world instanceof Level _level)
							_level.sendBlockUpdated(_bp, _bs, _bs, 3);
					}
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
							if (!world.isClientSide()) {
								BlockPos _bp = BlockPos.containing(x, y, z);
								BlockEntity _blockEntity = world.getBlockEntity(_bp);
								BlockState _bs = world.getBlockState(_bp);
								if (_blockEntity != null) {
									_blockEntity.getPersistentData().putDouble("cProgress", (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "wait_time")));
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
									_blockEntity.getPersistentData().putDouble("cProgress", (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "cProgress") + feSpeed));
								}
								if (world instanceof Level _level)
									_level.sendBlockUpdated(_bp, _bs, _bs, 3);
							}
						}
						{
							BlockPos _pos = BlockPos.containing(x, y, z);
							BlockState _bs = world.getBlockState(_pos);
							if (_bs.getBlock().getStateDefinition().getProperty("on") instanceof BooleanProperty _booleanProp)
								world.setBlock(_pos, _bs.setValue(_booleanProp, true), 3);
						}
						SpecialGenFeatureProcedure.execute(world, x, y, z, BuiltInRegistries.ITEM.getKey((new ItemStack((world.getBlockState(BlockPos.containing(x, y, z))).getBlock())).getItem()).toString());
						if (world.getBlockEntity(new BlockPos((int) x, (int) y, (int) z)) instanceof IEnergyReceiver be) {
							be.addEnergy((int) feSpeed);
						}
					}
				} else if (canSlowBurn) {
					if (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "wait_time") > getBlockNBTNumber(world, BlockPos.containing(x, y, z), "cProgress")) {
						if (!world.isClientSide()) {
							BlockPos _bp = BlockPos.containing(x, y, z);
							BlockEntity _blockEntity = world.getBlockEntity(_bp);
							BlockState _bs = world.getBlockState(_bp);
							if (_blockEntity != null) {
								_blockEntity.getPersistentData().putDouble("cProgress", (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "cProgress") + feSpeed / 32));
							}
							if (world instanceof Level _level)
								_level.sendBlockUpdated(_bp, _bs, _bs, 3);
						}
					}
				}
				if (canGenerate || canSlowBurn) {
					if (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "wait_time") <= getBlockNBTNumber(world, BlockPos.containing(x, y, z), "cProgress")) {
						if (!world.isClientSide()) {
							BlockPos _bp = BlockPos.containing(x, y, z);
							BlockEntity _blockEntity = world.getBlockEntity(_bp);
							BlockState _bs = world.getBlockState(_bp);
							if (_blockEntity != null) {
								_blockEntity.getPersistentData().putDouble("cProgress", 0);
								_blockEntity.getPersistentData().putDouble("wait_time", 0);
								_blockEntity.getPersistentData().putDouble("feSpeed", 0);
								_blockEntity.getPersistentData().putString("currentItem", "");
							}
							if (world instanceof Level _level)
								_level.sendBlockUpdated(_bp, _bs, _bs, 3);
						}
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
				if (getPropertyByName(blockstate, "on") instanceof BooleanProperty _getbp160 && blockstate.getValue(_getbp160)) {
					if (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "offCounter") > 2) {
						if (!world.isClientSide()) {
							BlockPos _bp = BlockPos.containing(x, y, z);
							BlockEntity _blockEntity = world.getBlockEntity(_bp);
							BlockState _bs = world.getBlockState(_bp);
							if (_blockEntity != null) {
								_blockEntity.getPersistentData().putDouble("offCounter", 0);
							}
							if (world instanceof Level _level)
								_level.sendBlockUpdated(_bp, _bs, _bs, 3);
						}
						{
							BlockPos _pos = BlockPos.containing(x, y, z);
							BlockState _bs = world.getBlockState(_pos);
							if (_bs.getBlock().getStateDefinition().getProperty("on") instanceof BooleanProperty _booleanProp)
								world.setBlock(_pos, _bs.setValue(_booleanProp, false), 3);
						}
					} else {
						if (!world.isClientSide()) {
							BlockPos _bp = BlockPos.containing(x, y, z);
							BlockEntity _blockEntity = world.getBlockEntity(_bp);
							BlockState _bs = world.getBlockState(_bp);
							if (_blockEntity != null) {
								_blockEntity.getPersistentData().putDouble("offCounter", (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "offCounter") + 1));
							}
							if (world instanceof Level _level)
								_level.sendBlockUpdated(_bp, _bs, _bs, 3);
						}
					}
				}
			} else {
				if (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "offCounter") > 0) {
					if (!world.isClientSide()) {
						BlockPos _bp = BlockPos.containing(x, y, z);
						BlockEntity _blockEntity = world.getBlockEntity(_bp);
						BlockState _bs = world.getBlockState(_bp);
						if (_blockEntity != null) {
							_blockEntity.getPersistentData().putDouble("offCounter", 0);
						}
						if (world instanceof Level _level)
							_level.sendBlockUpdated(_bp, _bs, _bs, 3);
					}
				}
			}
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
					if (canSendEnergy) {
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

	private static ItemStack itemFromBlockInventory(LevelAccessor world, BlockPos pos, int slot) {
		if (world instanceof ILevelExtension ext) {
			IItemHandler itemHandler = ext.getCapability(Capabilities.ItemHandler.BLOCK, pos, null);
			if (itemHandler != null)
				return itemHandler.getStackInSlot(slot);
		}
		return ItemStack.EMPTY;
	}

	private static boolean getBlockNBTLogic(LevelAccessor world, BlockPos pos, String tag) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity != null)
			return blockEntity.getPersistentData().getBoolean(tag);
		return false;
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

	private static Property<?> getPropertyByName(BlockState state, String name) {
		for (Property<?> property : state.getProperties()) {
			if (property.getName().equals(name)) {
				return property;
			}
		}
		return null;
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