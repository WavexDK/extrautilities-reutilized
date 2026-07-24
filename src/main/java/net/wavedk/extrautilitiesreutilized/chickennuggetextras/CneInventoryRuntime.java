package net.wavedk.extrautilitiesreutilized.chickennuggetextras;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent;

/**
 * Renders the widgets defined by Inventory Editor elements into the vanilla
 * survival and creative inventories.
 *
 * Each Inventory Editor element writes a layout JSON to
 * assets/&lt;modid&gt;/cne_inventory_layouts/. This client runtime loads every such
 * file on resource reload and the CneInventoryScreenMixin calls renderComponents()
 * at the end of the inventory screen's render. Positions are offsets from the
 * inventory's top-left corner so they stay aligned at any GUI scale.
 */
@EventBusSubscriber(modid = "euru", value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public final class CneInventoryRuntime {
	// One list per render SURFACE: the real survival inventory screen, the creative menu's
	// own survival-inventory tab, and the creative menu's item-browsing tabs. A widget's
	// "gui" scope decides which surfaces it joins (see reload). Surfaces are: 0 = survival
	// screen, 1 = creative inventory tab, 2 = creative item tabs.
	private static final List<Widget> SURVIVAL = new ArrayList<>();
	private static final List<Widget> CREATIVE_INV = new ArrayList<>();
	private static final List<Widget> CREATIVE_ITEMS = new ArrayList<>();
	private static final Map<ResourceLocation, LivingEntity> ENTITY_CACHE = new HashMap<>();
	// Entity ids that are valid but not LivingEntity (tnt, arrow, ...): remember them so
	// we do not re-create a throwaway entity every frame for a misconfigured widget.
	private static final java.util.Set<ResourceLocation> NON_LIVING = new java.util.HashSet<>();

	private CneInventoryRuntime() {
	}

	@SubscribeEvent
	public static void registerReloadListener(RegisterClientReloadListenersEvent event) {
		event.registerReloadListener((ResourceManagerReloadListener) CneInventoryRuntime::reload);
	}

	private static void reload(ResourceManager manager) {
		SURVIVAL.clear();
		CREATIVE_INV.clear();
		CREATIVE_ITEMS.clear();
		ENTITY_CACHE.clear();
		NON_LIVING.clear();
		manager.listResources("cne_inventory_layouts", path -> path.getPath().endsWith(".json")).forEach((location, resource) -> {
			try (InputStreamReader reader = new InputStreamReader(resource.open(), StandardCharsets.UTF_8)) {
				JsonObject root = JsonParser.parseReader(reader).getAsJsonObject();
				String gui = root.has("gui") ? root.get("gui").getAsString() : "survival";
				String path = location.getPath();
				int slash = path.lastIndexOf('/');
				String layoutName = path.substring(slash + 1, path.endsWith(".json") ? path.length() - 5 : path.length());
				List<Widget> widgets = parseWidgets(root, layoutName);
				// "main inventory" = the player inventory wherever it shows (survival screen +
				// creative's inventory tab) but NOT the creative item-browsing tabs.
				if (gui.equals("survival") || gui.equals("both") || gui.equals("main inventory")) SURVIVAL.addAll(widgets);
				if (gui.equals("creative") || gui.equals("both") || gui.equals("main inventory")) CREATIVE_INV.addAll(widgets);
				if (gui.equals("creative") || gui.equals("both")) CREATIVE_ITEMS.addAll(widgets);
			} catch (Exception ignored) {
			}
		});
	}

	private static List<Widget> parseWidgets(JsonObject root, String layoutName) {
		List<Widget> widgets = new ArrayList<>();
		if (!root.has("components") || !root.get("components").isJsonArray()) return widgets;
		JsonArray array = root.getAsJsonArray("components");
		for (JsonElement element : array) {
			if (!element.isJsonObject()) continue;
			JsonObject c = element.getAsJsonObject();
			Widget widget = new Widget();
			widget.type = str(c, "type", "label");
			widget.x = intOf(c, "x", 0);
			widget.y = intOf(c, "y", 0);
			widget.value = str(c, "value", "");
			widget.color = str(c, "color", "#ffffff");
			widget.shadow = boolOf(c, "shadow", true);
			widget.w = intOf(c, "w", 16);
			widget.h = intOf(c, "h", 16);
			widget.u = intOf(c, "u", 0);
			widget.v = intOf(c, "v", 0);
			widget.texW = intOf(c, "texW", 16);
			widget.texH = intOf(c, "texH", 16);
			widget.scale = intOf(c, "scale", 30);
			widget.buttonId = intOf(c, "buttonId", -1);
			widget.sourceLayout = layoutName;
			widgets.add(widget);
		}
		return widgets;
	}

	// surface: 0 = survival inventory screen, 1 = creative inventory tab, 2 = creative item tabs.
	// (anchorX, anchorY) is the screen position of the inventory's top-left corner for this
	// surface - the mixin corrects it so the creative inventory tab lines up with the survival one.
	private static List<Widget> listForSurface(int surface) {
		return surface == 0 ? SURVIVAL : surface == 1 ? CREATIVE_INV : CREATIVE_ITEMS;
	}

	public static void renderComponents(GuiGraphics graphics, int surface, int anchorX, int anchorY, float posScaleX, float posScaleY, int mouseX, int mouseY) {
		List<Widget> list = listForSurface(surface);
		if (list.isEmpty()) return;
		Minecraft minecraft = Minecraft.getInstance();
		Font font = minecraft.font;
		// Map the survival layout onto this surface's inventory window: each component's POSITION is mapped
		// proportionally (X by the window width ratio posScaleX, Y by the height ratio posScaleY) so it keeps
		// its relative spot (a top-right component stays top-right), and its SIZE is scaled down by the height
		// ratio. On the survival screen both ratios are 1.0 -> the unchanged 1:1 path.
		float sizeScale = posScaleY;
		boolean scaled = sizeScale > 0.0F && (posScaleX != 1.0F || posScaleY != 1.0F);
		for (Widget widget : list) {
			if ("tooltip".equals(widget.type)) continue;
			int sx = scaled ? Math.round(anchorX + widget.x * posScaleX) : anchorX + widget.x;
			int sy = scaled ? Math.round(anchorY + widget.y * posScaleY) : anchorY + widget.y;
			boolean pushed = false;
			int px = sx, py = sy;
			if (scaled && sizeScale != 1.0F) {
				graphics.pose().pushPose();
				pushed = true;
				graphics.pose().translate(sx, sy, 0.0F);
				graphics.pose().scale(sizeScale, sizeScale, 1.0F);
				px = 0;
				py = 0;
			}
			try {
				switch (widget.type) {
					case "label" -> graphics.drawString(font, widget.value, px, py, parseColor(widget.color), widget.shadow);
					case "image" -> {
						ResourceLocation texture = ResourceLocation.tryParse(widget.value);
						if (texture != null) graphics.blit(texture, px, py, 0.0F, 0.0F, widget.w, widget.h, widget.w, widget.h);
					}
					case "sprite" -> {
						ResourceLocation texture = ResourceLocation.tryParse(widget.value);
						if (texture != null) graphics.blit(texture, px, py, (float) widget.u, (float) widget.v, widget.w, widget.h, widget.texW, widget.texH);
					}
					case "entity" -> renderEntity(graphics, minecraft, widget, px, py, mouseX, mouseY);
					case "button" -> {
						// Hover uses the same screen-space bounds as mouseClicked() so the highlight and the clickable
						// region match; the button draws at local px/py inside the per-widget pose.
						int bw = scaled ? Math.round(widget.w * sizeScale) : widget.w;
						int bh = scaled ? Math.round(widget.h * sizeScale) : widget.h;
						boolean hovered = mouseX >= sx && mouseX <= sx + bw && mouseY >= sy && mouseY <= sy + bh;
						ResourceLocation buttonSprite = ResourceLocation.withDefaultNamespace(hovered ? "widget/button_highlighted" : "widget/button");
						graphics.blitSprite(buttonSprite, px, py, widget.w, widget.h);
						if (widget.value != null && !widget.value.isEmpty())
							graphics.drawCenteredString(font, widget.value, px + widget.w / 2, py + (widget.h - 8) / 2, 0xFFFFFFFF);
					}
					default -> {
					}
				}
			} catch (Exception ignored) {
			}
			if (pushed) graphics.pose().popPose();
		}
		// Tooltips render last so they overlay the rest; hit-test in SCREEN space (mapped/scaled bounds).
		for (Widget widget : list) {
			if (!"tooltip".equals(widget.type)) continue;
			int sx = scaled ? Math.round(anchorX + widget.x * posScaleX) : anchorX + widget.x;
			int sy = scaled ? Math.round(anchorY + widget.y * posScaleY) : anchorY + widget.y;
			int w = scaled ? Math.round(widget.w * sizeScale) : widget.w;
			int h = scaled ? Math.round(widget.h * sizeScale) : widget.h;
			if (mouseX >= sx && mouseX <= sx + w && mouseY >= sy && mouseY <= sy + h) {
				graphics.renderTooltip(font, Component.literal(widget.value), mouseX, mouseY);
			}
		}
	}

	// Called from CneInventoryScreenMixin on a left-click. Hit-tests button widgets and,
	// when one is hit, asks the server to run that button's command (the command itself
	// lives only server-side; we only send which layout + button was pressed). Returns
	// true to consume the click so it does not also act on the inventory underneath.
	public static boolean mouseClicked(double mouseX, double mouseY, int mouseButton, int surface, int anchorX, int anchorY, float posScaleX, float posScaleY) {
		if (mouseButton != 0) return false;
		List<Widget> list = listForSurface(surface);
		float sizeScale = posScaleY;
		boolean scaled = sizeScale > 0.0F && (posScaleX != 1.0F || posScaleY != 1.0F);
		for (Widget widget : list) {
			if (!"button".equals(widget.type) || widget.buttonId < 0) continue;
			int sx = scaled ? Math.round(anchorX + widget.x * posScaleX) : anchorX + widget.x;
			int sy = scaled ? Math.round(anchorY + widget.y * posScaleY) : anchorY + widget.y;
			int w = scaled ? Math.round(widget.w * sizeScale) : widget.w;
			int h = scaled ? Math.round(widget.h * sizeScale) : widget.h;
			if (mouseX >= sx && mouseX <= sx + w && mouseY >= sy && mouseY <= sy + h) {
				try {
					CneInventoryButtonRuntime.sendClick(widget.sourceLayout, widget.buttonId);
				} catch (Exception ignored) {
				}
				return true;
			}
		}
		return false;
	}

	private static void renderEntity(GuiGraphics graphics, Minecraft minecraft, Widget widget, int px, int py, int mouseX, int mouseY) {
		if (minecraft.level == null) return;
		ResourceLocation id = ResourceLocation.tryParse(widget.value);
		if (id == null || NON_LIVING.contains(id) || !BuiltInRegistries.ENTITY_TYPE.containsKey(id)) return;
		LivingEntity entity = ENTITY_CACHE.get(id);
		if (entity == null || entity.level() != minecraft.level) {
			Entity created = BuiltInRegistries.ENTITY_TYPE.get(id).create(minecraft.level);
			if (!(created instanceof LivingEntity living)) {
				NON_LIVING.add(id); // not a living entity; do not retry every frame
				return;
			}
			entity = living;
			ENTITY_CACHE.put(id, entity);
		}
		int scale = Math.max(1, widget.scale);
		InventoryScreen.renderEntityInInventoryFollowsMouse(graphics, px, py, px + scale + 20, py + (scale * 2), scale, 0.0625F, mouseX, mouseY, entity);
	}

	private static int parseColor(String color) {
		String value = color == null ? "" : color.trim();
		if (value.startsWith("#")) value = value.substring(1);
		try {
			long parsed = Long.parseLong(value, 16);
			if (value.length() <= 6) parsed |= 0xFF000000L;
			return (int) parsed;
		} catch (Exception ignored) {
			return 0xFFFFFFFF;
		}
	}

	private static String str(JsonObject object, String key, String fallback) {
		try {
			return object.has(key) ? object.get(key).getAsString() : fallback;
		} catch (Exception ignored) {
			return fallback;
		}
	}

	private static int intOf(JsonObject object, String key, int fallback) {
		try {
			return object.has(key) ? object.get(key).getAsInt() : fallback;
		} catch (Exception ignored) {
			return fallback;
		}
	}

	private static boolean boolOf(JsonObject object, String key, boolean fallback) {
		try {
			return object.has(key) ? object.get(key).getAsBoolean() : fallback;
		} catch (Exception ignored) {
			return fallback;
		}
	}

	private static final class Widget {
		String type = "label";
		int x;
		int y;
		String value = "";
		String color = "#ffffff";
		boolean shadow = true;
		int w = 16;
		int h = 16;
		int u;
		int v;
		int texW = 16;
		int texH = 16;
		int scale = 30;
		int buttonId = -1;
		String sourceLayout = "";
	}
}
