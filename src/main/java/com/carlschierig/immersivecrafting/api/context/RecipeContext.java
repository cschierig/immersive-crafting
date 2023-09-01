package com.carlschierig.immersivecrafting.api.context;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Provides a context for matching and assembling recipes.
 * The context can be viewed as a snapshot of the level surrounding a player when a recipe is triggered.
 * <p>
 * Use {@link RecipeContext.Builder} to create new contexts.
 */
public final class RecipeContext {
    private final Map<ContextType<?>, Object> holders;

    private RecipeContext(Map<ContextType<?>, Object> holders) {
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

    public static final class Builder {
        private final Map<ContextType<?>, Object> holders = new HashMap<>();

        public <T> Builder putHolder(ContextType<T> type, T object) {
            holders.put(type, object);
            return this;
        }

        public RecipeContext build() {
            return new RecipeContext(holders);
        }
    }
}
