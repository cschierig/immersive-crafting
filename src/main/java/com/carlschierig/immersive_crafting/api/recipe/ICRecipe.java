package com.carlschierig.immersive_crafting.api.recipe;

import com.carlschierig.immersive_crafting.api.context.RecipeContext;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public abstract class ICRecipe {
	public abstract boolean matches(RecipeContext context);

	public abstract List<ItemStack> assembleResults(RecipeContext context);

	public abstract ResourceLocation getId();

	public abstract ICRecipeSerializer<?> getSerializer();

	public abstract ICRecipeType<?> getType();

}
