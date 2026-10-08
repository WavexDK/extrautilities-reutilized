/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.wavedk.extrautilitiesreutilized.init;

import net.wavedk.extrautilitiesreutilized.EuruMod;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.core.registries.Registries;

public class EuruModPotions {
	public static final DeferredRegister<Potion> REGISTRY = DeferredRegister.create(Registries.POTION, EuruMod.MODID);
	public static final DeferredHolder<Potion, Potion> POTION_OF_DOOM = REGISTRY.register("potion_of_doom", () -> new Potion(new MobEffectInstance(EuruModMobEffects.DOOM, 1200, 0, false, true)));
	public static final DeferredHolder<Potion, Potion> OILY_POTION = REGISTRY.register("oily_potion", () -> new Potion());
	public static final DeferredHolder<Potion, Potion> LEVITATION_POTION = REGISTRY.register("levitation_potion", () -> new Potion(new MobEffectInstance(MobEffects.LEVITATION, 600, 0, false, true)));
	public static final DeferredHolder<Potion, Potion> GRAVITY_POTION = REGISTRY.register("gravity_potion", () -> new Potion(new MobEffectInstance(EuruModMobEffects.GRAVITY_EFFECT, 1200, 0, false, true)));
	public static final DeferredHolder<Potion, Potion> LONG_GRAVITY_POTION = REGISTRY.register("long_gravity_potion", () -> new Potion(new MobEffectInstance(EuruModMobEffects.GRAVITY_EFFECT, 9600, 0, false, true)));
	public static final DeferredHolder<Potion, Potion> GREEK_FIRE_POTION = REGISTRY.register("greek_fire_potion", () -> new Potion(new MobEffectInstance(EuruModMobEffects.GREEK_FIRE_EFFECT, 2400, 0, false, true)));
}