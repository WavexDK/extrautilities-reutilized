package net.wavedk.extrautilitiesreutilized.init;

import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.CuriosCapability;

import net.wavedk.extrautilitiesreutilized.procedures.SquidRingItemInInventoryTickProcedure;
import net.wavedk.extrautilitiesreutilized.procedures.ChickenRingItemInInventoryTickProcedure;
import net.wavedk.extrautilitiesreutilized.procedures.AngelRingItemInInventoryTickProcedure;

import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

import net.minecraft.world.item.ItemStack;

public class EuruModCuriosCompat {
	public static void registerCapabilities(RegisterCapabilitiesEvent event) {
		event.registerItem(CuriosCapability.ITEM, (stack, context) -> new ICurio() {
			@Override
			public ItemStack getStack() {
				return stack;
			}

			@Override
			public void curioTick(SlotContext slotContext) {
				AngelRingItemInInventoryTickProcedure.execute(stack);
			}
		}, EuruModItems.ANGEL_RING.get());
		event.registerItem(CuriosCapability.ITEM, (stack, context) -> new ICurio() {
			@Override
			public ItemStack getStack() {
				return stack;
			}

			@Override
			public void curioTick(SlotContext slotContext) {
				SquidRingItemInInventoryTickProcedure.execute(slotContext.entity(), stack);
			}
		}, EuruModItems.SQUID_RING.get());
		event.registerItem(CuriosCapability.ITEM, (stack, context) -> new ICurio() {
			@Override
			public ItemStack getStack() {
				return stack;
			}

			@Override
			public void curioTick(SlotContext slotContext) {
				ChickenRingItemInInventoryTickProcedure.execute(slotContext.entity(), stack);
			}
		}, EuruModItems.CHICKEN_RING.get());
	}
}