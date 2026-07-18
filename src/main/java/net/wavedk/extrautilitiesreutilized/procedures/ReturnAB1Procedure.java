package net.wavedk.extrautilitiesreutilized.procedures;

import net.wavedk.extrautilitiesreutilized.network.EuruModVariables;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.chat.Component;

public class ReturnAB1Procedure {
	public static String execute(Entity entity) {
		if (entity == null)
			return "";
		if (entity.getData(EuruModVariables.PLAYER_VARIABLES).changeAB) {
			if (entity.getData(EuruModVariables.PLAYER_VARIABLES).updateAB1) {
				{
					EuruModVariables.PlayerVariables _vars = entity.getData(EuruModVariables.PLAYER_VARIABLES);
					_vars.updateAB1 = false;
					_vars.markSyncDirty();
				}
				if (entity instanceof Player _player && !_player.level().isClientSide())
					_player.displayClientMessage(Component.literal(""), true);
				{
					EuruModVariables.PlayerVariables _vars = entity.getData(EuruModVariables.PLAYER_VARIABLES);
					_vars.oldAB1 = entity.getData(EuruModVariables.PLAYER_VARIABLES).playerAB1;
					_vars.markSyncDirty();
				}
				return entity.getData(EuruModVariables.PLAYER_VARIABLES).playerAB1;
			}
			return entity.getData(EuruModVariables.PLAYER_VARIABLES).oldAB1;
		}
		return "";
	}
}