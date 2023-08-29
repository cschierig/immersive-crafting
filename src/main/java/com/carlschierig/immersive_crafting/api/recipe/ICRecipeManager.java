package com.carlschierig.immersive_crafting.api.recipe;

import com.carlschierig.immersive_crafting.api.context.RecipeContext;

import java.util.Optional;

public interface ICRecipeManager {

    <T extends ICRecipe> Optional<T> getRecipe(ICRecipeType<T> type, RecipeContext context);
}
