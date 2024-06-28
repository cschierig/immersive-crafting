package com.carlschierig.immersivecrafting.impl.render;

import com.carlschierig.immersivecrafting.api.render.ICRenderFlags;
import com.carlschierig.immersivecrafting.mixin.GuiGraphicsAccessor;
import com.mojang.blaze3d.platform.Lighting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class ICRenderHelper {
    private static final Minecraft CLIENT = Minecraft.getInstance();


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

    public static void renderItem(ItemStack stack, GuiGraphics draw, int x, int y, float delta, int flags) {
        if (ICRenderFlags.RENDER_ICON.test(flags)) {
            Lighting.setupFor3DItems();
            draw.renderItem(stack, x, y);
            draw.renderItemDecorations(Minecraft.getInstance().font, stack, x, y, "");
        }
        if (ICRenderFlags.RENDER_AMOUNT.test(flags)) {
            String count = "";
            var amount = stack.getCount();
            if (amount != 1) {
                count += amount;
            }
            ICRenderHelper.renderItemAnnotation(draw, x, y, Component.literal(count));
        }
    }
}
