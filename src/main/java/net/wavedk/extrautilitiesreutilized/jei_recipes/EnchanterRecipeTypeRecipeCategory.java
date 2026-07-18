package net.wavedk.extrautilitiesreutilized.jei_recipes;

import net.wavedk.extrautilitiesreutilized.procedures.EnchanterRecipeTypeValueProcedure;
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
import java.util.ArrayList;

public class EnchanterRecipeTypeRecipeCategory implements IRecipeCategory<EnchanterRecipeTypeRecipe> {
	public final static ResourceLocation UID = ResourceLocation.parse("euru:enchanter_recipe_type");
	public final static ResourceLocation TEXTURE = ResourceLocation.parse("euru:textures/screens/enchanterbackdrop.png");
	private final IDrawable background;
	private final IDrawable icon;
	private final Minecraft mc = Minecraft.getInstance();

	public EnchanterRecipeTypeRecipeCategory(IGuiHelper helper) {
		this.background = helper.createDrawable(TEXTURE, 0, 0, 180, 80);
		this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(EuruModBlocks.ENCHANTER.get().asItem()));
	}

	@Override
	public mezz.jei.api.recipe.RecipeType<EnchanterRecipeTypeRecipe> getRecipeType() {
		return EuruModJeiPlugin.EnchanterRecipeType_Type;
	}

	@Override
	public Component getTitle() {
		return Component.literal("Enchanter");
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
	public void draw(EnchanterRecipeTypeRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
		this.background.draw(guiGraphics);

	}

	public void getTooltip(ITooltipBuilder tooltip, EnchanterRecipeTypeRecipe recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
		if (mouseX > 58 && mouseX < 82 && mouseY > 32 && mouseY < 56) {
			String hoverText = EnchanterRecipeTypeValueProcedure.execute(recipe.strings());
			if (hoverText != null) {
				tooltip.addAll(Arrays.stream(hoverText.split("\n")).map(Component::literal).collect(Collectors.toList()));
			}
		}
		if (mouseX > 58 && mouseX < 82 && mouseY > 55 && mouseY < 79) {
			String hoverText = EnchanterRecipeTypeValueProcedure.execute(recipe.strings());
			if (hoverText != null) {
				tooltip.addAll(Arrays.stream(hoverText.split("\n")).map(Component::literal).collect(Collectors.toList()));
			}
		}
		if (mouseX > 100 && mouseX < 124 && mouseY > 32 && mouseY < 56) {
			String hoverText = EnchanterRecipeTypeValueProcedure.execute(recipe.strings());
			if (hoverText != null) {
				tooltip.addAll(Arrays.stream(hoverText.split("\n")).map(Component::literal).collect(Collectors.toList()));
			}
		}
		if (mouseX > 100 && mouseX < 124 && mouseY > 55 && mouseY < 79) {
			String hoverText = EnchanterRecipeTypeValueProcedure.execute(recipe.strings());
			if (hoverText != null) {
				tooltip.addAll(Arrays.stream(hoverText.split("\n")).map(Component::literal).collect(Collectors.toList()));
			}
		}
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, EnchanterRecipeTypeRecipe recipe, IFocusGroup focuses) {
		List<ItemStack> stacks = new ArrayList<>();
		List<ItemStack> recipeOutputs = recipe.getResultItems();
		List<ItemStack> actualOutputs = NonNullList.withSize(1, ItemStack.EMPTY);
		for (int i = 0; i < recipeOutputs.size(); i++) {
			actualOutputs.set(i, recipeOutputs.get(i));
		}
		stacks.clear();
		for (ItemStack item : (List<ItemStack>) List.of(recipe.getIngredients().get(0).getItems()))
			stacks.add(new ItemStack(item.getItem(), recipe.integers().get(0)));
		builder.addSlot(RecipeIngredientRole.INPUT, 63, 14).addItemStacks(stacks);
		stacks.clear();
		for (ItemStack item : (List<ItemStack>) List.of(recipe.getIngredients().get(1).getItems()))
			stacks.add(new ItemStack(item.getItem(), recipe.integers().get(1)));
		builder.addSlot(RecipeIngredientRole.INPUT, 103, 14).addItemStacks(stacks);
		builder.addSlot(RecipeIngredientRole.OUTPUT, 83, 53).addItemStack(actualOutputs.get(0));
	}
}