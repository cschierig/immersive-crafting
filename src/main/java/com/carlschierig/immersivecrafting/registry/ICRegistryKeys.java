package com.carlschierig.immersivecrafting.registry;

import com.carlschierig.immersivecrafting.api.context.ContextType;
import com.carlschierig.immersivecrafting.api.predicate.condition.ICConditionSerializer;
import com.carlschierig.immersivecrafting.api.recipe.ICRecipeSerializer;
import com.carlschierig.immersivecrafting.api.recipe.ICRecipeType;
import com.carlschierig.immersivecrafting.util.ICUtil;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public class ICRegistryKeys {
    public static final ResourceKey<Registry<ICConditionSerializer<?>>> CONDITION_SERIALIZER = createRegistryKey("condition_serializer");
    public static final ResourceKey<Registry<ICRecipeType<?>>> RECIPE_TYPE = createRegistryKey("recipe_type");
    public static final ResourceKey<Registry<ICRecipeSerializer<?>>> RECIPE_SERIALIZER = createRegistryKey("recipe_serializer");
    public static final ResourceKey<Registry<ContextType<?>>> CONTEXT_TYPE = createRegistryKey("context_type");

    private static <T> ResourceKey<Registry<T>> createRegistryKey(String path) {
        return ResourceKey.createRegistryKey(ICUtil.createResourceLocation(path));
    }
}
