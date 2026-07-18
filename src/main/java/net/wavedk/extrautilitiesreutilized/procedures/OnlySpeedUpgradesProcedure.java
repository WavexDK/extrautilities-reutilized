package net.wavedk.extrautilitiesreutilized.procedures;

import net.wavedk.extrautilitiesreutilized.init.EuruModItems;

import net.minecraft.world.item.ItemStack;

public class OnlySpeedUpgradesProcedure {
	public static boolean execute(ItemStack itemstack) {
		if (itemstack.getItem() == EuruModItems.SPEED_UPGRADE.get() || itemstack.getItem() == EuruModItems.MAGICAL_SPEED_UPGRADE.get() || itemstack.getItem() == EuruModItems.ULTIMATE_SPEED_UPGRADE.get()) {
			return false;
		}
		return true;
	}
}