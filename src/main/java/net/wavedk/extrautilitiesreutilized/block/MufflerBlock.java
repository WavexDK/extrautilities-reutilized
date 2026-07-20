package net.wavedk.extrautilitiesreutilized.block;

import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.Block;

public class MufflerBlock extends Block {
	public MufflerBlock() {
		super(BlockBehaviour.Properties.of().sound(SoundType.WOOL).strength(1f, 6f).instrument(NoteBlockInstrument.GUITAR));
	}
}