package com.carlschierig.immersive_crafting.recipe;

import com.carlschierig.immersive_crafting.ImmersiveCrafting;
import com.carlschierig.immersive_crafting.api.recipe.ICRecipe;
import com.carlschierig.immersive_crafting.api.recipe.ICRecipeType;
import com.carlschierig.immersive_crafting.api.registry.ICRegistries;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

public final class ICRecipeTypes {
    public static ICRecipeType<UseItemOnRecipe> USE_ITEM = register("use_item");

    private static <T extends ICRecipe> ICRecipeType<T> register(String name) {
        return Registry.register(
                ICRegistries.RECIPE_TYPE, new ResourceLocation(ImmersiveCrafting.MODID, name), new ICRecipeType<T>() {
                    public String toString() {
                        return name;
                    }
                });
    }

    public static void init() {
    }
}
