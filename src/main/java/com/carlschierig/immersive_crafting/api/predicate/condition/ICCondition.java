package com.carlschierig.immersive_crafting.api.predicate.condition;

import com.carlschierig.immersive_crafting.api.context.RecipeContext;
import com.carlschierig.immersive_crafting.api.context.ValidationContext;

import java.util.function.Predicate;

public interface ICCondition extends Predicate<RecipeContext> {
    ICConditionSerializer<?> getSerializer();

    ValidationContext getRequirements();
}
