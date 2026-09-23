package net.wavedk.extrautilitiesreutilized.command;

import net.wavedk.extrautilitiesreutilized.procedures.ReturnSuccessReloadProcedure;
import net.wavedk.extrautilitiesreutilized.procedures.EURUUnifiedConfigManagerProcedure;

import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.common.util.FakePlayerFactory;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.Direction;
import net.minecraft.commands.Commands;

@EventBusSubscriber
public class ExtraUtilitiesSPCommand {
	@SubscribeEvent
	public static void registerCommand(RegisterCommandsEvent event) {
		if (event.getCommandSelection() == Commands.CommandSelection.INTEGRATED)
			event.getDispatcher().register(Commands.literal("extrautilities")

					.then(Commands.literal("reload").executes(arguments -> {
						Level world = arguments.getSource().getUnsidedLevel();
						double x = arguments.getSource().getPosition().x();
						double y = arguments.getSource().getPosition().y();
						double z = arguments.getSource().getPosition().z();
						Entity entity = arguments.getSource().getEntity();
						if (entity == null && world instanceof ServerLevel _servLevel)
							entity = FakePlayerFactory.getMinecraft(_servLevel);
						Direction direction = Direction.DOWN;
						if (entity != null)
							direction = entity.getDirection();

						EURUUnifiedConfigManagerProcedure.execute();
						return 0;
					}).executes(arguments -> {
						Level world = arguments.getSource().getUnsidedLevel();
						double x = arguments.getSource().getPosition().x();
						double y = arguments.getSource().getPosition().y();
						double z = arguments.getSource().getPosition().z();
						Entity entity = arguments.getSource().getEntity();
						if (entity == null && world instanceof ServerLevel _servLevel)
							entity = FakePlayerFactory.getMinecraft(_servLevel);
						Direction direction = Direction.DOWN;
						if (entity != null)
							direction = entity.getDirection();

						ReturnSuccessReloadProcedure.execute(entity);
						return 0;
					})));
	}

}