package net.wavedk.extrautilitiesreutilized.procedures;

import org.apache.commons.lang3.function.FailableFunction;

import net.wavedk.extrautilitiesreutilized.network.EuruModVariables;
import net.wavedk.extrautilitiesreutilized.init.EuruModItems;
import net.wavedk.extrautilitiesreutilized.init.EuruModBlocks;
import net.wavedk.extrautilitiesreutilized.block.entity.ElectricFurnaceBlockEntity;

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
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.item.crafting.RecipeType;
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

public class ElectricFurnaceOnTickUpdateProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, BlockState blockstate) {
		String getDep = "";
		Entity player = null;
		File cfil = new File("");
		com.google.gson.JsonObject obj = new com.google.gson.JsonObject();
		com.google.gson.JsonObject catobj = new com.google.gson.JsonObject();
		com.google.gson.JsonObject bobj = new com.google.gson.JsonObject();
		double feNeeded = 0;
		double gpNeeded = 0;
		double energyRemove = 0;
		double mult = 0;
		double updateGP = 0;
		getDep = ((x + "" + y) + "" + z) + "" + (blockstate + "" + (world instanceof Level _lvl ? _lvl.dimension() : (world instanceof WorldGenLevel _wgl ? _wgl.getLevel().dimension() : Level.OVERWORLD)));
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
		mult = 1;
		if ((itemFromBlockInventory(world, BlockPos.containing(x, y, z), 2).copy()).getItem() == EuruModItems.SPEED_UPGRADE.get()) {
			mult = mult + itemFromBlockInventory(world, BlockPos.containing(x, y, z), 2).getCount() / 4d;
		} else if ((itemFromBlockInventory(world, BlockPos.containing(x, y, z), 2).copy()).getItem() == EuruModItems.MAGICAL_SPEED_UPGRADE.get()) {
			mult = mult + itemFromBlockInventory(world, BlockPos.containing(x, y, z), 2).getCount() / 2d;
		} else if ((itemFromBlockInventory(world, BlockPos.containing(x, y, z), 2).copy()).getItem() == EuruModItems.ULTIMATE_SPEED_UPGRADE.get()) {
			mult = mult + itemFromBlockInventory(world, BlockPos.containing(x, y, z), 2).getCount() / 1.25;
		}
		updateGP = updateGP + itemFromBlockInventory(world, BlockPos.containing(x, y, z), 2).getCount();
		if (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "fe_needed") == 0 || getBlockNBTNumber(world, BlockPos.containing(x, y, z), "wait_time") == 0 || getBlockNBTNumber(world, BlockPos.containing(x, y, z), "gp_needed") == 0) {
			cfil = new File((FMLPaths.GAMEDIR.get().toString() + "/config/euru/"), File.separator + "euru_unified_config.json");
			{
				try {
					BufferedReader bufferedReader = new BufferedReader(new FileReader(cfil));
					StringBuilder jsonstringbuilder = new StringBuilder();
					String line;
					while ((line = bufferedReader.readLine()) != null) {
						jsonstringbuilder.append(line);
					}
					bufferedReader.close();
					obj = new com.google.gson.Gson().fromJson(jsonstringbuilder.toString(), com.google.gson.JsonObject.class);
					catobj = obj.get("machines").getAsJsonObject();
					bobj = catobj.get((BuiltInRegistries.ITEM.getKey(EuruModBlocks.ELECTRIC_FURNACE.get().asItem()).toString())).getAsJsonObject();
					if (!world.isClientSide()) {
						BlockPos _bp = BlockPos.containing(x, y, z);
						BlockEntity _blockEntity = world.getBlockEntity(_bp);
						BlockState _bs = world.getBlockState(_bp);
						if (_blockEntity != null) {
							_blockEntity.getPersistentData().putDouble("fe_needed", bobj.get("fe_needed").getAsDouble());
							_blockEntity.getPersistentData().putDouble("gp_needed", bobj.get("gp_needed").getAsDouble());
							_blockEntity.getPersistentData().putDouble("wait_time", bobj.get("wait_time").getAsDouble());
						}
						if (world instanceof Level _level)
							_level.sendBlockUpdated(_bp, _bs, _bs, 3);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		feNeeded = getBlockNBTNumber(world, BlockPos.containing(x, y, z), "fe_needed");
		gpNeeded = getBlockNBTNumber(world, BlockPos.containing(x, y, z), "gp_needed");
		if ((getBlockNBTString(world, BlockPos.containing(x, y, z), "currentItem")).equals("") || getBlockNBTNumber(world, BlockPos.containing(x, y, z), "cProgress") == 0
				|| (getBlockNBTString(world, BlockPos.containing(x, y, z), "currentOutput")).equals("")) {
			energyRemove = (feNeeded / getBlockNBTNumber(world, BlockPos.containing(x, y, z), "wait_time")) * mult;
			if (world instanceof Level _level43 && _level43.getRecipeManager().getRecipeFor(RecipeType.SMELTING, new SingleRecipeInput((itemFromBlockInventory(world, BlockPos.containing(x, y, z), 0).copy())), _level43).isPresent()) {
				if (getEnergyStored(world, BlockPos.containing(x, y, z), null) >= energyRemove) {
					if (!(player.getData(EuruModVariables.PLAYER_VARIABLES).playerGP_Used > player.getData(EuruModVariables.PLAYER_VARIABLES).playerGP_Total)) {
						if (player.getData(EuruModVariables.PLAYER_VARIABLES).playerGPChecking) {
							updateGP = updateGP + gpNeeded;
							if (!world.isClientSide()) {
								BlockPos _bp = BlockPos.containing(x, y, z);
								BlockEntity _blockEntity = world.getBlockEntity(_bp);
								BlockState _bs = world.getBlockState(_bp);
								if (_blockEntity != null) {
									_blockEntity.getPersistentData().putString("currentItem", (BuiltInRegistries.ITEM.getKey((itemFromBlockInventory(world, BlockPos.containing(x, y, z), 0).copy()).getItem()).toString()));
									_blockEntity.getPersistentData().putString("currentOutput",
											(BuiltInRegistries.ITEM.getKey((world instanceof Level _lvlSmeltResult
													? _lvlSmeltResult.getRecipeManager().getRecipeFor(RecipeType.SMELTING, new SingleRecipeInput((itemFromBlockInventory(world, BlockPos.containing(x, y, z), 0).copy())), _lvlSmeltResult)
															.map(recipe -> recipe.value().getResultItem(_lvlSmeltResult.registryAccess()).copy()).orElse(ItemStack.EMPTY)
													: ItemStack.EMPTY).getItem()).toString()));
									_blockEntity.getPersistentData().putDouble("cProgress", (1 * mult));
								}
								if (world instanceof Level _level)
									_level.sendBlockUpdated(_bp, _bs, _bs, 3);
							}
							if (world.getBlockEntity(new BlockPos((int) x, (int) y, (int) z)) instanceof ElectricFurnaceBlockEntity be) {
								be.removeEnergy((int) energyRemove);
							}
							if (player.getData(EuruModVariables.PLAYER_VARIABLES).playerGPChecking) {
								updateGP = updateGP + gpNeeded;
							}
							{
								BlockPos _pos = BlockPos.containing(x, y, z);
								BlockState _bs = world.getBlockState(_pos);
								if (_bs.getBlock().getStateDefinition().getProperty("on") instanceof BooleanProperty _booleanProp)
									world.setBlock(_pos, _bs.setValue(_booleanProp, true), 3);
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
				}
			}
		} else if ((BuiltInRegistries.ITEM.getKey((itemFromBlockInventory(world, BlockPos.containing(x, y, z), 0).copy()).getItem()).toString()).equals(getBlockNBTString(world, BlockPos.containing(x, y, z), "currentItem"))) {
			energyRemove = (feNeeded / getBlockNBTNumber(world, BlockPos.containing(x, y, z), "wait_time")) * mult;
			if ((BuiltInRegistries.ITEM.getKey((itemFromBlockInventory(world, BlockPos.containing(x, y, z), 1).copy()).getItem()).toString()).equals(getBlockNBTString(world, BlockPos.containing(x, y, z), "currentOutput"))
					&& (itemFromBlockInventory(world, BlockPos.containing(x, y, z), 1).copy()).getCount() + (world instanceof Level _lvlSmeltResult
							? _lvlSmeltResult.getRecipeManager().getRecipeFor(RecipeType.SMELTING, new SingleRecipeInput((itemFromBlockInventory(world, BlockPos.containing(x, y, z), 0).copy())), _lvlSmeltResult)
									.map(recipe -> recipe.value().getResultItem(_lvlSmeltResult.registryAccess()).copy()).orElse(ItemStack.EMPTY)
							: ItemStack.EMPTY).getCount() <= (itemFromBlockInventory(world, BlockPos.containing(x, y, z), 1).copy()).getMaxStackSize()
					|| (itemFromBlockInventory(world, BlockPos.containing(x, y, z), 1).copy()).getCount() == 0) {
				if (getEnergyStored(world, BlockPos.containing(x, y, z), null) >= energyRemove) {
					if (!(player.getData(EuruModVariables.PLAYER_VARIABLES).playerGP_Used > player.getData(EuruModVariables.PLAYER_VARIABLES).playerGP_Total)) {
						if (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "cProgress") >= getBlockNBTNumber(world, BlockPos.containing(x, y, z), "wait_time")) {
							if (world instanceof ILevelExtension _ext && _ext.getCapability(Capabilities.ItemHandler.BLOCK, BlockPos.containing(x, y, z), null) instanceof IItemHandlerModifiable _itemHandlerModifiable) {
								ItemStack _setstack = (itemFromBlockInventory(world, BlockPos.containing(x, y, z), 0).copy()).copy();
								_setstack.setCount((itemFromBlockInventory(world, BlockPos.containing(x, y, z), 0).copy()).getCount() - 1);
								_itemHandlerModifiable.setStackInSlot(0, _setstack);
							}
							if (world instanceof ILevelExtension _ext && _ext.getCapability(Capabilities.ItemHandler.BLOCK, BlockPos.containing(x, y, z), null) instanceof IItemHandlerModifiable _itemHandlerModifiable) {
								ItemStack _setstack = new ItemStack(BuiltInRegistries.ITEM.get(ResourceLocation.parse(((getBlockNBTString(world, BlockPos.containing(x, y, z), "currentOutput"))).toLowerCase(java.util.Locale.ENGLISH)))).copy();
								_setstack
										.setCount(
												(itemFromBlockInventory(world, BlockPos.containing(x, y, z), 1).copy()).getCount() + (world instanceof Level _lvlSmeltResult
														? _lvlSmeltResult.getRecipeManager()
																.getRecipeFor(RecipeType.SMELTING,
																		new SingleRecipeInput(new ItemStack(
																				BuiltInRegistries.ITEM.get(ResourceLocation.parse(((getBlockNBTString(world, BlockPos.containing(x, y, z), "currentItem"))).toLowerCase(java.util.Locale.ENGLISH))))),
																		_lvlSmeltResult)
																.map(recipe -> recipe.value().getResultItem(_lvlSmeltResult.registryAccess()).copy()).orElse(ItemStack.EMPTY)
														: ItemStack.EMPTY).getCount());
								_itemHandlerModifiable.setStackInSlot(1, _setstack);
							}
							if (!world.isClientSide()) {
								BlockPos _bp = BlockPos.containing(x, y, z);
								BlockEntity _blockEntity = world.getBlockEntity(_bp);
								BlockState _bs = world.getBlockState(_bp);
								if (_blockEntity != null) {
									_blockEntity.getPersistentData().putDouble("cProgress", 0);
									_blockEntity.getPersistentData().putString("currentOutput", "");
									_blockEntity.getPersistentData().putString("currentItem", "");
								}
								if (world instanceof Level _level)
									_level.sendBlockUpdated(_bp, _bs, _bs, 3);
							}
							if (world.getBlockEntity(new BlockPos((int) x, (int) y, (int) z)) instanceof ElectricFurnaceBlockEntity be) {
								be.removeEnergy((int) energyRemove);
							}
						} else {
							{
								BlockPos _pos = BlockPos.containing(x, y, z);
								BlockState _bs = world.getBlockState(_pos);
								if (_bs.getBlock().getStateDefinition().getProperty("on") instanceof BooleanProperty _booleanProp)
									world.setBlock(_pos, _bs.setValue(_booleanProp, true), 3);
							}
							if (world.getBlockEntity(new BlockPos((int) x, (int) y, (int) z)) instanceof ElectricFurnaceBlockEntity be) {
								be.removeEnergy((int) energyRemove);
							}
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
			if (player.getData(EuruModVariables.PLAYER_VARIABLES).playerGPChecking) {
				updateGP = updateGP + gpNeeded;
			}
		} else {
			if (!world.isClientSide()) {
				BlockPos _bp = BlockPos.containing(x, y, z);
				BlockEntity _blockEntity = world.getBlockEntity(_bp);
				BlockState _bs = world.getBlockState(_bp);
				if (_blockEntity != null) {
					_blockEntity.getPersistentData().putDouble("cProgress", 0);
					_blockEntity.getPersistentData().putString("currentItem", "");
				}
				if (world instanceof Level _level)
					_level.sendBlockUpdated(_bp, _bs, _bs, 3);
			}
		}
		if (player.getData(EuruModVariables.PLAYER_VARIABLES).playerGPChecking) {
			{
				EuruModVariables.PlayerVariables _vars = player.getData(EuruModVariables.PLAYER_VARIABLES);
				_vars.playerGP_Used_Update = player.getData(EuruModVariables.PLAYER_VARIABLES).playerGP_Used_Update + updateGP;
				_vars.markSyncDirty();
			}
		}
		if (getPropertyByName(blockstate, "on") instanceof BooleanProperty _getbp98 && blockstate.getValue(_getbp98) && getBlockNBTNumber(world, BlockPos.containing(x, y, z), "cProgress") == 0) {
			if (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "onCounter") > 4) {
				{
					BlockPos _pos = BlockPos.containing(x, y, z);
					BlockState _bs = world.getBlockState(_pos);
					if (_bs.getBlock().getStateDefinition().getProperty("on") instanceof BooleanProperty _booleanProp)
						world.setBlock(_pos, _bs.setValue(_booleanProp, false), 3);
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
			} else {
				if (!world.isClientSide()) {
					BlockPos _bp = BlockPos.containing(x, y, z);
					BlockEntity _blockEntity = world.getBlockEntity(_bp);
					BlockState _bs = world.getBlockState(_bp);
					if (_blockEntity != null) {
						_blockEntity.getPersistentData().putDouble("onCounter", (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "onCounter") + 1));
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
					_blockEntity.getPersistentData().putDouble("onCounter", 0);
				}
				if (world instanceof Level _level)
					_level.sendBlockUpdated(_bp, _bs, _bs, 3);
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

	private static ItemStack itemFromBlockInventory(LevelAccessor world, BlockPos pos, int slot) {
		if (world instanceof ILevelExtension ext) {
			IItemHandler itemHandler = ext.getCapability(Capabilities.ItemHandler.BLOCK, pos, null);
			if (itemHandler != null)
				return itemHandler.getStackInSlot(slot);
		}
		return ItemStack.EMPTY;
	}

	private static double getBlockNBTNumber(LevelAccessor world, BlockPos pos, String tag) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity != null)
			return blockEntity.getPersistentData().getDouble(tag);
		return -1;
	}

	public static int getEnergyStored(LevelAccessor level, BlockPos pos, Direction direction) {
		if (level instanceof ILevelExtension levelExtension) {
			IEnergyStorage energyStorage = levelExtension.getCapability(Capabilities.EnergyStorage.BLOCK, pos, direction);
			if (energyStorage != null)
				return energyStorage.getEnergyStored();
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
}