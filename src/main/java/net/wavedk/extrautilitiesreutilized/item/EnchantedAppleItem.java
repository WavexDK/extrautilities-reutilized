package net.wavedk.extrautilitiesreutilized.item;

import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.entity.LivingEntity;

public class EnchantedAppleItem extends Item {
	public EnchantedAppleItem() {
		super(new Item.Properties().rarity(Rarity.UNCOMMON).food((new FoodProperties.Builder()).nutrition(8).saturationModifier(0.3f).alwaysEdible().build()));
	}

	@Override
	public int getUseDuration(ItemStack itemstack, LivingEntity livingEntity) {
		return 24;
	}
}