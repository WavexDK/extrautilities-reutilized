package net.wavedk.extrautilitiesreutilized.init;

import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;

import mezz.jei.api.JeiPlugin;
import mezz.jei.api.IModPlugin;

@JeiPlugin
public class EuruModJeiRuntimeHolder implements IModPlugin {
	public static mezz.jei.api.runtime.IJeiRuntime runtime;

	@Override
	public ResourceLocation getPluginUid() {
		return ResourceLocation.parse("euru:jei_runtime_holder");
	}

	@Override
	public void onRuntimeAvailable(mezz.jei.api.runtime.IJeiRuntime jeiRuntime) {
		runtime = jeiRuntime;
	}

	@Override
	public void onRuntimeUnavailable() {
		runtime = null;
	}

	public static void showUses(ItemStack stack) {
		if (runtime == null || stack == null || stack.isEmpty()) {
			return;
		}
		mezz.jei.api.recipe.IFocus<ItemStack> focus = runtime.getJeiHelpers().getFocusFactory().createFocus(mezz.jei.api.recipe.RecipeIngredientRole.INPUT, mezz.jei.api.constants.VanillaTypes.ITEM_STACK, stack);
		runtime.getRecipesGui().show(focus);
	}
}