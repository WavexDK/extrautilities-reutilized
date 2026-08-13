package net.wavedk.extrautilitiesreutilized.block;

import net.wavedk.extrautilitiesreutilized.procedures.TransferCableOnTickUpdateProcedure;
import net.wavedk.extrautilitiesreutilized.procedures.TransferCableOnCableDisconnectFromBlockProcedure;
import net.wavedk.extrautilitiesreutilized.procedures.TransferCableOnCableConnectToABlockProcedure;
import net.wavedk.extrautilitiesreutilized.procedures.TransferCableConnectProcedureProcedure;
import net.wavedk.extrautilitiesreutilized.init.EuruModBlockEntities;
import net.wavedk.extrautilitiesreutilized.block.entity.TransferCableBlockEntity;

import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;

public class TransferCableBlock extends Block implements EntityBlock {
	public static final BooleanProperty NORTH = BlockStateProperties.NORTH;
	public static final BooleanProperty SOUTH = BlockStateProperties.SOUTH;
	public static final BooleanProperty WEST = BlockStateProperties.WEST;
	public static final BooleanProperty EAST = BlockStateProperties.EAST;
	public static final BooleanProperty UP = BlockStateProperties.UP;
	public static final BooleanProperty DOWN = BlockStateProperties.DOWN;
	private static final VoxelShape CORE = box(5, 5, 5, 11, 11, 11);
	private static final VoxelShape ARM_NORTH = box(5, 5, 0, 11, 11, 5);
	private static final VoxelShape ARM_SOUTH = box(5, 5, 11, 11, 11, 16);
	private static final VoxelShape ARM_WEST = box(0, 5, 5, 5, 11, 11);
	private static final VoxelShape ARM_EAST = box(11, 5, 5, 16, 11, 11);
	private static final VoxelShape ARM_UP = box(5, 11, 5, 11, 16, 11);
	private static final VoxelShape ARM_DOWN = box(5, 0, 5, 11, 5, 11);
	private static final VoxelShape[] SHAPES = makeShapes();

	public TransferCableBlock() {
		super(BlockBehaviour.Properties.of().strength(1f, 10f).sound(SoundType.STONE).lightLevel(blockstate -> Math.max(0, Math.min(15, (int) 0))).requiresCorrectToolForDrops().noOcclusion());
		registerDefaultState(stateDefinition.any().setValue(NORTH, false).setValue(SOUTH, false).setValue(WEST, false).setValue(EAST, false).setValue(UP, false).setValue(DOWN, false));
	}

	private static VoxelShape[] makeShapes() {
		VoxelShape[] result = new VoxelShape[64];
		for (int mask = 0; mask < result.length; mask++) {
			VoxelShape shape = CORE;
			if ((mask & 1) != 0)
				shape = Shapes.or(shape, ARM_NORTH);
			if ((mask & 2) != 0)
				shape = Shapes.or(shape, ARM_SOUTH);
			if ((mask & 4) != 0)
				shape = Shapes.or(shape, ARM_WEST);
			if ((mask & 8) != 0)
				shape = Shapes.or(shape, ARM_EAST);
			if ((mask & 16) != 0)
				shape = Shapes.or(shape, ARM_UP);
			if ((mask & 32) != 0)
				shape = Shapes.or(shape, ARM_DOWN);
			result[mask] = shape.optimize();
		}
		return result;
	}

	private static int shapeIndex(BlockState state) {
		int index = 0;
		if (state.getValue(NORTH))
			index |= 1;
		if (state.getValue(SOUTH))
			index |= 2;
		if (state.getValue(WEST))
			index |= 4;
		if (state.getValue(EAST))
			index |= 8;
		if (state.getValue(UP))
			index |= 16;
		if (state.getValue(DOWN))
			index |= 32;
		return index;
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return SHAPES[shapeIndex(state)];
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return SHAPES[shapeIndex(state)];
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(NORTH, SOUTH, WEST, EAST, UP, DOWN);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		LevelAccessor level = context.getLevel();
		BlockPos pos = context.getClickedPos();
		return defaultBlockState().setValue(NORTH, connects(level, pos.north(), level.getBlockState(pos.north()), Direction.SOUTH)).setValue(SOUTH, connects(level, pos.south(), level.getBlockState(pos.south()), Direction.NORTH))
				.setValue(WEST, connects(level, pos.west(), level.getBlockState(pos.west()), Direction.EAST)).setValue(EAST, connects(level, pos.east(), level.getBlockState(pos.east()), Direction.WEST))
				.setValue(UP, connects(level, pos.above(), level.getBlockState(pos.above()), Direction.DOWN)).setValue(DOWN, connects(level, pos.below(), level.getBlockState(pos.below()), Direction.UP));
	}

