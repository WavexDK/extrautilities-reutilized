package net.wavedk.extrautilitiesreutilized.procedures;

import net.wavedk.extrautilitiesreutilized.network.EuruModVariables;
import net.wavedk.extrautilitiesreutilized.init.EuruModItems;

import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.core.component.DataComponents;

import javax.annotation.Nullable;

@EventBusSubscriber
public class RingItemInHandTickProcedure {
	@SubscribeEvent
	public static void onPlayerTick(PlayerTickEvent.Post event) {
		execute(event, event.getEntity());
	}

	public static void execute(Entity entity) {
		execute(null, entity);
	}

	private static void execute(@Nullable Event event, Entity entity) {
		if (entity == null)
			return;
		if ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == EuruModItems.ANGEL_RING.get()
				|| (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == EuruModItems.SQUID_RING.get()
				|| (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == EuruModItems.CHICKEN_RING.get()) {
			if (!entity.getData(EuruModVariables.PLAYER_VARIABLES).playerGPChecking) {
				if (entity.getData(EuruModVariables.PLAYER_VARIABLES).playerGP_Used > 0) {
					entity.getPersistentData().putBoolean("sendingGP-fromRing", true);
					{
						EuruModVariables.PlayerVariables _vars = entity.getData(EuruModVariables.PLAYER_VARIABLES);
						_vars.playerAB1 = "Grid Power: " + entity.getData(EuruModVariables.PLAYER_VARIABLES).playerGP_Used + "/" + entity.getData(EuruModVariables.PLAYER_VARIABLES).playerGP_Total;
						_vars.playerAB2 = "Using " + ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getDouble("gp-using")) + "GP";
						_vars.markSyncDirty();
					}
				}
			}
			{
				EuruModVariables.PlayerVariables _vars = entity.getData(EuruModVariables.PLAYER_VARIABLES);
				_vars.changeAB = true;
				_vars.markSyncDirty();
			}
		} else if (entity.getPersistentData().getBoolean("sendingGP-fromRing")) {
			{
				EuruModVariables.PlayerVariables _vars = entity.getData(EuruModVariables.PLAYER_VARIABLES);
				_vars.changeAB = false;
				_vars.markSyncDirty();
			}
			entity.getPersistentData().putBoolean("sendingGP-fromRing", false);
		}
	}
}