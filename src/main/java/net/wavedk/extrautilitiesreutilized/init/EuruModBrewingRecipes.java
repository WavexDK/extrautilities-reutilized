package net.wavedk.extrautilitiesreutilized.init;

import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.tags.ItemTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.component.DataComponents;

import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.recipe.vanilla.IVanillaRecipeFactory;
import mezz.jei.api.recipe.vanilla.IJeiBrewingRecipe;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.IModPlugin;

import java.util.stream.Collectors;
import java.util.List;
import java.util.ArrayList;

@JeiPlugin
public class EuruModBrewingRecipes implements IModPlugin {
	@Override
	public ResourceLocation getPluginUid() {
		return ResourceLocation.parse("euru:brewing_recipes");
	}

	@Override
	public void registerRecipes(IRecipeRegistration registration) {
		IVanillaRecipeFactory factory = registration.getVanillaRecipeFactory();
		List<IJeiBrewingRecipe> brewingRecipes = new ArrayList<>();
		ItemStack potion = new ItemStack(Items.POTION);
		ItemStack potion2 = new ItemStack(Items.POTION);
		List<ItemStack> ingredientStack = new ArrayList<>();
		List<ItemStack> inputStack = new ArrayList<>();
		ingredientStack.add(new ItemStack(Items.BEETROOT));
		potion.set(DataComponents.POTION_CONTENTS, new PotionContents(Potions.AWKWARD));
		potion2.set(DataComponents.POTION_CONTENTS, new PotionContents(EuruModPotions.OILY_POTION));
		brewingRecipes.add(factory.createBrewingRecipe(List.copyOf(ingredientStack), potion.copy(), potion2.copy()));
		ingredientStack.clear();
		ingredientStack.add(new ItemStack(Items.SUGAR));
		potion.set(DataComponents.POTION_CONTENTS, new PotionContents(Potions.LONG_LEAPING));
		potion2.set(DataComponents.POTION_CONTENTS, new PotionContents(EuruModPotions.LEVITATION_POTION));
		brewingRecipes.add(factory.createBrewingRecipe(List.copyOf(ingredientStack), potion.copy(), potion2.copy()));
		ingredientStack.clear();
		ingredientStack = new ArrayList<ItemStack>(
				BuiltInRegistries.ITEM.getOrCreateTag(ItemTags.create(ResourceLocation.parse("c:obsidians"))).stream().map(item -> new ItemStack((Item) item.value())).collect(Collectors.toCollection(ArrayList::new)));
		potion.set(DataComponents.POTION_CONTENTS, new PotionContents(Potions.AWKWARD));
		potion2.set(DataComponents.POTION_CONTENTS, new PotionContents(EuruModPotions.GRAVITY_POTION));
		brewingRecipes.add(factory.createBrewingRecipe(List.copyOf(ingredientStack), potion.copy(), potion2.copy()));
		ingredientStack.clear();
		ingredientStack.add(new ItemStack(Items.REDSTONE));
		potion.set(DataComponents.POTION_CONTENTS, new PotionContents(EuruModPotions.GRAVITY_POTION));
		potion2.set(DataComponents.POTION_CONTENTS, new PotionContents(EuruModPotions.LONG_GRAVITY_POTION));
		brewingRecipes.add(factory.createBrewingRecipe(List.copyOf(ingredientStack), potion.copy(), potion2.copy()));
		ingredientStack.clear();
		ingredientStack.add(new ItemStack(Items.LAVA_BUCKET));
		potion.set(DataComponents.POTION_CONTENTS, new PotionContents(EuruModPotions.OILY_POTION));
		potion2.set(DataComponents.POTION_CONTENTS, new PotionContents(EuruModPotions.GREEK_FIRE_POTION));
		brewingRecipes.add(factory.createBrewingRecipe(List.copyOf(ingredientStack), potion.copy(), potion2.copy()));
		ingredientStack.clear();
		registration.addRecipes(RecipeTypes.BREWING, brewingRecipes);
	}
}