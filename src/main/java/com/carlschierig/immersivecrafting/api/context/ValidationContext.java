package com.carlschierig.immersivecrafting.api.context;

import com.carlschierig.immersivecrafting.api.predicate.condition.ICCondition;
import com.google.common.collect.ImmutableSet;

public final class ValidationContext {
    public static final ValidationContext EMPTY = new ValidationContext(ImmutableSet.of());
    private final ImmutableSet<ContextType<?>> validTypes;

    private ValidationContext(ImmutableSet<ContextType<?>> validTypes) {
        this.validTypes = validTypes;
    }

    public boolean has(ContextType<?> type) {
        return validTypes.contains(type);
    }

    public boolean contains(ValidationContext context) {
        return validTypes.containsAll(context.validTypes);
    }

    public void validate(ICCondition condition) {
        if (!contains(condition.getRequirements())) {
            // TODO: better error
            throw new IllegalStateException("Condition has illegal requirements");
        }
    }

    public static ValidationContext of(ContextType<?> type) {
        return new ValidationContext(ImmutableSet.of(type));
    }

    public static ValidationContext merge(Iterable<ValidationContext> contexts) {
        ImmutableSet.Builder<ContextType<?>> builder = new ImmutableSet.Builder<>();

        for (var context : contexts) {
            builder.addAll(context.validTypes);
        }

        return new ValidationContext(builder.build());
    }

    public static final class Builder {
        private final ImmutableSet.Builder<ContextType<?>> validTypes = new ImmutableSet.Builder<>();

        public Builder put(ContextType<?> type) {
            validTypes.add(type);
            return this;
        }

        public ValidationContext build() {
            return new ValidationContext(validTypes.build());
        }
    }
}