	@Override
	public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos currentPos, BlockPos neighborPos) {
		BooleanProperty changedProperty = property(direction);
		boolean wasConnected = state.getValue(changedProperty);
		boolean isConnected = connects(level, neighborPos, neighborState, direction.getOpposite());
		BlockState updated = state.setValue(changedProperty, isConnected);
		if (wasConnected != isConnected) {
			if (level.getBlockEntity(currentPos) instanceof TransferCableBlockEntity cable)
				cable.invalidateNetworkCache();
			if (level instanceof ServerLevel serverLevel) {
				if (isConnected)
					fireCableConnected(serverLevel, neighborPos, neighborState, direction.getOpposite());
				else
					fireCableDisconnected(serverLevel, neighborPos, neighborState, direction.getOpposite());
			}
		}
		return updated;
	}

	private boolean connects(LevelAccessor world, BlockPos neighborPos, BlockState blockstate, Direction direction) {
		return TransferCableConnectProcedureProcedure.execute(world, neighborPos.getX(), neighborPos.getY(), neighborPos.getZ(), direction);
	}

	public static BooleanProperty property(Direction direction) {
		return switch (direction) {
			case NORTH -> NORTH;
			case SOUTH -> SOUTH;
			case WEST -> WEST;
			case EAST -> EAST;
			case UP -> UP;
			case DOWN -> DOWN;
		};
	}

	/** Returns true only when two adjacent blocks are visibly connected cables of this element type. */
	public static boolean isCableConnection(LevelAccessor level, BlockPos pos, Direction direction) {
		BlockState state = level.getBlockState(pos);
		BlockPos neighborPos = pos.relative(direction);
		BlockState neighbor = level.getBlockState(neighborPos);
		if (!(state.getBlock() instanceof TransferCableBlock) || !(neighbor.getBlock() instanceof TransferCableBlock))
			return false;
		return state.getValue(property(direction)) || neighbor.getValue(property(direction.getOpposite()));
	}

	@Override
	public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		super.setPlacedBy(level, pos, state, placer, stack);
		if (!level.isClientSide) {
			if (level.getBlockEntity(pos) instanceof TransferCableBlockEntity cable)
				cable.joinExistingNetwork();
			ServerLevel serverLevel = (ServerLevel) level;
			for (Direction outward : Direction.values()) {
				if (!state.getValue(property(outward)))
					continue;
				BlockPos neighborPos = pos.relative(outward);
				fireCableConnected(serverLevel, neighborPos, level.getBlockState(neighborPos), outward.getOpposite());
			}
		}
	}

	@Override
	public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
		if (state.getBlock() != newState.getBlock() && !level.isClientSide) {
			ServerLevel serverLevel = (ServerLevel) level;
			for (Direction outward : Direction.values()) {
				if (!state.getValue(property(outward)))
					continue;
				BlockPos neighborPos = pos.relative(outward);
				fireCableDisconnected(serverLevel, neighborPos, level.getBlockState(neighborPos), outward.getOpposite());
			}
			if (level.getBlockEntity(pos) instanceof TransferCableBlockEntity cable)
				cable.prepareForRemoval(state);
		}
		super.onRemove(state, level, pos, newState, isMoving);
	}

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
		if (!level.isClientSide && blockEntityType == EuruModBlockEntities.TRANSFER_CABLE.get())
			return (tickLevel, pos, blockstate, blockEntity) -> tickConnectedToNonCable((ServerLevel) tickLevel, pos, blockstate);
		return null;
	}

	private static void tickConnectedToNonCable(ServerLevel world, BlockPos pos, BlockState cableState) {
		for (Direction outward : Direction.values()) {
			if (!cableState.getValue(property(outward)))
				continue;
			BlockPos neighborPos = pos.relative(outward);
			BlockState blockstate = world.getBlockState(neighborPos);
			if (blockstate.getBlock() instanceof TransferCableBlock)
				continue;
			Direction direction = outward.getOpposite();
			TransferCableOnTickUpdateProcedure.execute(world, neighborPos.getX(), neighborPos.getY(), neighborPos.getZ(), direction);
		}
	}

	private static void fireCableConnected(ServerLevel world, BlockPos neighborPos, BlockState blockstate, Direction direction) {
		TransferCableOnCableConnectToABlockProcedure.execute(world, neighborPos.getX(), neighborPos.getY(), neighborPos.getZ(), direction);
	}

	private static void fireCableDisconnected(ServerLevel world, BlockPos neighborPos, BlockState blockstate, Direction direction) {
		TransferCableOnCableDisconnectFromBlockProcedure.execute(world, neighborPos.getX(), neighborPos.getY(), neighborPos.getZ(), direction);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new TransferCableBlockEntity(pos, state);
	}
}