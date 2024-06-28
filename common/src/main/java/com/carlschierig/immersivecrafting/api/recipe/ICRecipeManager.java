package com.carlschierig.immersivecrafting.api.recipe;

import com.carlschierig.immersivecrafting.api.context.RecipeContext;
import com.carlschierig.immersivecrafting.api.context.SimpleRecipeContext;
import com.carlschierig.immersivecrafting.impl.recipe.ICRecipeManagerImpl;

import java.util.Collection;
import java.util.Optional;

public final class ICRecipeManager {
    /**
     * Returns the first recipe which matches the given {@link SimpleRecipeContext}
     *
     * @param type    The Recipe Type whose recipes should be searched.
     * @param context The context against which recipes should be tested.
     * @param <T>     The type of the recipe which is returned.
     * @return The first recipe which matches the predicate, if any.
     */
    public static <T extends ICRecipe> Optional<ICRecipeHolder<T>> getRecipe(ICRecipeType<T> type, RecipeContext context) {
        return ICRecipeManagerImpl.INSTANCE.getRecipe(type, context);
    }

    /**
     * Returns all recipes of the given {@link SimpleRecipeContext}.
     *
     * @param type The Recipe Type whose recipes should be returned.
     * @return All recipes of the given {@link SimpleRecipeContext}.
     */
    public static <T extends ICRecipe> Collection<ICRecipeHolder<T>> getRecipes(ICRecipeType<T> type) {
        return ICRecipeManagerImpl.INSTANCE.getRecipes(type);
    }

    /**
     * Returns a list containing all registered recipes.
     *
     * @return a list containing all registered recipes.
     */
    public static Collection<ICRecipeHolder<?>> getRecipes() {
        return ICRecipeManagerImpl.INSTANCE.getRecipes();
    }
}
