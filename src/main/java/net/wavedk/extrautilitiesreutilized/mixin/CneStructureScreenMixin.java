package net.wavedk.extrautilitiesreutilized.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.StructureBlockEditScreen;
import net.minecraft.world.level.block.entity.StructureBlockEntity;

@Mixin(StructureBlockEditScreen.class)
public abstract class CneStructureScreenMixin {
	@Shadow @Final private StructureBlockEntity structure;
	@Shadow private EditBox nameEdit;
	@Shadow private EditBox posXEdit;
	@Shadow private EditBox posYEdit;
	@Shadow private EditBox posZEdit;
	@Shadow private EditBox sizeXEdit;
	@Shadow private EditBox sizeYEdit;
	@Shadow private EditBox sizeZEdit;
	@Shadow private EditBox integrityEdit;
	@Shadow private EditBox seedEdit;
	@Shadow private EditBox dataEdit;

	@Inject(method = "sendToServer", at = @At("HEAD"), cancellable = true)
	private void cne$sendFullSizeUpdate(StructureBlockEntity.UpdateType updateType, CallbackInfoReturnable<Boolean> cir) {
		net.wavedk.extrautilitiesreutilized.chickennuggetextras.CneStructureBlockRuntime.sendFromScreen(this.structure, updateType,
			this.nameEdit.getValue(), this.posXEdit.getValue(), this.posYEdit.getValue(), this.posZEdit.getValue(),
			this.sizeXEdit.getValue(), this.sizeYEdit.getValue(), this.sizeZEdit.getValue(),
			this.integrityEdit.getValue(), this.seedEdit.getValue(), this.dataEdit.getValue());
		cir.setReturnValue(true);
	}
}
