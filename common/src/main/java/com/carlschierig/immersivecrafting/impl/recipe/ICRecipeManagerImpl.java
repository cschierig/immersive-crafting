package com.carlschierig.immersivecrafting.impl.recipe;

import com.carlschierig.immersivecrafting.api.context.RecipeContext;
import com.carlschierig.immersivecrafting.api.recipe.ICRecipe;
import com.carlschierig.immersivecrafting.api.recipe.ICRecipeHolder;
import com.carlschierig.immersivecrafting.api.recipe.ICRecipeType;

import java.util.Collection;
import java.util.Optional;

public abstract class ICRecipeManagerImpl {
    public static ICRecipeManagerImpl INSTANCE;

    public abstract void setRecipes(Iterable<ICRecipeHolder<?>> recipes);

    public abstract Collection<ICRecipeHolder<?>> getRecipes();

    public abstract <T extends ICRecipe> Collection<ICRecipeHolder<T>> getRecipes(ICRecipeType<T> type);

    public abstract <T extends ICRecipe> Optional<ICRecipeHolder<T>> getRecipe(ICRecipeType<T> type, RecipeContext context);
}
