package com.carlschierig.immersivecrafting.compat.emi.category;

import dev.emi.emi.api.render.EmiRenderable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

public enum DefaultCategoryRenderer implements EmiRenderable {
    INSTANCE;

    @Override
    public void render(GuiGraphics draw, int x, int y, float delta) {
        draw.drawString(Minecraft.getInstance().font, "IC", x, y, 0);
    }
}
