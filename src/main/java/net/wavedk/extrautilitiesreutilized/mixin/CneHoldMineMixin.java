package net.wavedk.extrautilitiesreutilized.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

@Mixin(Minecraft.class)
public abstract class CneHoldMineMixin {
	@Shadow public LocalPlayer player;
	@Shadow public MultiPlayerGameMode gameMode;
	@Shadow public HitResult hitResult;

	// Let a player HOLD attack to mine a breakable movable block/group instead of click-spamming:
	// while held and aimed at one, auto-attack it a few times a second so the break-time accrues.
	@Inject(method = "continueAttack", at = @At("HEAD"))
	private void cne$holdMineMovableBlock(boolean leftClick, CallbackInfo ci) {
		if (!leftClick || this.player == null || this.gameMode == null || this.hitResult == null) return;
		if (this.hitResult.getType() != HitResult.Type.ENTITY) return;
		if ((this.player.tickCount % 5) != 0) return;
		Entity target = ((EntityHitResult) this.hitResult).getEntity();
		boolean breakable = (target instanceof net.wavedk.extrautilitiesreutilized.chickennuggetextras.CneMovableBlockEntity mb && mb.isBreakable())
			|| (target instanceof net.wavedk.extrautilitiesreutilized.chickennuggetextras.CneMovableBlockGroupEntity gp && gp.isBreakable());
		if (!breakable) return;
		this.gameMode.attack(this.player, target);
		this.player.swing(InteractionHand.MAIN_HAND);
	}
}
