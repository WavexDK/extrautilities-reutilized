package net.wavedk.extrautilitiesreutilized.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.Options;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.OptionsSubScreen;
import net.minecraft.client.gui.screens.options.SoundOptionsScreen;
import net.minecraft.network.chat.Component;

@Mixin(SoundOptionsScreen.class)
public abstract class CneAudioOptionsMixin extends OptionsSubScreen {
	protected CneAudioOptionsMixin(Screen lastScreen, Options options, Component title) {
		super(lastScreen, options, title);
	}

	@Inject(method = "addOptions", at = @At("TAIL"))
	private void cne$addMicrophoneOptions(CallbackInfo ci) {
		if (this.list == null) return;
		this.list.addSmall(net.wavedk.extrautilitiesreutilized.chickennuggetextras.CneAudioClientRuntime.buildSoundOptionsWidgets());
	}
}
