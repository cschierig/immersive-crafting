package com.carlschierig.immersivecrafting.impl.render;

import com.carlschierig.immersivecrafting.mixin.GuiGraphicsAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import net.minecraft.network.chat.Component;
import org.quiltmc.loader.api.minecraft.ClientOnly;

import java.util.List;

@ClientOnly
public class ICRenderHelper {
    private static Minecraft CLIENT = Minecraft.getInstance();


    public static void renderItemAnnotation(GuiGraphics draw, int x, int y, Component annotation) {
        var pose = draw.pose();
        pose.pushPose();
        pose.translate(0, 0, 200);

        // taken from EMI
        int tx = x + 17 - Math.min(14, Minecraft.getInstance().font.width(annotation));
        draw.drawString(Minecraft.getInstance().font, annotation, tx, y + 9, -1, true);
        pose.popPose();
    }

    public static void renderTooltip(Screen screen, GuiGraphics draw, int x, int y, List<ClientTooltipComponent> components) {
        renderTooltip(screen, draw, x, y, screen.width / 2 - 16, components);
    }

    public static void renderTooltip(Screen screen, GuiGraphics draw, int x, int y, int maxWidth, List<ClientTooltipComponent> components) {
        y = Math.max(16, y);
        ((GuiGraphicsAccessor) draw).invokeRenderTooltip(CLIENT.font, components, x, y, DefaultTooltipPositioner.INSTANCE);
    }
}
