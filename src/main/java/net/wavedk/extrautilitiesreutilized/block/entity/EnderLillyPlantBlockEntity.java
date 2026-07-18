package net.wavedk.extrautilitiesreutilized.block.entity;

import net.wavedk.extrautilitiesreutilized.init.EuruModBlockEntities;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.BlockPos;

public class EnderLillyPlantBlockEntity extends BlockEntity {
	public EnderLillyPlantBlockEntity(BlockPos pos, BlockState state) {
		super(EuruModBlockEntities.ENDER_LILLY_PLANT.get(), pos, state);
	}

	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public CompoundTag getUpdateTag(HolderLookup.Provider lookupProvider) {
		return this.saveWithFullMetadata(lookupProvider);
	}
}