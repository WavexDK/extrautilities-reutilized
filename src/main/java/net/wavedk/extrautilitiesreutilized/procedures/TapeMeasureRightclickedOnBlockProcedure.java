package net.wavedk.extrautilitiesreutilized.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.Entity;
import net.minecraft.core.component.DataComponents;

public class TapeMeasureRightclickedOnBlockProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity, ItemStack itemstack) {
		if (entity == null)
			return;
		double cN = 0;
		double cX = 0;
		double cY = 0;
		double cZ = 0;
		if (!(itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getString("syncedBlock")).isEmpty()) {
			if ((itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getString("linked_dimension")).equals(entity.level().dimension().location().toString())) {
				cN = 1;
				{
					final String _tagName = "syncedBlock_secondary";
					final String _tagValue = ((x + ",") + "" + (y + ",") + z);
					CustomData.update(DataComponents.CUSTOM_DATA, itemstack, tag -> tag.putString(_tagName, _tagValue));
				}
			} else {
				{
					final String _tagName = "syncedBlock";
					final String _tagValue = "";
					CustomData.update(DataComponents.CUSTOM_DATA, itemstack, tag -> tag.putString(_tagName, _tagValue));
				}
				TapeMeasureRightclickedOnBlockProcedure.execute(world, x, y, z, entity, itemstack);
			}
		} else {
			{
				final String _tagName = "syncedBlock";
				final String _tagValue = ((x + ",") + "" + (y + ",") + z);
				CustomData.update(DataComponents.CUSTOM_DATA, itemstack, tag -> tag.putString(_tagName, _tagValue));
			}
			{
				final String _tagName = "linked_dimension";
				final String _tagValue = entity.level().dimension().location().toString();
				CustomData.update(DataComponents.CUSTOM_DATA, itemstack, tag -> tag.putString(_tagName, _tagValue));
			}
		}
	}
}