package net.wavedk.extrautilitiesreutilized.procedures;

import net.wavedk.extrautilitiesreutilized.init.EuruModBlocks;

import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.common.extensions.ILevelExtension;
import net.neoforged.neoforge.capabilities.Capabilities;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;

import java.util.regex.Pattern;

public class WRFHCItemExtensionValueProcedure {
	public static double execute(ItemStack itemstack) {
		double cX = 0;
		double cY = 0;
		double cZ = 0;
		double cN = 0;
		double returnNumber = 0;
		InteractionResult dimensionId = InteractionResult.PASS;
		boolean isLoading = false;
		boolean foundBlock = false;
		returnNumber = 0;
		if (!(itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getString("syncedBlock")).isEmpty()) {
			cN = 1;
			String _splitContent25 = Pattern.quote(",");
			String _toSplit25 = (itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getString("syncedBlock"));
			String[] _array25 = _toSplit25.split(_splitContent25);
			if (_array25.length != 0) {
				for (String stringiterator : _array25) {
					if (cN == 1) {
						cX = new Object() {
							double convert(String s) {
								try {
									return Double.parseDouble(s.trim());
								} catch (Exception e) {
								}
								return 0;
							}
						}.convert(stringiterator);
					} else if (cN == 2) {
						cY = new Object() {
							double convert(String s) {
								try {
									return Double.parseDouble(s.trim());
								} catch (Exception e) {
								}
								return 0;
							}
						}.convert(stringiterator);
					} else if (cN == 3) {
						cZ = new Object() {
							double convert(String s) {
								try {
									return Double.parseDouble(s.trim());
								} catch (Exception e) {
								}
								return 0;
							}
						}.convert(stringiterator);
					} else if (cN == 4) {
						foundBlock = false;
						if (net.neoforged.neoforge.server.ServerLifecycleHooks.getCurrentServer() != null) {
							for (net.minecraft.server.level.ServerLevel _serverWorld : net.neoforged.neoforge.server.ServerLifecycleHooks.getCurrentServer().getAllLevels()) {
								final net.minecraft.world.level.LevelAccessor world = _serverWorld;
								BlockPos pos = new BlockPos((int) cX, (int) cY, (int) cZ);
								isLoading = world.hasChunkAt(pos);
								if ((((Level) world).dimension().location().toString()).equals(stringiterator)) {
									if (isLoading) {
										if ((world.getBlockState(BlockPos.containing(cX, cY, cZ))).getBlock() == EuruModBlocks.WIRELESS_BATTERY.get()
												&& (getBlockNBTString(world, BlockPos.containing(cX, cY, cZ), "syncedBlock_id")).contains(itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getString("syncedBlock_id"))) {
											foundBlock = true;
											if (getEnergyStored(world, BlockPos.containing(cX, cY, cZ), null) >= itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getDouble("required_fe_per_tick")) {
												if (world instanceof ILevelExtension _ext) {
													IEnergyStorage _entityStorage = _ext.getCapability(Capabilities.EnergyStorage.BLOCK, BlockPos.containing(cX, cY, cZ), null);
													if (_entityStorage != null)
														_entityStorage.extractEnergy((int) itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getDouble("required_fe_per_tick"), false);
												}
												returnNumber = 1;
											}
										}
									} else {
										foundBlock = true;
									}
								}
							}
						}
						if (!foundBlock) {
							{
								final String _tagName = "syncedBlock_id";
								final String _tagValue = "";
								CustomData.update(DataComponents.CUSTOM_DATA, itemstack, tag -> tag.putString(_tagName, _tagValue));
							}
							{
								final String _tagName = "syncedBlock";
								final String _tagValue = "";
								CustomData.update(DataComponents.CUSTOM_DATA, itemstack, tag -> tag.putString(_tagName, _tagValue));
							}
						}
					}
					cN = cN + 1;
				}
			} else {
				String stringiterator = _toSplit25;
				for (int _yourmother25 = 0; _yourmother25 < 1; _yourmother25++) {
					if (cN == 1) {
						cX = new Object() {
							double convert(String s) {
								try {
									return Double.parseDouble(s.trim());
								} catch (Exception e) {
								}
								return 0;
							}
						}.convert(stringiterator);
					} else if (cN == 2) {
						cY = new Object() {
							double convert(String s) {
								try {
									return Double.parseDouble(s.trim());
								} catch (Exception e) {
								}
								return 0;
							}
						}.convert(stringiterator);
					} else if (cN == 3) {
						cZ = new Object() {
							double convert(String s) {
								try {
									return Double.parseDouble(s.trim());
								} catch (Exception e) {
								}
								return 0;
							}
						}.convert(stringiterator);
					} else if (cN == 4) {
						foundBlock = false;
						if (net.neoforged.neoforge.server.ServerLifecycleHooks.getCurrentServer() != null) {
							for (net.minecraft.server.level.ServerLevel _serverWorld : net.neoforged.neoforge.server.ServerLifecycleHooks.getCurrentServer().getAllLevels()) {
								final net.minecraft.world.level.LevelAccessor world = _serverWorld;
								BlockPos pos = new BlockPos((int) cX, (int) cY, (int) cZ);
								isLoading = world.hasChunkAt(pos);
								if ((((Level) world).dimension().location().toString()).equals(stringiterator)) {
									if (isLoading) {
										if ((world.getBlockState(BlockPos.containing(cX, cY, cZ))).getBlock() == EuruModBlocks.WIRELESS_BATTERY.get()
												&& (getBlockNBTString(world, BlockPos.containing(cX, cY, cZ), "syncedBlock_id")).contains(itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getString("syncedBlock_id"))) {
											foundBlock = true;
											if (getEnergyStored(world, BlockPos.containing(cX, cY, cZ), null) >= itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getDouble("required_fe_per_tick")) {
												if (world instanceof ILevelExtension _ext) {
													IEnergyStorage _entityStorage = _ext.getCapability(Capabilities.EnergyStorage.BLOCK, BlockPos.containing(cX, cY, cZ), null);
													if (_entityStorage != null)
														_entityStorage.extractEnergy((int) itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getDouble("required_fe_per_tick"), false);
												}
												returnNumber = 1;
											}
										}
									} else {
										foundBlock = true;
									}
								}
							}
						}
						if (!foundBlock) {
							{
								final String _tagName = "syncedBlock_id";
								final String _tagValue = "";
								CustomData.update(DataComponents.CUSTOM_DATA, itemstack, tag -> tag.putString(_tagName, _tagValue));
							}
							{
								final String _tagName = "syncedBlock";
								final String _tagValue = "";
								CustomData.update(DataComponents.CUSTOM_DATA, itemstack, tag -> tag.putString(_tagName, _tagValue));
							}
						}
					}
					cN = cN + 1;
				}
			}
		}
		return returnNumber;
	}

	private static String getBlockNBTString(LevelAccessor world, BlockPos pos, String tag) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity != null)
			return blockEntity.getPersistentData().getString(tag);
		return "";
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