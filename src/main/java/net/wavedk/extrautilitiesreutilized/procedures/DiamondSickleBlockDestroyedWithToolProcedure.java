package net.wavedk.extrautilitiesreutilized.procedures;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.tags.ItemTags;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.BlockPos;

public class DiamondSickleBlockDestroyedWithToolProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, ItemStack itemstack) {
		double cX = 0;
		double cY = 0;
		double cZ = 0;
		BlockState cBlock = Blocks.AIR.defaultBlockState();
		if ((new ItemStack((world.getBlockState(BlockPos.containing(x, y, z))).getBlock())).is(ItemTags.create(ResourceLocation.parse("minecraft:flowers"))) || (world.getBlockState(BlockPos.containing(x, y, z))).getBlock() == Blocks.SHORT_GRASS
				|| (world.getBlockState(BlockPos.containing(x, y, z))).getBlock() == Blocks.TALL_GRASS || (world.getBlockState(BlockPos.containing(x, y, z))).getBlock() == Blocks.SEAGRASS
				|| (world.getBlockState(BlockPos.containing(x, y, z))).getBlock() == Blocks.TALL_SEAGRASS) {
			{
				final int _radiusLoopCenterX27 = (int) Math.floor(x);
				final int _radiusLoopCenterY27 = (int) Math.floor(y);
				final int _radiusLoopCenterZ27 = (int) Math.floor(z);
				final int _radiusLoopRadius27 = Math.max(0, (int) Math.floor((itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getDouble("spreadSickle"))));
				final int _radiusLoopMinX27 = _radiusLoopCenterX27 - _radiusLoopRadius27;
				final int _radiusLoopMaxX27 = _radiusLoopCenterX27 + _radiusLoopRadius27;
				final int _radiusLoopMinY27 = _radiusLoopCenterY27 - _radiusLoopRadius27;
				final int _radiusLoopMaxY27 = _radiusLoopCenterY27 + _radiusLoopRadius27;
				final int _radiusLoopMinZ27 = _radiusLoopCenterZ27 - _radiusLoopRadius27;
				final int _radiusLoopMaxZ27 = _radiusLoopCenterZ27 + _radiusLoopRadius27;
				for (int _radiusLoopX27 = _radiusLoopMinX27; _radiusLoopX27 <= _radiusLoopMaxX27; _radiusLoopX27++) {
					for (int _radiusLoopY27 = _radiusLoopMinY27; _radiusLoopY27 <= _radiusLoopMaxY27; _radiusLoopY27++) {
						for (int _radiusLoopZ27 = _radiusLoopMinZ27; _radiusLoopZ27 <= _radiusLoopMaxZ27; _radiusLoopZ27++) {
							cX = _radiusLoopX27;
							cY = _radiusLoopY27;
							cZ = _radiusLoopZ27;
							if ((new ItemStack((world.getBlockState(BlockPos.containing(cX, cY, cZ))).getBlock())).is(ItemTags.create(ResourceLocation.parse("minecraft:flowers")))
									|| (world.getBlockState(BlockPos.containing(cX, cY, cZ))).getBlock() == Blocks.SHORT_GRASS || (world.getBlockState(BlockPos.containing(cX, cY, cZ))).getBlock() == Blocks.TALL_GRASS
									|| (world.getBlockState(BlockPos.containing(cX, cY, cZ))).getBlock() == Blocks.SEAGRASS || (world.getBlockState(BlockPos.containing(cX, cY, cZ))).getBlock() == Blocks.TALL_SEAGRASS) {
								{
									BlockPos _pos = BlockPos.containing(cX, cY, cZ);
									Block.dropResources(world.getBlockState(_pos), world, BlockPos.containing(x, y, z), null);
									world.destroyBlock(_pos, false);
								}
								if (world instanceof ServerLevel _level) {
									itemstack.hurtAndBreak(1, _level, null, _stkprov -> {
									});
								}
							}
						}
					}
				}
			}
		}
	}
}