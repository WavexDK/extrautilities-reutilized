package net.wavedk.extrautilitiesreutilized.block;

import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Block;

public class AngelBlockBlock extends Block {
	public AngelBlockBlock() {
		super(BlockBehaviour.Properties.of().strength(40f, 1000f).requiresCorrectToolForDrops().instrument(NoteBlockInstrument.BASEDRUM));
	}
}