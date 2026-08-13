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

public class TransferCableOnCableDisconnectFromBlockProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Direction direction) {
		if (direction == null)
			return;
		ListTag connectedList = new ListTag();
		boolean alreadyInList = false;
		double listNum = 0;
		CompoundTag compoundList = new CompoundTag();
		connectedList = (Tag) (world.getBlockEntity(BlockPos.containing((int) (x + direction.getStepX()), (int) (y + direction.getStepY()), (int) (z + direction.getStepZ()))) instanceof BlockEntity _blockEnt3
				? _blockEnt3.getPersistentData()
				: new CompoundTag()).get("connectedList") instanceof ListTag _list5 ? _list5 : new ListTag();
		listNum = 0;
		for (Tag tagiterator : connectedList.copy()) {
			if (((Tag) tagiterator instanceof StringTag _stringTag7 ? _stringTag7.getAsString() : "").equals(("x" + x) + "" + ("y" + y) + ("z" + z))) {
				connectedList.remove((int) listNum);
				break;
			} else {
				listNum = listNum + 1;
			}
		}
		compoundList = world.getBlockEntity(BlockPos.containing((int) (x + direction.getStepX()), (int) (y + direction.getStepY()), (int) (z + direction.getStepZ()))) instanceof BlockEntity _blockEnt13
				? _blockEnt13.getPersistentData()
				: new CompoundTag();
		compoundList.put("connectedList", connectedList.copy());
		BlockPos _blockPos18 = BlockPos.containing((int) (x + direction.getStepX()), (int) (y + direction.getStepY()), (int) (z + direction.getStepZ()));
		BlockState _blockState18 = world.getBlockState(_blockPos18);
		BlockEntity _blockEnt18 = world.getBlockEntity(_blockPos18);
		if (_blockEnt18 != null)
			_blockEnt18.setChanged();
		((Level) world).sendBlockUpdated(_blockPos18, _blockState18, _blockState18, 3);
	}
}