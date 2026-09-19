package net.wavedk.extrautilitiesreutilized.procedures;

import net.wavedk.extrautilitiesreutilized.init.EuruModItems;

import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.common.extensions.ILevelExtension;
import net.neoforged.neoforge.capabilities.Capabilities;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.BlockPos;

public class WirelessFEHeatingCoilSyncProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, double slot) {
		ItemStack cItem = ItemStack.EMPTY;
		String sbId = "";
		if ((itemFromBlockInventory(world, BlockPos.containing(x, y, z), (int) slot).copy()).getItem() == EuruModItems.WIRELESS_RF_HEATING_COIL.get()) {
			cItem = (itemFromBlockInventory(world, BlockPos.containing(x, y, z), (int) slot).copy()).copy();
			if ((getBlockNBTString(world, BlockPos.containing(x, y, z), "syncedBlock_id")).isEmpty()) {
				for (int _i1 = 0; _i1 < 32; _i1++) {
					sbId = sbId + "" + Mth.nextInt(RandomSource.create(), 0, 9);
				}
				if (!world.isClientSide()) {
					BlockPos _bp = BlockPos.containing(x, y, z);
					BlockEntity _blockEntity = world.getBlockEntity(_bp);
					BlockState _bs = world.getBlockState(_bp);
					if (_blockEntity != null) {
						_blockEntity.getPersistentData().putString("syncedBlock_id", sbId);
					}
					if (world instanceof Level _level)
						_level.sendBlockUpdated(_bp, _bs, _bs, 3);
				}
			} else {
				sbId = getBlockNBTString(world, BlockPos.containing(x, y, z), "syncedBlock_id");
			}
			{
				final String _tagName = "syncedBlock";
				final String _tagValue = ((x + ",") + "" + (y + ",") + (z + ",") + ((Level) world).dimension().location().toString());
				CustomData.update(DataComponents.CUSTOM_DATA, cItem, tag -> tag.putString(_tagName, _tagValue));
			}
			{
				final String _tagName = "syncedBlock_id";
				final String _tagValue = sbId;
				CustomData.update(DataComponents.CUSTOM_DATA, cItem, tag -> tag.putString(_tagName, _tagValue));
			}
			if (world instanceof ILevelExtension _ext && _ext.getCapability(Capabilities.ItemHandler.BLOCK, BlockPos.containing(x, y, z), null) instanceof IItemHandlerModifiable _itemHandlerModifiable) {
				ItemStack _setstack = new ItemStack(EuruModItems.WIRELESS_RF_HEATING_COIL.get()).copy();
				_setstack.setCount(0);
				_itemHandlerModifiable.setStackInSlot(0, _setstack);
			}
			if (world instanceof ILevelExtension _ext && _ext.getCapability(Capabilities.ItemHandler.BLOCK, BlockPos.containing(x, y, z), null) instanceof IItemHandlerModifiable _itemHandlerModifiable) {
				ItemStack _setstack = cItem.copy();
				_setstack.setCount(1);
				_itemHandlerModifiable.setStackInSlot(1, _setstack);
			}
		}
	}

	private static ItemStack itemFromBlockInventory(LevelAccessor world, BlockPos pos, int slot) {
		if (world instanceof ILevelExtension ext) {
			IItemHandler itemHandler = ext.getCapability(Capabilities.ItemHandler.BLOCK, pos, null);
			if (itemHandler != null)
				return itemHandler.getStackInSlot(slot);
		}
		return ItemStack.EMPTY;
	}

	private static String getBlockNBTString(LevelAccessor world, BlockPos pos, String tag) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity != null)
			return blockEntity.getPersistentData().getString(tag);
		return "";
	}
}