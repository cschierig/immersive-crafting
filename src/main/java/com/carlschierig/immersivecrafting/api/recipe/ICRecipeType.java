package com.carlschierig.immersivecrafting.api.recipe;

import com.carlschierig.immersivecrafting.api.render.ICRenderable;
import com.carlschierig.immersivecrafting.impl.render.DefaultCategoryRenderer;

public interface ICRecipeType<T extends ICRecipe> {
    default ICRenderable getRenderer() {
        return DefaultCategoryRenderer.INSTANCE;
    }

    default ICRenderable getSimplifiedRenderer() {
        return getRenderer();
    }
}
