package com.carlschierig.immersivecrafting.api.context;

import com.carlschierig.immersivecrafting.ImmersiveCrafting;
import com.carlschierig.immersivecrafting.api.predicate.condition.ICCondition;
import com.google.common.collect.ImmutableSet;

import java.util.Set;
import java.util.stream.Collectors;

public final class ValidationContext {
    public static final ValidationContext EMPTY = new ValidationContext(ImmutableSet.of());
    private final ImmutableSet<ContextType<?>> validTypes;

    private ValidationContext(ImmutableSet<ContextType<?>> validTypes) {
        this.validTypes = validTypes;
    }

    public boolean contains(ContextType<?> type) {
        return validTypes.contains(type);
    }

    public boolean contains(ValidationContext context) {
        return validTypes.containsAll(context.validTypes);
    }

    /**
     * Returns a {@link Set} containing all the {@link ContextType}s which are present in the given context,
     * but are missing in this context.
     *
     * @param context The context whose mismatches should be found.
     * @return a {@link Set} containing all the missing context types.
     */
    public Set<ContextType<?>> getMismatch(ValidationContext context) {
        return context.validTypes.stream().filter(type -> !validTypes.contains(type)).collect(Collectors.toSet());
    }

    public void validate(ICCondition condition) {
        var requirements = condition.getRequirements();
        if (!contains(requirements)) {
            var mismatchs = getMismatch(requirements);
            for (var missing : mismatchs) {
                ImmersiveCrafting.LOG.error(missing.toString() + " is required by the condition, but was not provided.");
            }
            throw new IllegalStateException("Condition has illegal requirements, see the log for details.");
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
