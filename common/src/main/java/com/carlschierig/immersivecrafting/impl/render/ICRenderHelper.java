package com.carlschierig.immersivecrafting.impl.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

import java.util.List;
import java.util.Optional;

public class ICRenderHelper {
    private static final Minecraft CLIENT = Minecraft.getInstance();


    public static void renderItemAnnotation(GuiGraphicsExtractor draw, int x, int y, Component annotation) {
        var pose = draw.pose();
        pose.pushMatrix();
        // TODO: fix this
//        pose.translate(0, 0, 200);

        // taken from EMI
        int tx = x + 17 - Math.min(14, Minecraft.getInstance().font.width(annotation));
        draw.text(Minecraft.getInstance().font, annotation, tx, y + 9, -1, true);
        pose.popMatrix();
    }

    public static void renderTooltip(Screen screen, GuiGraphicsExtractor draw, int x, int y, List<Component> components) {
        renderTooltip(screen, draw, x, y, screen.width / 2 - 16, components);
    }

    public static void renderTooltip(Screen screen, GuiGraphicsExtractor draw, int x, int y, int maxWidth, List<Component> components) {
        y = Math.max(16, y);
        // TODO: fix this
        draw.setTooltipForNextFrame(CLIENT.font, components, Optional.empty(), x, y);
    }
}
