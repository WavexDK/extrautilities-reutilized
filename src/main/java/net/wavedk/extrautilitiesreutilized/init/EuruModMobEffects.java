/*
 *	MCreator note: This file will be REGENERATED on each build.
 */
package net.wavedk.extrautilitiesreutilized.init;

import net.wavedk.extrautilitiesreutilized.potion.DoomMobEffect;
import net.wavedk.extrautilitiesreutilized.EuruMod;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.core.registries.Registries;

public class EuruModMobEffects {
	public static final DeferredRegister<MobEffect> REGISTRY = DeferredRegister.create(Registries.MOB_EFFECT, EuruMod.MODID);
	public static final DeferredHolder<MobEffect, MobEffect> DOOM = REGISTRY.register("doom", DoomMobEffect::new);
}