package com.carlschierig.immersivecrafting.impl.recipe;

import com.carlschierig.immersivecrafting.ImmersiveCrafting;
import com.carlschierig.immersivecrafting.api.recipe.ICRecipe;
import com.carlschierig.immersivecrafting.api.recipe.ICRecipeSerializer;
import com.carlschierig.immersivecrafting.api.recipe.UseItemOnRecipe;
import com.carlschierig.immersivecrafting.api.registry.ICRegistries;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public final class ICRecipeSerializers {
    public static final ICRecipeSerializer<UseItemOnRecipe> USE_ITEM_RECIPE = register("use_item_on", new UseItemOnRecipe.Serializer());

    private static <S extends ICRecipeSerializer<T>, T extends ICRecipe> S register(String id, S serializer) {
        return Registry.register(ICRegistries.RECIPE_SERIALIZER, new ResourceLocation(ImmersiveCrafting.MODID, id), serializer);
    }

    public static void init() {
    }
}
