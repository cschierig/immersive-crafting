package com.carlschierig.immersivecrafting.api.recipe;

import com.carlschierig.immersivecrafting.ImmersiveCrafting;
import com.carlschierig.immersivecrafting.api.registry.ICRegistries;
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
