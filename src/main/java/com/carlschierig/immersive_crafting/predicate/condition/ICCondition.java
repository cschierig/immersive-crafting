package com.carlschierig.immersive_crafting.predicate.condition;

import com.carlschierig.immersive_crafting.context.RecipeContext;

import java.util.function.Predicate;

public interface ICCondition extends Predicate<RecipeContext> {

	ICConditionSerializer<?> getSerializer();
}
