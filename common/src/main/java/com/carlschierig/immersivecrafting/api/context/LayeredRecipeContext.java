package com.carlschierig.immersivecrafting.api.context;

import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class LayeredRecipeContext implements RecipeContext {
    private final RecipeContext overlay;
    private final RecipeContext fallback;

    protected LayeredRecipeContext(RecipeContext overlay, RecipeContext fallback) {
        this.overlay = overlay;
        this.fallback = fallback;
    }

    @Override
    public <T> @NotNull T get(@NotNull ContextType<T> type) {
        return overlay.tryGet(type).orElseGet(() -> fallback.get(type));
    }

    @Override
    public @NotNull <T> Optional<T> tryGet(@NotNull ContextType<T> type) {
        return overlay.tryGet(type).or(() -> fallback.tryGet(type));
    }

    @Override
    public String toString() {
        return "LayeredRecipeContext{" +
                "overlay=" + overlay +
                ", fallback=" + fallback +
                '}';
    }
}
