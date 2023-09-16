package com.carlschierig.immersivecrafting.api.predicate;

import com.carlschierig.immersivecrafting.api.context.RecipeContext;
import com.carlschierig.immersivecrafting.api.predicate.condition.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a predicate which takes a {@link RecipeContext} to test if a recipe is valid.
 */
public class ICPredicate extends AndCondition {
    public ICPredicate(ICCondition[] conditions) {
        super(conditions);
    }

    @Override
    public ICConditionSerializer<?> getSerializer() {
        return ICConditionSerializers.PREDICATE;
    }

    public static class Serializer extends CompoundICCondition.Serializer<ICPredicate> {

        @Override
        protected ICPredicate create(ICCondition[] conditions) {
            return new ICPredicate(conditions);
        }
    }

    public static class Builder {
        private final List<ICCondition> builderConditions = new ArrayList<>();

        public Builder with(ICCondition condition) {
            builderConditions.add(condition);
            return this;
        }

        public Builder and(ICCondition... conditions) {
            builderConditions.add(new AndCondition(conditions));
            return this;
        }

        public Builder or(ICCondition... conditions) {
            builderConditions.add(new OrCondition(conditions));
            return this;
        }

        public ICPredicate build() {
            return new ICPredicate(builderConditions.toArray(ICCondition[]::new));
        }
    }
}
