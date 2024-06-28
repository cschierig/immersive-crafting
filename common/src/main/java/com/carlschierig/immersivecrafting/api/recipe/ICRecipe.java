package com.carlschierig.immersivecrafting.api.recipe;

import com.carlschierig.immersivecrafting.api.context.ContextType;
import com.carlschierig.immersivecrafting.api.context.RecipeContext;
import com.carlschierig.immersivecrafting.api.context.ValidationContext;
import com.carlschierig.immersivecrafting.api.predicate.ICPredicate;
import com.carlschierig.immersivecrafting.api.predicate.condition.ingredient.ICIngredient;
import com.carlschierig.immersivecrafting.api.predicate.condition.stack.ICStack;
import com.carlschierig.immersivecrafting.api.registry.ICRegistries;
import com.carlschierig.immersivecrafting.impl.registry.ICRegistryKeys;
import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.Recipe;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * A context-driven recipe which provides more freedom compared
 * to vanilla {@link Recipe}s.
 * <p>
 * Instead of matching against an inventory, the recipes are matched against a {@link RecipeContext} which contains
 * information about the surroundings of the player triggering a recipe. Other aspects of vanilla recipes remain largely
 * unchanged.
 */
public abstract class ICRecipe {
    public static final Codec<ICRecipe> CODEC = ICRegistries.RECIPE_SERIALIZER
            .byNameCodec()
            .dispatch(ICRecipe::getSerializer, ICRecipeSerializer::codec);
    public static final StreamCodec<RegistryFriendlyByteBuf, ICRecipe> STREAM_CODEC = ByteBufCodecs
            .registry(ICRegistryKeys.RECIPE_SERIALIZER)
            .dispatch(ICRecipe::getSerializer, ICRecipeSerializer::streamCodec);

    /**
     * Returns whether this recipe matches the given {@link RecipeContext}.
     * This is used by the {@link ICRecipeManager} to determine which recipe to use when crafting.
     * <p>
     *
     * @param context The context containing information on how the recipe was triggered.
     * @return whether this recipe matches the given {@link RecipeContext}.
     */
    public abstract boolean matches(RecipeContext context);

    /**
     * Return the stacks this recipe produces.
     *
     * @return a {@link List} containing the recipe's results.
     */
    public abstract List<ICStack> getResults();

    /**
     * Craft the recipe using the given context.
     *
     * @param recipeContext The Recipe Context used to find the recipe.
     */
    public abstract void craft(RecipeContext recipeContext);

    /**
     * Consumes the ingredients as if the recipe was crafted.
     *
     * @param recipeContext The Recipe Context used to find the recipe.
     */
    public abstract void consumeIngredients(RecipeContext recipeContext);

    /**
     * Returns a {@link List} containing the ingredients of the recipe.
     *
     * @return a {@link List} containing the ingredients of the recipe.
     */
    public abstract ImmutableList<ICIngredient> getIngredients();

    /**
     * Returns the predicate which is used to check if the recipe matches a {@link RecipeContext}.
     *
     * @return the predicate which is used to check if the recipe matches a {@link RecipeContext}.
     */
    public abstract ICPredicate getPredicate();

    /**
     * Returns the serializer used to (de)serialize recipes of this type.
     * The serializer must return instances of the class overriding this method.
     *
     * @return The serializer used to (de)serialize recipes of this type.
     */
    public abstract ICRecipeSerializer<?> getSerializer();

    /**
     * Returns the type of this recipe.
     *
     * @return the type of this recipe.
     */
    public abstract ICRecipeType<?> getType();

    /**
     * Returns a {@link ValidationContext} which contains all the {@link ContextType}s that must be provided to this recipe
     * when passing a {@link RecipeContext} as a parameter to any method.
     *
     * @return {@link ValidationContext} which contains the {@link ContextType}s this recipe needs.
     */
    public abstract ValidationContext getPredicateRequirements();

    /**
     * Returns a {@link ValidationContext} which contains the {@link ContextType}s that
     * ingredients of this recipe are allowed to use.
     *
     * @return {@link ValidationContext} which contains the {@link ContextType}s ingredients are allowed to use.
     */
    public abstract ValidationContext getIngredientRequirements();

    /**
     * Validate a list of ingredients against a {@link ValidationContext}.
     *
     * @param ingredients       The ingredients which need to be validated.
     * @param ingredientContext the context which all ingredients in the list need to match.
     * @return a {@link DataResult} indicating if all ingredients were validated successfully.
     */
    public static DataResult<List<ICIngredient>> validateIngredients(List<ICIngredient> ingredients, ValidationContext ingredientContext) {
        try {
            for (var ingredient : ingredients) {
                ingredientContext.validate(ingredient);
            }
        } catch (IllegalStateException e) {
            return DataResult.error(e::getMessage);
        }
        return DataResult.success(ingredients);
    }

    /**
     * Validate a list of ingredients against a {@link ValidationContext}.
     * Also validates the allowed size of the list.
     *
     * @param ingredients       The ingredients which need to be validated.
     * @param ingredientContext the context which all ingredients in the list need to match.
     * @return a {@link DataResult} indicating if all ingredients were validated successfully.
     * @throws IllegalArgumentException if one of the size arguments is negative or {@code minSize > maxSize}.
     */
    public static DataResult<List<ICIngredient>> validateIngredients(@NotNull List<ICIngredient> ingredients, ValidationContext ingredientContext, int minSize, int maxSize) {
        if (minSize < 0 || maxSize < 0) {
            throw new IllegalArgumentException("List size requirements cannot be negative.");
        } else if (minSize > maxSize) {
            throw new IllegalArgumentException("Minimum size must not be larger than maximum size");
        }
        if (ingredients.size() < minSize || ingredients.size() > maxSize) {
            return DataResult.error(() -> "Illegal amount of ingredients. Must be between %d and %d.".formatted(minSize, maxSize));
        }
        return validateIngredients(ingredients, ingredientContext);
    }

    /**
     * Validate a predicate against a {@link ValidationContext}.
     *
     * @param predicate The predicate which need to be validated.
     * @param context   the context which the predicate needs to match.
     * @return a {@link DataResult} indicating if the predicate was validated successfully.
     */
    public static DataResult<ICPredicate> validatePredicate(ICPredicate predicate, ValidationContext context) {
        try {
            context.validate(predicate);
        } catch (IllegalStateException e) {
            return DataResult.error(e::getMessage);
        }
        return DataResult.success(predicate);
    }

    /**
     * Validate a list of result stacks against a {@link ValidationContext}.
     *
     * @param results The result stacks which need to be validated.
     * @param context the context which the results need to match.
     * @return a {@link DataResult} indicating if the predicate was validated successfully.
     */
    public static DataResult<List<ICStack>> validateResults(List<ICStack> results, ValidationContext context) {
        try {
            for (var result : results) {
                context.validate(result);
            }
        } catch (IllegalStateException e) {
            return DataResult.error(e::getMessage);
        }
        return DataResult.success(results);
    }
}
