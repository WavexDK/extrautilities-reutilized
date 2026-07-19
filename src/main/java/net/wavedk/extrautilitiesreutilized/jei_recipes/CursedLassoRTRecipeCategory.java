package net.wavedk.extrautilitiesreutilized.jei_recipes;

import net.wavedk.extrautilitiesreutilized.procedures.GhastRTShowProcedure;

import net.wavedk.extrautilitiesreutilized.init.EuruModJeiPlugin;
import net.wavedk.extrautilitiesreutilized.init.EuruModItems;

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
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.constants.VanillaTypes;

import java.util.List;

public class CursedLassoRTRecipeCategory implements IRecipeCategory<CursedLassoRTRecipe> {
	public final static ResourceLocation UID = ResourceLocation.parse("euru:cursed_lasso_rt");
	public final static ResourceLocation TEXTURE = ResourceLocation.parse("euru:textures/screens/blankecbd.png");
	private final IDrawable background;
	private final IDrawable icon;
	private final Minecraft mc = Minecraft.getInstance();

	public CursedLassoRTRecipeCategory(IGuiHelper helper) {
		this.background = helper.createDrawable(TEXTURE, 0, 0, 180, 80);
		this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(EuruModItems.CURSED_LASSO.get()));
	}

	@Override
	public mezz.jei.api.recipe.RecipeType<CursedLassoRTRecipe> getRecipeType() {
		return EuruModJeiPlugin.CursedLassoRT_Type;
	}

	@Override
	public Component getTitle() {
		return Component.literal("Entity Capturing");
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
	public void draw(CursedLassoRTRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
		this.background.draw(guiGraphics);

		
		if (GhastRTShowProcedure.execute(recipe.strings())) {
			guiGraphics.blit(ResourceLocation.parse("euru:textures/screens/ghast.png"), 75, 24, 28, 28, 0, 0, 280, 280, 280, 280);
		}

	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, CursedLassoRTRecipe recipe, IFocusGroup focuses) {
		List<ItemStack> recipeOutputs = recipe.getResultItems();
		List<ItemStack> actualOutputs = NonNullList.withSize(1, ItemStack.EMPTY);
		for (int i = 0; i < recipeOutputs.size(); i++) {
			actualOutputs.set(i, recipeOutputs.get(i));
		}
		builder.addSlot(RecipeIngredientRole.INPUT, 19, 31).addIngredients(recipe.getIngredients().get(0));
		builder.addSlot(RecipeIngredientRole.OUTPUT, 145, 31).addItemStack(actualOutputs.get(0));
	}
}