package com.carlschierig.immersivecrafting.api.recipe;

import com.carlschierig.immersivecrafting.api.context.RecipeContext;
import com.carlschierig.immersivecrafting.impl.recipe.RecipeReloader;

import java.util.Optional;

public final class ICRecipeManager {
    /**
     * Returns the first recipe which matches the given {@link RecipeContext}
     *
     * @param context the context against which recipes should be tested.
     * @return The first recipe which matches the predicate, if any.
     */
    public static <T extends ICRecipe> Optional<T> getRecipe(ICRecipeType<T> type, RecipeContext context) {
        return RecipeReloader.getRecipe(type, context);
    }
}
