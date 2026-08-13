package net.wavedk.extrautilitiesreutilized.procedures;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;

public class TransferCableOnCableConnectToABlockProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Direction direction) {
		if (direction == null)
			return;
		ListTag connectedList = new ListTag();
		boolean alreadyInList = false;
		CompoundTag compoundList = new CompoundTag();
		connectedList = (Tag) (world.getBlockEntity(BlockPos.containing((int) (x + direction.getStepX()), (int) (y + direction.getStepY()), (int) (z + direction.getStepZ()))) instanceof BlockEntity _blockEnt3
				? _blockEnt3.getPersistentData()
				: new CompoundTag()).get("connectedList") instanceof ListTag _list5 ? _list5 : new ListTag();
		for (Tag tagiterator : connectedList.copy()) {
			if (((Tag) tagiterator instanceof StringTag _stringTag7 ? _stringTag7.getAsString() : "").equals(("x" + x) + "" + ("y" + y) + ("z" + z))) {
				alreadyInList = true;
				break;
			}
		}
		if (!alreadyInList) {
			connectedList.add(StringTag.valueOf((("x" + x) + "" + ("y" + y) + ("z" + z))));
		}
		compoundList = world.getBlockEntity(BlockPos.containing((int) (x + direction.getStepX()), (int) (y + direction.getStepY()), (int) (z + direction.getStepZ()))) instanceof BlockEntity _blockEnt14
				? _blockEnt14.getPersistentData()
				: new CompoundTag();
		compoundList.put("connectedList", connectedList.copy());
		BlockPos _blockPos19 = BlockPos.containing((int) (x + direction.getStepX()), (int) (y + direction.getStepY()), (int) (z + direction.getStepZ()));
		BlockState _blockState19 = world.getBlockState(_blockPos19);
		BlockEntity _blockEnt19 = world.getBlockEntity(_blockPos19);
		if (_blockEnt19 != null)
			_blockEnt19.setChanged();
		((Level) world).sendBlockUpdated(_blockPos19, _blockState19, _blockState19, 3);
	}
}