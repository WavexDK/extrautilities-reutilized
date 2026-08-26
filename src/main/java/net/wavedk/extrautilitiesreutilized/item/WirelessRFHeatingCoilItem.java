package net.wavedk.extrautilitiesreutilized.item;

import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.network.chat.Component;

import java.util.List;

public class WirelessRFHeatingCoilItem extends Item {
	public WirelessRFHeatingCoilItem() {
		super(new Item.Properties().stacksTo(2));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack itemstack, Item.TooltipContext context, List<Component> list, TooltipFlag flag) {
		super.appendHoverText(itemstack, context, list, flag);
		list.add(Component.translatable("item.euru.wireless_rf_heating_coil.description_0"));
		list.add(Component.translatable("item.euru.wireless_rf_heating_coil.description_1"));
	}
}