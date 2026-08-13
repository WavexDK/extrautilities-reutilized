package net.wavedk.extrautilitiesreutilized.item;

import net.wavedk.extrautilitiesreutilized.init.EuruModBlocks;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.BlockItem;

public class TransferCableItem extends BlockItem {
	public TransferCableItem() {
		super(EuruModBlocks.TRANSFER_CABLE.get(), new Item.Properties().stacksTo(64));
	}
}