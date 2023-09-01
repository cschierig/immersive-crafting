package com.carlschierig.immersivecrafting.api.recipe;

import com.carlschierig.immersivecrafting.api.context.RecipeContext;

import java.util.Optional;

public interface ICRecipeManager {

    <T extends ICRecipe> Optional<T> getRecipe(ICRecipeType<T> type, RecipeContext context);
}
