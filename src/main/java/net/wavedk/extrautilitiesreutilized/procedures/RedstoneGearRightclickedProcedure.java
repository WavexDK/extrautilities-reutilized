package net.wavedk.extrautilitiesreutilized.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.Entity;
import net.minecraft.core.BlockPos;

public class RedstoneGearRightclickedProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		net.minecraft.world.entity.Entity _jerEntity2 = entity;
		net.minecraft.world.item.ItemStack _jerStack2 = (new ItemStack((world.getBlockState(BlockPos.containing(x, y, z))).getBlock())).copy();
		if (_jerEntity2 != null && !_jerStack2.isEmpty())
			EuruModJerIntegration.jerOpenJei(_jerEntity2, net.minecraft.core.registries.BuiltInRegistries.ITEM.getKey(_jerStack2.getItem()).toString(), 1);
	}
}