package net.wavedk.extrautilitiesreutilized.procedures;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.BlockPos;

public class ReturnEnchanterErrrorProcedure {
	public static String execute(LevelAccessor world, double x, double y, double z) {
		if ((getBlockNBTString(world, BlockPos.containing(x, y, z), "errorMessage")).equals("notEnoughFE")) {
			return "Not enough FE available! (requires " + getBlockNBTNumber(world, BlockPos.containing(x, y, z), "fe_required") + "FE)";
		} else if ((getBlockNBTString(world, BlockPos.containing(x, y, z), "errorMessage")).equals("tmGP")) {
			return "Not enough Grid Power available!";
		} else if ((getBlockNBTString(world, BlockPos.containing(x, y, z), "errorMessage")).equals("notEnoughLapis")) {
			return "Not enough "
					+ ((new ItemStack(BuiltInRegistries.ITEM.get(ResourceLocation.parse(((getBlockNBTString(world, BlockPos.containing(x, y, z), "lapis_input"))).toLowerCase(java.util.Locale.ENGLISH)))).getDisplayName().getString()).replace("[", ""))
							.replace("]", "")
					+ " available! (requires " + getBlockNBTNumber(world, BlockPos.containing(x, y, z), "lapis_required") + " "
					+ ((new ItemStack(BuiltInRegistries.ITEM.get(ResourceLocation.parse(((getBlockNBTString(world, BlockPos.containing(x, y, z), "lapis_input"))).toLowerCase(java.util.Locale.ENGLISH)))).getDisplayName().getString()).replace("[", ""))
							.replace("]", "")
					+ ")";
		} else if ((getBlockNBTString(world, BlockPos.containing(x, y, z), "errorMessage")).equals("outputFilled")) {
			return "The output slot is filled!";
		}
		return "";
	}

	private static String getBlockNBTString(LevelAccessor world, BlockPos pos, String tag) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity != null)
			return blockEntity.getPersistentData().getString(tag);
		return "";
	}

	private static double getBlockNBTNumber(LevelAccessor world, BlockPos pos, String tag) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity != null)
			return blockEntity.getPersistentData().getDouble(tag);
		return -1;
	}
}