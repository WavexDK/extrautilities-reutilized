package net.wavedk.extrautilitiesreutilized.jei_recipes;

import net.wavedk.extrautilitiesreutilized.procedures.SGenRecipeTypeValueProcedure;
import net.wavedk.extrautilitiesreutilized.init.EuruModJeiPlugin;
import net.wavedk.extrautilitiesreutilized.init.EuruModBlocks;

import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.core.NonNullList;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.Minecraft;

import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.constants.VanillaTypes;

import java.util.stream.Collectors;
import java.util.List;
import java.util.Arrays;

public class EGenRecipeTypeRecipeCategory implements IRecipeCategory<EGenRecipeTypeRecipe> {
	public final static ResourceLocation UID = ResourceLocation.parse("euru:e_gen_recipe_type");
	public final static ResourceLocation TEXTURE = ResourceLocation.parse("euru:textures/screens/endergenbackdrop.png");
	private final IDrawable background;
	private final IDrawable icon;
	private final Minecraft mc = Minecraft.getInstance();

	public EGenRecipeTypeRecipeCategory(IGuiHelper helper) {
		this.background = helper.createDrawable(TEXTURE, 0, 0, 180, 80);
		this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(EuruModBlocks.ENDER_GENERATOR.get().asItem()));
	}

	@Override
	public mezz.jei.api.recipe.RecipeType<EGenRecipeTypeRecipe> getRecipeType() {
		return EuruModJeiPlugin.EGenRecipeType_Type;
	}

	@Override
	public Component getTitle() {
		return Component.literal("Ender Generator");
	}

	@Override
	public IDrawable getIcon() {
		return this.icon;
	}

	@Override
	public int getWidth() {
		return this.background.getWidth();
	}

	@Override
	public int getHeight() {
		return this.background.getHeight();
	}

	@Override
	public boolean needsRecipeBorder() {
		return false;
	}

	@Override
	public void draw(EGenRecipeTypeRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
		this.background.draw(guiGraphics);

		guiGraphics.blit(ResourceLocation.parse("euru:textures/screens/asprite.png"), 77, 28, 24 * ((mc.player.tickCount / 10) % 23) - 1, 0, 24, 24, 552, 24);

	}

	public void getTooltip(ITooltipBuilder tooltip, EGenRecipeTypeRecipe recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
		if (mouseX > 77 && mouseX < 101 && mouseY > 30 && mouseY < 54) {
			String hoverText = SGenRecipeTypeValueProcedure.execute(recipe.strings());
			if (hoverText != null) {
				tooltip.addAll(Arrays.stream(hoverText.split("\n")).map(Component::literal).collect(Collectors.toList()));
			}
		}
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, EGenRecipeTypeRecipe recipe, IFocusGroup focuses) {
		List<ItemStack> recipeOutputs = recipe.getResultItems();
		List<ItemStack> actualOutputs = NonNullList.withSize(1, ItemStack.EMPTY);
		for (int i = 0; i < recipeOutputs.size(); i++) {
			actualOutputs.set(i, recipeOutputs.get(i));
		}
		builder.addSlot(RecipeIngredientRole.INPUT, 52, 31).addIngredients(recipe.getIngredients().get(0));
		builder.addSlot(RecipeIngredientRole.OUTPUT, 9999, 9999).addItemStack(actualOutputs.get(0));
	}
}