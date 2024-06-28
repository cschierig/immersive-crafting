package com.carlschierig.immersivecrafting.api.context;

import com.google.common.collect.ImmutableMap;
import org.jetbrains.annotations.NotNull;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.StringJoiner;

/**
 * A simple {@link RecipeContext} which bundles multiple {@link ContextTypes} into an instance.
 * <p>
 * Use {@link SimpleRecipeContext.Builder} to create new contexts, or {@link}
 */
public final class SimpleRecipeContext implements RecipeContext {
    /**
     * An empty Recipe context without containing no context types.
     */
    public static final RecipeContext EMPTY = new SimpleRecipeContext(ImmutableMap.of());
    private final ImmutableMap<ContextType<?>, Object> holders;

    private SimpleRecipeContext(ImmutableMap<ContextType<?>, Object> holders) {
        this.holders = holders;
    }

    @Override
    @NotNull
    public <T> T get(@NotNull ContextType<T> type) {
        return tryGet(type).orElseThrow(() -> new NoSuchElementException("No '" + type.id() + "' context in this recipe context."));
    }

    @Override
    @SuppressWarnings("unchecked")
    public @NotNull <T> Optional<T> tryGet(@NotNull ContextType<T> type) {
        return Optional.ofNullable((T) holders.get(type));
    }

    public static <T> SimpleRecipeContext of(@NotNull ContextType<T> type, T object) {
        return new SimpleRecipeContext(ImmutableMap.of(type, object));
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("SimpleRecipeContext[");
        var joiner = new StringJoiner(",");
        for (var entry : holders.entrySet()) {
            joiner.add(entry.toString());
        }
        builder.append(joiner).append("]");
        return builder.toString();
    }

    /**
     * A builder for creating {@link SimpleRecipeContext}s.
     */
    public static final class Builder {
        private final ImmutableMap.Builder<ContextType<?>, Object> holders = new ImmutableMap.Builder<>();

        /**
         * Adds the object to the builder. If an object of that type is already present, it will be replaced.
         *
         * @param type   The type of the object which should be added.
         * @param object The object which should be added.
         * @param <T>    The type of the object which is added.
         * @return the builder.
         */
        public <T> Builder putHolder(@NotNull ContextType<T> type, @NotNull T object) {
            holders.put(type, object);
            return this;
        }

        /**
         * Create a {@link SimpleRecipeContext} based on the builder.
         *
         * @return a {@link SimpleRecipeContext} based on the builder.
         */
        public RecipeContext build() {
            return new SimpleRecipeContext(holders.build());
        }
    }
}
