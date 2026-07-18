package net.wavedk.extrautilitiesreutilized.block;

import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Block;

public class GildedObsidianBlock extends Block {
	public GildedObsidianBlock() {
		super(BlockBehaviour.Properties.of().strength(30f, 700f).requiresCorrectToolForDrops().instrument(NoteBlockInstrument.BASEDRUM));
	}
}