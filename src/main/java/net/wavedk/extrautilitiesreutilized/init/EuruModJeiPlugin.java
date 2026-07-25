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
	public static mezz.jei.api.recipe.RecipeType<GoldenLassoRTRecipe> GoldenLassoRT_Type = new mezz.jei.api.recipe.RecipeType<>(GoldenLassoRTRecipeCategory.UID, GoldenLassoRTRecipe.class);
	public static mezz.jei.api.recipe.RecipeType<CursedLassoRTRecipe> CursedLassoRT_Type = new mezz.jei.api.recipe.RecipeType<>(CursedLassoRTRecipeCategory.UID, CursedLassoRTRecipe.class);
	public static mezz.jei.api.recipe.RecipeType<CGenRecipeTypeRecipe> CGenRecipeType_Type = new mezz.jei.api.recipe.RecipeType<>(CGenRecipeTypeRecipeCategory.UID, CGenRecipeTypeRecipe.class);
	public static mezz.jei.api.recipe.RecipeType<DEGenRecipeTypeRecipe> DEGenRecipeType_Type = new mezz.jei.api.recipe.RecipeType<>(DEGenRecipeTypeRecipeCategory.UID, DEGenRecipeTypeRecipe.class);

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
		registration.addRecipeCategories(new GoldenLassoRTRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
		registration.addRecipeCategories(new CursedLassoRTRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
		registration.addRecipeCategories(new CGenRecipeTypeRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
		registration.addRecipeCategories(new DEGenRecipeTypeRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
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
		List<GoldenLassoRTRecipe> GoldenLassoRTRecipes = recipeManager.getAllRecipesFor(GoldenLassoRTRecipe.Type.INSTANCE).stream().map(RecipeHolder::value).collect(Collectors.toList());
		registration.addRecipes(GoldenLassoRT_Type, GoldenLassoRTRecipes);
		List<CursedLassoRTRecipe> CursedLassoRTRecipes = recipeManager.getAllRecipesFor(CursedLassoRTRecipe.Type.INSTANCE).stream().map(RecipeHolder::value).collect(Collectors.toList());
		registration.addRecipes(CursedLassoRT_Type, CursedLassoRTRecipes);
		List<CGenRecipeTypeRecipe> CGenRecipeTypeRecipes = recipeManager.getAllRecipesFor(CGenRecipeTypeRecipe.Type.INSTANCE).stream().map(RecipeHolder::value).collect(Collectors.toList());
		registration.addRecipes(CGenRecipeType_Type, CGenRecipeTypeRecipes);
		List<DEGenRecipeTypeRecipe> DEGenRecipeTypeRecipes = recipeManager.getAllRecipesFor(DEGenRecipeTypeRecipe.Type.INSTANCE).stream().map(RecipeHolder::value).collect(Collectors.toList());
		registration.addRecipes(DEGenRecipeType_Type, DEGenRecipeTypeRecipes);
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
		registration.addRecipeCatalyst(new ItemStack(EuruModItems.GOLDEN_LASSO.get()), GoldenLassoRT_Type);
		registration.addRecipeCatalyst(new ItemStack(EuruModItems.CURSED_LASSO.get()), CursedLassoRT_Type);
		registration.addRecipeCatalyst(new ItemStack(EuruModBlocks.CULINARY_GENERATOR.get().asItem()), CGenRecipeType_Type);
		registration.addRecipeCatalyst(new ItemStack(EuruModBlocks.DISENCHANTMENT_GENERATOR.get().asItem()), DEGenRecipeType_Type);
	}
}