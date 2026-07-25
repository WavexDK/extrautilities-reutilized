package net.wavedk.extrautilitiesreutilized.item;

import net.wavedk.extrautilitiesreutilized.procedures.WateringCanItemIsCraftedsmeltedProcedure;
import net.wavedk.extrautilitiesreutilized.procedures.WateringCanItemInHandTickProcedure;

import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;

public class WateringCanItem extends Item {
	public WateringCanItem() {
		super(new Item.Properties().durability(256));
	}

	@Override
	public void onCraftedBy(ItemStack itemstack, Level world, Player entity) {
		super.onCraftedBy(itemstack, world, entity);
		WateringCanItemIsCraftedsmeltedProcedure.execute(itemstack);
	}

	@Override
	public void inventoryTick(ItemStack itemstack, Level world, Entity entity, int slot, boolean selected) {
		super.inventoryTick(itemstack, world, entity, slot, selected);
		if (selected)
			WateringCanItemInHandTickProcedure.execute(world, entity.getX(), entity.getY(), entity.getZ(), entity, itemstack);
	}
}