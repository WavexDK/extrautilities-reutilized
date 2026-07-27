package net.wavedk.extrautilitiesreutilized.procedures;

import net.wavedk.extrautilitiesreutilized.init.EuruModItems;

import net.neoforged.fml.loading.FMLPaths;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.entity.Entity;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.BlockPos;

import java.io.IOException;
import java.io.FileReader;
import java.io.File;
import java.io.BufferedReader;

public class WateringCanItemInHandTickProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity, ItemStack itemstack) {
		if (entity == null)
			return;
		double cx = 0;
		double cy = 0;
		double cz = 0;
		double chanceNum = 0;
		double lX = 0;
		double lY = 0;
		double lZ = 0;
		double loopNumber = 0;
		BlockState cb = Blocks.AIR.defaultBlockState();
		File cfile = new File("");
		com.google.gson.JsonObject obj = new com.google.gson.JsonObject();
		com.google.gson.JsonObject cobj = new com.google.gson.JsonObject();
		com.google.gson.JsonObject iobj = new com.google.gson.JsonObject();
		if (itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getDouble("range-configUpdate-min") == 0
				|| itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getDouble("range-configUpdate-max") == 0
				|| itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getDouble("range-configUpdate-counter") == 0
				|| itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getDouble("range-configUpdate") == 0
				|| itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getDouble("chance_for_growtick") == 0
				|| itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getDouble("chance_for_plant_growth") == 0
				|| itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getDouble("number_of_plants_grown") == 0) {
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
					cobj = obj.get("general").getAsJsonObject();
					iobj = cobj.get((BuiltInRegistries.ITEM.getKey(EuruModItems.WATERING_CAN.get()).toString())).getAsJsonObject();
					{
						final String _tagName = "chance_for_growtick";
						final double _tagValue = iobj.get("chance_for_growtick").getAsDouble();
						CustomData.update(DataComponents.CUSTOM_DATA, itemstack, tag -> tag.putDouble(_tagName, _tagValue));
					}
					{
						final String _tagName = "chance_for_plant_growth";
						final double _tagValue = iobj.get("chance_for_plant_growth").getAsDouble();
						CustomData.update(DataComponents.CUSTOM_DATA, itemstack, tag -> tag.putDouble(_tagName, _tagValue));
					}
					{
						final String _tagName = "number_of_plants_grown";
						final double _tagValue = iobj.get("number_of_plants_grown").getAsDouble();
						CustomData.update(DataComponents.CUSTOM_DATA, itemstack, tag -> tag.putDouble(_tagName, _tagValue));
					}
					{
						final String _tagName = "range-configUpdate-min";
						final double _tagValue = obj.get("range-configUpdate-min").getAsDouble();
						CustomData.update(DataComponents.CUSTOM_DATA, itemstack, tag -> tag.putDouble(_tagName, _tagValue));
					}
					{
						final String _tagName = "range-configUpdate-max";
						final double _tagValue = obj.get("range-configUpdate-max").getAsDouble();
						CustomData.update(DataComponents.CUSTOM_DATA, itemstack, tag -> tag.putDouble(_tagName, _tagValue));
					}
					{
						final String _tagName = "range-configUpdate";
						final double _tagValue = (Mth.nextInt(RandomSource.create(), (int) obj.get("range-configUpdate-min").getAsDouble(), (int) obj.get("range-configUpdate-max").getAsDouble()));
						CustomData.update(DataComponents.CUSTOM_DATA, itemstack, tag -> tag.putDouble(_tagName, _tagValue));
					}
					{
						final String _tagName = "range-configUpdate-counter";
						final double _tagValue = 0;
						CustomData.update(DataComponents.CUSTOM_DATA, itemstack, tag -> tag.putDouble(_tagName, _tagValue));
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		if (net.wavedk.extrautilitiesreutilized.chickennuggetextras.CneCameraRuntime.isKeyDown(entity, String.valueOf("key.mouse.right"))) {
			cx = entity.level().clip(new ClipContext(entity.getEyePosition(1f), entity.getEyePosition(1f).add(entity.getViewVector(1f).scale(3)), ClipContext.Block.OUTLINE, ClipContext.Fluid.ANY, entity)).getBlockPos().getX();
			cy = entity.level().clip(new ClipContext(entity.getEyePosition(1f), entity.getEyePosition(1f).add(entity.getViewVector(1f).scale(3)), ClipContext.Block.OUTLINE, ClipContext.Fluid.ANY, entity)).getBlockPos().getY();
			cz = entity.level().clip(new ClipContext(entity.getEyePosition(1f), entity.getEyePosition(1f).add(entity.getViewVector(1f).scale(3)), ClipContext.Block.OUTLINE, ClipContext.Fluid.ANY, entity)).getBlockPos().getZ();
			cb = (world.getFluidState(BlockPos.containing(cx, cy, cz)).createLegacyBlock());
			if (cb.getBlock() == Blocks.WATER || cb.getBlock() == Blocks.BUBBLE_COLUMN) {
				if (!(itemstack.getDamageValue() == 0)) {
					itemstack.setDamageValue(itemstack.getDamageValue() - 1);
				}
			} else {
				cx = entity.level().clip(new ClipContext(entity.getEyePosition(1f), entity.getEyePosition(1f).add(entity.getViewVector(1f).scale(3)), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, entity)).getBlockPos().getX();
				cy = entity.level().clip(new ClipContext(entity.getEyePosition(1f), entity.getEyePosition(1f).add(entity.getViewVector(1f).scale(3)), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, entity)).getBlockPos().getY();
				cz = entity.level().clip(new ClipContext(entity.getEyePosition(1f), entity.getEyePosition(1f).add(entity.getViewVector(1f).scale(3)), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, entity)).getBlockPos().getZ();
				if (!(itemstack.getDamageValue() == itemstack.getMaxDamage())) {
					chanceNum = Mth.nextInt(RandomSource.create(), 1, 100);
					itemstack.setDamageValue(itemstack.getDamageValue() + 1);
					if (world instanceof ServerLevel _level)
						_level.sendParticles(ParticleTypes.FALLING_DRIPSTONE_WATER, cx, (cy + 1), cz, 20, 3, 1, 3, 2);
					if (chanceNum < itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getDouble("chance_for_growtick")) {
						loopNumber = itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getDouble("number_of_plants_grown");
						{
							final int _radiusLoopCenterX78 = (int) Math.floor(x);
							final int _radiusLoopCenterY78 = (int) Math.floor(y);
							final int _radiusLoopCenterZ78 = (int) Math.floor(z);
							final int _radiusLoopRadius78 = Math.max(0, (int) Math.floor(2));
							final int _radiusLoopMinX78 = _radiusLoopCenterX78 - _radiusLoopRadius78;
							final int _radiusLoopMaxX78 = _radiusLoopCenterX78 + _radiusLoopRadius78;
							final int _radiusLoopMinY78 = _radiusLoopCenterY78 - _radiusLoopRadius78;
							final int _radiusLoopMaxY78 = _radiusLoopCenterY78 + _radiusLoopRadius78;
							final int _radiusLoopMinZ78 = _radiusLoopCenterZ78 - _radiusLoopRadius78;
							final int _radiusLoopMaxZ78 = _radiusLoopCenterZ78 + _radiusLoopRadius78;
							for (int _radiusLoopX78 = _radiusLoopMinX78; _radiusLoopX78 <= _radiusLoopMaxX78; _radiusLoopX78++) {
								for (int _radiusLoopY78 = _radiusLoopMinY78; _radiusLoopY78 <= _radiusLoopMaxY78; _radiusLoopY78++) {
									for (int _radiusLoopZ78 = _radiusLoopMinZ78; _radiusLoopZ78 <= _radiusLoopMaxZ78; _radiusLoopZ78++) {
										lX = _radiusLoopX78;
										lY = _radiusLoopY78;
										lZ = _radiusLoopZ78;
										if (cy == lY || Math.round(cy + 1) == Math.round(lY) || Math.round(cy - 1) == Math.round(lY)) {
											if ((world.getBlockState(BlockPos.containing(lX, lY, lZ))).getBlock() instanceof BonemealableBlock) {
												if (0 < loopNumber) {
													chanceNum = Mth.nextInt(RandomSource.create(), 1, 100);
													if (chanceNum < itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getDouble("chance_for_plant_growth")) {
														if (world instanceof Level _level) {
															BlockPos _bp = BlockPos.containing(lX, lY, lZ);
															if (BoneMealItem.growCrop(new ItemStack(Items.BONE_MEAL), _level, _bp) || BoneMealItem.growWaterPlant(new ItemStack(Items.BONE_MEAL), _level, _bp, null)) {
																if (!_level.isClientSide())
																	_level.levelEvent(2005, _bp, 0);
															}
														}
														loopNumber = loopNumber - 1;
													}
												} else {
													break;
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		if (itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getDouble("range-configUpdate-counter") >= itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getDouble("range-configUpdate")) {
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
					cobj = obj.get("general").getAsJsonObject();
					iobj = cobj.get((BuiltInRegistries.ITEM.getKey(EuruModItems.WATERING_CAN.get()).toString())).getAsJsonObject();
					{
						final String _tagName = "chance_for_growtick";
						final double _tagValue = iobj.get("chance_for_growtick").getAsDouble();
						CustomData.update(DataComponents.CUSTOM_DATA, itemstack, tag -> tag.putDouble(_tagName, _tagValue));
					}
					{
						final String _tagName = "chance_for_plant_growth";
						final double _tagValue = iobj.get("chance_for_plant_growth").getAsDouble();
						CustomData.update(DataComponents.CUSTOM_DATA, itemstack, tag -> tag.putDouble(_tagName, _tagValue));
					}
					{
						final String _tagName = "number_of_plants_grown";
						final double _tagValue = iobj.get("number_of_plants_grown").getAsDouble();
						CustomData.update(DataComponents.CUSTOM_DATA, itemstack, tag -> tag.putDouble(_tagName, _tagValue));
					}
					{
						final String _tagName = "range-configUpdate-min";
						final double _tagValue = obj.get("range-configUpdate-min").getAsDouble();
						CustomData.update(DataComponents.CUSTOM_DATA, itemstack, tag -> tag.putDouble(_tagName, _tagValue));
					}
					{
						final String _tagName = "range-configUpdate-max";
						final double _tagValue = obj.get("range-configUpdate-max").getAsDouble();
						CustomData.update(DataComponents.CUSTOM_DATA, itemstack, tag -> tag.putDouble(_tagName, _tagValue));
					}
					{
						final String _tagName = "range-configUpdate";
						final double _tagValue = (Mth.nextInt(RandomSource.create(), (int) obj.get("range-configUpdate-min").getAsDouble(), (int) obj.get("range-configUpdate-max").getAsDouble()));
						CustomData.update(DataComponents.CUSTOM_DATA, itemstack, tag -> tag.putDouble(_tagName, _tagValue));
					}
					{
						final String _tagName = "range-configUpdate-counter";
						final double _tagValue = 0;
						CustomData.update(DataComponents.CUSTOM_DATA, itemstack, tag -> tag.putDouble(_tagName, _tagValue));
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else {
			{
				final String _tagName = "range-configUpdate-counter";
				final double _tagValue = (itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getDouble("range-configUpdate-counter") + 1);
				CustomData.update(DataComponents.CUSTOM_DATA, itemstack, tag -> tag.putDouble(_tagName, _tagValue));
			}
		}
	}
}