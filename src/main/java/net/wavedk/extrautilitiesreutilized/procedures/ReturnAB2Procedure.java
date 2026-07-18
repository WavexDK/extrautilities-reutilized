package net.wavedk.extrautilitiesreutilized.procedures;

import net.wavedk.extrautilitiesreutilized.network.EuruModVariables;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.chat.Component;

public class ReturnAB2Procedure {
	public static String execute(Entity entity) {
		if (entity == null)
			return "";
		if (entity.getData(EuruModVariables.PLAYER_VARIABLES).changeAB) {
			if (entity.getData(EuruModVariables.PLAYER_VARIABLES).updateab2) {
				{
					EuruModVariables.PlayerVariables _vars = entity.getData(EuruModVariables.PLAYER_VARIABLES);
					_vars.updateab2 = false;
					_vars.markSyncDirty();
				}
				if (entity instanceof Player _player && !_player.level().isClientSide())
					_player.displayClientMessage(Component.literal(""), true);
				{
					EuruModVariables.PlayerVariables _vars = entity.getData(EuruModVariables.PLAYER_VARIABLES);
					_vars.oldAB2 = entity.getData(EuruModVariables.PLAYER_VARIABLES).playerAB2;
					_vars.markSyncDirty();
				}
				return entity.getData(EuruModVariables.PLAYER_VARIABLES).playerAB2;
			}
			return entity.getData(EuruModVariables.PLAYER_VARIABLES).oldAB2;
		}
		return "";
	}
}