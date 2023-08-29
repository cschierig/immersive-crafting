package com.carlschierig.immersive_crafting.recipe;

import com.carlschierig.immersive_crafting.ImmersiveCrafting;
import com.carlschierig.immersive_crafting.api.recipe.ICRecipe;
import com.carlschierig.immersive_crafting.api.recipe.ICRecipeSerializer;
import com.carlschierig.immersive_crafting.api.registry.ICRegistries;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

public final class ICRecipeSerializers {
    public static final ICRecipeSerializer<UseItemOnRecipe> USE_ITEM_RECIPE = register("use_item_on", new UseItemOnRecipe.Serializer());

    private static <S extends ICRecipeSerializer<T>, T extends ICRecipe> S register(String id, S serializer) {
        return Registry.register(ICRegistries.RECIPE_SERIALIZER, new ResourceLocation(ImmersiveCrafting.MODID, id), serializer);
    }

    public static void init() {
    }
}
