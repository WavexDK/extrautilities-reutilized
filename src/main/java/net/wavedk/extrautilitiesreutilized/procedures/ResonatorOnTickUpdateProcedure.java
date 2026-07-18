package net.wavedk.extrautilitiesreutilized.procedures;

import org.apache.commons.lang3.function.FailableFunction;

import net.wavedk.extrautilitiesreutilized.network.EuruModVariables;
import net.wavedk.extrautilitiesreutilized.init.EuruModItems;

import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.common.extensions.ILevelExtension;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.fml.loading.FMLPaths;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.Vec2;
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
import net.minecraft.network.chat.Component;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.BlockPos;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

import java.util.function.Supplier;
import java.util.UUID;

import java.io.IOException;
import java.io.FileReader;
import java.io.File;
import java.io.BufferedReader;

public class ResonatorOnTickUpdateProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		File recipeList = new File("");
		File configFile = new File("");
		Entity player = null;
		boolean foundItem = false;
		boolean hasItem = false;
		com.google.gson.JsonArray rlArray = new com.google.gson.JsonArray();
		String currentItem = "";
		String output = "";
		String cItem = "";
		double cProgress = 0;
		double gpr = 0;
		double fProgress = 0;
		double mult = 0;
		double cost = 0;
		double cNumber = 0;
		com.google.gson.JsonObject recipeOBJ = new com.google.gson.JsonObject();
		com.google.gson.JsonObject resoOBJ = new com.google.gson.JsonObject();
		com.google.gson.JsonObject itemOBJ = new com.google.gson.JsonObject();
		com.google.gson.JsonObject tobj = new com.google.gson.JsonObject();
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
				setBlockNBTLogic(world, x, y, z, "redstoneModeOn", true);
			} else if (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "redstoneMode") == 1) {
				if (world instanceof Level _level13 && _level13.hasNeighborSignal(BlockPos.containing(x, y, z))) {
					setBlockNBTLogic(world, x, y, z, "redstoneModeOn", true);
				} else {
					setBlockNBTLogic(world, x, y, z, "redstoneModeOn", false);
				}
			} else if (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "redstoneMode") == 2) {
				if (world instanceof Level _level17 && _level17.hasNeighborSignal(BlockPos.containing(x, y, z))) {
					setBlockNBTLogic(world, x, y, z, "redstoneModeOn", false);
				} else {
					setBlockNBTLogic(world, x, y, z, "redstoneModeOn", true);
				}
			} else if (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "redstoneMode") == 3) {
				setBlockNBTLogic(world, x, y, z, "redstoneModeOn", false);
			} else {
				setBlockNBTLogic(world, x, y, z, "redstoneModeOn", true);
			}
			if (getBlockNBTLogic(world, BlockPos.containing(x, y, z), "redstoneModeOn")) {
				mult = 1;
				if ((itemFromBlockInventory(world, BlockPos.containing(x, y, z), 2).copy()).getItem() == EuruModItems.SPEED_UPGRADE.get()) {
					mult = 1 + itemFromBlockInventory(world, BlockPos.containing(x, y, z), 2).getCount() / 4d;
					cost = itemFromBlockInventory(world, BlockPos.containing(x, y, z), 2).getCount();
				} else if ((itemFromBlockInventory(world, BlockPos.containing(x, y, z), 2).copy()).getItem() == EuruModItems.MAGICAL_SPEED_UPGRADE.get()) {
					mult = 1 + itemFromBlockInventory(world, BlockPos.containing(x, y, z), 2).getCount() / 2d;
					cost = itemFromBlockInventory(world, BlockPos.containing(x, y, z), 2).getCount();
				} else if ((itemFromBlockInventory(world, BlockPos.containing(x, y, z), 2).copy()).getItem() == EuruModItems.ULTIMATE_SPEED_UPGRADE.get()) {
					mult = 1 + itemFromBlockInventory(world, BlockPos.containing(x, y, z), 2).getCount() / 1.25;
					cost = itemFromBlockInventory(world, BlockPos.containing(x, y, z), 2).getCount();
				}
				if (itemFromBlockInventory(world, BlockPos.containing(x, y, z), 0).getCount() > 0) {
					setBlockNBTText(world, x, y, z, "currentItem", "N/A");
					cProgress = getBlockNBTNumber(world, BlockPos.containing(x, y, z), "cProgress");
					if (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "gp_required") == 0 || (getBlockNBTString(world, BlockPos.containing(x, y, z), "currentOutput")).equals("")) {
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
								recipeOBJ = new com.google.gson.Gson().fromJson(jsonstringbuilder.toString(), com.google.gson.JsonObject.class);
								tobj = recipeOBJ.get("recipes").getAsJsonObject();
								if (tobj.get((BuiltInRegistries.BLOCK.getKey((world.getBlockState(BlockPos.containing(x, y, z))).getBlock()).toString())).isJsonObject()) {
									resoOBJ = tobj.get((BuiltInRegistries.BLOCK.getKey((world.getBlockState(BlockPos.containing(x, y, z))).getBlock()).toString())).getAsJsonObject();
									String itemKey = BuiltInRegistries.ITEM.getKey(itemFromBlockInventory(world, BlockPos.containing(x, y, z), 0).copy().getItem()).toString();
									if (resoOBJ.has(itemKey) && resoOBJ.get(itemKey).isJsonObject()) {
										itemOBJ = resoOBJ.get(itemKey).getAsJsonObject();
										hasItem = true;
									}
									if (hasItem) {
										itemOBJ = resoOBJ.get((BuiltInRegistries.ITEM.getKey((itemFromBlockInventory(world, BlockPos.containing(x, y, z), 0).copy()).getItem()).toString())).getAsJsonObject();
										if (itemOBJ.get("gp_required").isJsonPrimitive() ? itemOBJ.get("gp_required").getAsJsonPrimitive().isNumber() : false) {
											setBlockNBTNumber(world, x, y, z, "gp_required", itemOBJ.get("gp_required").getAsDouble());
											if (itemOBJ.get("output").isJsonPrimitive() ? itemOBJ.get("output").getAsJsonPrimitive().isString() : false) {
												setBlockNBTText(world, x, y, z, "currentOutput", itemOBJ.get("output").getAsString());
											}
										}
									}
								}
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
					output = getBlockNBTString(world, BlockPos.containing(x, y, z), "currentOutput");
					gpr = getBlockNBTNumber(world, BlockPos.containing(x, y, z), "gp_required") + cost;
					foundItem = false;
					if (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "wait_time") == 0 || (getBlockNBTString(world, BlockPos.containing(x, y, z), "currentItem")).equals("")
							|| (getBlockNBTString(world, BlockPos.containing(x, y, z), "currentItem")).equals("N/A")) {
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
								recipeOBJ = new com.google.gson.Gson().fromJson(jsonstringbuilder.toString(), com.google.gson.JsonObject.class);
								tobj = recipeOBJ.get("recipes").getAsJsonObject();
								resoOBJ = tobj.get((BuiltInRegistries.BLOCK.getKey((world.getBlockState(BlockPos.containing(x, y, z))).getBlock()).toString())).getAsJsonObject();
								rlArray = resoOBJ.get("recipeList").getAsJsonArray();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
						if (rlArray.size() > 0) {
							cNumber = 0;
							for (int index0 = 0; index0 < (int) rlArray.size(); index0++) {
								cItem = rlArray.get((int) cNumber).getAsString();
								if ((cItem).equals(BuiltInRegistries.ITEM.getKey((itemFromBlockInventory(world, BlockPos.containing(x, y, z), 0).copy()).getItem()).toString())
										|| (itemFromBlockInventory(world, BlockPos.containing(x, y, z), 0).copy()).is(ItemTags.create(ResourceLocation.parse((cItem).toLowerCase(java.util.Locale.ENGLISH))))) {
									setBlockNBTText(world, x, y, z, "currentItem", cItem);
									{
										try {
											BufferedReader bufferedReader = new BufferedReader(new FileReader(configFile));
											StringBuilder jsonstringbuilder = new StringBuilder();
											String line;
											while ((line = bufferedReader.readLine()) != null) {
												jsonstringbuilder.append(line);
											}
											bufferedReader.close();
											resoOBJ = new com.google.gson.Gson().fromJson(jsonstringbuilder.toString(), com.google.gson.JsonObject.class);
											tobj = resoOBJ.get("recipes").getAsJsonObject();
											recipeOBJ = tobj.get((BuiltInRegistries.BLOCK.getKey((world.getBlockState(BlockPos.containing(x, y, z))).getBlock()).toString())).getAsJsonObject();
											itemOBJ = recipeOBJ.get(cItem).getAsJsonObject();
											output = itemOBJ.get("output").getAsString();
											if (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "wait_time") != itemOBJ.get("wait_time").getAsDouble()) {
												setBlockNBTNumber(world, x, y, z, "cProgress", 0);
											}
											setBlockNBTNumber(world, x, y, z, "wait_time", itemOBJ.get("wait_time").getAsDouble());
										} catch (IOException e) {
											e.printStackTrace();
										}
									}
									foundItem = true;
									break;
								}
								cNumber = cNumber + 1;
							}
						} else {
							if (world instanceof ServerLevel _level)
								_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
										"say ERROR: Something has gone terribly wrong with Extra Utilities: Reutilized. Contact admins immediately. (E-102)");
						}
					} else {
						foundItem = true;
					}
					if (foundItem) {
						if (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "wait_time") > 0) {
							if (player.getData(EuruModVariables.PLAYER_VARIABLES).playerGPChecking) {
								{
									EuruModVariables.PlayerVariables _vars = player.getData(EuruModVariables.PLAYER_VARIABLES);
									_vars.playerGP_Used_Update = player.getData(EuruModVariables.PLAYER_VARIABLES).playerGP_Used_Update + gpr;
									_vars.markSyncDirty();
								}
							}
						}
						fProgress = getBlockNBTNumber(world, BlockPos.containing(x, y, z), "wait_time");
						if (!(getBlockNBTString(world, BlockPos.containing(x, y, z), "currentItem")).equals("N/A")) {
							if (player.getData(EuruModVariables.PLAYER_VARIABLES).playerGP_Total >= gpr) {
								if ((itemFromBlockInventory(world, BlockPos.containing(x, y, z), 1).copy()).getItem() == BuiltInRegistries.ITEM.get(ResourceLocation.parse((output).toLowerCase(java.util.Locale.ENGLISH)))
										&& itemFromBlockInventory(world, BlockPos.containing(x, y, z), 1).getCount() < new ItemStack(BuiltInRegistries.ITEM.get(ResourceLocation.parse((output).toLowerCase(java.util.Locale.ENGLISH)))).getMaxStackSize()
										|| itemFromBlockInventory(world, BlockPos.containing(x, y, z), 1).getCount() == 0) {
									setBlockNBTLogic(world, x, y, z, "tmI", false);
									setBlockNBTLogic(world, x, y, z, "nsI", false);
									if (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "cProgress") < fProgress) {
										if ((itemFromBlockInventory(world, BlockPos.containing(x, y, z), 0).copy()).getItem() == BuiltInRegistries.ITEM
												.get(ResourceLocation.parse(((getBlockNBTString(world, BlockPos.containing(x, y, z), "currentItem"))).toLowerCase(java.util.Locale.ENGLISH)))
												&& itemFromBlockInventory(world, BlockPos.containing(x, y, z), 0).getCount() != 0) {
											if (player.getData(EuruModVariables.PLAYER_VARIABLES).playerGP_Used > player.getData(EuruModVariables.PLAYER_VARIABLES).playerGP_Total) {
												setBlockNBTLogic(world, x, y, z, "tmGP", true);
											} else {
												setBlockNBTLogic(world, x, y, z, "tmGP", false);
												setBlockNBTNumber(world, x, y, z, "cProgress", (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "cProgress") + 1 * mult));
											}
										} else {
											setBlockNBTText(world, x, y, z, "currentItem", "N/A");
										}
									} else {
										if ((itemFromBlockInventory(world, BlockPos.containing(x, y, z), 0).copy()).getItem() == BuiltInRegistries.ITEM
												.get(ResourceLocation.parse(((getBlockNBTString(world, BlockPos.containing(x, y, z), "currentItem"))).toLowerCase(java.util.Locale.ENGLISH)))
												&& itemFromBlockInventory(world, BlockPos.containing(x, y, z), 0).getCount() != 0) {
											if (player.getData(EuruModVariables.PLAYER_VARIABLES).playerGP_Used > player.getData(EuruModVariables.PLAYER_VARIABLES).playerGP_Total) {
												setBlockNBTLogic(world, x, y, z, "tmGP", true);
											} else {
												setBlockNBTLogic(world, x, y, z, "tmGP", false);
												setBlockNBTNumber(world, x, y, z, "cProgress", 0);
												setBlockNBTText(world, x, y, z, "currentOutput", "");
												if (world instanceof ILevelExtension _ext && _ext.getCapability(Capabilities.ItemHandler.BLOCK, BlockPos.containing(x, y, z), null) instanceof IItemHandlerModifiable _itemHandlerModifiable) {
													ItemStack _setstack = (itemFromBlockInventory(world, BlockPos.containing(x, y, z), 0).copy()).copy();
													_setstack.setCount(itemFromBlockInventory(world, BlockPos.containing(x, y, z), 0).getCount() - 1);
													_itemHandlerModifiable.setStackInSlot(0, _setstack);
												}
												if (world instanceof ILevelExtension _ext && _ext.getCapability(Capabilities.ItemHandler.BLOCK, BlockPos.containing(x, y, z), null) instanceof IItemHandlerModifiable _itemHandlerModifiable) {
													ItemStack _setstack = new ItemStack(BuiltInRegistries.ITEM.get(ResourceLocation.parse((output).toLowerCase(java.util.Locale.ENGLISH)))).copy();
													_setstack.setCount(itemFromBlockInventory(world, BlockPos.containing(x, y, z), 1).getCount() + 1);
													_itemHandlerModifiable.setStackInSlot(1, _setstack);
												}
											}
										} else {
											setBlockNBTText(world, x, y, z, "currentItem", "N/A");
										}
									}
								} else if (itemFromBlockInventory(world, BlockPos.containing(x, y, z), 1).getCount() >= new ItemStack(BuiltInRegistries.ITEM.get(ResourceLocation.parse((output).toLowerCase(java.util.Locale.ENGLISH))))
										.getMaxStackSize()) {
									setBlockNBTLogic(world, x, y, z, "tmI", true);
								} else if (!((itemFromBlockInventory(world, BlockPos.containing(x, y, z), 1).copy()).getItem() == BuiltInRegistries.ITEM.get(ResourceLocation.parse((output).toLowerCase(java.util.Locale.ENGLISH))))
										&& itemFromBlockInventory(world, BlockPos.containing(x, y, z), 1).getCount() > 0) {
									setBlockNBTLogic(world, x, y, z, "nsI", true);
								} else {
									setBlockNBTLogic(world, x, y, z, "nsI", true);
								}
							} else {
								setBlockNBTLogic(world, x, y, z, "tmGP", true);
								if (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "cProgress") > 0) {
									setBlockNBTNumber(world, x, y, z, "cProgress", (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "cProgress") - 0.2));
									setBlockNBTText(world, x, y, z, "currentItem", "N/A");
									setBlockNBTText(world, x, y, z, "currentOutput", "");
								} else {
									setBlockNBTNumber(world, x, y, z, "wait_time", 0);
								}
							}
						} else {
							if (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "cProgress") > 0) {
								setBlockNBTText(world, x, y, z, "currentItem", "N/A");
								setBlockNBTText(world, x, y, z, "currentOutput", "");
								setBlockNBTNumber(world, x, y, z, "cProgress", (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "cProgress") - 0.2));
							} else {
								setBlockNBTNumber(world, x, y, z, "wait_time", 0);
							}
						}
					}
				} else {
					setBlockNBTLogic(world, x, y, z, "tmGP", false);
					if (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "cProgress") > 0) {
						setBlockNBTText(world, x, y, z, "currentItem", "N/A");
						setBlockNBTText(world, x, y, z, "currentOutput", "");
						setBlockNBTNumber(world, x, y, z, "cProgress", (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "cProgress") - 0.2));
					} else {
						setBlockNBTNumber(world, x, y, z, "wait_time", 0);
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