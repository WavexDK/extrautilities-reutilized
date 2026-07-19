package net.wavedk.extrautilitiesreutilized.procedures;

import net.wavedk.extrautilitiesreutilized.init.EuruModItems;

import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.InteractionHand;
import net.minecraft.network.chat.Component;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.component.DataComponents;

import javax.annotation.Nullable;

import java.io.IOException;
import java.io.FileReader;
import java.io.File;
import java.io.BufferedReader;

@EventBusSubscriber
public class LassoRCOEProcedure {
	@SubscribeEvent
	public static void onRightClickEntity(PlayerInteractEvent.EntityInteract event) {
		if (event.getHand() != InteractionHand.MAIN_HAND)
			return;
		execute(event, event.getTarget(), event.getEntity());
	}

	public static void execute(Entity entity, Entity sourceentity) {
		execute(null, entity, sourceentity);
	}

	private static void execute(@Nullable Event event, Entity entity, Entity sourceentity) {
		if (entity == null || sourceentity == null)
			return;
		File file = new File("");
		com.google.gson.JsonObject mobj = new com.google.gson.JsonObject();
		com.google.gson.JsonObject lobj = new com.google.gson.JsonObject();
		com.google.gson.JsonArray array = new com.google.gson.JsonArray();
		double cNum = 0;
		boolean foundEntity = false;
		String cEntity = "";
		String entityType = "";
		String entityNbt = "";
		String gDep = "";
		if ((sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == EuruModItems.GOLDEN_LASSO.get()) {
			if (((sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getString("entityType")).equals("")) {
				file = new File((FMLPaths.GAMEDIR.get().toString() + "/config/euru/"), File.separator + "euru_unified_config.json");
				{
					try {
						BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
						StringBuilder jsonstringbuilder = new StringBuilder();
						String line;
						while ((line = bufferedReader.readLine()) != null) {
							jsonstringbuilder.append(line);
						}
						bufferedReader.close();
						mobj = new com.google.gson.Gson().fromJson(jsonstringbuilder.toString(), com.google.gson.JsonObject.class);
						lobj = mobj.get("lasso_entities").getAsJsonObject();
						array = lobj.get("golden").getAsJsonArray();
						cNum = 0;
						for (int index9 = 0; index9 < (int) array.size(); index9++) {
							cEntity = array.get((int) cNum).getAsString();
							if ((BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).toString()).equals(cEntity)) {
								foundEntity = true;
								break;
							}
							cNum = cNum + 1;
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (foundEntity) {
					if (true == false) {
						if (sourceentity instanceof LivingEntity _entity) {
							ItemStack _setstack12 = new ItemStack(EuruModItems.GOLDEN_LASSO_CW.get()).copy();
							_setstack12.setCount(1);
							_entity.setItemInHand(InteractionHand.MAIN_HAND, _setstack12);
							if (_entity instanceof Player _player)
								_player.getInventory().setChanged();
						}
						getOrCreateCustomData((sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY)).put("entityNBT", saveWithId(entity).copy());
						{
							final String _tagName = "healthMax";
							final double _tagValue = (entity instanceof LivingEntity _livEnt ? _livEnt.getMaxHealth() : -1);
							CustomData.update(DataComponents.CUSTOM_DATA, (sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY), tag -> tag.putDouble(_tagName, _tagValue));
						}
						{
							final String _tagName = "healthMin";
							final double _tagValue = (entity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1);
							CustomData.update(DataComponents.CUSTOM_DATA, (sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY), tag -> tag.putDouble(_tagName, _tagValue));
						}
						{
							final String _tagName = "entityType";
							final String _tagValue = (entity.getDisplayName().getString());
							CustomData.update(DataComponents.CUSTOM_DATA, (sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY), tag -> tag.putString(_tagName, _tagValue));
						}
						{
							final String _tagName = "entityReg";
							final String _tagValue = (BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).toString());
							CustomData.update(DataComponents.CUSTOM_DATA, (sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY), tag -> tag.putString(_tagName, _tagValue));
						}
						if (!entity.level().isClientSide())
							entity.discard();
					} else if ((BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).toString()).equals("minecraft:bat")) {
						if (sourceentity instanceof LivingEntity _entity) {
							ItemStack _setstack32 = new ItemStack(EuruModItems.GOLDEN_LASSO_AR.get()).copy();
							_setstack32.setCount(1);
							_entity.setItemInHand(InteractionHand.MAIN_HAND, _setstack32);
							if (_entity instanceof Player _player)
								_player.getInventory().setChanged();
						}
						getOrCreateCustomData((sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY)).put("entityNBT", saveWithId(entity).copy());
						{
							final String _tagName = "healthMax";
							final double _tagValue = (entity instanceof LivingEntity _livEnt ? _livEnt.getMaxHealth() : -1);
							CustomData.update(DataComponents.CUSTOM_DATA, (sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY), tag -> tag.putDouble(_tagName, _tagValue));
						}
						{
							final String _tagName = "healthMin";
							final double _tagValue = (entity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1);
							CustomData.update(DataComponents.CUSTOM_DATA, (sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY), tag -> tag.putDouble(_tagName, _tagValue));
						}
						{
							final String _tagName = "entityReg";
							final String _tagValue = (BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).toString());
							CustomData.update(DataComponents.CUSTOM_DATA, (sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY), tag -> tag.putString(_tagName, _tagValue));
						}
						{
							final String _tagName = "entityType";
							final String _tagValue = (entity.getDisplayName().getString());
							CustomData.update(DataComponents.CUSTOM_DATA, (sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY), tag -> tag.putString(_tagName, _tagValue));
						}
						if (!entity.level().isClientSide())
							entity.discard();
					} else if ((BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).toString()).equals("minecraft:squid") || (BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).toString()).equals("minecraft:glow_squid")) {
						if (sourceentity instanceof LivingEntity _entity) {
							ItemStack _setstack53 = new ItemStack(EuruModItems.GOLDEN_LASSO_SW.get()).copy();
							_setstack53.setCount(1);
							_entity.setItemInHand(InteractionHand.MAIN_HAND, _setstack53);
							if (_entity instanceof Player _player)
								_player.getInventory().setChanged();
						}
						getOrCreateCustomData((sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY)).put("entityNBT", saveWithId(entity).copy());
						{
							final String _tagName = "healthMax";
							final double _tagValue = (entity instanceof LivingEntity _livEnt ? _livEnt.getMaxHealth() : -1);
							CustomData.update(DataComponents.CUSTOM_DATA, (sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY), tag -> tag.putDouble(_tagName, _tagValue));
						}
						{
							final String _tagName = "healthMin";
							final double _tagValue = (entity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1);
							CustomData.update(DataComponents.CUSTOM_DATA, (sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY), tag -> tag.putDouble(_tagName, _tagValue));
						}
						{
							final String _tagName = "entityReg";
							final String _tagValue = (BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).toString());
							CustomData.update(DataComponents.CUSTOM_DATA, (sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY), tag -> tag.putString(_tagName, _tagValue));
						}
						{
							final String _tagName = "entityType";
							final String _tagValue = (entity.getDisplayName().getString());
							CustomData.update(DataComponents.CUSTOM_DATA, (sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY), tag -> tag.putString(_tagName, _tagValue));
						}
						if (!entity.level().isClientSide())
							entity.discard();
					} else {
						getOrCreateCustomData((sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY)).put("entityNBT", saveWithId(entity).copy());
						{
							final String _tagName = "healthMax";
							final double _tagValue = (entity instanceof LivingEntity _livEnt ? _livEnt.getMaxHealth() : -1);
							CustomData.update(DataComponents.CUSTOM_DATA, (sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY), tag -> tag.putDouble(_tagName, _tagValue));
						}
						{
							final String _tagName = "healthMin";
							final double _tagValue = (entity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1);
							CustomData.update(DataComponents.CUSTOM_DATA, (sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY), tag -> tag.putDouble(_tagName, _tagValue));
						}
						{
							final String _tagName = "entityReg";
							final String _tagValue = (BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).toString());
							CustomData.update(DataComponents.CUSTOM_DATA, (sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY), tag -> tag.putString(_tagName, _tagValue));
						}
						{
							final String _tagName = "entityType";
							final String _tagValue = (entity.getDisplayName().getString());
							CustomData.update(DataComponents.CUSTOM_DATA, (sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY), tag -> tag.putString(_tagName, _tagValue));
						}
						if (!entity.level().isClientSide())
							entity.discard();
					}
				} else {
					if (sourceentity instanceof Player _player && !_player.level().isClientSide())
						_player.displayClientMessage(Component.literal("\u00A7cYou cannot use a Golden Lasso on this entity! (try a cursed lasso)"), false);
				}
			}
		} else if ((sourceentity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY).getItem() == EuruModItems.GOLDEN_LASSO.get()) {
			if (((sourceentity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY).getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getString("entityType")).equals("")) {
				file = new File((FMLPaths.GAMEDIR.get().toString() + "/config/euru/"), File.separator + "euru_unified_config.json");
				{
					try {
						BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
						StringBuilder jsonstringbuilder = new StringBuilder();
						String line;
						while ((line = bufferedReader.readLine()) != null) {
							jsonstringbuilder.append(line);
						}
						bufferedReader.close();
						mobj = new com.google.gson.Gson().fromJson(jsonstringbuilder.toString(), com.google.gson.JsonObject.class);
						lobj = mobj.get("lasso_entities").getAsJsonObject();
						array = lobj.get("golden").getAsJsonArray();
						cNum = 0;
						for (int index10 = 0; index10 < (int) array.size(); index10++) {
							cEntity = array.get((int) cNum).getAsString();
							if ((BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).toString()).equals(cEntity)) {
								foundEntity = true;
								break;
							}
							cNum = cNum + 1;
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if ((BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).toString()).equals("minecraft:chicken")) {
					if (sourceentity instanceof LivingEntity _entity) {
						ItemStack _setstack104 = new ItemStack(EuruModItems.GOLDEN_LASSO_CW.get()).copy();
						_setstack104.setCount(1);
						_entity.setItemInHand(InteractionHand.OFF_HAND, _setstack104);
						if (_entity instanceof Player _player)
							_player.getInventory().setChanged();
					}
					getOrCreateCustomData((sourceentity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY)).put("entityNBT", saveWithId(entity).copy());
					{
						final String _tagName = "healthMax";
						final double _tagValue = (entity instanceof LivingEntity _livEnt ? _livEnt.getMaxHealth() : -1);
						CustomData.update(DataComponents.CUSTOM_DATA, (sourceentity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY), tag -> tag.putDouble(_tagName, _tagValue));
					}
					{
						final String _tagName = "healthMin";
						final double _tagValue = (entity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1);
						CustomData.update(DataComponents.CUSTOM_DATA, (sourceentity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY), tag -> tag.putDouble(_tagName, _tagValue));
					}
					{
						final String _tagName = "entityReg";
						final String _tagValue = (BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).toString());
						CustomData.update(DataComponents.CUSTOM_DATA, (sourceentity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY), tag -> tag.putString(_tagName, _tagValue));
					}
					{
						final String _tagName = "entityType";
						final String _tagValue = (entity.getDisplayName().getString());
						CustomData.update(DataComponents.CUSTOM_DATA, (sourceentity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY), tag -> tag.putString(_tagName, _tagValue));
					}
					if (!entity.level().isClientSide())
						entity.discard();
				} else if ((BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).toString()).equals("minecraft:bat")) {
					if (sourceentity instanceof LivingEntity _entity) {
						ItemStack _setstack124 = new ItemStack(EuruModItems.GOLDEN_LASSO_AR.get()).copy();
						_setstack124.setCount(1);
						_entity.setItemInHand(InteractionHand.OFF_HAND, _setstack124);
						if (_entity instanceof Player _player)
							_player.getInventory().setChanged();
					}
					getOrCreateCustomData((sourceentity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY)).put("entityNBT", saveWithId(entity).copy());
					{
						final String _tagName = "healthMax";
						final double _tagValue = (entity instanceof LivingEntity _livEnt ? _livEnt.getMaxHealth() : -1);
						CustomData.update(DataComponents.CUSTOM_DATA, (sourceentity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY), tag -> tag.putDouble(_tagName, _tagValue));
					}
					{
						final String _tagName = "healthMin";
						final double _tagValue = (entity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1);
						CustomData.update(DataComponents.CUSTOM_DATA, (sourceentity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY), tag -> tag.putDouble(_tagName, _tagValue));
					}
					{
						final String _tagName = "entityType";
						final String _tagValue = (entity.getDisplayName().getString());
						CustomData.update(DataComponents.CUSTOM_DATA, (sourceentity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY), tag -> tag.putString(_tagName, _tagValue));
					}
					{
						final String _tagName = "entityReg";
						final String _tagValue = (BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).toString());
						CustomData.update(DataComponents.CUSTOM_DATA, (sourceentity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY), tag -> tag.putString(_tagName, _tagValue));
					}
					if (!entity.level().isClientSide())
						entity.discard();
				} else if ((BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).toString()).equals("minecraft:squid") || (BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).toString()).equals("minecraft:glow_squid")) {
					if (sourceentity instanceof LivingEntity _entity) {
						ItemStack _setstack145 = new ItemStack(EuruModItems.GOLDEN_LASSO_SW.get()).copy();
						_setstack145.setCount(1);
						_entity.setItemInHand(InteractionHand.OFF_HAND, _setstack145);
						if (_entity instanceof Player _player)
							_player.getInventory().setChanged();
					}
					getOrCreateCustomData((sourceentity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY)).put("entityNBT", saveWithId(entity).copy());
					{
						final String _tagName = "healthMax";
						final double _tagValue = (entity instanceof LivingEntity _livEnt ? _livEnt.getMaxHealth() : -1);
						CustomData.update(DataComponents.CUSTOM_DATA, (sourceentity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY), tag -> tag.putDouble(_tagName, _tagValue));
					}
					{
						final String _tagName = "healthMin";
						final double _tagValue = (entity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1);
						CustomData.update(DataComponents.CUSTOM_DATA, (sourceentity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY), tag -> tag.putDouble(_tagName, _tagValue));
					}
					{
						final String _tagName = "entityType";
						final String _tagValue = (entity.getDisplayName().getString());
						CustomData.update(DataComponents.CUSTOM_DATA, (sourceentity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY), tag -> tag.putString(_tagName, _tagValue));
					}
					{
						final String _tagName = "entityReg";
						final String _tagValue = (BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).toString());
						CustomData.update(DataComponents.CUSTOM_DATA, (sourceentity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY), tag -> tag.putString(_tagName, _tagValue));
					}
					if (!entity.level().isClientSide())
						entity.discard();
				} else {
					getOrCreateCustomData((sourceentity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY)).put("entityNBT", saveWithId(entity).copy());
					{
						final String _tagName = "healthMax";
						final double _tagValue = (entity instanceof LivingEntity _livEnt ? _livEnt.getMaxHealth() : -1);
						CustomData.update(DataComponents.CUSTOM_DATA, (sourceentity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY), tag -> tag.putDouble(_tagName, _tagValue));
					}
					{
						final String _tagName = "healthMin";
						final double _tagValue = (entity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1);
						CustomData.update(DataComponents.CUSTOM_DATA, (sourceentity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY), tag -> tag.putDouble(_tagName, _tagValue));
					}
					{
						final String _tagName = "entityReg";
						final String _tagValue = (BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).toString());
						CustomData.update(DataComponents.CUSTOM_DATA, (sourceentity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY), tag -> tag.putString(_tagName, _tagValue));
					}
					{
						final String _tagName = "entityType";
						final String _tagValue = (entity.getDisplayName().getString());
						CustomData.update(DataComponents.CUSTOM_DATA, (sourceentity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY), tag -> tag.putString(_tagName, _tagValue));
					}
					if (!entity.level().isClientSide())
						entity.discard();
				}
			}
		} else if ((sourceentity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY).getItem() == EuruModItems.CURSED_LASSO.get()) {
			if (((sourceentity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY).getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getString("entityType")).equals("")) {
				file = new File((FMLPaths.GAMEDIR.get().toString() + "/config/euru/"), File.separator + "euru_unified_config.json");
				{
					try {
						BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
						StringBuilder jsonstringbuilder = new StringBuilder();
						String line;
						while ((line = bufferedReader.readLine()) != null) {
							jsonstringbuilder.append(line);
						}
						bufferedReader.close();
						mobj = new com.google.gson.Gson().fromJson(jsonstringbuilder.toString(), com.google.gson.JsonObject.class);
						lobj = mobj.get("lasso_entities").getAsJsonObject();
						array = lobj.get("cursed").getAsJsonArray();
						cNum = 0;
						for (int index11 = 0; index11 < (int) array.size(); index11++) {
							cEntity = array.get((int) cNum).getAsString();
							if ((BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).toString()).equals(cEntity)) {
								foundEntity = true;
								break;
							}
							cNum = cNum + 1;
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (foundEntity) {
					if ((BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).toString()).equals("minecraft:ghast")) {
						if (sourceentity instanceof LivingEntity _entity) {
							ItemStack _setstack195 = new ItemStack(EuruModItems.CURSED_LASSO_AR.get()).copy();
							_setstack195.setCount(1);
							_entity.setItemInHand(InteractionHand.OFF_HAND, _setstack195);
							if (_entity instanceof Player _player)
								_player.getInventory().setChanged();
						}
						getOrCreateCustomData((sourceentity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY)).put("entityNBT", saveWithId(entity).copy());
						{
							final String _tagName = "healthMax";
							final double _tagValue = (entity instanceof LivingEntity _livEnt ? _livEnt.getMaxHealth() : -1);
							CustomData.update(DataComponents.CUSTOM_DATA, (sourceentity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY), tag -> tag.putDouble(_tagName, _tagValue));
						}
						{
							final String _tagName = "healthMin";
							final double _tagValue = (entity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1);
							CustomData.update(DataComponents.CUSTOM_DATA, (sourceentity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY), tag -> tag.putDouble(_tagName, _tagValue));
						}
						{
							final String _tagName = "entityReg";
							final String _tagValue = (BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).toString());
							CustomData.update(DataComponents.CUSTOM_DATA, (sourceentity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY), tag -> tag.putString(_tagName, _tagValue));
						}
						{
							final String _tagName = "entityType";
							final String _tagValue = (entity.getDisplayName().getString());
							CustomData.update(DataComponents.CUSTOM_DATA, (sourceentity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY), tag -> tag.putString(_tagName, _tagValue));
						}
						if (!entity.level().isClientSide())
							entity.discard();
					} else {
						getOrCreateCustomData((sourceentity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY)).put("entityNBT", saveWithId(entity).copy());
						{
							final String _tagName = "healthMax";
							final double _tagValue = (entity instanceof LivingEntity _livEnt ? _livEnt.getMaxHealth() : -1);
							CustomData.update(DataComponents.CUSTOM_DATA, (sourceentity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY), tag -> tag.putDouble(_tagName, _tagValue));
						}
						{
							final String _tagName = "healthMin";
							final double _tagValue = (entity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1);
							CustomData.update(DataComponents.CUSTOM_DATA, (sourceentity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY), tag -> tag.putDouble(_tagName, _tagValue));
						}
						{
							final String _tagName = "entityReg";
							final String _tagValue = (BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).toString());
							CustomData.update(DataComponents.CUSTOM_DATA, (sourceentity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY), tag -> tag.putString(_tagName, _tagValue));
						}
						{
							final String _tagName = "entityType";
							final String _tagValue = (entity.getDisplayName().getString());
							CustomData.update(DataComponents.CUSTOM_DATA, (sourceentity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY), tag -> tag.putString(_tagName, _tagValue));
						}
						if (!entity.level().isClientSide())
							entity.discard();
					}
				} else {
					if (sourceentity instanceof Player _player && !_player.level().isClientSide())
						_player.displayClientMessage(Component.literal("\u00A7cYou cannot use a Cursed Lasso on this entity! (try a golden lasso)"), false);
				}
			}
		} else if ((sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == EuruModItems.CURSED_LASSO.get()) {
			if (((sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getString("entityType")).equals("")) {
				file = new File((FMLPaths.GAMEDIR.get().toString() + "/config/euru/"), File.separator + "euru_unified_config.json");
				{
					try {
						BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
						StringBuilder jsonstringbuilder = new StringBuilder();
						String line;
						while ((line = bufferedReader.readLine()) != null) {
							jsonstringbuilder.append(line);
						}
						bufferedReader.close();
						mobj = new com.google.gson.Gson().fromJson(jsonstringbuilder.toString(), com.google.gson.JsonObject.class);
						lobj = mobj.get("lasso_entities").getAsJsonObject();
						array = lobj.get("cursed").getAsJsonArray();
						cNum = 0;
						for (int index12 = 0; index12 < (int) array.size(); index12++) {
							cEntity = array.get((int) cNum).getAsString();
							if ((BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).toString()).equals(cEntity)) {
								foundEntity = true;
								break;
							}
							cNum = cNum + 1;
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (foundEntity) {
					if (entity instanceof Player _player && !_player.level().isClientSide())
						_player.displayClientMessage(Component.literal((BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).toString())), false);
					if ((BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).toString()).equals("minecraft:ghast")) {
						if (sourceentity instanceof LivingEntity _entity) {
							ItemStack _setstack248 = new ItemStack(EuruModItems.CURSED_LASSO_AR.get()).copy();
							_setstack248.setCount(1);
							_entity.setItemInHand(InteractionHand.MAIN_HAND, _setstack248);
							if (_entity instanceof Player _player)
								_player.getInventory().setChanged();
						}
						getOrCreateCustomData((sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY)).put("entityNBT", saveWithId(entity).copy());
						{
							final String _tagName = "healthMax";
							final double _tagValue = (entity instanceof LivingEntity _livEnt ? _livEnt.getMaxHealth() : -1);
							CustomData.update(DataComponents.CUSTOM_DATA, (sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY), tag -> tag.putDouble(_tagName, _tagValue));
						}
						{
							final String _tagName = "healthMin";
							final double _tagValue = (entity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1);
							CustomData.update(DataComponents.CUSTOM_DATA, (sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY), tag -> tag.putDouble(_tagName, _tagValue));
						}
						{
							final String _tagName = "entityReg";
							final String _tagValue = (BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).toString());
							CustomData.update(DataComponents.CUSTOM_DATA, (sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY), tag -> tag.putString(_tagName, _tagValue));
						}
						{
							final String _tagName = "entityType";
							final String _tagValue = (entity.getDisplayName().getString());
							CustomData.update(DataComponents.CUSTOM_DATA, (sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY), tag -> tag.putString(_tagName, _tagValue));
						}
						if (!entity.level().isClientSide())
							entity.discard();
					} else {
						getOrCreateCustomData((sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY)).put("entityNBT", saveWithId(entity).copy());
						{
							final String _tagName = "healthMax";
							final double _tagValue = (entity instanceof LivingEntity _livEnt ? _livEnt.getMaxHealth() : -1);
							CustomData.update(DataComponents.CUSTOM_DATA, (sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY), tag -> tag.putDouble(_tagName, _tagValue));
						}
						{
							final String _tagName = "healthMin";
							final double _tagValue = (entity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1);
							CustomData.update(DataComponents.CUSTOM_DATA, (sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY), tag -> tag.putDouble(_tagName, _tagValue));
						}
						{
							final String _tagName = "entityReg";
							final String _tagValue = (BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).toString());
							CustomData.update(DataComponents.CUSTOM_DATA, (sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY), tag -> tag.putString(_tagName, _tagValue));
						}
						{
							final String _tagName = "entityType";
							final String _tagValue = (entity.getDisplayName().getString());
							CustomData.update(DataComponents.CUSTOM_DATA, (sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY), tag -> tag.putString(_tagName, _tagValue));
						}
						if (!entity.level().isClientSide())
							entity.discard();
					}
				} else {
					if (sourceentity instanceof Player _player && !_player.level().isClientSide())
						_player.displayClientMessage(Component.literal("\u00A7cYou cannot use a Cursed Lasso on this entity! (try a golden lasso)"), false);
				}
			}
		}
	}

	private static CompoundTag getOrCreateCustomData(ItemStack itemstack) {
		if (!itemstack.has(DataComponents.CUSTOM_DATA))
			itemstack.set(DataComponents.CUSTOM_DATA, CustomData.of(new CompoundTag()));
		return itemstack.get(DataComponents.CUSTOM_DATA).getUnsafe();
	}

	private static CompoundTag saveWithId(Entity entity) {
		CompoundTag data = entity.saveWithoutId(new CompoundTag());
		String id = entity.getEncodeId();
		if (id != null)
			data.putString("id", id);
		return data;
	}
}