package net.wavedk.extrautilitiesreutilized.procedures;

import net.wavedk.extrautilitiesreutilized.init.EuruModMenus;

import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;

public class ShadyMerchantGUIWhileThisGUIIsOpenTickProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		if ((entity instanceof Player _plrSlotItem && _plrSlotItem.containerMenu instanceof EuruModMenus.MenuAccessor _menu0 ? _menu0.getSlots().get(0).getItem() : ItemStack.EMPTY).getItem() == Items.EMERALD && getAmountInGUISlot(entity, 0) == 1) {
			if (entity instanceof Player _player && _player.containerMenu instanceof EuruModMenus.MenuAccessor _menu) {
				ItemStack _setstack3 = new ItemStack(Items.NETHER_STAR).copy();
				_setstack3.setCount(1);
				_menu.getSlots().get(2).set(_setstack3);
				_player.containerMenu.broadcastChanges();
			}
		} else if ((entity instanceof Player _plrSlotItem && _plrSlotItem.containerMenu instanceof EuruModMenus.MenuAccessor _menu4 ? _menu4.getSlots().get(0).getItem() : ItemStack.EMPTY).getItem() == Items.EMERALD
				&& getAmountInGUISlot(entity, 0) == 2) {
			if (entity instanceof Player _player && _player.containerMenu instanceof EuruModMenus.MenuAccessor _menu) {
				ItemStack _setstack7 = new ItemStack(Items.DIAMOND).copy();
				_setstack7.setCount(1);
				_menu.getSlots().get(2).set(_setstack7);
				_player.containerMenu.broadcastChanges();
			}
		} else if ((entity instanceof Player _plrSlotItem && _plrSlotItem.containerMenu instanceof EuruModMenus.MenuAccessor _menu8 ? _menu8.getSlots().get(0).getItem() : ItemStack.EMPTY).getItem() == Items.EMERALD
				&& getAmountInGUISlot(entity, 0) == 3) {
			if (entity instanceof Player _player && _player.containerMenu instanceof EuruModMenus.MenuAccessor _menu) {
				ItemStack _setstack11 = new ItemStack(Items.NETHERITE_INGOT).copy();
				_setstack11.setCount(1);
				_menu.getSlots().get(2).set(_setstack11);
				_player.containerMenu.broadcastChanges();
			}
		} else if ((entity instanceof Player _plrSlotItem && _plrSlotItem.containerMenu instanceof EuruModMenus.MenuAccessor _menu12 ? _menu12.getSlots().get(0).getItem() : ItemStack.EMPTY).getItem() == Items.EMERALD
				&& getAmountInGUISlot(entity, 0) >= 4) {
			if (entity instanceof Player _player && _player.containerMenu instanceof EuruModMenus.MenuAccessor _menu) {
				ItemStack _setstack15 = new ItemStack(Items.NETHERITE_PICKAXE).copy();
				_setstack15.setCount(1);
				_menu.getSlots().get(2).set(_setstack15);
				_player.containerMenu.broadcastChanges();
			}
		} else {
			if (entity instanceof Player _player && _player.containerMenu instanceof EuruModMenus.MenuAccessor _menu) {
				ItemStack _setstack16 = new ItemStack(Items.NETHER_STAR).copy();
				_setstack16.setCount(0);
				_menu.getSlots().get(2).set(_setstack16);
				_player.containerMenu.broadcastChanges();
			}
		}
	}

	private static int getAmountInGUISlot(Entity entity, int sltid) {
		if (entity instanceof Player player && player.containerMenu instanceof EuruModMenus.MenuAccessor menuAccessor) {
			ItemStack stack = menuAccessor.getSlots().get(sltid).getItem();
			if (stack != null)
				return stack.getCount();
		}
		return 0;
	}
}