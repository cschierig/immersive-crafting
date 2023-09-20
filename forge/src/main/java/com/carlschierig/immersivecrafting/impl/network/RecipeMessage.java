package com.carlschierig.immersivecrafting.impl.network;

import com.carlschierig.immersivecrafting.api.recipe.ICRecipe;

import java.util.List;

public record RecipeMessage(List<ICRecipe> recipes) {
}
