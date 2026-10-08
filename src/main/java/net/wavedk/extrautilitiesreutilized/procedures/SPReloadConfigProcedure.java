package net.wavedk.extrautilitiesreutilized.procedures;

import net.wavedk.extrautilitiesreutilized.EuruMod;

import net.minecraft.world.level.LevelAccessor;

public class SPReloadConfigProcedure {
	public static void execute(LevelAccessor world) {
		EuruMod.queueServerWork(1, () -> {
			EURUUnifiedConfigManagerProcedure.execute();
		});
	}
}