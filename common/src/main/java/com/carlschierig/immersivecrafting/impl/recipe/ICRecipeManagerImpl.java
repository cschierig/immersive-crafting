package com.carlschierig.immersivecrafting.impl.recipe;

import com.carlschierig.immersivecrafting.api.context.RecipeContext;
import com.carlschierig.immersivecrafting.api.recipe.ICRecipe;
import com.carlschierig.immersivecrafting.api.recipe.ICRecipeType;
import com.google.common.collect.ImmutableCollection;

import java.util.List;
import java.util.Optional;

public abstract class ICRecipeManagerImpl {
    public static ICRecipeManagerImpl INSTANCE;

    public abstract void setRecipes(Iterable<ICRecipe> recipes);

    public abstract List<ICRecipe> getRecipes();

    public abstract ImmutableCollection<ICRecipe> getRecipes(ICRecipeType<?> type);

    public abstract <T extends ICRecipe> Optional<T> getRecipe(ICRecipeType<T> type, RecipeContext context);
}
