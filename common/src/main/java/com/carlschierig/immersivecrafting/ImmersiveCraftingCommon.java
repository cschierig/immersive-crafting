package com.carlschierig.immersivecrafting;

import com.carlschierig.immersivecrafting.api.predicate.condition.ICConditionSerializers;
import com.carlschierig.immersivecrafting.api.recipe.ICRecipeTypes;
import com.carlschierig.immersivecrafting.impl.recipe.ICRecipeSerializers;

public class ImmersiveCraftingCommon {

    public static void init() {
        ICRecipeTypes.init();
        ICRecipeSerializers.init();
        ICConditionSerializers.init();
    }
}
