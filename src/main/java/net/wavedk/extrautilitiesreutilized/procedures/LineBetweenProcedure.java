package net.wavedk.extrautilitiesreutilized.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.particles.ParticleTypes;

public class LineBetweenProcedure {
	public static void execute(LevelAccessor world, double endX, double endY, double endZ, double spacing, double startX, double startY, double startZ) {
		double Steps = 0;
		double StepZ = 0;
		double StepY = 0;
		double StepX = 0;
		double Distance = 0;
		double cN = 0;
		Distance = Math.sqrt(Math.abs(startX - endX) * Math.abs(startX - endX) + Math.abs(startY - endY) * Math.abs(startY - endY) + Math.abs(startZ - endZ) * Math.abs(startZ - endZ));
		Steps = Math.round(Distance / spacing);
		if (Steps > 0) {
			StepX = (endX - startX) / Steps;
			StepY = (endY - startY) / Steps;
			StepZ = (endZ - startZ) / Steps;
			cN = 0;
			for (int _i1 = 0; _i1 < (int) (Steps + 1); _i1++) {
				if (world instanceof ServerLevel _level)
					_level.sendParticles(ParticleTypes.ELECTRIC_SPARK, (startX + StepX * cN), (startY + StepY * cN), (startZ + StepZ * cN), 1, 0, 0, 0, 0);
				cN = cN + 1;
			}
		}
	}
}