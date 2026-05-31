package com.carlschierig.immersivecrafting.api.render;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import org.jetbrains.annotations.NotNull;

/**
 * Represents an element which can be rendered on a gui.
 */
@FunctionalInterface
public interface ICRenderable {
    /**
     * Render this instance using the given {@link GuiGraphicsExtractor}.
     *
     * @param draw  The {@link GuiGraphicsExtractor} used for rendering.
     * @param x     the x coordinate of the mouse.
     * @param y     the y coordinate of the mouse.
     * @param delta The time delta used for animation.
     */
    void render(@NotNull GuiGraphicsExtractor draw, int x, int y, float delta);
}
