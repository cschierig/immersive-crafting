package com.carlschierig.immersivecrafting.api.predicate;

import com.carlschierig.immersivecrafting.api.context.RecipeContext;
import com.carlschierig.immersivecrafting.api.predicate.condition.*;
import org.jetbrains.annotations.Contract;

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
        @Contract("_->new")
        protected ICPredicate create(ICCondition[] conditions) {
            return new ICPredicate(conditions);
        }
    }

    /**
     * Builder used to create predicates.
     */
    public static class Builder {
        private final List<ICCondition> builderConditions = new ArrayList<>();

        /**
         * Adds a new condition to the predicate.
         * It will have to be true for the predicate to be true.
         *
         * @param condition The condition to add.
         * @return this Builder.
         */
        @Contract("_->this")
        public Builder with(ICCondition condition) {
            builderConditions.add(condition);
            return this;
        }

        /**
         * Creates a new predicate from the builder.
         *
         * @return a new predicate.
         */
        @Contract("->new")
        public ICPredicate build() {
            return new ICPredicate(builderConditions.toArray(ICCondition[]::new));
        }
    }
}
