package net.wavedk.extrautilitiesreutilized.procedures;

import net.wavedk.extrautilitiesreutilized.init.EuruModMenus;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;

public class CreativeGenGUIThisGUIIsOpenedProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		if (entity instanceof Player _player && _player.containerMenu instanceof EuruModMenus.MenuAccessor _menu)
			_menu.sendMenuStateUpdate(_player, 0, "energySend", "100", true);
	}
}