package net.wavedk.extrautilitiesreutilized.chickennuggetextras;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

/**
 * Per-item render-scale control. Two independent scales are stored on the ItemStack in
 * the vanilla CUSTOM_DATA component (so they sync to the client and persist with the stack):
 *   cne_render_scale - used in WORLD contexts (held, dropped, item frame, on head),
 *   cne_gui_scale    - used in GUI / inventory slots.
 * The render mixin(s) call scaleFor(stack, isGui) and scale the item model around its center.
 * Scales are clamped to (0, 16]; an unset value (or 1.0) leaves the item at its normal size.
 * The set/get/reset methods are common and may run on either side.
 */
public final class CneItemScaleRuntime {
    public static final String WORLD_KEY = "cne_render_scale";
    public static final String GUI_KEY = "cne_gui_scale";

    private CneItemScaleRuntime() {
    }

    public static void setWorldScale(ItemStack stack, double scale) {
        putScale(stack, WORLD_KEY, scale);
    }

    public static void setGuiScale(ItemStack stack, double scale) {
        putScale(stack, GUI_KEY, scale);
    }

    public static double getWorldScale(ItemStack stack) {
        Float v = readRaw(stack, WORLD_KEY);
        return v == null ? 1.0 : v;
    }

    public static double getGuiScale(ItemStack stack) {
        Float v = readRaw(stack, GUI_KEY);
        return v == null ? 1.0 : v;
    }

    public static void resetScale(ItemStack stack) {
        if (stack == null || stack.isEmpty()) return;
        CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> {
            tag.remove(WORLD_KEY);
            tag.remove(GUI_KEY);
        });
    }

    // Effective scale used by the render mixin(s). guiContext = drawn in a GUI/inventory slot.
    public static float scaleFor(ItemStack stack, boolean guiContext) {
        try {
            Float v = readRaw(stack, guiContext ? GUI_KEY : WORLD_KEY);
            return v == null ? 1.0F : clamp(v);
        } catch (Exception ignored) {
            return 1.0F;
        }
    }

    private static void putScale(ItemStack stack, String key, double scale) {
        if (stack == null || stack.isEmpty()) return;
        final float v = clamp((float) scale);
        CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> tag.putFloat(key, v));
    }

    private static Float readRaw(ItemStack stack, String key) {
        if (stack == null || stack.isEmpty()) return null;
        CompoundTag tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        return tag.contains(key) ? tag.getFloat(key) : null;
    }

    private static float clamp(float v) {
        if (Float.isNaN(v) || v <= 0.0F) return 1.0F;
        return Math.min(v, 16.0F);
    }
}
