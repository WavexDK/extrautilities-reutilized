package net.wavedk.extrautilitiesreutilized.block;

import net.wavedk.extrautilitiesreutilized.procedures.EnderLillyOnTickUpdateProcedure;
import net.wavedk.extrautilitiesreutilized.procedures.EnderLillyCanBoneMealBeUsedOnThisBlockProcedure;
import net.wavedk.extrautilitiesreutilized.procedures.EnderLillyBlockDestroyedByPlayerProcedure;
import net.wavedk.extrautilitiesreutilized.procedures.EnderLillyBlockDestroyedByExplosionProcedure;
import net.wavedk.extrautilitiesreutilized.block.entity.EnderLillyBlockEntity;

import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.Containers;
import net.minecraft.util.RandomSource;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;

import com.google.common.collect.ImmutableMap;

public class EnderLillyBlock extends Block implements EntityBlock, BonemealableBlock {
	public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
	public static final IntegerProperty STAGE = IntegerProperty.create("stage", 0, 7);
	private final ImmutableMap<BlockState, VoxelShape> shapes = this.makeShapes();

	public EnderLillyBlock() {
		super(BlockBehaviour.Properties.of().sound(SoundType.GRASS).instabreak().noCollission().randomTicks().isRedstoneConductor((bs, br, bp) -> false).ignitedByLava());
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(STAGE, 0));
	}

	private ImmutableMap<BlockState, VoxelShape> makeShapes() {
		return this.getShapeForEachState(state -> {
			if (state.getValue(STAGE) == 2) {
				return switch (state.getValue(FACING)) {
					case NORTH -> box(6, 0, 6, 12, 6, 12);
					case EAST -> box(4, 0, 6, 10, 6, 12);
					case WEST -> box(6, 0, 4, 12, 6, 10);
					default -> box(4, 0, 4, 10, 6, 10);
				};
			} else if (state.getValue(STAGE) == 3) {
				return switch (state.getValue(FACING)) {
					case NORTH -> box(5, 0, 5, 13, 8, 13);
					case EAST -> box(3, 0, 5, 11, 8, 13);
					case WEST -> box(5, 0, 3, 13, 8, 11);
					default -> box(3, 0, 3, 11, 8, 11);
				};
			} else if (state.getValue(STAGE) == 4) {
				return switch (state.getValue(FACING)) {
					case NORTH -> box(3, 0, 3, 14, 10, 14);
					case EAST -> box(2, 0, 3, 13, 10, 14);
					case WEST -> box(3, 0, 2, 14, 10, 13);
					default -> box(2, 0, 2, 13, 10, 13);
				};
			} else if (state.getValue(STAGE) == 5) {
				return switch (state.getValue(FACING)) {
					case NORTH -> box(1, 0, 1, 15, 15, 15);
					case EAST -> box(1, 0, 1, 15, 15, 15);
					case WEST -> box(1, 0, 1, 15, 15, 15);
					default -> box(1, 0, 1, 15, 15, 15);
				};
			} else if (state.getValue(STAGE) == 6) {
				return switch (state.getValue(FACING)) {
					case NORTH -> box(1, 0, 1, 15, 15, 15);
					case EAST -> box(1, 0, 1, 15, 15, 15);
					case WEST -> box(1, 0, 1, 15, 15, 15);
					default -> box(1, 0, 1, 15, 15, 15);
				};
			} else if (state.getValue(STAGE) == 7) {
				return switch (state.getValue(FACING)) {
					case NORTH -> box(1, 0, 1, 15, 15, 15);
					case EAST -> box(1, 0, 1, 15, 15, 15);
					case WEST -> box(1, 0, 1, 15, 15, 15);
					default -> box(1, 0, 1, 15, 15, 15);
				};
			}
			return switch (state.getValue(FACING)) {
				case NORTH -> box(6, 0, 6, 12, 4, 12);
				case EAST -> box(4, 0, 6, 10, 4, 12);
				case WEST -> box(6, 0, 4, 12, 4, 10);
				default -> box(4, 0, 4, 10, 4, 10);
			};
		});
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return shapes.get(state);
	}

	@Override
	public boolean propagatesSkylightDown(BlockState state, BlockGetter reader, BlockPos pos) {
		return true;
	}

	@Override
	public int getLightBlock(BlockState state, BlockGetter worldIn, BlockPos pos) {
		return 0;
	}

	@Override
	public VoxelShape getVisualShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return Shapes.empty();
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(FACING, STAGE);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		BlockState state = super.getStateForPlacement(context);
		if (state == null)
			return null;
		return state.setValue(FACING, context.getHorizontalDirection().getOpposite()).setValue(STAGE, 0);
	}

	public BlockState rotate(BlockState state, Rotation rot) {
		return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
	}

	public BlockState mirror(BlockState state, Mirror mirrorIn) {
		return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
	}

	@Override
	public int getFlammability(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
		return 2;
	}

	@Override
	public void randomTick(BlockState blockstate, ServerLevel world, BlockPos pos, RandomSource random) {
		super.randomTick(blockstate, world, pos, random);
		EnderLillyOnTickUpdateProcedure.execute(world, pos.getX(), pos.getY(), pos.getZ(), blockstate);
	}

	@Override
	public boolean onDestroyedByPlayer(BlockState blockstate, Level world, BlockPos pos, Player entity, boolean willHarvest, FluidState fluid) {
		boolean retval = super.onDestroyedByPlayer(blockstate, world, pos, entity, willHarvest, fluid);
		EnderLillyBlockDestroyedByPlayerProcedure.execute(world, pos.getX(), pos.getY(), pos.getZ());
		return retval;
	}

	@Override
	public void wasExploded(Level world, BlockPos pos, Explosion e) {
		super.wasExploded(world, pos, e);
		EnderLillyBlockDestroyedByExplosionProcedure.execute(world, pos.getX(), pos.getY(), pos.getZ());
	}

	@Override
	public boolean isValidBonemealTarget(LevelReader worldIn, BlockPos pos, BlockState blockstate) {
		return true;
	}

	@Override
	public boolean isBonemealSuccess(Level world, RandomSource random, BlockPos pos, BlockState blockstate) {
		return true;
	}

	@Override
	public void performBonemeal(ServerLevel world, RandomSource random, BlockPos pos, BlockState blockstate) {
		EnderLillyCanBoneMealBeUsedOnThisBlockProcedure.execute(world, pos.getX(), pos.getY(), pos.getZ());
	}

	@Override
	public MenuProvider getMenuProvider(BlockState state, Level worldIn, BlockPos pos) {
		BlockEntity tileEntity = worldIn.getBlockEntity(pos);
		return tileEntity instanceof MenuProvider menuProvider ? menuProvider : null;
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new EnderLillyBlockEntity(pos, state);
	}

	@Override
	public boolean triggerEvent(BlockState state, Level world, BlockPos pos, int eventID, int eventParam) {
		super.triggerEvent(state, world, pos, eventID, eventParam);
		BlockEntity blockEntity = world.getBlockEntity(pos);
		return blockEntity != null && blockEntity.triggerEvent(eventID, eventParam);
	}

	@Override
	public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
		if (state.getBlock() != newState.getBlock()) {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof EnderLillyBlockEntity be) {
				Containers.dropContents(world, pos, be);
				world.updateNeighbourForOutputSignal(pos, this);
			}
			super.onRemove(state, world, pos, newState, isMoving);
		}
	}

	@Override
	public boolean hasAnalogOutputSignal(BlockState state) {
		return true;
	}

	@Override
	public int getAnalogOutputSignal(BlockState blockState, Level world, BlockPos pos) {
		BlockEntity tileentity = world.getBlockEntity(pos);
		if (tileentity instanceof EnderLillyBlockEntity be)
			return AbstractContainerMenu.getRedstoneSignalFromContainer(be);
		else
			return 0;
	}
}