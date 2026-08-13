package net.wavedk.extrautilitiesreutilized.ai;

import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.Mob;
import net.minecraft.tags.TagKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.HolderSet;
import net.minecraft.core.BlockPos;

import java.util.EnumSet;

public class MoveToStructureGoal extends Goal {
	private final Mob mob;
	private final String structureInput;
	private final double speed;
	private final String modid;
	private BlockPos targetPos = null;
	private boolean finished = false;

	public MoveToStructureGoal(Mob mob, String structureInput, double speed, String modid) {
		this.mob = mob;
		this.structureInput = structureInput;
		this.speed = speed;
		this.modid = modid;
		this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
	}

	@Override
	public boolean canUse() {
		return !finished && mob.isAlive();
	}

	@Override
	public boolean canContinueToUse() {
		return !finished && targetPos != null && mob.isAlive();
	}

	@Override
	public void start() {
		if (!(mob.level() instanceof ServerLevel level))
			return;
		String input = structureInput;
		if (!input.contains(":") && !input.startsWith("#")) {
			input = modid + ":" + input;
		}
		HolderSet<Structure> holderSet = null;
		var registry = level.registryAccess().lookupOrThrow(Registries.STRUCTURE);
		if (input.startsWith("#")) {
			ResourceLocation tagLoc = ResourceLocation.parse(input.substring(1));
			TagKey<Structure> structTag = TagKey.create(Registries.STRUCTURE, tagLoc);
			holderSet = registry.get(structTag).orElse(null);
		} else {
			ResourceLocation structLoc = ResourceLocation.parse(input);
			ResourceKey<Structure> structKey = ResourceKey.create(Registries.STRUCTURE, structLoc);
			var holderOpt = registry.get(structKey);
			if (holderOpt.isPresent()) {
				holderSet = HolderSet.direct(holderOpt.get());
			}
		}
		if (holderSet != null) {
			var foundStruct = level.getChunkSource().getGenerator().findNearestMapStructure(level, holderSet, mob.blockPosition(), 100, false);
			if (foundStruct != null && foundStruct.getFirst() != null) {
				this.targetPos = foundStruct.getFirst();
			}
		}
		if (this.targetPos == null) {
			this.finished = true;
		}
	}

	@Override
	public void tick() {
		if (targetPos == null)
			return;
		mob.getLookControl().setLookAt(targetPos.getX() + 0.5D, mob.getEyeY(), targetPos.getZ() + 0.5D, 180.0F, 180.0F);
		double dx = (targetPos.getX() + 0.5D) - mob.getX();
		double dz = (targetPos.getZ() + 0.5D) - mob.getZ();
		float yaw = (float) (Math.atan2(dz, dx) * (180.0D / Math.PI)) - 90.0F;
		mob.setYRot(yaw);
		mob.setYHeadRot(yaw);
		mob.setYBodyRot(yaw);
		double distSq2D = dx * dx + dz * dz;
		if (distSq2D <= 16.0D) {
			mob.getNavigation().stop();
			this.finished = true;
		} else {
			if (mob.getNavigation().isDone() || mob.tickCount % 10 == 0) {
				mob.getNavigation().moveTo(targetPos.getX() + 0.5D, mob.getY(), targetPos.getZ() + 0.5D, speed);
			}
		}
	}

	@Override
	public void stop() {
		mob.getNavigation().stop();
	}
}