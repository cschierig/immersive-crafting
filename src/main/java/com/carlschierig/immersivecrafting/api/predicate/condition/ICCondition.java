package com.carlschierig.immersivecrafting.api.predicate.condition;

import com.carlschierig.immersivecrafting.api.context.RecipeContext;
import com.carlschierig.immersivecrafting.api.context.ValidationContext;

import java.util.function.Predicate;

public interface ICCondition extends Predicate<RecipeContext> {
    ICConditionSerializer<?> getSerializer();

    ValidationContext getRequirements();
}
