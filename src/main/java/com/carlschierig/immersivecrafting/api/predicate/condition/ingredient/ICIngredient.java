package com.carlschierig.immersivecrafting.api.predicate.condition.ingredient;

import com.carlschierig.immersivecrafting.api.predicate.condition.ICCondition;
import com.carlschierig.immersivecrafting.api.render.ICRenderable;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

import java.util.List;

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
    default void render(GuiGraphics draw, int x, int y, float delta) {
        render(draw, x, y, delta, -1);
    }

    void render(GuiGraphics draw, int x, int y, float delta, int flags);

    /**
     * Returns the chance that the ingredient is consumed/produced.
     * The value must be in the range [0, 1].
     *
     * @return the chance that the ingredient is consumed/produced.
     */
    float getChance();

    ICIngredient copy();

    List<ICStack> getParts();

    Component getName();

    boolean isEmpty();
}
