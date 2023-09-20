package com.carlschierig.immersivecrafting.api.predicate.condition.ingredient;

import com.carlschierig.immersivecrafting.api.predicate.condition.ICCondition;
import com.carlschierig.immersivecrafting.api.render.ICRenderFlags;
import com.carlschierig.immersivecrafting.api.render.ICRenderable;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * An ingredient is something that can be consumed or produced by a recipe.
 * Recipes require {@link ICStack}s to be the output as they contain a craft method.
 *
 * @see ICStack
 */
public interface ICIngredient extends ICRenderable, ICCondition {

    /**
     * Returns the amount which is needed by the ingredient.
     * <p>
     * For example, if a recipe needs five diamonds to be present, the amount should return 5.
     *
     * @return the amount which is needed by the ingredient.
     */
    int getAmount();

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
    default void render(GuiGraphics draw, int x, int y, float delta, int flags) {

    }

    /**
     * Returns the chance that the ingredient is consumed/produced.
     * The value must be in the range [0, 1].
     *
     * @return the chance that the ingredient is consumed/produced.
     */
    float getChance();

    /**
     * Copies this ingredient. Must return a new instance on each call.
     *
     * @return A copy of this ingredient.
     */
    ICIngredient copy();

    /**
     * Returns the individual parts of this ingredient.
     * <p>
     * If this ingredient is a consumer,
     * the returned stacks have an <b>or relation</b>,
     * meaning that any of the returned stacks is valid for consumption to fulfill this ingredient.
     * <p>
     * If this ingredient is a producer, the returned stack have an <b>and relation</b>,
     * meaning that this ingredient will produce all the returned stacks.
     *
     * @return a list of stacks which are used by the ingredient.
     */
    List<ICStack> getParts();

    /**
     * Returns the name of this ingredient.
     *
     * @return the name of this ingredient.
     */
    @NotNull
    Component getName();

    /**
     * Returns whether this ingredient is empty.
     *
     * @return {@code true} if this ingredient is empty, {@code false} otherwise.
     */
    boolean isEmpty();
}
