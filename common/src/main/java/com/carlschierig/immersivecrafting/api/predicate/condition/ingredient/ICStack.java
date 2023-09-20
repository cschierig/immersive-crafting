package com.carlschierig.immersivecrafting.api.predicate.condition.ingredient;

import com.carlschierig.immersivecrafting.api.context.CraftingContext;
import com.carlschierig.immersivecrafting.api.context.RecipeContext;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

/**
 * A stack is an identifiable ingredient that can be crafted and spawned into the world.
 */
public abstract class ICStack implements ICIngredient {
    /**
     * Returns the identifier of the underlying resource.
     *
     * @return the identifier of the underlying resource.
     */
    public abstract ResourceLocation getIdentifier();

    @Override
    public List<ICStack> getParts() {
        return List.of(this);
    }

    @Override
    public abstract ICStack copy();

    /**
     * Used by recipe viewers to determine for which items recipes need to be shown.
     *
     * @return The resource which is represented by this stack.
     */
    public abstract Object getKey();

    /**
     * Craft the stack.
     *
     * @param recipeContext   contains information of the surroundings.
     * @param craftingContext contains the necessary location information to spawn the resources.
     */
    public abstract void craft(RecipeContext recipeContext, CraftingContext craftingContext);
}
