package net.wavedk.extrautilitiesreutilized.procedures;

import net.wavedk.extrautilitiesreutilized.network.EuruModVariables;
import net.wavedk.extrautilitiesreutilized.init.EuruModItems;

import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
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

import java.io.File;

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
				lobj = EuruModVariables.unified_config.get("lasso_entities").getAsJsonObject();
				array = lobj.get("golden").getAsJsonArray();
				cNum = 0;
				for (int _i1 = 0; _i1 < (int) array.size(); _i1++) {
					cEntity = array.get((int) cNum).getAsString();
					if ((BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).toString()).equals(cEntity)) {
						foundEntity = true;
						break;
					}
					cNum = cNum + 1;
				}
				if (foundEntity) {
					if ((BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).toString()).equals("minecraft:chicken")) {
						if (sourceentity instanceof LivingEntity _entity) {
							ItemStack _setstack10 = new ItemStack(EuruModItems.GOLDEN_LASSO_CW.get()).copy();
							_setstack10.setCount(1);
							_entity.setItemInHand(InteractionHand.MAIN_HAND, _setstack10);
							if (_entity instanceof Player _player)
								_player.getInventory().setChanged();
						}
						(sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().put("entityNBT", saveWithId(entity).copy());
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
					} else if ((BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).toString()).equals("minecraft:villager")) {
						if (sourceentity instanceof LivingEntity _entity) {
							ItemStack _setstack30 = new ItemStack(EuruModItems.GOLDEN_LASSO_CLW.get()).copy();
							_setstack30.setCount(1);
							_entity.setItemInHand(InteractionHand.MAIN_HAND, _setstack30);
							if (_entity instanceof Player _player)
								_player.getInventory().setChanged();
						}
						(sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().put("entityNBT", saveWithId(entity).copy());
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
							ItemStack _setstack50 = new ItemStack(EuruModItems.GOLDEN_LASSO_AR.get()).copy();
							_setstack50.setCount(1);
							_entity.setItemInHand(InteractionHand.MAIN_HAND, _setstack50);
							if (_entity instanceof Player _player)
								_player.getInventory().setChanged();
						}
						(sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().put("entityNBT", saveWithId(entity).copy());
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
							ItemStack _setstack71 = new ItemStack(EuruModItems.GOLDEN_LASSO_SW.get()).copy();
							_setstack71.setCount(1);
							_entity.setItemInHand(InteractionHand.MAIN_HAND, _setstack71);
							if (_entity instanceof Player _player)
								_player.getInventory().setChanged();
						}
						(sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().put("entityNBT", saveWithId(entity).copy());
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
						(sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().put("entityNBT", saveWithId(entity).copy());
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
				lobj = EuruModVariables.unified_config.get("lasso_entities").getAsJsonObject();
				array = lobj.get("golden").getAsJsonArray();
				cNum = 0;
				for (int _i1 = 0; _i1 < (int) array.size(); _i1++) {
					cEntity = array.get((int) cNum).getAsString();
					if ((BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).toString()).equals(cEntity)) {
						foundEntity = true;
						break;
					}
					cNum = cNum + 1;
				}
				if ((BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).toString()).equals("minecraft:chicken")) {
					if (sourceentity instanceof LivingEntity _entity) {
						ItemStack _setstack119 = new ItemStack(EuruModItems.GOLDEN_LASSO_CW.get()).copy();
						_setstack119.setCount(1);
						_entity.setItemInHand(InteractionHand.OFF_HAND, _setstack119);
						if (_entity instanceof Player _player)
							_player.getInventory().setChanged();
					}
					(sourceentity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY).getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().put("entityNBT", saveWithId(entity).copy());
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
						ItemStack _setstack139 = new ItemStack(EuruModItems.GOLDEN_LASSO_AR.get()).copy();
						_setstack139.setCount(1);
						_entity.setItemInHand(InteractionHand.OFF_HAND, _setstack139);
						if (_entity instanceof Player _player)
							_player.getInventory().setChanged();
					}
					(sourceentity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY).getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().put("entityNBT", saveWithId(entity).copy());
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
						ItemStack _setstack160 = new ItemStack(EuruModItems.GOLDEN_LASSO_SW.get()).copy();
						_setstack160.setCount(1);
						_entity.setItemInHand(InteractionHand.OFF_HAND, _setstack160);
						if (_entity instanceof Player _player)
							_player.getInventory().setChanged();
					}
					(sourceentity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY).getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().put("entityNBT", saveWithId(entity).copy());
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
					(sourceentity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY).getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().put("entityNBT", saveWithId(entity).copy());
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
				lobj = EuruModVariables.unified_config.get("lasso_entities").getAsJsonObject();
				array = lobj.get("cursed").getAsJsonArray();
				cNum = 0;
				for (int _i1 = 0; _i1 < (int) array.size(); _i1++) {
					cEntity = array.get((int) cNum).getAsString();
					if ((BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).toString()).equals(cEntity)) {
						foundEntity = true;
						break;
					}
					cNum = cNum + 1;
				}
				if (foundEntity) {
					if ((BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).toString()).equals("minecraft:ghast")) {
						if (sourceentity instanceof LivingEntity _entity) {
							ItemStack _setstack207 = new ItemStack(EuruModItems.CURSED_LASSO_AR.get()).copy();
							_setstack207.setCount(1);
							_entity.setItemInHand(InteractionHand.OFF_HAND, _setstack207);
							if (_entity instanceof Player _player)
								_player.getInventory().setChanged();
						}
						(sourceentity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY).getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().put("entityNBT", saveWithId(entity).copy());
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
						(sourceentity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY).getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().put("entityNBT", saveWithId(entity).copy());
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
				lobj = EuruModVariables.unified_config.get("lasso_entities").getAsJsonObject();
				array = lobj.get("cursed").getAsJsonArray();
				cNum = 0;
				for (int _i1 = 0; _i1 < (int) array.size(); _i1++) {
					cEntity = array.get((int) cNum).getAsString();
					if ((BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).toString()).equals(cEntity)) {
						foundEntity = true;
						break;
					}
					cNum = cNum + 1;
				}
				if (foundEntity) {
					if (entity instanceof Player _player && !_player.level().isClientSide())
						_player.displayClientMessage(Component.literal((BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).toString())), false);
					if ((BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).toString()).equals("minecraft:ghast")) {
						if (sourceentity instanceof LivingEntity _entity) {
							ItemStack _setstack257 = new ItemStack(EuruModItems.CURSED_LASSO_AR.get()).copy();
							_setstack257.setCount(1);
							_entity.setItemInHand(InteractionHand.MAIN_HAND, _setstack257);
							if (_entity instanceof Player _player)
								_player.getInventory().setChanged();
						}
						(sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().put("entityNBT", saveWithId(entity).copy());
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
						(sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().put("entityNBT", saveWithId(entity).copy());
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

	private static CompoundTag saveWithId(Entity entity) {
		CompoundTag data = entity.saveWithoutId(new CompoundTag());
		String id = entity.getEncodeId();
		if (id != null)
			data.putString("id", id);
		return data;
	}
}