package net.wavedk.extrautilitiesreutilized.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.chat.Component;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.component.DataComponents;

import java.util.regex.Pattern;

public class TapeMeasureItemInHandTickProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity, ItemStack itemstack) {
		if (entity == null)
			return;
		double cX = 0;
		double cY = 0;
		double cZ = 0;
		double cN = 0;
		double cX2 = 0;
		double cY2 = 0;
		double cZ2 = 0;
		double cN2 = 0;
		double distance = 0;
		String si2 = "";
		if (!(itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getString("syncedBlock")).isEmpty()) {
			if ((itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getString("linked_dimension")).equals(entity.level().dimension().location().toString())) {
				cN = 1;
				if (!(itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getString("syncedBlock_secondary")).isEmpty()) {
					cN2 = 1;
					String _splitContent12 = Pattern.quote(",");
					String _toSplit12 = (itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getString("syncedBlock"));
					String[] _array12 = _toSplit12.split(_splitContent12);
					if (_array12.length != 0) {
						for (String stringiterator : _array12) {
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
							}
							cN = cN + 1;
						}
					} else {
						String stringiterator = _toSplit12;
						for (int _yourmother12 = 0; _yourmother12 < 1; _yourmother12++) {
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
							}
							cN = cN + 1;
						}
					}
					String _splitContent18 = Pattern.quote(",");
					String _toSplit18 = (itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getString("syncedBlock_secondary"));
					String[] _array18 = _toSplit18.split(_splitContent18);
					if (_array18.length != 0) {
						for (String stringiterator : _array18) {
							if (cN2 == 1) {
								cX2 = new Object() {
									double convert(String s) {
										try {
											return Double.parseDouble(s.trim());
										} catch (Exception e) {
										}
										return 0;
									}
								}.convert(stringiterator);
							} else if (cN2 == 2) {
								cY2 = new Object() {
									double convert(String s) {
										try {
											return Double.parseDouble(s.trim());
										} catch (Exception e) {
										}
										return 0;
									}
								}.convert(stringiterator);
							} else if (cN2 == 3) {
								cZ2 = new Object() {
									double convert(String s) {
										try {
											return Double.parseDouble(s.trim());
										} catch (Exception e) {
										}
										return 0;
									}
								}.convert(stringiterator);
							}
							cN2 = cN2 + 1;
						}
					} else {
						String stringiterator = _toSplit18;
						for (int _yourmother18 = 0; _yourmother18 < 1; _yourmother18++) {
							if (cN2 == 1) {
								cX2 = new Object() {
									double convert(String s) {
										try {
											return Double.parseDouble(s.trim());
										} catch (Exception e) {
										}
										return 0;
									}
								}.convert(stringiterator);
							} else if (cN2 == 2) {
								cY2 = new Object() {
									double convert(String s) {
										try {
											return Double.parseDouble(s.trim());
										} catch (Exception e) {
										}
										return 0;
									}
								}.convert(stringiterator);
							} else if (cN2 == 3) {
								cZ2 = new Object() {
									double convert(String s) {
										try {
											return Double.parseDouble(s.trim());
										} catch (Exception e) {
										}
										return 0;
									}
								}.convert(stringiterator);
							}
							cN2 = cN2 + 1;
						}
					}
					distance = 0 <= Math.round((new Vec3(cX2, cY2, cZ2)).distanceTo((new Vec3(cX, cY, cZ))) * 100) / 100d ? Math.round((new Vec3(cX2, cY2, cZ2)).distanceTo((new Vec3(cX, cY, cZ))) * 100) / 100d : 0;
					if (entity instanceof Player _player && !_player.level().isClientSide())
						_player.displayClientMessage(
								Component.literal(("Distance: " + (("" + distance).length() > 4 + ("" + Math.round(distance)).length() - 1 ? ("" + distance).substring(0, 4 + ("" + Math.round(distance)).length() - 1) : "" + distance))), true);
					if (!(itemstack.getEnchantmentLevel(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.UNBREAKING)) != 0)) {
						itemstack.enchant(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.UNBREAKING), 1);
					}
					LineBetweenProcedure.execute(world, cX2, cY2 + 1, cZ2, 0.3, cX, cY + 1, cZ);
				} else {
					if (!(itemstack.getEnchantmentLevel(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.UNBREAKING)) != 0)) {
						itemstack.enchant(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.UNBREAKING), 1);
					}
					String _splitContent39 = Pattern.quote(",");
					String _toSplit39 = (itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getString("syncedBlock"));
					String[] _array39 = _toSplit39.split(_splitContent39);
					if (_array39.length != 0) {
						for (String stringiterator : _array39) {
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
							}
							cN = cN + 1;
						}
					} else {
						String stringiterator = _toSplit39;
						for (int _yourmother39 = 0; _yourmother39 < 1; _yourmother39++) {
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
							}
							cN = cN + 1;
						}
					}
					cX2 = x;
					cY2 = y;
					cZ2 = z;
					distance = 0 <= Math.round((new Vec3(cX2, cY2, cZ2)).distanceTo((new Vec3(cX, cY, cZ))) * 100) / 100d ? Math.round((new Vec3(cX2, cY2, cZ2)).distanceTo((new Vec3(cX, cY, cZ))) * 100) / 100d : 0;
					if (entity instanceof Player _player && !_player.level().isClientSide())
						_player.displayClientMessage(
								Component.literal(("Distance: " + (("" + distance).length() > 4 + ("" + Math.round(distance)).length() - 1 ? ("" + distance).substring(0, 4 + ("" + Math.round(distance)).length() - 1) : "" + distance))), true);
					LineBetweenProcedure.execute(world, x, y + 1, z, 0.3, cX, cY + 1, cZ);
				}
			} else {
				{
					final String _tagName = "syncedBlock";
					final String _tagValue = "";
					CustomData.update(DataComponents.CUSTOM_DATA, itemstack, tag -> tag.putString(_tagName, _tagValue));
				}
			}
		} else {
			if (itemstack.getEnchantmentLevel(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.UNBREAKING)) != 0) {
				EnchantmentHelper.updateEnchantments(itemstack, mutableEnchantments -> mutableEnchantments.removeIf(enchantment -> enchantment.is(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.UNBREAKING))));
				if (entity instanceof Player _player && !_player.level().isClientSide())
					_player.displayClientMessage(Component.literal(""), true);
			}
		}
	}
}