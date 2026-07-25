package net.wavedk.extrautilitiesreutilized.jei_recipes;

import net.wavedk.extrautilitiesreutilized.procedures.*;
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

public class DEGenRecipeTypeRecipeCategory implements IRecipeCategory<DEGenRecipeTypeRecipe> {
	public final static ResourceLocation UID = ResourceLocation.parse("euru:de_gen_recipe_type");
	public final static ResourceLocation TEXTURE = ResourceLocation.parse("euru:textures/screens/degenbd.png");
	private final IDrawable background;
	private final IDrawable icon;
	private final Minecraft mc = Minecraft.getInstance();

	public DEGenRecipeTypeRecipeCategory(IGuiHelper helper) {
		this.background = helper.createDrawable(TEXTURE, 0, 0, 180, 175);
		this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(EuruModBlocks.DISENCHANTMENT_GENERATOR.get().asItem()));
	}

	@Override
	public mezz.jei.api.recipe.RecipeType<DEGenRecipeTypeRecipe> getRecipeType() {
		return EuruModJeiPlugin.DEGenRecipeType_Type;
	}

	@Override
	public Component getTitle() {
		return Component.literal("Culinary Generator");
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
	public void draw(DEGenRecipeTypeRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
		this.background.draw(guiGraphics);

		guiGraphics.blit(ResourceLocation.parse("euru:textures/screens/asprite.png"), 77, 27, 24 * ((mc.player.tickCount / 10) % 23) - 1, 0, 24, 24, 552, 24);

		guiGraphics.blit(ResourceLocation.parse("euru:textures/screens/asprite.png"), 77, 53, 24 * ((mc.player.tickCount / 10) % 23) - 1, 0, 24, 24, 552, 24);

		guiGraphics.blit(ResourceLocation.parse("euru:textures/screens/asprite.png"), 77, 79, 24 * ((mc.player.tickCount / 10) % 23) - 1, 0, 24, 24, 552, 24);

		guiGraphics.blit(ResourceLocation.parse("euru:textures/screens/asprite.png"), 77, 105, 24 * ((mc.player.tickCount / 10) % 23) - 1, 0, 24, 24, 552, 24);

		guiGraphics.blit(ResourceLocation.parse("euru:textures/screens/asprite.png"), 77, 131, 24 * ((mc.player.tickCount / 10) % 23) - 1, 0, 24, 24, 552, 24);

		guiGraphics.drawString(mc.font, MAXtProcedure.execute(recipe.strings()), 43, 160, -12829636, false);
	}

	public void getTooltip(ITooltipBuilder tooltip, DEGenRecipeTypeRecipe recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
		if (ShowDE1Procedure.execute(recipe.strings()))
			if (mouseX > 47 && mouseX < 104 && mouseY > 30 && mouseY < 54) {
				String hoverText = ReturnDE1Procedure.execute(recipe.strings());
				if (hoverText != null) {
					tooltip.addAll(Arrays.stream(hoverText.split("\n")).map(Component::literal).collect(Collectors.toList()));
				}
			}
		if (ShowDE2Procedure.execute(recipe.strings()))
			if (mouseX > 47 && mouseX < 104 && mouseY > 54 && mouseY < 80) {
				String hoverText = ReturnDE2Procedure.execute(recipe.strings());
				if (hoverText != null) {
					tooltip.addAll(Arrays.stream(hoverText.split("\n")).map(Component::literal).collect(Collectors.toList()));
				}
			}
		if (ShowDE3Procedure.execute(recipe.strings()))
			if (mouseX > 47 && mouseX < 104 && mouseY > 80 && mouseY < 107) {
				String hoverText = ReturnDE3Procedure.execute(recipe.strings());
				if (hoverText != null) {
					tooltip.addAll(Arrays.stream(hoverText.split("\n")).map(Component::literal).collect(Collectors.toList()));
				}
			}
		if (ShowDE4Procedure.execute(recipe.strings()))
			if (mouseX > 47 && mouseX < 104 && mouseY > 107 && mouseY < 132) {
				String hoverText = ReturnDE4Procedure.execute(recipe.strings());
				if (hoverText != null) {
					tooltip.addAll(Arrays.stream(hoverText.split("\n")).map(Component::literal).collect(Collectors.toList()));
				}
			}
		if (ShowDE5Procedure.execute(recipe.strings()))
			if (mouseX > 47 && mouseX < 104 && mouseY > 132 && mouseY < 157) {
				String hoverText = ReturnDE5Procedure.execute(recipe.strings());
				if (hoverText != null) {
					tooltip.addAll(Arrays.stream(hoverText.split("\n")).map(Component::literal).collect(Collectors.toList()));
				}
			}
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, DEGenRecipeTypeRecipe recipe, IFocusGroup focuses) {
		List<ItemStack> recipeOutputs = recipe.getResultItems();
		List<ItemStack> actualOutputs = NonNullList.withSize(1, ItemStack.EMPTY);
		for (int i = 0; i < recipeOutputs.size(); i++) {
			actualOutputs.set(i, recipeOutputs.get(i));
		}
		builder.addSlot(RecipeIngredientRole.INPUT, 52, 31).addIngredients(recipe.getIngredients().get(0));
		builder.addSlot(RecipeIngredientRole.INPUT, 52, 57).addIngredients(recipe.getIngredients().get(1));
		builder.addSlot(RecipeIngredientRole.INPUT, 52, 83).addIngredients(recipe.getIngredients().get(2));
		builder.addSlot(RecipeIngredientRole.INPUT, 52, 109).addIngredients(recipe.getIngredients().get(3));
		builder.addSlot(RecipeIngredientRole.INPUT, 52, 135).addIngredients(recipe.getIngredients().get(4));
		builder.addSlot(RecipeIngredientRole.OUTPUT, 9999, 9999).addItemStack(actualOutputs.get(0));
	}
}