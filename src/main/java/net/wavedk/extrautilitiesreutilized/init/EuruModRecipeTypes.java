package net.wavedk.extrautilitiesreutilized.init;

import net.wavedk.extrautilitiesreutilized.jei_recipes.*;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.fml.event.lifecycle.FMLConstructModEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.ModList;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.IEventBus;

import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.core.registries.BuiltInRegistries;

@EventBusSubscriber
public class EuruModRecipeTypes {
	public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(BuiltInRegistries.RECIPE_TYPE, "euru");
	public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, "euru");

	@SubscribeEvent
	public static void register(FMLConstructModEvent event) {
		IEventBus bus = ModList.get().getModContainerById("euru").get().getEventBus();
		event.enqueueWork(() -> {
			RECIPE_TYPES.register(bus);
			SERIALIZERS.register(bus);
			RECIPE_TYPES.register("resonator_recipe_type", () -> ResonatorRecipeTypeRecipe.Type.INSTANCE);
			SERIALIZERS.register("resonator_recipe_type", () -> ResonatorRecipeTypeRecipe.Serializer.INSTANCE);
			RECIPE_TYPES.register("s_gen_recipe_type", () -> SGenRecipeTypeRecipe.Type.INSTANCE);
			SERIALIZERS.register("s_gen_recipe_type", () -> SGenRecipeTypeRecipe.Serializer.INSTANCE);
			RECIPE_TYPES.register("f_gen_recipe_type", () -> FGenRecipeTypeRecipe.Type.INSTANCE);
			SERIALIZERS.register("f_gen_recipe_type", () -> FGenRecipeTypeRecipe.Serializer.INSTANCE);
			RECIPE_TYPES.register("enchanter_recipe_type", () -> EnchanterRecipeTypeRecipe.Type.INSTANCE);
			SERIALIZERS.register("enchanter_recipe_type", () -> EnchanterRecipeTypeRecipe.Serializer.INSTANCE);
			RECIPE_TYPES.register("e_gen_recipe_type", () -> EGenRecipeTypeRecipe.Type.INSTANCE);
			SERIALIZERS.register("e_gen_recipe_type", () -> EGenRecipeTypeRecipe.Serializer.INSTANCE);
			RECIPE_TYPES.register("o_gen_recipe_type", () -> OGenRecipeTypeRecipe.Type.INSTANCE);
			SERIALIZERS.register("o_gen_recipe_type", () -> OGenRecipeTypeRecipe.Serializer.INSTANCE);
			RECIPE_TYPES.register("ns_gen_recipe_type", () -> NSGenRecipeTypeRecipe.Type.INSTANCE);
			SERIALIZERS.register("ns_gen_recipe_type", () -> NSGenRecipeTypeRecipe.Serializer.INSTANCE);
			RECIPE_TYPES.register("golden_lasso_rt", () -> GoldenLassoRTRecipe.Type.INSTANCE);
			SERIALIZERS.register("golden_lasso_rt", () -> GoldenLassoRTRecipe.Serializer.INSTANCE);
			RECIPE_TYPES.register("cursed_lasso_rt", () -> CursedLassoRTRecipe.Type.INSTANCE);
			SERIALIZERS.register("cursed_lasso_rt", () -> CursedLassoRTRecipe.Serializer.INSTANCE);
		});
	}
}