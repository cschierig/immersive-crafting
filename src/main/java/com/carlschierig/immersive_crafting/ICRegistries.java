package com.carlschierig.immersive_crafting;

import com.carlschierig.immersive_crafting.crafting.ICRecipe;
import com.carlschierig.immersive_crafting.crafting.ICRecipeSerializer;
import com.carlschierig.immersive_crafting.crafting.ICRecipeType;
import com.carlschierig.immersive_crafting.predicate.condition.ICCondition;
import com.carlschierig.immersive_crafting.predicate.condition.ICConditionSerializer;
import com.mojang.serialization.Lifecycle;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public final class ICRegistries {
	public static final Registry<ICConditionSerializer<?>> CONDITION_SERIALIZER = createRegistry(ICRegistryKeys.CONDITION_SERIALIZER);
	public static final Registry<ICRecipeType<?>> RECIPE_TYPE = createRegistry(ICRegistryKeys.RECIPE_TYPE);
	public static final Registry<ICRecipeSerializer<?>> RECIPE_SERIALIZER = createRegistry(ICRegistryKeys.RECIPE_SERIALIZER);

	private static <T> Registry<T> createRegistry(ResourceKey<Registry<T>> registryKey) {
		return new MappedRegistry<>(registryKey, Lifecycle.stable());
	}
}
