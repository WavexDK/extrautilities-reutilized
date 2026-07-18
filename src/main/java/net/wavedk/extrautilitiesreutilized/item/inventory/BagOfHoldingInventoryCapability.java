package net.wavedk.extrautilitiesreutilized.item.inventory;

import net.wavedk.extrautilitiesreutilized.world.inventory.BagOfHoldingGUIMenu;
import net.wavedk.extrautilitiesreutilized.init.EuruModItems;

import net.neoforged.neoforge.items.ComponentItemHandler;
import net.neoforged.neoforge.event.entity.item.ItemTossEvent;
import net.neoforged.neoforge.common.MutableDataComponentHolder;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.component.DataComponents;

import javax.annotation.Nonnull;

@EventBusSubscriber
public class BagOfHoldingInventoryCapability extends ComponentItemHandler {
	@SubscribeEvent
	public static void onItemDropped(ItemTossEvent event) {
		if (event.getEntity().getItem().getItem() == EuruModItems.BAG_OF_HOLDING.get()) {
			Player player = event.getPlayer();
			if (player.containerMenu instanceof BagOfHoldingGUIMenu)
				player.closeContainer();
		}
	}

	public BagOfHoldingInventoryCapability(MutableDataComponentHolder parent) {
		super(parent, DataComponents.CONTAINER, 45);
	}

	@Override
	public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
		return stack.getItem() != EuruModItems.BAG_OF_HOLDING.get();
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return super.getStackInSlot(slot).copy();
	}
}