package net.wavedk.extrautilitiesreutilized.procedures;

import net.wavedk.extrautilitiesreutilized.init.EuruModItems;

import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.InteractionHand;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.component.DataComponents;

import javax.annotation.Nullable;

import java.util.UUID;

@EventBusSubscriber
public class LassoRCOBProcedure {
	@SubscribeEvent
	public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
		if (event.getHand() != InteractionHand.MAIN_HAND)
			return;
		execute(event, event.getLevel(), event.getPos().getX(), event.getPos().getY(), event.getPos().getZ(), event.getEntity());
	}

	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		execute(null, world, x, y, z, entity);
	}

	private static void execute(@Nullable Event event, LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		String gDep = "";
		String xs = "";
		String ys = "";
		String zs = "";
		String stringFull = "";
		double firstComma = 0;
		if (!world.isClientSide()) {
			if (!(((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getString("entityType")).equals(""))) {
				if (((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getString("entityReg")).equals("minecraft:chicken")) {
					{
						Entity _ent = spawnEntityFromCompoundTag(
								((Tag) getOrCreateCustomData((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY)).get("entityNBT") instanceof CompoundTag _compound8 ? _compound8 : new CompoundTag()), world, true);
						double _tx = x;
						double _ty = (y + 1);
						double _tz = z;
						_ent.teleportTo(_tx, _ty, _tz);
						if (_ent instanceof ServerPlayer _serverPlayer)
							_serverPlayer.connection.teleport(_tx, _ty, _tz, _ent.getYRot(), _ent.getXRot());
					}
					if (entity instanceof LivingEntity _entity) {
						ItemStack _setstack11 = new ItemStack(EuruModItems.GOLDEN_LASSO.get()).copy();
						_setstack11.setCount(1);
						_entity.setItemInHand(InteractionHand.MAIN_HAND, _setstack11);
						if (_entity instanceof Player _player)
							_player.getInventory().setChanged();
					}
				} else if (((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getString("entityReg")).equals("minecraft:bat")) {
					{
						Entity _ent = spawnEntityFromCompoundTag(
								((Tag) getOrCreateCustomData((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY)).get("entityNBT") instanceof CompoundTag _compound17 ? _compound17 : new CompoundTag()), world, true);
						double _tx = x;
						double _ty = (y + 1);
						double _tz = z;
						_ent.teleportTo(_tx, _ty, _tz);
						if (_ent instanceof ServerPlayer _serverPlayer)
							_serverPlayer.connection.teleport(_tx, _ty, _tz, _ent.getYRot(), _ent.getXRot());
					}
					if (entity instanceof LivingEntity _entity) {
						ItemStack _setstack20 = new ItemStack(EuruModItems.GOLDEN_LASSO.get()).copy();
						_setstack20.setCount(1);
						_entity.setItemInHand(InteractionHand.MAIN_HAND, _setstack20);
						if (_entity instanceof Player _player)
							_player.getInventory().setChanged();
					}
				} else if (((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getString("entityReg")).equals("minecraft:ghast")) {
					{
						Entity _ent = spawnEntityFromCompoundTag(
								((Tag) getOrCreateCustomData((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY)).get("entityNBT") instanceof CompoundTag _compound26 ? _compound26 : new CompoundTag()), world, true);
						double _tx = x;
						double _ty = (y + 1);
						double _tz = z;
						_ent.teleportTo(_tx, _ty, _tz);
						if (_ent instanceof ServerPlayer _serverPlayer)
							_serverPlayer.connection.teleport(_tx, _ty, _tz, _ent.getYRot(), _ent.getXRot());
					}
					if (entity instanceof LivingEntity _entity) {
						ItemStack _setstack29 = new ItemStack(EuruModItems.CURSED_LASSO.get()).copy();
						_setstack29.setCount(1);
						_entity.setItemInHand(InteractionHand.MAIN_HAND, _setstack29);
						if (_entity instanceof Player _player)
							_player.getInventory().setChanged();
					}
				} else if (((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getString("entityReg")).equals("minecraft:squid")
						|| ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getString("entityReg")).equals("minecraft:glow_squid")) {
					{
						Entity _ent = spawnEntityFromCompoundTag(
								((Tag) getOrCreateCustomData((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY)).get("entityNBT") instanceof CompoundTag _compound37 ? _compound37 : new CompoundTag()), world, true);
						double _tx = x;
						double _ty = (y + 1);
						double _tz = z;
						_ent.teleportTo(_tx, _ty, _tz);
						if (_ent instanceof ServerPlayer _serverPlayer)
							_serverPlayer.connection.teleport(_tx, _ty, _tz, _ent.getYRot(), _ent.getXRot());
					}
					if (entity instanceof LivingEntity _entity) {
						ItemStack _setstack40 = new ItemStack(EuruModItems.GOLDEN_LASSO.get()).copy();
						_setstack40.setCount(1);
						_entity.setItemInHand(InteractionHand.MAIN_HAND, _setstack40);
						if (_entity instanceof Player _player)
							_player.getInventory().setChanged();
					}
				} else {
					{
						Entity _ent = spawnEntityFromCompoundTag(
								((Tag) getOrCreateCustomData((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY)).get("entityNBT") instanceof CompoundTag _compound44 ? _compound44 : new CompoundTag()), world, true);
						double _tx = x;
						double _ty = (y + 1);
						double _tz = z;
						_ent.teleportTo(_tx, _ty, _tz);
						if (_ent instanceof ServerPlayer _serverPlayer)
							_serverPlayer.connection.teleport(_tx, _ty, _tz, _ent.getYRot(), _ent.getXRot());
					}
					{
						final String _tagName = "entityType";
						final String _tagValue = "";
						CustomData.update(DataComponents.CUSTOM_DATA, (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY), tag -> tag.putString(_tagName, _tagValue));
					}
				}
			} else if (!(((entity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY).getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getString("entityType")).equals(""))) {
				if (((entity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY).getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getString("entityReg")).equals("minecraft:chicken")) {
					{
						Entity _ent = spawnEntityFromCompoundTag(
								((Tag) getOrCreateCustomData((entity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY)).get("entityNBT") instanceof CompoundTag _compound56 ? _compound56 : new CompoundTag()), world, true);
						double _tx = x;
						double _ty = (y + 1);
						double _tz = z;
						_ent.teleportTo(_tx, _ty, _tz);
						if (_ent instanceof ServerPlayer _serverPlayer)
							_serverPlayer.connection.teleport(_tx, _ty, _tz, _ent.getYRot(), _ent.getXRot());
					}
					if (entity instanceof LivingEntity _entity) {
						ItemStack _setstack59 = new ItemStack(EuruModItems.GOLDEN_LASSO.get()).copy();
						_setstack59.setCount(1);
						_entity.setItemInHand(InteractionHand.OFF_HAND, _setstack59);
						if (_entity instanceof Player _player)
							_player.getInventory().setChanged();
					}
				} else if (((entity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY).getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getString("entityReg")).equals("minecraft:ghast")) {
					((Tag) getOrCreateCustomData((entity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY)).get("entityNBT") instanceof CompoundTag _compound65 ? _compound65 : new CompoundTag()).put("Pos",
							StringTag.valueOf(("[" + x + "d," + y + "d," + z + "d]")));
					{
						Entity _ent = spawnEntityFromCompoundTag(
								((Tag) getOrCreateCustomData((entity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY)).get("entityNBT") instanceof CompoundTag _compound71 ? _compound71 : new CompoundTag()), world, true);
						double _tx = x;
						double _ty = (y + 1);
						double _tz = z;
						_ent.teleportTo(_tx, _ty, _tz);
						if (_ent instanceof ServerPlayer _serverPlayer)
							_serverPlayer.connection.teleport(_tx, _ty, _tz, _ent.getYRot(), _ent.getXRot());
					}
					if (entity instanceof LivingEntity _entity) {
						ItemStack _setstack74 = new ItemStack(EuruModItems.CURSED_LASSO.get()).copy();
						_setstack74.setCount(1);
						_entity.setItemInHand(InteractionHand.OFF_HAND, _setstack74);
						if (_entity instanceof Player _player)
							_player.getInventory().setChanged();
					}
				} else if (((entity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY).getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getString("entityReg")).equals("minecraft:bat")) {
					{
						Entity _ent = spawnEntityFromCompoundTag(
								((Tag) getOrCreateCustomData((entity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY)).get("entityNBT") instanceof CompoundTag _compound80 ? _compound80 : new CompoundTag()), world, true);
						double _tx = x;
						double _ty = (y + 1);
						double _tz = z;
						_ent.teleportTo(_tx, _ty, _tz);
						if (_ent instanceof ServerPlayer _serverPlayer)
							_serverPlayer.connection.teleport(_tx, _ty, _tz, _ent.getYRot(), _ent.getXRot());
					}
					if (entity instanceof LivingEntity _entity) {
						ItemStack _setstack83 = new ItemStack(EuruModItems.GOLDEN_LASSO.get()).copy();
						_setstack83.setCount(1);
						_entity.setItemInHand(InteractionHand.OFF_HAND, _setstack83);
						if (_entity instanceof Player _player)
							_player.getInventory().setChanged();
					}
				} else if (((entity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY).getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getString("entityReg")).equals("minecraft:squid")
						|| ((entity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY).getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getString("entityReg")).equals("minecraft:glow_squid")) {
					{
						Entity _ent = spawnEntityFromCompoundTag(
								((Tag) getOrCreateCustomData((entity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY)).get("entityNBT") instanceof CompoundTag _compound91 ? _compound91 : new CompoundTag()), world, true);
						double _tx = x;
						double _ty = (y + 1);
						double _tz = z;
						_ent.teleportTo(_tx, _ty, _tz);
						if (_ent instanceof ServerPlayer _serverPlayer)
							_serverPlayer.connection.teleport(_tx, _ty, _tz, _ent.getYRot(), _ent.getXRot());
					}
					if (entity instanceof LivingEntity _entity) {
						ItemStack _setstack94 = new ItemStack(EuruModItems.GOLDEN_LASSO.get()).copy();
						_setstack94.setCount(1);
						_entity.setItemInHand(InteractionHand.OFF_HAND, _setstack94);
						if (_entity instanceof Player _player)
							_player.getInventory().setChanged();
					}
				} else {
					{
						Entity _ent = spawnEntityFromCompoundTag(
								((Tag) getOrCreateCustomData((entity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY)).get("entityNBT") instanceof CompoundTag _compound98 ? _compound98 : new CompoundTag()), world, true);
						double _tx = x;
						double _ty = (y + 1);
						double _tz = z;
						_ent.teleportTo(_tx, _ty, _tz);
						if (_ent instanceof ServerPlayer _serverPlayer)
							_serverPlayer.connection.teleport(_tx, _ty, _tz, _ent.getYRot(), _ent.getXRot());
					}
					{
						final String _tagName = "entityType";
						final String _tagValue = "";
						CustomData.update(DataComponents.CUSTOM_DATA, (entity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY), tag -> tag.putString(_tagName, _tagValue));
					}
				}
			}
		}
	}

	private static CompoundTag getOrCreateCustomData(ItemStack itemstack) {
		if (!itemstack.has(DataComponents.CUSTOM_DATA))
			itemstack.set(DataComponents.CUSTOM_DATA, CustomData.of(new CompoundTag()));
		return itemstack.get(DataComponents.CUSTOM_DATA).getUnsafe();
	}

	private static Entity spawnEntityFromCompoundTag(CompoundTag data, LevelAccessor world, boolean randomizeUUID) {
		if (world instanceof ServerLevel server) {
			Entity toSpawn = EntityType.create(data, server).orElse(null);
			if (toSpawn != null) {
				if (randomizeUUID)
					toSpawn.setUUID(UUID.randomUUID());
				server.addFreshEntity(toSpawn);
				return toSpawn;
			}
		}
		return null;
	}
}