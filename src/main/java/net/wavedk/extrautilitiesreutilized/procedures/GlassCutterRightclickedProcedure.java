package net.wavedk.extrautilitiesreutilized.procedures;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.Entity;

public class GlassCutterRightclickedProcedure {
	public static void execute(Entity entity, ItemStack itemstack) {
		if (entity == null)
			return;
		net.minecraft.world.entity.Entity _jerEntity1 = entity;
		net.minecraft.world.item.ItemStack _jerStack1 = itemstack.copy();
		if (_jerEntity1 != null && !_jerStack1.isEmpty())
			EuruModJerIntegration.jerOpenJei(_jerEntity1, net.minecraft.core.registries.BuiltInRegistries.ITEM.getKey(_jerStack1.getItem()).toString(), 0);
	}
}