package net.wavedk.extrautilitiesreutilized.procedures;

import net.wavedk.extrautilitiesreutilized.init.EuruModItems;
import net.wavedk.extrautilitiesreutilized.init.EuruModBlocks;

import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.InteractionHand;
import net.minecraft.server.level.ServerLevel;

import javax.annotation.Nullable;

@EventBusSubscriber
public class RightClickUncraftCLTProcedure {
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
		if ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == EuruModBlocks.CHUNK_LOADER_TESTER.get().asItem()) {
			for (int index1 = 0; index1 < (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getCount(); index1++) {
				if (world instanceof ServerLevel _level) {
					ItemEntity entityToSpawn = new ItemEntity(_level, x, y, z, new ItemStack(EuruModBlocks.MACHINE_BLOCK.get()));
					entityToSpawn.setPickUpDelay(0);
					_level.addFreshEntity(entityToSpawn);
				}
				if (world instanceof ServerLevel _level) {
					ItemEntity entityToSpawn = new ItemEntity(_level, x, y, z, new ItemStack(EuruModItems.RESONATING_REDSTONE_CRYSTAL.get()));
					entityToSpawn.setPickUpDelay(0);
					_level.addFreshEntity(entityToSpawn);
				}
			}
			if (entity instanceof LivingEntity _entity) {
				ItemStack _setstack6 = new ItemStack(EuruModBlocks.CHUNK_LOADER_TESTER.get()).copy();
				_setstack6.setCount(0);
				_entity.setItemInHand(InteractionHand.MAIN_HAND, _setstack6);
				if (_entity instanceof Player _player)
					_player.getInventory().setChanged();
			}
		} else if ((entity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY).getItem() == EuruModBlocks.CHUNK_LOADER_TESTER.get().asItem()) {
			for (int index2 = 0; index2 < (entity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY).getCount(); index2++) {
				if (world instanceof ServerLevel _level) {
					ItemEntity entityToSpawn = new ItemEntity(_level, x, y, z, new ItemStack(EuruModBlocks.MACHINE_BLOCK.get()));
					entityToSpawn.setPickUpDelay(0);
					_level.addFreshEntity(entityToSpawn);
				}
				if (world instanceof ServerLevel _level) {
					ItemEntity entityToSpawn = new ItemEntity(_level, x, y, z, new ItemStack(EuruModItems.RESONATING_REDSTONE_CRYSTAL.get()));
					entityToSpawn.setPickUpDelay(0);
					_level.addFreshEntity(entityToSpawn);
				}
			}
			if (entity instanceof LivingEntity _entity) {
				ItemStack _setstack13 = new ItemStack(EuruModBlocks.CHUNK_LOADER_TESTER.get()).copy();
				_setstack13.setCount(0);
				_entity.setItemInHand(InteractionHand.OFF_HAND, _setstack13);
				if (_entity instanceof Player _player)
					_player.getInventory().setChanged();
			}
		}
	}
}