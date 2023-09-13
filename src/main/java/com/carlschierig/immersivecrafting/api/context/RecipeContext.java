package com.carlschierig.immersivecrafting.api.context;

import com.google.common.collect.ImmutableMap;

import java.util.NoSuchElementException;

/**
 * Provides a context for matching and assembling recipes.
 * The context can be viewed as a snapshot of the level surrounding a player when a recipe is triggered.
 * <p>
 * Use {@link RecipeContext.Builder} to create new contexts.
 */
public final class RecipeContext {
    public static final RecipeContext EMPTY = new RecipeContext(ImmutableMap.of());
    private final ImmutableMap<ContextType<?>, Object> holders;

    private RecipeContext(ImmutableMap<ContextType<?>, Object> holders) {
        this.holders = holders;
    }

    /**
     * Returns the object associated with the given type or throws an exception if it isn't present.
     *
     * @param type The type for which the object should be returned.
     * @param <T>  The type of object which is returned.
     * @return the object associated with the given type.
     * @throws NoSuchElementException if no object of that type is present.
     */
    @SuppressWarnings("unchecked")
    public <T> T get(ContextType<T> type) {
        var holder = holders.get(type);
        if (holder != null) {
            // can only break if the insertion logic is messed with
            return (T) holder;
        }
        throw new NoSuchElementException("No '" + type.id() + "' context in this recipe context.");
    }

    /**
     * A builder for creating {@link RecipeContext}s.
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
        public <T> Builder putHolder(ContextType<T> type, T object) {
            holders.put(type, object);
            return this;
        }

        /**
         * Create a {@link RecipeContext} based on the builder.
         *
         * @return a {@link RecipeContext} based on the builder.
         */
        public RecipeContext build() {
            return new RecipeContext(holders.build());
        }
    }
}
