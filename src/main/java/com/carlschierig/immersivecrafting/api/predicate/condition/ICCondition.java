package com.carlschierig.immersivecrafting.api.predicate.condition;

import com.carlschierig.immersivecrafting.api.context.RecipeContext;
import com.carlschierig.immersivecrafting.api.context.ValidationContext;
import com.carlschierig.immersivecrafting.api.predicate.PredicateVisitor;
import com.carlschierig.immersivecrafting.api.predicate.Visitable;
import com.carlschierig.immersivecrafting.api.render.ICRenderable;

import java.util.function.Predicate;

public interface ICCondition extends Predicate<RecipeContext>, Visitable, ICRenderable {
    ICConditionSerializer<?> getSerializer();

    ValidationContext getRequirements();

    @Override
    default void accept(PredicateVisitor visitor) {
        visitor.visitCondition(this);
    }
}
