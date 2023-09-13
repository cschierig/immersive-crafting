package com.carlschierig.immersivecrafting.api.render;

import net.minecraft.client.gui.GuiGraphics;

@FunctionalInterface
public interface ICRenderable {
    void render(GuiGraphics draw, int x, int y, float delta);
}
