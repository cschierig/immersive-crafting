package com.carlschierig.immersivecrafting.api.context;

import com.carlschierig.immersivecrafting.api.predicate.ICPredicate;
import com.carlschierig.immersivecrafting.api.predicate.condition.ICCondition;
import com.carlschierig.immersivecrafting.impl.util.ICUtil;
import com.google.common.collect.ImmutableSet;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * A class whose instances can be used to validate that a {@link ICPredicate predicate} and its conditions only
 * use the {@link ContextType context types} which are provided.
 * <p>
 * To validate a condition, use {@link ValidationContext#validate(ICCondition)}.
 */
public final class ValidationContext {
    /**
     * A validation context containing no context types.
     */
    public static final ValidationContext EMPTY = new ValidationContext(ImmutableSet.of());
    private final ImmutableSet<ContextType<?>> validTypes;

    private ValidationContext(ImmutableSet<ContextType<?>> validTypes) {
        this.validTypes = validTypes;
    }

    /**
     * Checks whether the context contains the given type.
     *
     * @param type The type whose existence should be checked.
     * @return {@code true} if the type is contained in the context, {@code false} otherwise.
     */
    public boolean contains(@Nullable ContextType<?> type) {
        return validTypes.contains(type);
    }

    /**
     * Checks whether this context is a superset of the given context.
     *
     * @param context The context which should be tested.
     * @return {@code true} if instance the method is called on is a superset of the given context, {@code false}
     * otherwise.
     */
    public boolean supersetOf(@NotNull ValidationContext context) {
        return validTypes.containsAll(context.validTypes);
    }

    /**
     * Returns a {@link Set} containing all the {@link ContextType}s which are present in the given context,
     * but are missing in this context.
     *
     * @param context The context whose mismatches should be found.
     * @return a {@link Set} containing all the missing context types.
     */
    @NotNull
    @Contract(pure = true)
    public Set<ContextType<?>> getMismatch(@NotNull ValidationContext context) {
        return context.validTypes.stream().filter(type -> !validTypes.contains(type)).collect(Collectors.toSet());
    }

    /**
     * Validate an {@link ICCondition}. This method checks if all the requirements of the condition are present in
     * the context. If any context types are missing, they are logged and an exception is thrown.
     *
     * @param condition The condition which should be validated against the context.
     * @throws IllegalStateException if the condition wasn't successfully validated.
     */
    public void validate(@NotNull ICCondition condition) {
        var requirements = condition.getRequirements();
        if (!supersetOf(requirements)) {
            var mismatchs = getMismatch(requirements);
            for (var missing : mismatchs) {
                ICUtil.LOG.error(missing.toString() + " is required by the condition, but was not provided.");
            }
            throw new IllegalStateException("Condition has illegal requirements, see the log for details.");
        }
    }

    /**
     * Creates a new {@link ValidationContext} containing a single context type.
     *
     * @param type The context type the condition will contain.
     * @return a new {@link ValidationContext} containing the given context type.
     */
    public static ValidationContext of(@NotNull ContextType<?> type) {
        return new ValidationContext(ImmutableSet.of(type));
    }

    /**
     * Merges multiple Validation contexts and returns a new one which contains all the context types present
     * in at least one of them. This is equivalent to a union operation on sets.
     *
     * @param contexts The contexts which should be merged.
     * @return A new {@link ValidationContext} which merges all the given contexts.
     */
    public static ValidationContext merge(@NotNull Iterable<ValidationContext> contexts) {
        ImmutableSet.Builder<ContextType<?>> builder = new ImmutableSet.Builder<>();

        for (var context : contexts) {
            builder.addAll(context.validTypes);
        }

        return new ValidationContext(builder.build());
    }

    /**
     * A builder for creating {@link ValidationContext}s.
     */
    public static final class Builder {
        private final ImmutableSet.Builder<ContextType<?>> validTypes = new ImmutableSet.Builder<>();

        /**
         * Adds the context type to the builder.
         *
         * @param type The type which should be added.
         * @return the builder.
         */
        public Builder put(@NotNull ContextType<?> type) {
            validTypes.add(type);
            return this;
        }

        /**
         * Create a {@link ValidationContext} based on the builder.
         *
         * @return a {@link ValidationContext} based on the builder.
         */
        public ValidationContext build() {
            return new ValidationContext(validTypes.build());
        }
    }
}
