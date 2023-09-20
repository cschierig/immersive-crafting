package com.carlschierig.immersivecrafting.api.recipe;

import com.carlschierig.immersivecrafting.api.registry.ICRegistries;
import com.carlschierig.immersivecrafting.impl.util.ICUtil;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

/**
 * Recipe types provided by immersive crafting.
 */
public final class ICRecipeTypes {
    public static ICRecipeType<UseItemOnRecipe> USE_ITEM = register("use_item");

    private static <T extends ICRecipe> ICRecipeType<T> register(String name) {
        return Registry.register(
                ICRegistries.RECIPE_TYPE, new ResourceLocation(ICUtil.MODID, name), new ICRecipeType<T>() {
                    public String toString() {
                        return "<Recipe Type: " + name + ">";
                    }
                });
    }

    public static void init() {
    }
}
