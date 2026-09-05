package net.wavedk.extrautilitiesreutilized.procedures;

import net.wavedk.extrautilitiesreutilized.network.EuruModVariables;

import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;

import javax.annotation.Nullable;

import java.io.File;

@EventBusSubscriber
public class UpdateConfigProcedure {
	@SubscribeEvent
	public static void onWorldTick(LevelTickEvent.Post event) {
		execute(event);
	}

	public static void execute() {
		execute(null);
	}

	private static void execute(@Nullable Event event) {
		File config = new File("");
		File cfile = new File("");
		com.google.gson.JsonObject uobj = new com.google.gson.JsonObject();
		com.google.gson.JsonObject fobj = new com.google.gson.JsonObject();
		if (EuruModVariables.configUpdateCounter > Mth.nextInt(RandomSource.create(), (int) EuruModVariables.unified_config.get("range-configUpdate-min").getAsDouble(),
				(int) EuruModVariables.unified_config.get("range-configUpdate-max").getAsDouble())) {
			EuruModVariables.configUpdateCounter = 0;
			EURUUnifiedConfigManagerProcedure.execute();
		}
		EuruModVariables.configUpdateCounter = EuruModVariables.configUpdateCounter + 1;
	}
}