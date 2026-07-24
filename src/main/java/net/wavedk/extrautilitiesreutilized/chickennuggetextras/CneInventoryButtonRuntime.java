package net.wavedk.extrautilitiesreutilized.chickennuggetextras;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;

/**
 * Server side of Inventory Editor buttons.
 *
 * Security model: a button's command is authored at design time and written ONLY to
 * the server data pack (data/&lt;modid&gt;/cne_inventory_buttons/&lt;layout&gt;.json) - it is
 * never sent to the client. The client just renders the button and, on click, sends
 * the layout name + button id. The server looks the command up in its own copy and
 * runs it as the clicking player at op level 2, so a modified client can only trigger
 * the predefined buttons, never inject an arbitrary command.
 */
@EventBusSubscriber(modid = "euru")
public final class CneInventoryButtonRuntime {
	private record ButtonAction(String command, int permission) {
	}

	// layout registry name -> (button id -> action). Rebuilt on every server data reload.
	private static final Map<String, Map<Integer, ButtonAction>> BUTTONS = new ConcurrentHashMap<>();
	// Per-player time (game ticks) of the last accepted button click, for debouncing.
	private static final Map<UUID, Long> LAST_CLICK = new ConcurrentHashMap<>();
	private static final long DEBOUNCE_TICKS = 3L;

	private CneInventoryButtonRuntime() {
	}

	/** Client-side: tell the server a button was pressed. */
	public static void sendClick(String layout, int buttonId) {
		if (FMLEnvironment.dist != Dist.CLIENT) return;
		if (layout == null) return;
		PacketDistributor.sendToServer(new ButtonClickMessage(layout, buttonId));
	}

	@SubscribeEvent
	public static void onAddReloadListener(AddReloadListenerEvent event) {
		event.addListener((ResourceManagerReloadListener) CneInventoryButtonRuntime::reload);
	}

	private static void reload(ResourceManager manager) {
		BUTTONS.clear();
		manager.listResources("cne_inventory_buttons", path -> path.getPath().endsWith(".json")).forEach((location, resource) -> {
			try (InputStreamReader reader = new InputStreamReader(resource.open(), StandardCharsets.UTF_8)) {
				String path = location.getPath();
				int slash = path.lastIndexOf('/');
				String layoutName = path.substring(slash + 1, path.endsWith(".json") ? path.length() - 5 : path.length());
				JsonObject root = JsonParser.parseReader(reader).getAsJsonObject();
				if (!root.has("buttons") || !root.get("buttons").isJsonArray()) return;
				Map<Integer, ButtonAction> actions = new HashMap<>();
				for (JsonElement element : root.getAsJsonArray("buttons")) {
					if (!element.isJsonObject()) continue;
					JsonObject b = element.getAsJsonObject();
					int id = b.has("id") ? b.get("id").getAsInt() : -1;
					if (id < 0) continue;
					String command = b.has("command") ? b.get("command").getAsString() : "";
					int permission = b.has("permission") ? b.get("permission").getAsInt() : 2;
					permission = Math.max(0, Math.min(4, permission));
					actions.put(id, new ButtonAction(command, permission));
				}
				if (!actions.isEmpty()) BUTTONS.put(layoutName, actions);
			} catch (Exception ignored) {
			}
		});
	}

	private static void runButton(ServerPlayer player, String layout, int buttonId) {
		// These buttons only show in the player's own inventory / creative screen, both
		// backed by inventoryMenu. If a different container (chest, etc.) is open the
		// buttons are not visible, so ignore the click - it cannot be a real press.
		if (player.containerMenu != player.inventoryMenu) return;
		// Per-player debounce so an authored button cannot be spammed by a scripted client.
		long now = player.level().getGameTime();
		Long last = LAST_CLICK.get(player.getUUID());
		if (last != null && now - last < DEBOUNCE_TICKS) return;
		LAST_CLICK.put(player.getUUID(), now);

		Map<Integer, ButtonAction> actions = BUTTONS.get(layout);
		if (actions == null) return;
		ButtonAction action = actions.get(buttonId);
		if (action == null || action.command() == null || action.command().isEmpty()) return;
		MinecraftServer server = player.getServer();
		if (server == null) return;
		String command = action.command();
		if (command.startsWith("/")) command = command.substring(1);
		try {
			CommandSourceStack source = player.createCommandSourceStack().withPermission(action.permission());
			server.getCommands().performPrefixedCommand(source, command);
		} catch (Exception ignored) {
		}
	}

	@EventBusSubscriber(modid = "euru", bus = EventBusSubscriber.Bus.MOD)
	public static final class Registration {
		@SubscribeEvent
		public static void register(RegisterPayloadHandlersEvent event) {
			event.registrar("euru").playToServer(ButtonClickMessage.TYPE, ButtonClickMessage.STREAM_CODEC, ButtonClickMessage::handleData);
		}
	}

	public record ButtonClickMessage(String layout, int buttonId) implements CustomPacketPayload {
		public static final CustomPacketPayload.Type<ButtonClickMessage> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("euru", "cne_inventory_button"));
		public static final StreamCodec<RegistryFriendlyByteBuf, ButtonClickMessage> STREAM_CODEC = StreamCodec.of((buffer, message) -> {
			buffer.writeUtf(message.layout);
			buffer.writeVarInt(message.buttonId);
		}, buffer -> new ButtonClickMessage(buffer.readUtf(), buffer.readVarInt()));

		@Override
		public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
			return TYPE;
		}

		public static void handleData(ButtonClickMessage message, IPayloadContext context) {
			if (context.flow() != PacketFlow.SERVERBOUND) return;
			context.enqueueWork(() -> {
				if (context.player() instanceof ServerPlayer player) {
					runButton(player, message.layout(), message.buttonId());
				}
			});
		}
	}
}
