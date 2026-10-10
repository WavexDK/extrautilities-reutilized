package net.wavedk.extrautilitiesreutilized.procedures;

import net.wavedk.extrautilitiesreutilized.network.EuruModVariables;

import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;

import javax.annotation.Nullable;

import java.io.File;

@EventBusSubscriber
public class PlayerGPTickUpdateProcedure {
	@SubscribeEvent
	public static void onPlayerTick(PlayerTickEvent.Post event) {
		execute(event, event.getEntity().level(), event.getEntity());
	}

	public static void execute(LevelAccessor world, Entity entity) {
		execute(null, world, entity);
	}

	private static void execute(@Nullable Event event, LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		double solarPanelCutOff = 0;
		com.google.gson.JsonObject cObj = new com.google.gson.JsonObject();
		com.google.gson.JsonObject catobj = new com.google.gson.JsonObject();
		com.google.gson.JsonObject obj = new com.google.gson.JsonObject();
		File cfile = new File("");
		boolean isEquippedCurios = false;
		boolean isFlying = false;
		Entity cEntity = null;
		if (entity.getData(EuruModVariables.PLAYER_VARIABLES).playerGPTickUpdateCounter < 5) {
			{
				EuruModVariables.PlayerVariables _vars = entity.getData(EuruModVariables.PLAYER_VARIABLES);
				_vars.playerGPTickUpdateCounter = entity.getData(EuruModVariables.PLAYER_VARIABLES).playerGPTickUpdateCounter + 1;
				_vars.playerGP_Total_SI = entity.getData(EuruModVariables.PLAYER_VARIABLES).playerGP_Total;
				_vars.playerGP_Used_SI = entity.getData(EuruModVariables.PLAYER_VARIABLES).playerGP_Used;
				_vars.markSyncDirty();
			}
			GPOverlayTickProcedure.execute(world, entity);
			{
				EuruModVariables.PlayerVariables _vars = entity.getData(EuruModVariables.PLAYER_VARIABLES);
				_vars.updateMultipliers = false;
				_vars.markSyncDirty();
			}
		} else {
			UpdateGPProcedure.execute(world, entity);
		}
		if (entity.getPersistentData().getDouble("canFlyCounterAngelRing") > 0) {
			entity.getPersistentData().putDouble("canFlyCounterAngelRing", (entity.getPersistentData().getDouble("canFlyCounterAngelRing") - 1));
			if (!entity.getPersistentData().getBoolean("canFlyAR")) {
				entity.getPersistentData().putBoolean("canFlyAR", true);
				if (entity instanceof Player _player) {
					_player.getAbilities().mayfly = true;
					_player.onUpdateAbilities();
				}
			}
		} else if (entity.getPersistentData().getBoolean("canFlyAR")) {
			entity.getPersistentData().putBoolean("canFlyAR", false);
			if (entity instanceof Player _player) {
				_player.getAbilities().mayfly = (true == false);
				_player.onUpdateAbilities();
			}
		}
		{
			EuruModVariables.PlayerVariables _vars = entity.getData(EuruModVariables.PLAYER_VARIABLES);
			_vars.updateAB1 = true;
			_vars.updateab2 = true;
			_vars.markSyncDirty();
		}
	}
}