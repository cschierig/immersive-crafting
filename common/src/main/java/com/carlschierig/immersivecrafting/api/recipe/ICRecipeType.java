package com.carlschierig.immersivecrafting.api.recipe;

import com.carlschierig.immersivecrafting.api.render.ICRenderable;
import com.carlschierig.immersivecrafting.impl.render.DefaultCategoryRenderer;

/**
 * Groups multiple recipes into categories.
 *
 * @param <T> The type of recipe this type represents.
 */
public interface ICRecipeType<T extends ICRecipe> {
    /**
     * Returns the renderer used to render the icon of the recipe type in recipe viewers.
     *
     * @return the renderer used to render the icon of the recipe type in recipe viewers.
     */
    default ICRenderable getRenderer() {
        return DefaultCategoryRenderer.INSTANCE;
    }

    /**
     * Returns a simplified version of {@link #getRenderer()}.
     * Used when less detail is desired.
     *
     * @return a simplified icon renderer.
     */
    default ICRenderable getSimplifiedRenderer() {
        return getRenderer();
    }
}
