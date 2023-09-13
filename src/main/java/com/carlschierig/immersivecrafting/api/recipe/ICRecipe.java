package com.carlschierig.immersivecrafting.api.recipe;

import com.carlschierig.immersivecrafting.api.context.ContextType;
import com.carlschierig.immersivecrafting.api.context.CraftingContext;
import com.carlschierig.immersivecrafting.api.context.RecipeContext;
import com.carlschierig.immersivecrafting.api.context.ValidationContext;
import com.carlschierig.immersivecrafting.api.predicate.ICPredicate;
import com.carlschierig.immersivecrafting.api.predicate.condition.ingredient.ICIngredient;
import com.carlschierig.immersivecrafting.api.predicate.condition.ingredient.ICStack;
import com.google.common.collect.ImmutableList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;

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
    /**
     * Returns whether this recipe matches the given {@link RecipeContext}.
     * This is used by the {@link ICRecipeManager} to determine which recipe to use when crafting.
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
     * @param recipeContext   The Recipe Context used to find the recipe.
     * @param craftingContext A context indicating how the stacks should be crafted.
     */
    public abstract void craft(RecipeContext recipeContext, CraftingContext craftingContext);

    /**
     * Returns a {@link List} containing the ingredients of the recipe.
     *
     * @return a {@link List} containing the ingredients of the recipe.
     */
    public abstract ImmutableList<ICIngredient> getIngredients();

    public abstract ICPredicate getPredicate();

    /**
     * Returns the unique {@link ResourceLocation} of the recipe.
     *
     * @return the unique {@link ResourceLocation} of the recipe.
     */
    public abstract ResourceLocation getId();

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
    public abstract ValidationContext getRequirements();

    /**
     * Returns a {@link ValidationContext} which contains all the {@link ContextType}s that must be provided to this recipe
     * when passing a {@link RecipeContext} as a parameter to any method.
     *
     * @return {@link ValidationContext} which contains the {@link ContextType}s this recipe needs.
     */
    public abstract ValidationContext getIngredientRequirements();
}
