package com.carlschierig.immersivecrafting.compat.client.jei;

import com.carlschierig.immersivecrafting.api.recipe.ICRecipe;
import com.carlschierig.immersivecrafting.api.recipe.ICRecipeType;
import com.carlschierig.immersivecrafting.api.registry.ICRegistries;
import mezz.jei.api.recipe.types.IRecipeType;
import net.minecraft.resources.Identifier;

public class ICJeiRecipeType<T extends ICRecipe> implements IRecipeType<T> {
    private final ICRecipeType<T> type;

    public ICJeiRecipeType(ICRecipeType<T> type) {
        this.type = type;
    }

    @Override
    public Identifier getUid() {
        return ICRegistries.RECIPE_TYPE.getKey(type);
    }

    @Override
    public Class<T> getRecipeClass() {
        return type.getRecipeClass();
    }

    public ICRecipeType<T> getType() {
        return type;
    }
}
