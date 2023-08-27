package com.carlschierig.immersive_crafting;

import com.carlschierig.immersive_crafting.crafting.ICRecipe;
import com.carlschierig.immersive_crafting.crafting.ICRecipeSerializer;
import com.carlschierig.immersive_crafting.crafting.ICRecipeType;
import com.carlschierig.immersive_crafting.predicate.condition.ICCondition;
import com.carlschierig.immersive_crafting.predicate.condition.ICConditionSerializer;
import com.carlschierig.immersive_crafting.util.ICUtil;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public class ICRegistryKeys {
	public static final ResourceKey<Registry<ICCondition>> CONDITION = createRegistryKey("condition");
	public static final ResourceKey<Registry<ICConditionSerializer<?>>> CONDITION_SERIALIZER = createRegistryKey("condition_serializer");
	public static final ResourceKey<Registry<ICRecipe>> RECIPE = createRegistryKey("recipe");
	public static final ResourceKey<Registry<ICRecipeType<?>>> RECIPE_TYPE = createRegistryKey("recipe_type");
	public static final ResourceKey<Registry<ICRecipeSerializer<?>>> RECIPE_SERIALIZER = createRegistryKey("recipe_serializer");

	private static <T> ResourceKey<Registry<T>> createRegistryKey(String path) {
		return ResourceKey.createRegistryKey(ICUtil.createResourceLocation(path));
	}
}
