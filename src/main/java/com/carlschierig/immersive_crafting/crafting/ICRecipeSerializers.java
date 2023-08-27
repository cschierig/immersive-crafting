package com.carlschierig.immersive_crafting.crafting;

import com.carlschierig.immersive_crafting.ICRegistries;
import com.carlschierig.immersive_crafting.ImmersiveCrafting;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

public final class ICRecipeSerializers {
	public static final ICRecipeSerializer<UseItemRecipe> USE_ITEM_RECIPE = register("use_item", new UseItemRecipe.Serializer());

	private static <S extends ICRecipeSerializer<T>, T extends ICRecipe> S register(String id, S serializer) {
		return Registry.register(ICRegistries.RECIPE_SERIALIZER, new ResourceLocation(ImmersiveCrafting.MODID, id), serializer);
	}

	public static void init() {
	}
}
