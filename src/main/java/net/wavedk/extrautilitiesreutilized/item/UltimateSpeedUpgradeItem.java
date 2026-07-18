package net.wavedk.extrautilitiesreutilized.item;

import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.network.chat.Component;

import java.util.List;

public class UltimateSpeedUpgradeItem extends Item {
	public UltimateSpeedUpgradeItem() {
		super(new Item.Properties().rarity(Rarity.UNCOMMON));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public boolean isFoil(ItemStack itemstack) {
		return true;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack itemstack, Item.TooltipContext context, List<Component> list, TooltipFlag flag) {
		super.appendHoverText(itemstack, context, list, flag);
		list.add(Component.translatable("item.euru.ultimate_speed_upgrade.description_0"));
		list.add(Component.translatable("item.euru.ultimate_speed_upgrade.description_1"));
		list.add(Component.translatable("item.euru.ultimate_speed_upgrade.description_2"));
	}
}