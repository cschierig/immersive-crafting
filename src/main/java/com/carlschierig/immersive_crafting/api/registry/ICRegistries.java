package com.carlschierig.immersive_crafting.api.registry;

import com.carlschierig.immersive_crafting.api.context.ContextType;
import com.carlschierig.immersive_crafting.api.predicate.condition.ICConditionSerializer;
import com.carlschierig.immersive_crafting.api.recipe.ICRecipeSerializer;
import com.carlschierig.immersive_crafting.api.recipe.ICRecipeType;
import com.carlschierig.immersive_crafting.registry.ICRegistryKeys;
import com.mojang.serialization.Lifecycle;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public final class ICRegistries {
    public static final Registry<ICConditionSerializer<?>> CONDITION_SERIALIZER = createRegistry(ICRegistryKeys.CONDITION_SERIALIZER);
    public static final Registry<ICRecipeType<?>> RECIPE_TYPE = createRegistry(ICRegistryKeys.RECIPE_TYPE);
    public static final Registry<ICRecipeSerializer<?>> RECIPE_SERIALIZER = createRegistry(ICRegistryKeys.RECIPE_SERIALIZER);
    public static final Registry<ContextType<?>> CONTEXT_TYPE = createRegistry(ICRegistryKeys.CONTEXT_TYPE);

    private static <T> Registry<T> createRegistry(ResourceKey<Registry<T>> registryKey) {
        return new MappedRegistry<>(registryKey, Lifecycle.stable());
    }
}
