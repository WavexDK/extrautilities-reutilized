package net.wavedk.extrautilitiesreutilized.procedures;

import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.common.extensions.ILevelExtension;
import net.neoforged.neoforge.capabilities.Capabilities;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockPos;

public class TrashChestGUIThisGUIIsClosedProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		double n = 0;
		n = 0;
		for (int _i1 = 0; _i1 < 27; _i1++) {
			if (world instanceof ILevelExtension _ext && _ext.getCapability(Capabilities.ItemHandler.BLOCK, BlockPos.containing(x, y, z), null) instanceof IItemHandlerModifiable _itemHandlerModifiable) {
				ItemStack _setstack = new ItemStack(Blocks.POLISHED_ANDESITE).copy();
				_setstack.setCount(0);
				_itemHandlerModifiable.setStackInSlot((int) n, _setstack);
			}
			n = n + 1;
		}
	}
}