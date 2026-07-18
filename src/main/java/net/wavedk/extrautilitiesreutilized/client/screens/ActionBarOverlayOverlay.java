package net.wavedk.extrautilitiesreutilized.client.screens;

import net.wavedk.extrautilitiesreutilized.procedures.ReturnAB2Procedure;
import net.wavedk.extrautilitiesreutilized.procedures.ReturnAB1Procedure;
import net.wavedk.extrautilitiesreutilized.procedures.ActionBarOverlayDisplayOverlayIngameProcedure;

import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.client.Minecraft;

@EventBusSubscriber(Dist.CLIENT)
public class ActionBarOverlayOverlay {
    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void eventHandler(RenderGuiEvent.Pre event) {
        int w = event.getGuiGraphics().guiWidth();
        int h = event.getGuiGraphics().guiHeight();

        Level world = null;
        double x = 0;
        double y = 0;
        double z = 0;

        Player entity = Minecraft.getInstance().player;
        if (entity != null) {
            world = entity.level();
            x = entity.getX();
            y = entity.getY();
            z = entity.getZ();
        }

        if (ActionBarOverlayDisplayOverlayIngameProcedure.execute(entity)) {
            String ab1Text = ReturnAB1Procedure.execute(entity);
            String ab2Text = ReturnAB2Procedure.execute(entity);

            event.getGuiGraphics().drawString(Minecraft.getInstance().font,
                    ab1Text, (w - Minecraft.getInstance().font.width(ab1Text)) / 2, h - 65, -1, true);
            event.getGuiGraphics().drawString(Minecraft.getInstance().font,
                    ab2Text, (w - Minecraft.getInstance().font.width(ab2Text)) / 2, h - 85, -1, true);
        }
    }
}