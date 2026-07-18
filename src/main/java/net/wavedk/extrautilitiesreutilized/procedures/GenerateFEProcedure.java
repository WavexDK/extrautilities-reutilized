package net.wavedk.extrautilitiesreutilized.procedures;

import net.wavedk.extrautilitiesreutilized.block.entity.IEnergyReceiver;

import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.BlockPos;

public class GenerateFEProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, ItemStack itemstack, double feSpeedDep, String worldIDDep) {
		if (worldIDDep == null)
			return;
		String gen = "";
		String provWorldID = "";
		String getDep = "";
		double feSpeed = 0;
		gen = x + "" + y + z + (world instanceof Level _lvl ? _lvl.dimension() : (world instanceof WorldGenLevel _wgl ? _wgl.getLevel().dimension() : Level.OVERWORLD));
		gen = (BuiltInRegistries.ITEM.getKey(itemstack.getItem()).toString()).replace(" ", "") + "BlockEntity";
		feSpeed = feSpeedDep;
		provWorldID = worldIDDep;
		if (world.getServer() != null) {
			LevelAccessor _origWorld = world;
			for (ServerLevel worlditerator : world.getServer().getAllLevels()) {
				world = worlditerator;
				if ((worldIDDep).equals("" + (world instanceof Level _lvl ? _lvl.dimension() : (world instanceof WorldGenLevel _wgl ? _wgl.getLevel().dimension() : Level.OVERWORLD)))) {
					if (world.getBlockEntity(new BlockPos((int) x, (int) y, (int) z)) instanceof IEnergyReceiver be) {
						be.addEnergy((int) feSpeed);
					}
				}
			}
			world = _origWorld;
		}
	}
}