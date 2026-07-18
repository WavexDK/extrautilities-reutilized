package net.wavedk.extrautilitiesreutilized.block;

import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Block;

public class StoneBurntBlock extends Block {
	public StoneBurntBlock() {
		super(BlockBehaviour.Properties.of().strength(6f, 20f).requiresCorrectToolForDrops().instrument(NoteBlockInstrument.BASEDRUM));
	}
}