package com.carlschierig.immersivecrafting.api.context;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

public final class RecipeContext {
    private final Map<ContextType<?>, Object> holders;

    private RecipeContext(Map<ContextType<?>, Object> holders) {
        this.holders = holders;
    }

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
