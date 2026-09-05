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
				if (!((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getString("entityReg")).isEmpty()) {
					if ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == EuruModItems.CURSED_LASSO_AR.get()
							|| (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == EuruModItems.CURSED_LASSO.get()) {
						{
							Entity _ent = spawnEntityFromCompoundTag(
									((Tag) (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().get("entityNBT") instanceof CompoundTag _compound12
											? _compound12
											: new CompoundTag()),
									world, true);
							double _tx = x;
							double _ty = (y + 1);
							double _tz = z;
							_ent.teleportTo(_tx, _ty, _tz);
							if (_ent instanceof ServerPlayer _serverPlayer)
								_serverPlayer.connection.teleport(_tx, _ty, _tz, _ent.getYRot(), _ent.getXRot());
						}
						if (entity instanceof LivingEntity _entity) {
							ItemStack _setstack15 = new ItemStack(EuruModItems.CURSED_LASSO.get()).copy();
							_setstack15.setCount(1);
							_entity.setItemInHand(InteractionHand.MAIN_HAND, _setstack15);
							if (_entity instanceof Player _player)
								_player.getInventory().setChanged();
						}
					} else {
						{
							Entity _ent = spawnEntityFromCompoundTag(
									((Tag) (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().get("entityNBT") instanceof CompoundTag _compound19
											? _compound19
											: new CompoundTag()),
									world, true);
							double _tx = x;
							double _ty = (y + 1);
							double _tz = z;
							_ent.teleportTo(_tx, _ty, _tz);
							if (_ent instanceof ServerPlayer _serverPlayer)
								_serverPlayer.connection.teleport(_tx, _ty, _tz, _ent.getYRot(), _ent.getXRot());
						}
						if (entity instanceof LivingEntity _entity) {
							ItemStack _setstack22 = new ItemStack(EuruModItems.GOLDEN_LASSO.get()).copy();
							_setstack22.setCount(1);
							_entity.setItemInHand(InteractionHand.MAIN_HAND, _setstack22);
							if (_entity instanceof Player _player)
								_player.getInventory().setChanged();
						}
					}
				}
			} else if (!(((entity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY).getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getString("entityType")).equals(""))) {
				if (!((entity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY).getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getString("entityReg")).isEmpty()) {
					if ((entity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY).getItem() == EuruModItems.CURSED_LASSO_AR.get()
							|| (entity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY).getItem() == EuruModItems.CURSED_LASSO.get()) {
						{
							Entity _ent = spawnEntityFromCompoundTag(
									((Tag) (entity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY).getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().get("entityNBT") instanceof CompoundTag _compound34
											? _compound34
											: new CompoundTag()),
									world, true);
							double _tx = x;
							double _ty = (y + 1);
							double _tz = z;
							_ent.teleportTo(_tx, _ty, _tz);
							if (_ent instanceof ServerPlayer _serverPlayer)
								_serverPlayer.connection.teleport(_tx, _ty, _tz, _ent.getYRot(), _ent.getXRot());
						}
						if (entity instanceof LivingEntity _entity) {
							ItemStack _setstack37 = new ItemStack(EuruModItems.CURSED_LASSO.get()).copy();
							_setstack37.setCount(1);
							_entity.setItemInHand(InteractionHand.OFF_HAND, _setstack37);
							if (_entity instanceof Player _player)
								_player.getInventory().setChanged();
						}
					} else {
						{
							Entity _ent = spawnEntityFromCompoundTag(
									((Tag) (entity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY).getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().get("entityNBT") instanceof CompoundTag _compound41
											? _compound41
											: new CompoundTag()),
									world, true);
							double _tx = x;
							double _ty = (y + 1);
							double _tz = z;
							_ent.teleportTo(_tx, _ty, _tz);
							if (_ent instanceof ServerPlayer _serverPlayer)
								_serverPlayer.connection.teleport(_tx, _ty, _tz, _ent.getYRot(), _ent.getXRot());
						}
						if (entity instanceof LivingEntity _entity) {
							ItemStack _setstack44 = new ItemStack(EuruModItems.GOLDEN_LASSO.get()).copy();
							_setstack44.setCount(1);
							_entity.setItemInHand(InteractionHand.OFF_HAND, _setstack44);
							if (_entity instanceof Player _player)
								_player.getInventory().setChanged();
						}
					}
				}
			}
		}
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