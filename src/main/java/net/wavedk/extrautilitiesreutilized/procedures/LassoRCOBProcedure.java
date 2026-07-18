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
					((Tag) getOrCreateCustomData((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY)).get("entityNBT") instanceof CompoundTag _compound8 ? _compound8 : new CompoundTag()).put("Pos",
							StringTag.valueOf(("[" + x + "d," + y + "d," + z + "d]")));
					{
						Entity _ent = spawnEntityFromCompoundTag(
								((Tag) getOrCreateCustomData((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY)).get("entityNBT") instanceof CompoundTag _compound14 ? _compound14 : new CompoundTag()), world, true);
						double _tx = x;
						double _ty = (y + 1);
						double _tz = z;
						_ent.teleportTo(_tx, _ty, _tz);
						if (_ent instanceof ServerPlayer _serverPlayer)
							_serverPlayer.connection.teleport(_tx, _ty, _tz, _ent.getYRot(), _ent.getXRot());
					}
					if (entity instanceof LivingEntity _entity) {
						ItemStack _setstack17 = new ItemStack(EuruModItems.GOLDEN_LASSO.get()).copy();
						_setstack17.setCount(1);
						_entity.setItemInHand(InteractionHand.MAIN_HAND, _setstack17);
						if (_entity instanceof Player _player)
							_player.getInventory().setChanged();
					}
				} else if (((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getString("entityReg")).equals("minecraft:bat")) {
					((Tag) getOrCreateCustomData((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY)).get("entityNBT") instanceof CompoundTag _compound23 ? _compound23 : new CompoundTag()).put("Pos",
							StringTag.valueOf(("[" + x + "d," + y + "d," + z + "d]")));
					{
						Entity _ent = spawnEntityFromCompoundTag(
								((Tag) getOrCreateCustomData((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY)).get("entityNBT") instanceof CompoundTag _compound29 ? _compound29 : new CompoundTag()), world, true);
						double _tx = x;
						double _ty = (y + 1);
						double _tz = z;
						_ent.teleportTo(_tx, _ty, _tz);
						if (_ent instanceof ServerPlayer _serverPlayer)
							_serverPlayer.connection.teleport(_tx, _ty, _tz, _ent.getYRot(), _ent.getXRot());
					}
					if (entity instanceof LivingEntity _entity) {
						ItemStack _setstack32 = new ItemStack(EuruModItems.GOLDEN_LASSO.get()).copy();
						_setstack32.setCount(1);
						_entity.setItemInHand(InteractionHand.MAIN_HAND, _setstack32);
						if (_entity instanceof Player _player)
							_player.getInventory().setChanged();
					}
				} else if (((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getString("entityReg")).equals("minecraft:ghast")) {
					((Tag) getOrCreateCustomData((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY)).get("entityNBT") instanceof CompoundTag _compound38 ? _compound38 : new CompoundTag()).put("Pos",
							StringTag.valueOf(("[" + x + "d," + y + "d," + z + "d]")));
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
					if (entity instanceof LivingEntity _entity) {
						ItemStack _setstack47 = new ItemStack(EuruModItems.CURSED_LASSO.get()).copy();
						_setstack47.setCount(1);
						_entity.setItemInHand(InteractionHand.MAIN_HAND, _setstack47);
						if (_entity instanceof Player _player)
							_player.getInventory().setChanged();
					}
				} else if (((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getString("entityReg")).equals("minecraft:squid")
						|| ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getString("entityReg")).equals("minecraft:glow_squid")) {
					((Tag) getOrCreateCustomData((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY)).get("entityNBT") instanceof CompoundTag _compound55 ? _compound55 : new CompoundTag()).put("Pos",
							StringTag.valueOf(("[" + x + "d," + y + "d," + z + "d]")));
					{
						Entity _ent = spawnEntityFromCompoundTag(
								((Tag) getOrCreateCustomData((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY)).get("entityNBT") instanceof CompoundTag _compound61 ? _compound61 : new CompoundTag()), world, true);
						double _tx = x;
						double _ty = (y + 1);
						double _tz = z;
						_ent.teleportTo(_tx, _ty, _tz);
						if (_ent instanceof ServerPlayer _serverPlayer)
							_serverPlayer.connection.teleport(_tx, _ty, _tz, _ent.getYRot(), _ent.getXRot());
					}
					if (entity instanceof LivingEntity _entity) {
						ItemStack _setstack64 = new ItemStack(EuruModItems.GOLDEN_LASSO.get()).copy();
						_setstack64.setCount(1);
						_entity.setItemInHand(InteractionHand.MAIN_HAND, _setstack64);
						if (_entity instanceof Player _player)
							_player.getInventory().setChanged();
					}
				} else {
					((Tag) getOrCreateCustomData((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY)).get("entityNBT") instanceof CompoundTag _compound68 ? _compound68 : new CompoundTag()).put("Pos",
							StringTag.valueOf(("[" + x + "d," + y + "d," + z + "d]")));
					{
						Entity _ent = spawnEntityFromCompoundTag(
								((Tag) getOrCreateCustomData((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY)).get("entityNBT") instanceof CompoundTag _compound74 ? _compound74 : new CompoundTag()), world, true);
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
					((Tag) getOrCreateCustomData((entity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY)).get("entityNBT") instanceof CompoundTag _compound86 ? _compound86 : new CompoundTag()).put("Pos",
							StringTag.valueOf(("[" + x + "d," + y + "d," + z + "d]")));
					{
						Entity _ent = spawnEntityFromCompoundTag(
								((Tag) getOrCreateCustomData((entity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY)).get("entityNBT") instanceof CompoundTag _compound92 ? _compound92 : new CompoundTag()), world, true);
						double _tx = x;
						double _ty = (y + 1);
						double _tz = z;
						_ent.teleportTo(_tx, _ty, _tz);
						if (_ent instanceof ServerPlayer _serverPlayer)
							_serverPlayer.connection.teleport(_tx, _ty, _tz, _ent.getYRot(), _ent.getXRot());
					}
					if (entity instanceof LivingEntity _entity) {
						ItemStack _setstack95 = new ItemStack(EuruModItems.GOLDEN_LASSO.get()).copy();
						_setstack95.setCount(1);
						_entity.setItemInHand(InteractionHand.OFF_HAND, _setstack95);
						if (_entity instanceof Player _player)
							_player.getInventory().setChanged();
					}
				} else if (((entity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY).getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getString("entityReg")).equals("minecraft:ghast")) {
					((Tag) getOrCreateCustomData((entity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY)).get("entityNBT") instanceof CompoundTag _compound101 ? _compound101 : new CompoundTag()).put("Pos",
							StringTag.valueOf(("[" + x + "d," + y + "d," + z + "d]")));
					{
						Entity _ent = spawnEntityFromCompoundTag(
								((Tag) getOrCreateCustomData((entity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY)).get("entityNBT") instanceof CompoundTag _compound107 ? _compound107 : new CompoundTag()), world,
								true);
						double _tx = x;
						double _ty = (y + 1);
						double _tz = z;
						_ent.teleportTo(_tx, _ty, _tz);
						if (_ent instanceof ServerPlayer _serverPlayer)
							_serverPlayer.connection.teleport(_tx, _ty, _tz, _ent.getYRot(), _ent.getXRot());
					}
					if (entity instanceof LivingEntity _entity) {
						ItemStack _setstack110 = new ItemStack(EuruModItems.CURSED_LASSO.get()).copy();
						_setstack110.setCount(1);
						_entity.setItemInHand(InteractionHand.OFF_HAND, _setstack110);
						if (_entity instanceof Player _player)
							_player.getInventory().setChanged();
					}
				} else if (((entity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY).getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getString("entityReg")).equals("minecraft:bat")) {
					((Tag) getOrCreateCustomData((entity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY)).get("entityNBT") instanceof CompoundTag _compound116 ? _compound116 : new CompoundTag()).put("Pos",
							StringTag.valueOf(("[" + x + "d," + y + "d," + z + "d]")));
					{
						Entity _ent = spawnEntityFromCompoundTag(
								((Tag) getOrCreateCustomData((entity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY)).get("entityNBT") instanceof CompoundTag _compound122 ? _compound122 : new CompoundTag()), world,
								true);
						double _tx = x;
						double _ty = (y + 1);
						double _tz = z;
						_ent.teleportTo(_tx, _ty, _tz);
						if (_ent instanceof ServerPlayer _serverPlayer)
							_serverPlayer.connection.teleport(_tx, _ty, _tz, _ent.getYRot(), _ent.getXRot());
					}
					if (entity instanceof LivingEntity _entity) {
						ItemStack _setstack125 = new ItemStack(EuruModItems.GOLDEN_LASSO.get()).copy();
						_setstack125.setCount(1);
						_entity.setItemInHand(InteractionHand.OFF_HAND, _setstack125);
						if (_entity instanceof Player _player)
							_player.getInventory().setChanged();
					}
				} else if (((entity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY).getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getString("entityReg")).equals("minecraft:squid")
						|| ((entity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY).getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getString("entityReg")).equals("minecraft:glow_squid")) {
					((Tag) getOrCreateCustomData((entity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY)).get("entityNBT") instanceof CompoundTag _compound133 ? _compound133 : new CompoundTag()).put("Pos",
							StringTag.valueOf(("[" + x + "d," + y + "d," + z + "d]")));
					{
						Entity _ent = spawnEntityFromCompoundTag(
								((Tag) getOrCreateCustomData((entity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY)).get("entityNBT") instanceof CompoundTag _compound139 ? _compound139 : new CompoundTag()), world,
								true);
						double _tx = x;
						double _ty = (y + 1);
						double _tz = z;
						_ent.teleportTo(_tx, _ty, _tz);
						if (_ent instanceof ServerPlayer _serverPlayer)
							_serverPlayer.connection.teleport(_tx, _ty, _tz, _ent.getYRot(), _ent.getXRot());
					}
					if (entity instanceof LivingEntity _entity) {
						ItemStack _setstack142 = new ItemStack(EuruModItems.GOLDEN_LASSO.get()).copy();
						_setstack142.setCount(1);
						_entity.setItemInHand(InteractionHand.OFF_HAND, _setstack142);
						if (_entity instanceof Player _player)
							_player.getInventory().setChanged();
					}
				} else {
					((Tag) getOrCreateCustomData((entity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY)).get("entityNBT") instanceof CompoundTag _compound146 ? _compound146 : new CompoundTag()).put("Pos",
							StringTag.valueOf(("[" + x + "d," + y + "d," + z + "d]")));
					{
						Entity _ent = spawnEntityFromCompoundTag(
								((Tag) getOrCreateCustomData((entity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY)).get("entityNBT") instanceof CompoundTag _compound152 ? _compound152 : new CompoundTag()), world,
								true);
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