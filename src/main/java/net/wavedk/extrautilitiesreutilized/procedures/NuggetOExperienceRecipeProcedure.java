package net.wavedk.extrautilitiesreutilized.procedures;

import net.wavedk.extrautilitiesreutilized.init.EuruModItems;

import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.InteractionHand;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.Component;

import javax.annotation.Nullable;

@EventBusSubscriber
public class NuggetOExperienceRecipeProcedure {
	@SubscribeEvent
	public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
		if (event.getHand() != event.getEntity().getUsedItemHand())
			return;
		execute(event, event.getLevel(), event.getPos().getX(), event.getPos().getY(), event.getPos().getZ(), event.getEntity());
	}

	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		execute(null, world, x, y, z, entity);
	}

	private static void execute(@Nullable Event event, LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		boolean condition = false;
		double non = 0;
		if (entity.isShiftKeyDown() && (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == Items.GOLD_NUGGET) {
			non = Math.floor((entity instanceof Player _plr ? _plr.experienceLevel : 0) / 10d);
			if (non == 0) {
				if (entity instanceof Player _player && !_player.level().isClientSide())
					_player.displayClientMessage(Component.literal("\u00A7cYou do not have enough experience!"), true);
			} else if (non > (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getCount()) {
				for (int index4 = 0; index4 < (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getCount(); index4++) {
					if (world instanceof ServerLevel _level) {
						ItemEntity entityToSpawn = new ItemEntity(_level, x, y, z, new ItemStack(EuruModItems.NUGGETO_EXPERIENCE.get()));
						entityToSpawn.setPickUpDelay(10);
						_level.addFreshEntity(entityToSpawn);
					}
					if (entity instanceof Player _player)
						_player.giveExperiencePoints(-(10));
				}
				if (entity instanceof LivingEntity _entity) {
					ItemStack _setstack12 = (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).copy();
					_setstack12.setCount(0);
					_entity.setItemInHand(InteractionHand.MAIN_HAND, _setstack12);
					if (_entity instanceof Player _player)
						_player.getInventory().setChanged();
				}
			} else {
				for (int index5 = 0; index5 < (int) non; index5++) {
					if (world instanceof ServerLevel _level) {
						ItemEntity entityToSpawn = new ItemEntity(_level, x, y, z, new ItemStack(EuruModItems.NUGGETO_EXPERIENCE.get()));
						entityToSpawn.setPickUpDelay(10);
						_level.addFreshEntity(entityToSpawn);
					}
					if (entity instanceof Player _player)
						_player.giveExperiencePoints(-(10));
				}
				if (entity instanceof LivingEntity _entity) {
					ItemStack _setstack18 = (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).copy();
					_setstack18.setCount((int) ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getCount() - non));
					_entity.setItemInHand(InteractionHand.MAIN_HAND, _setstack18);
					if (_entity instanceof Player _player)
						_player.getInventory().setChanged();
				}
			}
		} else if (entity.isShiftKeyDown() && (entity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY).getItem() == Items.GOLD_NUGGET) {
			non = Math.floor((entity instanceof Player _plr ? _plr.experienceLevel : 0) / 10d);
			if (non == 0) {
				if (entity instanceof Player _player && !_player.level().isClientSide())
					_player.displayClientMessage(Component.literal("\u00A7cYou do not have enough experience!"), true);
			} else if (non > (entity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY).getCount()) {
				for (int index6 = 0; index6 < (entity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY).getCount(); index6++) {
					if (world instanceof ServerLevel _level) {
						ItemEntity entityToSpawn = new ItemEntity(_level, x, y, z, new ItemStack(EuruModItems.NUGGETO_EXPERIENCE.get()));
						entityToSpawn.setPickUpDelay(10);
						_level.addFreshEntity(entityToSpawn);
					}
					if (entity instanceof Player _player)
						_player.giveExperiencePoints(-(10));
				}
				if (entity instanceof LivingEntity _entity) {
					ItemStack _setstack31 = (entity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY).copy();
					_setstack31.setCount(0);
					_entity.setItemInHand(InteractionHand.OFF_HAND, _setstack31);
					if (_entity instanceof Player _player)
						_player.getInventory().setChanged();
				}
			} else {
				for (int index7 = 0; index7 < (int) non; index7++) {
					if (world instanceof ServerLevel _level) {
						ItemEntity entityToSpawn = new ItemEntity(_level, x, y, z, new ItemStack(EuruModItems.NUGGETO_EXPERIENCE.get()));
						entityToSpawn.setPickUpDelay(10);
						_level.addFreshEntity(entityToSpawn);
					}
					if (entity instanceof Player _player)
						_player.giveExperiencePoints(-(10));
				}
				if (entity instanceof LivingEntity _entity) {
					ItemStack _setstack37 = (entity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY).copy();
					_setstack37.setCount((int) ((entity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY).getCount() - non));
					_entity.setItemInHand(InteractionHand.OFF_HAND, _setstack37);
					if (_entity instanceof Player _player)
						_player.getInventory().setChanged();
				}
			}
		}
	}
}