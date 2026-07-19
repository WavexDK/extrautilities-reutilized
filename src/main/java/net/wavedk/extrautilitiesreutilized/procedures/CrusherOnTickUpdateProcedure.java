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

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
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

public class CrusherOnTickUpdateProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		Entity player = null;
		double mult = 0;
		double cost = 0;
		double cNum = 0;
		double feSpeed = 0;
		File cfile = new File("");
		com.google.gson.JsonObject obj = new com.google.gson.JsonObject();
		com.google.gson.JsonObject cobj = new com.google.gson.JsonObject();
		com.google.gson.JsonObject bobj = new com.google.gson.JsonObject();
		com.google.gson.JsonObject iobj = new com.google.gson.JsonObject();
		com.google.gson.JsonArray rlist = new com.google.gson.JsonArray();
		boolean canCrush = false;
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
				if ((getBlockNBTString(world, BlockPos.containing(x, y, z), "currentItem")).isEmpty()
						|| (BuiltInRegistries.ITEM.getKey(BuiltInRegistries.ITEM.get(ResourceLocation.parse(((getBlockNBTString(world, BlockPos.containing(x, y, z), "currentItem"))).toLowerCase(java.util.Locale.ENGLISH)))).toString())
								.equals("minecraft:air")) {
					if (itemFromBlockInventory(world, BlockPos.containing(x, y, z), 0).getCount() != 0) {
						cfile = new File((FMLPaths.GAMEDIR.get().toString() + "/config/euru/"), File.separator + "euru_unified_config.json");
						{
							try {
								BufferedReader bufferedReader = new BufferedReader(new FileReader(cfile));
								StringBuilder jsonstringbuilder = new StringBuilder();
								String line;
								while ((line = bufferedReader.readLine()) != null) {
									jsonstringbuilder.append(line);
								}
								bufferedReader.close();
								obj = new com.google.gson.Gson().fromJson(jsonstringbuilder.toString(), com.google.gson.JsonObject.class);
								cobj = obj.get("machines").getAsJsonObject();
								bobj = cobj.get((BuiltInRegistries.ITEM.getKey(EuruModBlocks.CRUSHER.get().asItem()).toString())).getAsJsonObject();
								rlist = bobj.get("recipeList").getAsJsonArray();
								if (!(getBlockNBTString(world, BlockPos.containing(x, y, z), "lastCheckedItemID")).equals(BuiltInRegistries.ITEM.getKey((itemFromBlockInventory(world, BlockPos.containing(x, y, z), 0).copy()).getItem()).toString())) {
									if (!world.isClientSide()) {
										BlockPos _bp = BlockPos.containing(x, y, z);
										BlockEntity _blockEntity = world.getBlockEntity(_bp);
										BlockState _bs = world.getBlockState(_bp);
										if (_blockEntity != null) {
											_blockEntity.getPersistentData().putString("lastCheckedItemID", (BuiltInRegistries.ITEM.getKey((itemFromBlockInventory(world, BlockPos.containing(x, y, z), 0).copy()).getItem()).toString()));
										}
										if (world instanceof Level _level)
											_level.sendBlockUpdated(_bp, _bs, _bs, 3);
									}
									cNum = 0;
									for (int index14 = 0; index14 < (int) rlist.size(); index14++) {
										if ((BuiltInRegistries.ITEM.getKey((itemFromBlockInventory(world, BlockPos.containing(x, y, z), 0).copy()).getItem()).toString()).equals(rlist.get((int) cNum).getAsString())) {
											iobj = bobj.get(rlist.get((int) cNum).getAsString()).getAsJsonObject();
											if (getEnergyStored(world, BlockPos.containing(x, y, z), null) >= iobj.get("fe_required").getAsDouble()) {
												if (!world.isClientSide()) {
													BlockPos _bp = BlockPos.containing(x, y, z);
													BlockEntity _blockEntity = world.getBlockEntity(_bp);
													BlockState _bs = world.getBlockState(_bp);
													if (_blockEntity != null) {
														_blockEntity.getPersistentData().putString("currentItem", (BuiltInRegistries.ITEM.getKey((itemFromBlockInventory(world, BlockPos.containing(x, y, z), 0).copy()).getItem()).toString()));
														_blockEntity.getPersistentData().putString("resultItem", iobj.get("result").getAsString());
														_blockEntity.getPersistentData().putString("lastCheckedItemID", "");
														_blockEntity.getPersistentData().putDouble("cProgress", 0);
														_blockEntity.getPersistentData().putDouble("wait_time", iobj.get("wait_time").getAsDouble());
														_blockEntity.getPersistentData().putDouble("fe_required", iobj.get("fe_required").getAsDouble());
														_blockEntity.getPersistentData().putDouble("gp_required", iobj.get("gp_required").getAsDouble());
													}
													if (world instanceof Level _level)
														_level.sendBlockUpdated(_bp, _bs, _bs, 3);
												}
												canCrush = true;
												break;
											} else {
												if (!world.isClientSide()) {
													BlockPos _bp = BlockPos.containing(x, y, z);
													BlockEntity _blockEntity = world.getBlockEntity(_bp);
													BlockState _bs = world.getBlockState(_bp);
													if (_blockEntity != null) {
														_blockEntity.getPersistentData().putString("lastCheckedItemID", "");
													}
													if (world instanceof Level _level)
														_level.sendBlockUpdated(_bp, _bs, _bs, 3);
												}
												break;
											}
										}
										cNum = cNum + 1;
									}
								}
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				} else {
					if (itemFromBlockInventory(world, BlockPos.containing(x, y, z), 1).getCount() == 0 || (itemFromBlockInventory(world, BlockPos.containing(x, y, z), 1).copy()).getItem() == BuiltInRegistries.ITEM
							.get(ResourceLocation.parse(((getBlockNBTString(world, BlockPos.containing(x, y, z), "resultItem"))).toLowerCase(java.util.Locale.ENGLISH)))
							&& 1 + itemFromBlockInventory(world, BlockPos.containing(x, y, z), 1).getCount() <= getBlockInventorySlotStackLimit(world, BlockPos.containing(x, y, z), 1)) {
						if (getEnergyStored(world, BlockPos.containing(x, y, z), null) >= getBlockNBTNumber(world, BlockPos.containing(x, y, z), "fe_required") / getBlockNBTNumber(world, BlockPos.containing(x, y, z), "wait_time")) {
							canCrush = true;
						}
					}
				}
				if (canCrush) {
					if (player.getData(EuruModVariables.PLAYER_VARIABLES).playerGPChecking) {
						{
							EuruModVariables.PlayerVariables _vars = player.getData(EuruModVariables.PLAYER_VARIABLES);
							_vars.playerGP_Used_Update = player.getData(EuruModVariables.PLAYER_VARIABLES).playerGP_Used_Update + getBlockNBTNumber(world, BlockPos.containing(x, y, z), "gp_required") + cost;
							_vars.markSyncDirty();
						}
					}
					if (player.getData(EuruModVariables.PLAYER_VARIABLES).playerGP_Total >= player.getData(EuruModVariables.PLAYER_VARIABLES).playerGP_Used) {
						if (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "cProgress") >= getBlockNBTNumber(world, BlockPos.containing(x, y, z), "wait_time")) {
							if (world instanceof ILevelExtension _ext && _ext.getCapability(Capabilities.ItemHandler.BLOCK, BlockPos.containing(x, y, z), null) instanceof IItemHandlerModifiable _itemHandlerModifiable) {
								int _slotid = 0;
								ItemStack _stk = _itemHandlerModifiable.getStackInSlot(_slotid).copy();
								_stk.shrink(1);
								_itemHandlerModifiable.setStackInSlot(_slotid, _stk);
							}
							if (world instanceof ILevelExtension _ext && _ext.getCapability(Capabilities.ItemHandler.BLOCK, BlockPos.containing(x, y, z), null) instanceof IItemHandlerModifiable _itemHandlerModifiable) {
								ItemStack _setstack = new ItemStack(BuiltInRegistries.ITEM.get(ResourceLocation.parse(((getBlockNBTString(world, BlockPos.containing(x, y, z), "resultItem"))).toLowerCase(java.util.Locale.ENGLISH)))).copy();
								_setstack.setCount(1 + itemFromBlockInventory(world, BlockPos.containing(x, y, z), 1).getCount());
								_itemHandlerModifiable.setStackInSlot(1, _setstack);
							}
							if (!world.isClientSide()) {
								BlockPos _bp = BlockPos.containing(x, y, z);
								BlockEntity _blockEntity = world.getBlockEntity(_bp);
								BlockState _bs = world.getBlockState(_bp);
								if (_blockEntity != null) {
									_blockEntity.getPersistentData().putString("currentItem", "");
									_blockEntity.getPersistentData().putDouble("cProgress", 0);
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
									_blockEntity.getPersistentData().putDouble("cProgress", (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "cProgress") + 1 * mult));
								}
								if (world instanceof Level _level)
									_level.sendBlockUpdated(_bp, _bs, _bs, 3);
							}
							feSpeed = (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "fe_required") / getBlockNBTNumber(world, BlockPos.containing(x, y, z), "wait_time")) * mult;
							if (world.getBlockEntity(new BlockPos((int) x, (int) y, (int) z)) instanceof IEnergyReceiver be) {
								be.removeEnergy((int) feSpeed);
							}
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

	private static int getBlockInventorySlotStackLimit(LevelAccessor world, BlockPos pos, int slotId) {
		if (world instanceof ILevelExtension ext) {
			IItemHandler itemHandler = ext.getCapability(Capabilities.ItemHandler.BLOCK, pos, null);
			if (itemHandler != null && slotId >= 0 && slotId < itemHandler.getSlots())
				return itemHandler.getSlotLimit(slotId);
		}
		return 0;
	}
}