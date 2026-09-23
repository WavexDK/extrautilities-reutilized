package net.wavedk.extrautilitiesreutilized.procedures;

import net.wavedk.extrautilitiesreutilized.init.EuruModMenus;

import net.neoforged.neoforge.items.ItemHandlerHelper;

import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;

public class ShadyMerchantNethPickButtonClickedProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		ItemStack cItem = ItemStack.EMPTY;
		{
			net.wavedk.extrautilitiesreutilized.chickennuggetextras.CneExtrasRuntime.playSoundAtEntity(entity, "ui.button.click", "NEUTRAL", (float) 1, (float) 1, false, false);
		}
		cItem = new ItemStack(Items.EMERALD).copy();
		cItem.setCount(4);
		if (hasEntityInInventory(entity, cItem) && getAmountInGUISlot(entity, 0) == 0) {
			if (entity instanceof Player _player) {
				ItemStack _stktoremove = new ItemStack(Items.EMERALD);
				_player.getInventory().clearOrCountMatchingItems(p -> _stktoremove.getItem() == p.getItem(), 4, _player.inventoryMenu.getCraftSlots());
			}
			if (entity instanceof Player _player && _player.containerMenu instanceof EuruModMenus.MenuAccessor _menu) {
				ItemStack _setstack5 = new ItemStack(Items.EMERALD).copy();
				_setstack5.setCount(4);
				_menu.getSlots().get(0).set(_setstack5);
				_player.containerMenu.broadcastChanges();
			}
		} else {
			if (entity instanceof Player _player) {
				ItemStack _setstack = (entity instanceof Player _plrSlotItem && _plrSlotItem.containerMenu instanceof EuruModMenus.MenuAccessor _menu6 ? _menu6.getSlots().get(0).getItem() : ItemStack.EMPTY).copy();
				_setstack.setCount(getAmountInGUISlot(entity, 0));
				ItemHandlerHelper.giveItemToPlayer(_player, _setstack);
			}
			if (entity instanceof Player _player && _player.containerMenu instanceof EuruModMenus.MenuAccessor _menu) {
				ItemStack _setstack9 = new ItemStack(Items.EMERALD).copy();
				_setstack9.setCount(0);
				_menu.getSlots().get(0).set(_setstack9);
				_player.containerMenu.broadcastChanges();
			}
			ShadyMerchantNethPickButtonClickedProcedure.execute(entity);
		}
	}

	private static boolean hasEntityInInventory(Entity entity, ItemStack itemstack) {
		if (entity instanceof Player player)
			return player.getInventory().contains(stack -> !stack.isEmpty() && ItemStack.isSameItem(stack, itemstack));
		return false;
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