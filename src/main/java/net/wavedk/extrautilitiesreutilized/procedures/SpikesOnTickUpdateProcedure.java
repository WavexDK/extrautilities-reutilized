package net.wavedk.extrautilitiesreutilized.procedures;

import org.apache.commons.lang3.function.FailableFunction;

import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.projectile.windcharge.WindCharge;
import net.minecraft.world.entity.projectile.windcharge.BreezeWindCharge;
import net.minecraft.world.entity.projectile.SpectralArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.BlockPos;

import java.util.function.Supplier;
import java.util.UUID;

public class SpikesOnTickUpdateProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		Entity player = null;
		if (world.getServer() != null) {
			LevelAccessor _origWorld = world;
			for (ServerLevel worlditerator : world.getServer().getAllLevels()) {
				world = worlditerator;
				player = world instanceof ServerLevel _serverGetEntityUUID ? _serverGetEntityUUID.getEntity(tryOrDefault((getBlockNBTString(world, BlockPos.containing(x, y, z), "placedBy")), UUID::fromString, () -> new UUID(0, 0))) : null;
				if (player instanceof ServerPlayer || player instanceof Player) {
					break;
				}
			}
			world = _origWorld;
		}
		if (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "damage_tickcounter") >= getBlockNBTNumber(world, BlockPos.containing(x, y, z), "damage_tickrate")) {
			if (!world.isClientSide()) {
				BlockPos _bp = BlockPos.containing(x, y, z);
				BlockEntity _blockEntity = world.getBlockEntity(_bp);
				BlockState _bs = world.getBlockState(_bp);
				if (_blockEntity != null) {
					_blockEntity.getPersistentData().putDouble("damage_tickcounter", 0);
				}
				if (world instanceof Level _level)
					_level.sendBlockUpdated(_bp, _bs, _bs, 3);
			}
			if (getBlockNBTLogic(world, BlockPos.containing(x, y, z), "damage_byplayer")) {
				for (Entity entityiterator : world.getEntities(null, new AABB(x, (y + 0.25), z, (x + 1), (y + 3), (z + 1)))) {
					if (!(entityiterator instanceof ExperienceOrb) && !(entityiterator instanceof ItemEntity) && !(entityiterator instanceof Arrow) && !(entityiterator instanceof SpectralArrow) && !(entityiterator instanceof BreezeWindCharge)
							&& !(entityiterator instanceof WindCharge)) {
						if (player instanceof ServerPlayer || player instanceof Player) {
							if ((BuiltInRegistries.ITEM.getKey((new ItemStack((world.getBlockState(BlockPos.containing(x, y - 1, z))).getBlock())).getItem()).toString())
									.equals(getBlockNBTString(world, BlockPos.containing(x, y, z), "damage_booster_block"))) {
								entityiterator.hurt(new DamageSource(world.holderOrThrow(DamageTypes.GENERIC), player), (float) (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "damage_tick") * 2));
							} else {
								entityiterator.hurt(new DamageSource(world.holderOrThrow(DamageTypes.GENERIC), player), (float) getBlockNBTNumber(world, BlockPos.containing(x, y, z), "damage_tick"));
							}
						} else {
							if ((BuiltInRegistries.ITEM.getKey((new ItemStack((world.getBlockState(BlockPos.containing(x, y - 1, z))).getBlock())).getItem()).toString())
									.equals(getBlockNBTString(world, BlockPos.containing(x, y, z), "damage_booster_block"))) {
								entityiterator.hurt(new DamageSource(world.holderOrThrow(DamageTypes.GENERIC)), (float) (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "damage_tick") * 2));
							} else {
								entityiterator.hurt(new DamageSource(world.holderOrThrow(DamageTypes.GENERIC)), (float) getBlockNBTNumber(world, BlockPos.containing(x, y, z), "damage_tick"));
							}
						}
					}
				}
			} else {
				for (Entity entityiterator : world.getEntities(null, new AABB(x, (y + 0.25), z, (x + 1), (y + 3), (z + 1)))) {
					if (!(entityiterator instanceof ExperienceOrb) && !(entityiterator instanceof Arrow) && !(entityiterator instanceof ItemEntity) && !(entityiterator instanceof SpectralArrow) && !(entityiterator instanceof BreezeWindCharge)
							&& !(entityiterator instanceof WindCharge)) {
						if ((BuiltInRegistries.ITEM.getKey((new ItemStack((world.getBlockState(BlockPos.containing(x, y - 1, z))).getBlock())).getItem()).toString())
								.equals(getBlockNBTString(world, BlockPos.containing(x, y, z), "damage_booster_block"))) {
							entityiterator.hurt(new DamageSource(world.holderOrThrow(DamageTypes.GENERIC)), (float) (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "damage_tick") * 2));
						} else {
							entityiterator.hurt(new DamageSource(world.holderOrThrow(DamageTypes.GENERIC)), (float) getBlockNBTNumber(world, BlockPos.containing(x, y, z), "damage_tick"));
						}
					}
				}
			}
		} else {
			if (!world.isClientSide()) {
				BlockPos _bp = BlockPos.containing(x, y, z);
				BlockEntity _blockEntity = world.getBlockEntity(_bp);
				BlockState _bs = world.getBlockState(_bp);
				if (_blockEntity != null) {
					_blockEntity.getPersistentData().putDouble("damage_tickcounter", (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "damage_tickcounter") + 1));
				}
				if (world instanceof Level _level)
					_level.sendBlockUpdated(_bp, _bs, _bs, 3);
			}
		}
	}

	private static String getBlockNBTString(LevelAccessor world, BlockPos pos, String tag) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity != null)
			return blockEntity.getPersistentData().getString(tag);
		return "";
	}

	private static <A, B> A tryOrDefault(B funcArg, FailableFunction<B, A, Exception> func, Supplier<A> fallback) {
		try {
			return func.apply(funcArg);
		} catch (Exception e) {
			return fallback.get();
		}
	}

	private static double getBlockNBTNumber(LevelAccessor world, BlockPos pos, String tag) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity != null)
			return blockEntity.getPersistentData().getDouble(tag);
		return -1;
	}

	private static boolean getBlockNBTLogic(LevelAccessor world, BlockPos pos, String tag) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity != null)
			return blockEntity.getPersistentData().getBoolean(tag);
		return false;
	}
}