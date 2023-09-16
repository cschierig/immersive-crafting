package com.carlschierig.immersivecrafting.compat.emi;

import com.carlschierig.immersivecrafting.api.recipe.ICRecipeManager;
import com.carlschierig.immersivecrafting.api.recipe.ICRecipeType;
import com.carlschierig.immersivecrafting.api.registry.ICRegistries;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class ICEmiPlugin implements EmiPlugin {
    public static final Map<ResourceLocation, EmiRecipeCategory> CATEGORIES = new HashMap<>();

    @Override
    public void register(EmiRegistry registry) {
        for (var entry : ICRegistries.RECIPE_TYPE.entrySet()) {
            var key = entry.getKey().location();
            var value = entry.getValue();

            var category = new ICRecipeCategory(value);
            CATEGORIES.put(key, category);
            registry.addCategory(category);
            registerRecipeHandler(registry, category);
            registerRecipes(registry, key, value);
        }
    }

    private void registerRecipeHandler(EmiRegistry registry, ICRecipeCategory category) {
        registry.addRecipeHandler(null, new ICRecipeHandler(category));
    }

    private void registerRecipes(EmiRegistry registry, ResourceLocation id, ICRecipeType<?> value) {
        for (var recipe : ICRecipeManager.getRecipes(value)) {
            var emiRecipe = new ICEmiRecipe(CATEGORIES.get(id), recipe);
            registry.addRecipe(emiRecipe);
        }
    }
}
