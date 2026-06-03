package com.carlschierig.immersivecrafting.api.client.render;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import org.jetbrains.annotations.NotNull;

/**
 * Renders an element on a GUI.
 */
@FunctionalInterface
public interface ICRenderer<T> {
    /**
     * Render this instance using the given {@link GuiGraphicsExtractor}.
     *
     * @param element The which should be rendered.
     * @param draw    The {@link GuiGraphicsExtractor} used for rendering.
     * @param x       the x coordinate of the mouse.
     * @param y       the y coordinate of the mouse.
     * @param delta   The time delta used for animation.
     */
    void render(T element, @NotNull GuiGraphicsExtractor draw, int x, int y, float delta);

    default void renderUnknown(Object element, @NotNull GuiGraphicsExtractor draw, int x, int y, float delta) {
        render((T) element, draw, x, y, delta);
    }
}
