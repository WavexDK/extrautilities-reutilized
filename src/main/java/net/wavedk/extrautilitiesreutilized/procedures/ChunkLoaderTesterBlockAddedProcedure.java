package net.wavedk.extrautilitiesreutilized.procedures;

import net.wavedk.extrautilitiesreutilized.init.EuruModBlocks;

import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.chat.Component;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.BlockPos;

import javax.annotation.Nullable;

import java.io.IOException;
import java.io.FileReader;
import java.io.File;
import java.io.BufferedReader;

@EventBusSubscriber
public class ChunkLoaderTesterBlockAddedProcedure {
	@SubscribeEvent
	public static void onBlockPlace(BlockEvent.EntityPlaceEvent event) {
		execute(event, event.getLevel(), event.getPos().getX(), event.getPos().getY(), event.getPos().getZ(), event.getEntity());
	}

	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		execute(null, world, x, y, z, entity);
	}

	private static void execute(@Nullable Event event, LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		File file = new File("");
		com.google.gson.JsonObject obj = new com.google.gson.JsonObject();
		com.google.gson.JsonObject bobj = new com.google.gson.JsonObject();
		com.google.gson.JsonObject tobj = new com.google.gson.JsonObject();
		if ((world.getBlockState(BlockPos.containing(x, y, z))).getBlock() == EuruModBlocks.CHUNK_LOADER_TESTER.get()) {
			file = new File((FMLPaths.GAMEDIR.get().toString() + "/config/euru/"), File.separator + "euru_unified_config.json");
			{
				try {
					BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
					StringBuilder jsonstringbuilder = new StringBuilder();
					String line;
					while ((line = bufferedReader.readLine()) != null) {
						jsonstringbuilder.append(line);
					}
					bufferedReader.close();
					obj = new com.google.gson.Gson().fromJson(jsonstringbuilder.toString(), com.google.gson.JsonObject.class);
					tobj = obj.get("general").getAsJsonObject();
					bobj = tobj.get((BuiltInRegistries.BLOCK.getKey(EuruModBlocks.CHUNK_LOADER_TESTER.get()).toString())).getAsJsonObject();
					if (!bobj.get("enabled").getAsBoolean()) {
						if (entity instanceof Player _player && !_player.level().isClientSide())
							_player.displayClientMessage(Component.literal("\u00A7cSorry! This block is disabled according to the config. Right click to un-craft this item or contact admins for help."), false);
						if (event instanceof ICancellableEvent _cancellable) {
							_cancellable.setCanceled(true);
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}