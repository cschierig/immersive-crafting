package com.carlschierig.immersivecrafting.api.recipe;

/**
 * Groups multiple recipes into categories.
 *
 * @param <T> The type of recipe this type represents.
 */
public interface ICRecipeType<T extends ICRecipe> {

    Class<T> getRecipeClass();
}
