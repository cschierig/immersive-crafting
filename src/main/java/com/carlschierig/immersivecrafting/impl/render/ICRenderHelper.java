package com.carlschierig.immersivecrafting.impl.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;

import java.util.List;

public class ICRenderHelper {

    public static void renderItemAnnotation(GuiGraphics draw, int x, int y, Component annotation) {
        var pose = draw.pose();
        pose.pushPose();
        pose.translate(0, 0, 200);

        // taken from EMI
        int tx = x + 17 - Math.min(14, Minecraft.getInstance().font.width(annotation));
        draw.drawString(Minecraft.getInstance().font, annotation, tx, y + 9, -1, true);
        pose.popPose();
    }

    public static void renderTooltip(GuiGraphics draw, int x, int y, List<ClientTooltipComponent> components) {

    }
}
