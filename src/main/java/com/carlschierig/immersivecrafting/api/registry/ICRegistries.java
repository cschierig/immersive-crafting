package com.carlschierig.immersivecrafting.api.registry;

import com.carlschierig.immersivecrafting.api.context.ContextType;
import com.carlschierig.immersivecrafting.api.predicate.condition.ICConditionSerializer;
import com.carlschierig.immersivecrafting.api.recipe.ICRecipeSerializer;
import com.carlschierig.immersivecrafting.api.recipe.ICRecipeType;
import com.carlschierig.immersivecrafting.registry.ICRegistryKeys;
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
