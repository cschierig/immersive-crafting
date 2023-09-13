package com.carlschierig.immersivecrafting.api.data;

import com.carlschierig.immersivecrafting.api.recipe.ICRecipeType;
import com.carlschierig.immersivecrafting.api.registry.ICRegistries;

public final class ICTranslationHelper {
    private ICTranslationHelper() {
    }

    public static String translate(ICRecipeType<?> type) {
        return ICRegistries.RECIPE_TYPE.getKey(type).toLanguageKey("immersive_crafting.recipeType");
    }
}
