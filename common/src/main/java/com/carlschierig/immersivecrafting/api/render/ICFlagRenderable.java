package com.carlschierig.immersivecrafting.api.render;

import net.minecraft.client.gui.GuiGraphics;
import org.jetbrains.annotations.NotNull;

public interface ICFlagRenderable extends ICRenderable {
    @Override
    default void render(@NotNull GuiGraphics draw, int x, int y, float delta) {
        render(draw, x, y, delta, -1);
    }

    /**
     * Render this instance using the given {@link GuiGraphics}.
     * Ingredient icons should not be larger than a typical minecraft texture (16x16).
     *
     * @param draw  The {@link GuiGraphics} used for rendering.
     * @param x     the x coordinate of the mouse.
     * @param y     the y coordinate of the mouse.
     * @param delta The time delta used for animation.
     * @param flags The bits of this integer indicate what parts should be rendered.
     *              Use {@link ICRenderFlags#test(int)} to compare the given value against flags.
     */
    void render(GuiGraphics draw, int x, int y, float delta, int flags);
}
