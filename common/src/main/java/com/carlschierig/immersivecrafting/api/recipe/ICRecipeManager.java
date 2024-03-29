package com.carlschierig.immersivecrafting.api.recipe;

import com.carlschierig.immersivecrafting.api.context.RecipeContext;
import com.carlschierig.immersivecrafting.impl.recipe.ICRecipeManagerImpl;
import com.google.common.collect.ImmutableCollection;

import java.util.List;
import java.util.Optional;

public final class ICRecipeManager {
    /**
     * Returns the first recipe which matches the given {@link RecipeContext}
     *
     * @param type    The Recipe Type whose recipes should be searched.
     * @param context The context against which recipes should be tested.
     * @param <T>     The type of the recipe which is returned.
     * @return The first recipe which matches the predicate, if any.
     */
    public static <T extends ICRecipe> Optional<T> getRecipe(ICRecipeType<T> type, RecipeContext context) {
        return ICRecipeManagerImpl.INSTANCE.getRecipe(type, context);
    }

    /**
     * Returns all recipes of the given {@link RecipeContext}.
     *
     * @param type The Recipe Type whose recipes should be returned.
     * @return All recipes of the given {@link RecipeContext}.
     */
    public static ImmutableCollection<ICRecipe> getRecipes(ICRecipeType<?> type) {
        return ICRecipeManagerImpl.INSTANCE.getRecipes(type);
    }

    /**
     * Returns a list containing all registered recipes.
     *
     * @return a list containing all registered recipes.
     */
    public static List<ICRecipe> getRecipes() {
        return ICRecipeManagerImpl.INSTANCE.getRecipes();
    }
}
