package net.wavedk.extrautilitiesreutilized.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import net.minecraft.world.level.block.entity.StructureBlockEntity;

@Mixin(StructureBlockEntity.class)
public abstract class CneStructureBlockLimitMixin {
	@ModifyConstant(method = "loadAdditional", constant = @Constant(intValue = 48), require = 0)
	private int cne$structureSizeMax(int value) {
		return net.wavedk.extrautilitiesreutilized.chickennuggetextras.CneStructureBlockRuntime.MAX_SIZE;
	}

	@ModifyConstant(method = "loadAdditional", constant = @Constant(intValue = -48), require = 0)
	private int cne$structureOffsetMin(int value) {
		return -net.wavedk.extrautilitiesreutilized.chickennuggetextras.CneStructureBlockRuntime.MAX_SIZE;
	}
}
