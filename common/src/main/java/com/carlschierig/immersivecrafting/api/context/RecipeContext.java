package com.carlschierig.immersivecrafting.api.context;

import com.carlschierig.immersivecrafting.api.predicate.condition.ingredient.ICIngredient;
import org.jetbrains.annotations.NotNull;

import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Provides a context for matching and assembling recipes.
 * The context can be viewed as a snapshot of the level surrounding a player when a recipe is triggered.
 */
public interface RecipeContext {
    /**
     * Returns the object associated with the given type or throws an exception if it isn't present.
     *
     * @param type The type for which the object should be returned.
     * @param <T>  The type of object which is returned.
     * @return the object associated with the given type.
     * @throws NoSuchElementException if no object of that type is present.
     */
    @NotNull
    <T> T get(@NotNull ContextType<T> type);

    /**
     * Returns the object associated with the given type or empty if there isn't any.
     *
     * @param type The type for which the object should be returned.
     * @param <T>  The type of object which is returned.
     * @return the object associated with the given type.
     */
    @NotNull
    <T> Optional<T> tryGet(@NotNull ContextType<T> type);

    /**
     * Returns a recipe context which can be used to test the {@link ICIngredient} at the given index by
     * extracting and overlaying the ingredient's context found in the ingredients context list.
     *
     * @param index The index of the ingredient
     * @return this recipe context which the recipe context of the ingredient overlayed on top of it.
     * @throws NoSuchElementException    if no {@link ContextTypes#INGREDIENTS} is present.
     * @throws IndexOutOfBoundsException if the ingredients context doesn't contain an element with the given index.
     * @see ContextTypes#INGREDIENTS
     */
    default RecipeContext forIngredient(int index) {
        var contexts = get(ContextTypes.INGREDIENTS);
        return new LayeredRecipeContext(contexts.get(index), this);
    }
}
