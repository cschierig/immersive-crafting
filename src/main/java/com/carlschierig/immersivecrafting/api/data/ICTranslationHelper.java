package com.carlschierig.immersivecrafting.api.data;

import com.carlschierig.immersivecrafting.api.recipe.ICRecipeType;
import com.carlschierig.immersivecrafting.api.registry.ICRegistries;

/**
 * Provides helper methods to standardize translation keys.
 */
public final class ICTranslationHelper {
    private ICTranslationHelper() {
    }

    public static String translateRecipeType(ICRecipeType<?> type) {
        return ICRegistries.RECIPE_TYPE.getKey(type).toLanguageKey("immersive_crafting.recipeType");
    }

    public static String translateCondition(String conditionName) {
        return "immersive_crafting.condition." + conditionName;
    }

    public static String translateConditionDescription(String conditionName) {
        return "immersive_crafting.conditionDescription." + conditionName;
    }

    public static String translateConditionDescription(String conditionName, String postfix) {
        return "immersive_crafting.conditionDescription." + conditionName + "." + postfix;
    }
}
