package com.carlschierig.immersivecrafting.api.recipe;

import com.carlschierig.immersivecrafting.api.registry.ICRegistries;
import com.carlschierig.immersivecrafting.impl.util.ICUtil;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;

/**
 * Recipe types provided by immersive crafting.
 */
public final class ICRecipeTypes {
    public static ICRecipeType<UseItemOnRecipe> USE_ITEM = register("use_item", UseItemOnRecipe.class);

    private static <T extends ICRecipe> ICRecipeType<T> register(String name, Class<T> type) {
        return Registry.register(
                ICRegistries.RECIPE_TYPE, ICUtil.getId(name), new ICRecipeType<T>() {
                    @Override
                    public Class<T> getRecipeClass() {
                        return type;
                    }

                    public String toString() {
                        return "<Recipe Type: " + name + ">";
                    }
                });
    }

    public static void init() {
    }
}
