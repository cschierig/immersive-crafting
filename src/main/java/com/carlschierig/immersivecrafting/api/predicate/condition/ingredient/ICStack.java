package com.carlschierig.immersivecrafting.api.predicate.condition.ingredient;

import com.carlschierig.immersivecrafting.api.context.CraftingContext;
import com.carlschierig.immersivecrafting.api.context.RecipeContext;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public abstract class ICStack implements ICIngredient {
    public abstract ResourceLocation getIdentifier();

    @Override
    public List<ICStack> getParts() {
        return List.of(this);
    }

    @Override
    public abstract ICStack copy();

    public abstract Object getKey();

    public abstract void craft(RecipeContext recipeContext, CraftingContext craftingContext);
}
