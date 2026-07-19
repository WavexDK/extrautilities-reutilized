package net.wavedk.extrautilitiesreutilized.init;

import net.wavedk.extrautilitiesreutilized.jei_recipes.*;

import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.Minecraft;

import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.IModPlugin;

import java.util.stream.Collectors;
import java.util.Objects;
import java.util.List;

@JeiPlugin
public class EuruModJeiPlugin implements IModPlugin {
	public static mezz.jei.api.recipe.RecipeType<ResonatorRecipeTypeRecipe> ResonatorRecipeType_Type = new mezz.jei.api.recipe.RecipeType<>(ResonatorRecipeTypeRecipeCategory.UID, ResonatorRecipeTypeRecipe.class);
	public static mezz.jei.api.recipe.RecipeType<SGenRecipeTypeRecipe> SGenRecipeType_Type = new mezz.jei.api.recipe.RecipeType<>(SGenRecipeTypeRecipeCategory.UID, SGenRecipeTypeRecipe.class);
	public static mezz.jei.api.recipe.RecipeType<FGenRecipeTypeRecipe> FGenRecipeType_Type = new mezz.jei.api.recipe.RecipeType<>(FGenRecipeTypeRecipeCategory.UID, FGenRecipeTypeRecipe.class);
	public static mezz.jei.api.recipe.RecipeType<EnchanterRecipeTypeRecipe> EnchanterRecipeType_Type = new mezz.jei.api.recipe.RecipeType<>(EnchanterRecipeTypeRecipeCategory.UID, EnchanterRecipeTypeRecipe.class);
	public static mezz.jei.api.recipe.RecipeType<EGenRecipeTypeRecipe> EGenRecipeType_Type = new mezz.jei.api.recipe.RecipeType<>(EGenRecipeTypeRecipeCategory.UID, EGenRecipeTypeRecipe.class);
	public static mezz.jei.api.recipe.RecipeType<OGenRecipeTypeRecipe> OGenRecipeType_Type = new mezz.jei.api.recipe.RecipeType<>(OGenRecipeTypeRecipeCategory.UID, OGenRecipeTypeRecipe.class);
	public static mezz.jei.api.recipe.RecipeType<NSGenRecipeTypeRecipe> NSGenRecipeType_Type = new mezz.jei.api.recipe.RecipeType<>(NSGenRecipeTypeRecipeCategory.UID, NSGenRecipeTypeRecipe.class);

	@Override
	public ResourceLocation getPluginUid() {
		return ResourceLocation.parse("euru:jei_plugin");
	}

	@Override
	public void registerCategories(IRecipeCategoryRegistration registration) {
		registration.addRecipeCategories(new ResonatorRecipeTypeRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
		registration.addRecipeCategories(new SGenRecipeTypeRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
		registration.addRecipeCategories(new FGenRecipeTypeRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
		registration.addRecipeCategories(new EnchanterRecipeTypeRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
		registration.addRecipeCategories(new EGenRecipeTypeRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
		registration.addRecipeCategories(new OGenRecipeTypeRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
		registration.addRecipeCategories(new NSGenRecipeTypeRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
	}

	@Override
	public void registerRecipes(IRecipeRegistration registration) {
		RecipeManager recipeManager = Objects.requireNonNull(Minecraft.getInstance().level).getRecipeManager();
		List<ResonatorRecipeTypeRecipe> ResonatorRecipeTypeRecipes = recipeManager.getAllRecipesFor(ResonatorRecipeTypeRecipe.Type.INSTANCE).stream().map(RecipeHolder::value).collect(Collectors.toList());
		registration.addRecipes(ResonatorRecipeType_Type, ResonatorRecipeTypeRecipes);
		List<SGenRecipeTypeRecipe> SGenRecipeTypeRecipes = recipeManager.getAllRecipesFor(SGenRecipeTypeRecipe.Type.INSTANCE).stream().map(RecipeHolder::value).collect(Collectors.toList());
		registration.addRecipes(SGenRecipeType_Type, SGenRecipeTypeRecipes);
		List<FGenRecipeTypeRecipe> FGenRecipeTypeRecipes = recipeManager.getAllRecipesFor(FGenRecipeTypeRecipe.Type.INSTANCE).stream().map(RecipeHolder::value).collect(Collectors.toList());
		registration.addRecipes(FGenRecipeType_Type, FGenRecipeTypeRecipes);
		List<EnchanterRecipeTypeRecipe> EnchanterRecipeTypeRecipes = recipeManager.getAllRecipesFor(EnchanterRecipeTypeRecipe.Type.INSTANCE).stream().map(RecipeHolder::value).collect(Collectors.toList());
		registration.addRecipes(EnchanterRecipeType_Type, EnchanterRecipeTypeRecipes);
		List<EGenRecipeTypeRecipe> EGenRecipeTypeRecipes = recipeManager.getAllRecipesFor(EGenRecipeTypeRecipe.Type.INSTANCE).stream().map(RecipeHolder::value).collect(Collectors.toList());
		registration.addRecipes(EGenRecipeType_Type, EGenRecipeTypeRecipes);
		List<OGenRecipeTypeRecipe> OGenRecipeTypeRecipes = recipeManager.getAllRecipesFor(OGenRecipeTypeRecipe.Type.INSTANCE).stream().map(RecipeHolder::value).collect(Collectors.toList());
		registration.addRecipes(OGenRecipeType_Type, OGenRecipeTypeRecipes);
		List<NSGenRecipeTypeRecipe> NSGenRecipeTypeRecipes = recipeManager.getAllRecipesFor(NSGenRecipeTypeRecipe.Type.INSTANCE).stream().map(RecipeHolder::value).collect(Collectors.toList());
		registration.addRecipes(NSGenRecipeType_Type, NSGenRecipeTypeRecipes);
	}

	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
		registration.addRecipeCatalyst(new ItemStack(EuruModBlocks.RESONATOR.get().asItem()), ResonatorRecipeType_Type);
		registration.addRecipeCatalyst(new ItemStack(EuruModBlocks.SURVIVAL_GENERATOR.get().asItem()), SGenRecipeType_Type);
		registration.addRecipeCatalyst(new ItemStack(EuruModBlocks.FURNACE_GENERATOR.get().asItem()), FGenRecipeType_Type);
		registration.addRecipeCatalyst(new ItemStack(EuruModBlocks.ENCHANTER.get().asItem()), EnchanterRecipeType_Type);
		registration.addRecipeCatalyst(new ItemStack(EuruModBlocks.ENDER_GENERATOR.get().asItem()), EGenRecipeType_Type);
		registration.addRecipeCatalyst(new ItemStack(EuruModBlocks.OVERCLOCKED_GENERATOR.get().asItem()), OGenRecipeType_Type);
		registration.addRecipeCatalyst(new ItemStack(EuruModBlocks.NETHERSTAR_GENERATOR.get().asItem()), NSGenRecipeType_Type);
	}
}