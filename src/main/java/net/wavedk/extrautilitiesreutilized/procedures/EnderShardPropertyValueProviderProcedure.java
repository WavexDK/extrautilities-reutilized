package net.wavedk.extrautilitiesreutilized.procedures;

import net.minecraft.world.item.ItemStack;

public class EnderShardPropertyValueProviderProcedure {
	public static double execute(ItemStack itemstack) {
		return itemstack.getCount();
	}
}