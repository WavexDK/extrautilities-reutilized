package net.wavedk.extrautilitiesreutilized.procedures;

import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.component.DataComponents;

public class CursedLassoGhastSpecialInformationProcedure {
	public static String execute(ItemStack itemstack) {
		if (!(itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getString("entityType")).equals("")) {
			return "\u00A77"
					+ (((itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getString("entityType")).substring(0, 1)).toUpperCase() + ""
							+ (itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getString("entityType")).substring(1)).replace("_", " ")
					+ "\n" + ("\u00A77Health: " + itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getDouble("healthMin") + "/"
							+ itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getDouble("healthMax"));
		}
		return "\u00A77Ghast";
	}
}