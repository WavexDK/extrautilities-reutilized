package net.wavedk.extrautilitiesreutilized.item;

import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Item;

public class EnchantedIngotItem extends Item {
	public EnchantedIngotItem() {
		super(new Item.Properties().rarity(Rarity.UNCOMMON));
	}
}