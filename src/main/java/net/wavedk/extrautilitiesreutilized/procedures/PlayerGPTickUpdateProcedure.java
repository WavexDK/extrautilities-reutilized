package net.wavedk.extrautilitiesreutilized.procedures;

import net.wavedk.extrautilitiesreutilized.network.EuruModVariables;
import net.wavedk.extrautilitiesreutilized.init.EuruModItems;
import net.wavedk.extrautilitiesreutilized.EuruMod;

import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.GameType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.Minecraft;

import javax.annotation.Nullable;

import java.io.IOException;
import java.io.FileReader;
import java.io.File;
import java.io.BufferedReader;

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
		isEquippedCurios = false;
		if (hasEntityInInventory(entity, new ItemStack(EuruModItems.ANGEL_RING.get()))) {
			isEquippedCurios = true;
		}
		if (entity instanceof Player player3) {
			IItemHandler inventory3 = EuruMod.CuriosApiHelper.getCuriosInventory(player3);
			if (inventory3 != null) {
				for (int i = 0; i < inventory3.getSlots(); i++) {
					ItemStack itemstackiterator = inventory3.getStackInSlot(i);
					if (itemstackiterator.getItem() == EuruModItems.ANGEL_RING.get()) {
						isEquippedCurios = true;
						break;
					}
				}
			}
		}
		cEntity = entity;
		isFlying = cEntity instanceof net.minecraft.world.entity.player.Player player && player.getAbilities().mayfly;;
		if (!entity.getData(EuruModVariables.PLAYER_VARIABLES).playerGPChecking) {
			if (isEquippedCurios) {
				if (isFlying) {
					if (entity.getData(EuruModVariables.PLAYER_VARIABLES).ringFlying) {
						if (entity.getData(EuruModVariables.PLAYER_VARIABLES).playerGP_Used > entity.getData(EuruModVariables.PLAYER_VARIABLES).playerGP_Total) {
							if (cEntity instanceof net.minecraft.world.entity.player.Player player) {
								player.getAbilities().mayfly = false;
								player.getAbilities().flying = false;
								player.onUpdateAbilities();
							}
							{
								EuruModVariables.PlayerVariables _vars = entity.getData(EuruModVariables.PLAYER_VARIABLES);
								_vars.ringFlying = false;
								_vars.markSyncDirty();
							}
						}
					}
				} else {
					if (entity.getData(EuruModVariables.PLAYER_VARIABLES).playerGP_Used <= entity.getData(EuruModVariables.PLAYER_VARIABLES).playerGP_Total) {
						if (entity instanceof Player _player) {
							_player.getAbilities().mayfly = true;
							_player.onUpdateAbilities();
						}
						{
							EuruModVariables.PlayerVariables _vars = entity.getData(EuruModVariables.PLAYER_VARIABLES);
							_vars.ringFlying = true;
							_vars.markSyncDirty();
						}
					}
				}
			} else if (isFlying && entity.getData(EuruModVariables.PLAYER_VARIABLES).ringFlying) {
				if (cEntity instanceof net.minecraft.world.entity.player.Player player) {
					player.getAbilities().mayfly = false;
					player.getAbilities().flying = false;
					player.onUpdateAbilities();
				}
				{
					EuruModVariables.PlayerVariables _vars = entity.getData(EuruModVariables.PLAYER_VARIABLES);
					_vars.ringFlying = false;
					_vars.markSyncDirty();
				}
			}
		}
		if ((isEquippedCurios || isEquippedCurios) && entity.getData(EuruModVariables.PLAYER_VARIABLES).playerGPChecking && getEntityGameType(entity) == GameType.SURVIVAL) {
			cfile = new File((FMLPaths.GAMEDIR.get().toString() + "/config/euru/"), File.separator + "euru_unified_config.json");
			{
				try {
					BufferedReader bufferedReader = new BufferedReader(new FileReader(cfile));
					StringBuilder jsonstringbuilder = new StringBuilder();
					String line;
					while ((line = bufferedReader.readLine()) != null) {
						jsonstringbuilder.append(line);
					}
					bufferedReader.close();
					obj = new com.google.gson.Gson().fromJson(jsonstringbuilder.toString(), com.google.gson.JsonObject.class);
					catobj = obj.get("general").getAsJsonObject();
					cObj = catobj.get((BuiltInRegistries.ITEM.getKey(EuruModItems.ANGEL_RING.get()).toString())).getAsJsonObject();
					{
						EuruModVariables.PlayerVariables _vars = entity.getData(EuruModVariables.PLAYER_VARIABLES);
						_vars.playerGP_Used_Update = entity.getData(EuruModVariables.PLAYER_VARIABLES).playerGP_Used_Update + cObj.get("gp_needed").getAsDouble();
						_vars.markSyncDirty();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		{
			EuruModVariables.PlayerVariables _vars = entity.getData(EuruModVariables.PLAYER_VARIABLES);
			_vars.updateAB1 = true;
			_vars.updateab2 = true;
			_vars.markSyncDirty();
		}
	}

	private static boolean hasEntityInInventory(Entity entity, ItemStack itemstack) {
		if (entity instanceof Player player)
			return player.getInventory().contains(stack -> !stack.isEmpty() && ItemStack.isSameItem(stack, itemstack));
		return false;
	}

	private static GameType getEntityGameType(Entity entity) {
		if (entity instanceof ServerPlayer serverPlayer) {
			return serverPlayer.gameMode.getGameModeForPlayer();
		} else if (entity instanceof Player player && player.level().isClientSide()) {
			PlayerInfo playerInfo = Minecraft.getInstance().getConnection().getPlayerInfo(player.getGameProfile().getId());
			if (playerInfo != null)
				return playerInfo.getGameMode();
		}
		return null;
	}
}