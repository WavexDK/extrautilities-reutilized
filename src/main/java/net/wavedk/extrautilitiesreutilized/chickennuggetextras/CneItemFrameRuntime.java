package net.wavedk.extrautilitiesreutilized.chickennuggetextras;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ModelEvent;

/**
 * Per-item animation-frame control for the Spritesheet Creator's "per-item frames"
 * export.
 *
 * The frame index is stored on the ItemStack inside the vanilla CUSTOM_DATA
 * component (no custom component registration needed), so it syncs to the client
 * and persists with the stack. The set/get/advance methods are common and may run
 * on either side.
 *
 * The nested {@link Client} class is client-only: on every resource reload it reads
 * the manifests written to assets/&lt;modid&gt;/cne_item_frames/ (one per item),
 * registers each frame model so it bakes, and the CneItemFrameMixin asks
 * resolveFrameModel() to swap the rendered model when a stack has a frame set.
 */
public final class CneItemFrameRuntime {
	public static final String FRAME_KEY = "cne_item_frame";

	private CneItemFrameRuntime() {
	}

	public static void setItemFrame(ItemStack stack, int frame) {
		if (stack == null || stack.isEmpty()) return;
		final int value = Math.max(0, frame);
		CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> tag.putInt(FRAME_KEY, value));
	}

	public static int getItemFrame(ItemStack stack) {
		if (stack == null || stack.isEmpty()) return -1;
		CompoundTag tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
		return tag.contains(FRAME_KEY) ? tag.getInt(FRAME_KEY) : -1;
	}

	public static void advanceItemFrame(ItemStack stack, int frameCount) {
		if (stack == null || stack.isEmpty() || frameCount <= 0) return;
		int current = getItemFrame(stack);
		int next = (Math.max(current, -1) + 1) % frameCount; // unset (-1) advances to frame 0
		setItemFrame(stack, next);
	}

	@EventBusSubscriber(modid = "euru", value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
	public static final class Client {
		private static final Map<ResourceLocation, List<ModelResourceLocation>> FRAME_MODELS = new HashMap<>();

		private Client() {
		}

		@SubscribeEvent
		public static void onRegisterAdditional(ModelEvent.RegisterAdditional event) {
			FRAME_MODELS.clear();
			try {
				Minecraft minecraft = Minecraft.getInstance();
				if (minecraft == null) return;
				minecraft.getResourceManager().listResources("cne_item_frames", path -> path.getPath().endsWith(".json"))
					.forEach((location, resource) -> {
						try (InputStreamReader reader = new InputStreamReader(resource.open(), StandardCharsets.UTF_8)) {
							JsonObject root = JsonParser.parseReader(reader).getAsJsonObject();
							if (!root.has("item") || !root.has("models")) return;
							ResourceLocation itemId = ResourceLocation.tryParse(root.get("item").getAsString());
							if (itemId == null) return;
							JsonArray models = root.getAsJsonArray("models");
							List<ModelResourceLocation> list = new ArrayList<>();
							for (int i = 0; i < models.size(); i++) {
								ResourceLocation modelRl = ResourceLocation.tryParse(models.get(i).getAsString());
								if (modelRl == null) continue;
								ModelResourceLocation mrl = ModelResourceLocation.standalone(modelRl);
								event.register(mrl);
								list.add(mrl);
							}
							if (!list.isEmpty()) FRAME_MODELS.put(itemId, list);
						} catch (Exception ignored) {
						}
					});
			} catch (Exception ignored) {
			}
		}

		// Called from CneItemFrameMixin at the end of ItemRenderer.getModel. Returns a
		// per-frame baked model when the stack has a frame set and that frame exists;
		// otherwise returns the original so normal items are untouched.
		public static BakedModel resolveFrameModel(ItemStack stack, BakedModel original) {
			try {
				if (stack == null || stack.isEmpty()) return original;
				int frame = getItemFrame(stack);
				if (frame < 0) return original;
				ResourceLocation itemId = BuiltInRegistries.ITEM.getKey(stack.getItem());
				List<ModelResourceLocation> list = FRAME_MODELS.get(itemId);
				if (list == null || frame >= list.size()) return original;
				Minecraft minecraft = Minecraft.getInstance();
				if (minecraft == null) return original;
				ModelManager manager = minecraft.getModelManager();
				BakedModel model = manager.getModel(list.get(frame));
				if (model == null || model == manager.getMissingModel()) return original;
				return model;
			} catch (Exception ignored) {
				return original;
			}
		}
	}
}
