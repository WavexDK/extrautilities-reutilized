package net.wavedk.extrautilitiesreutilized.procedures;

import net.wavedk.extrautilitiesreutilized.network.EuruModVariables;
import net.wavedk.extrautilitiesreutilized.init.EuruModItems;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.core.BlockPos;

public class GPOverlayTickProcedure {
	public static void execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		String group = "";
		String placed_By = "";
		String gUpdate = "";
		String gCount = "";
		double c_x = 0;
		double c_y = 0;
		double c_z = 0;
		double fGUpdate = 0;
		double fgCount = 0;
		c_x = entity.level().clip(new ClipContext(entity.getEyePosition(1f), entity.getEyePosition(1f).add(entity.getViewVector(1f).scale(5)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity)).getBlockPos().getX();
		c_y = entity.level().clip(new ClipContext(entity.getEyePosition(1f), entity.getEyePosition(1f).add(entity.getViewVector(1f).scale(5)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity)).getBlockPos().getY();
		c_z = entity.level().clip(new ClipContext(entity.getEyePosition(1f), entity.getEyePosition(1f).add(entity.getViewVector(1f).scale(5)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity)).getBlockPos().getZ();
		placed_By = getBlockNBTString(world, BlockPos.containing(c_x, c_y, c_z), "placedBy");
		group = getBlockNBTString(world, BlockPos.containing(c_x, c_y, c_z), "gp_group");
		if ((placed_By).equals(entity.getStringUUID())) {
			if (!entity.getData(EuruModVariables.PLAYER_VARIABLES).playerGPChecking) {
				{
					EuruModVariables.PlayerVariables _vars = entity.getData(EuruModVariables.PLAYER_VARIABLES);
					_vars.changingAB1 = "Grid Power: " + entity.getData(EuruModVariables.PLAYER_VARIABLES).playerGP_Used + "/" + entity.getData(EuruModVariables.PLAYER_VARIABLES).playerGP_Total;
					_vars.markSyncDirty();
				}
			}
			if (!(group).equals("")) {
				gUpdate = "group_update_" + group;
				gCount = "group_count_" + group;
				fGUpdate = (Tag) entity.getData(EuruModVariables.PLAYER_VARIABLES).serializeNBT(world.registryAccess()).get(("group_update_" + group)) instanceof DoubleTag _numberTag8 ? _numberTag8.getAsDouble() : 0;
				fgCount = (Tag) entity.getData(EuruModVariables.PLAYER_VARIABLES).serializeNBT(world.registryAccess()).get(("group_count_" + group)) instanceof DoubleTag _numberTag11 ? _numberTag11.getAsDouble() : 0;
				if (!entity.getData(EuruModVariables.PLAYER_VARIABLES).playerGPChecking) {
					if (getBlockNBTLogic(world, BlockPos.containing(c_x, c_y, c_z), "generating")) {
						if (Math.floor((fGUpdate / fgCount) * 100) / 100 <= 0 || ("" + Math.floor((fGUpdate / fgCount) * 100) / 100).equals("NaN") || Math.floor((fGUpdate / fgCount) * 100) / 100 > 9999) {
							if (!world.isClientSide()) {
								BlockPos _bp = BlockPos.containing(c_x, c_y, c_z);
								BlockEntity _blockEntity = world.getBlockEntity(_bp);
								BlockState _bs = world.getBlockState(_bp);
								if (_blockEntity != null) {
									_blockEntity.getPersistentData().putDouble("old_calculated", (getBlockNBTNumber(world, BlockPos.containing(c_x, c_y, c_z), "gp_generated")));
								}
								if (world instanceof Level _level)
									_level.sendBlockUpdated(_bp, _bs, _bs, 3);
							}
							{
								EuruModVariables.PlayerVariables _vars = entity.getData(EuruModVariables.PLAYER_VARIABLES);
								_vars.changingAB2 = "Producing: " + getBlockNBTNumber(world, BlockPos.containing(c_x, c_y, c_z), "gp_generated") + " GP";
								_vars.markSyncDirty();
							}
						} else {
							if (!world.isClientSide()) {
								BlockPos _bp = BlockPos.containing(c_x, c_y, c_z);
								BlockEntity _blockEntity = world.getBlockEntity(_bp);
								BlockState _bs = world.getBlockState(_bp);
								if (_blockEntity != null) {
									_blockEntity.getPersistentData().putDouble("old_calculated", (Math.floor((fGUpdate / fgCount) * 100) / 100));
								}
								if (world instanceof Level _level)
									_level.sendBlockUpdated(_bp, _bs, _bs, 3);
							}
							{
								EuruModVariables.PlayerVariables _vars = entity.getData(EuruModVariables.PLAYER_VARIABLES);
								_vars.changingAB2 = "Producing: " + Math.floor((fGUpdate / fgCount) * 100) / 100 + " GP";
								_vars.markSyncDirty();
							}
						}
					} else {
						{
							EuruModVariables.PlayerVariables _vars = entity.getData(EuruModVariables.PLAYER_VARIABLES);
							_vars.changingAB2 = "Producing: " + "0.0" + " GP";
							_vars.markSyncDirty();
						}
					}
				} else {
					if (getBlockNBTLogic(world, BlockPos.containing(c_x, c_y, c_z), "generating")) {
						if (getBlockNBTNumber(world, BlockPos.containing(c_x, c_y, c_z), "old_calculated") > 0) {
							{
								EuruModVariables.PlayerVariables _vars = entity.getData(EuruModVariables.PLAYER_VARIABLES);
								_vars.changingAB2 = "Producing: " + getBlockNBTNumber(world, BlockPos.containing(c_x, c_y, c_z), "old_calculated") + " GP";
								_vars.markSyncDirty();
							}
						} else {
							{
								EuruModVariables.PlayerVariables _vars = entity.getData(EuruModVariables.PLAYER_VARIABLES);
								_vars.changingAB2 = "Producing: " + getBlockNBTNumber(world, BlockPos.containing(c_x, c_y, c_z), "gp_generated") + " GP";
								_vars.markSyncDirty();
							}
						}
					} else {
						{
							EuruModVariables.PlayerVariables _vars = entity.getData(EuruModVariables.PLAYER_VARIABLES);
							_vars.changingAB2 = "Producing: " + "0.0" + " GP";
							_vars.markSyncDirty();
						}
					}
				}
			} else if ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == EuruModItems.GP_SCANNER.get()
					|| (entity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY).getItem() == EuruModItems.GP_SCANNER.get()) {
				if (!(getBlockNBTNumber(world, BlockPos.containing(c_x, c_y, c_z), "wait_time") == 0)) {
					if (!entity.getData(EuruModVariables.PLAYER_VARIABLES).playerGPChecking) {
						{
							EuruModVariables.PlayerVariables _vars = entity.getData(EuruModVariables.PLAYER_VARIABLES);
							_vars.changingAB2 = "Progress: " + getBlockNBTNumber(world, BlockPos.containing(c_x, c_y, c_z), "cProgress") + "/" + getBlockNBTNumber(world, BlockPos.containing(c_x, c_y, c_z), "wait_time");
							_vars.markSyncDirty();
						}
					}
				} else {
					{
						EuruModVariables.PlayerVariables _vars = entity.getData(EuruModVariables.PLAYER_VARIABLES);
						_vars.changingAB2 = "Not in progress!";
						_vars.markSyncDirty();
					}
				}
			} else {
				{
					EuruModVariables.PlayerVariables _vars = entity.getData(EuruModVariables.PLAYER_VARIABLES);
					_vars.changingAB2 = "";
					_vars.markSyncDirty();
				}
			}
		} else {
			{
				EuruModVariables.PlayerVariables _vars = entity.getData(EuruModVariables.PLAYER_VARIABLES);
				_vars.changingAB1 = "";
				_vars.changingAB2 = "";
				_vars.markSyncDirty();
			}
		}
		if (entity.getData(EuruModVariables.PLAYER_VARIABLES).overlayCounter > 1) {
			{
				EuruModVariables.PlayerVariables _vars = entity.getData(EuruModVariables.PLAYER_VARIABLES);
				_vars.overlayCounter = 0;
				_vars.markSyncDirty();
			}
			if (!(entity.getData(EuruModVariables.PLAYER_VARIABLES).changingAB1).equals("") || !(entity.getData(EuruModVariables.PLAYER_VARIABLES).changingAB2).equals("")) {
				{
					EuruModVariables.PlayerVariables _vars = entity.getData(EuruModVariables.PLAYER_VARIABLES);
					_vars.playerAB1 = entity.getData(EuruModVariables.PLAYER_VARIABLES).changingAB1;
					_vars.playerAB2 = entity.getData(EuruModVariables.PLAYER_VARIABLES).changingAB2;
					_vars.changeAB = true;
					_vars.markSyncDirty();
				}
			} else {
				{
					EuruModVariables.PlayerVariables _vars = entity.getData(EuruModVariables.PLAYER_VARIABLES);
					_vars.changeAB = false;
					_vars.markSyncDirty();
				}
			}
		} else {
			{
				EuruModVariables.PlayerVariables _vars = entity.getData(EuruModVariables.PLAYER_VARIABLES);
				_vars.overlayCounter = entity.getData(EuruModVariables.PLAYER_VARIABLES).overlayCounter + 1;
				_vars.markSyncDirty();
			}
		}
	}

	private static String getBlockNBTString(LevelAccessor world, BlockPos pos, String tag) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity != null)
			return blockEntity.getPersistentData().getString(tag);
		return "";
	}

	private static boolean getBlockNBTLogic(LevelAccessor world, BlockPos pos, String tag) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity != null)
			return blockEntity.getPersistentData().getBoolean(tag);
		return false;
	}

	private static double getBlockNBTNumber(LevelAccessor world, BlockPos pos, String tag) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity != null)
			return blockEntity.getPersistentData().getDouble(tag);
		return -1;
	}
}