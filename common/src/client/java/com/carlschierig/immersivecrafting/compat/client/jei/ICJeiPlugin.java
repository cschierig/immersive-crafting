package com.carlschierig.immersivecrafting.compat.client.jei;

import com.carlschierig.immersivecrafting.api.recipe.ICRecipe;
import com.carlschierig.immersivecrafting.api.recipe.ICRecipeHolder;
import com.carlschierig.immersivecrafting.api.recipe.ICRecipeManager;
import com.carlschierig.immersivecrafting.api.registry.ICRegistries;
import com.carlschierig.immersivecrafting.impl.util.ICUtil;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.Identifier;

import java.util.HashMap;
import java.util.Map;

@JeiPlugin
public class ICJeiPlugin implements IModPlugin {
    public static final Identifier PLUGIN_ID = ICUtil.getId("ic_jei_plugin");

    public static final Map<Identifier, ICJeiRecipeCategory<?>> CATEGORIES = new HashMap<>();

    @Override
    public Identifier getPluginUid() {
        return PLUGIN_ID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        for (var entry : ICRegistries.RECIPE_TYPE.entrySet()) {
            var key = entry.getKey().identifier();
            var value = entry.getValue();

            var category = new ICJeiRecipeCategory<>(new ICJeiRecipeType<>(value), registration.getJeiHelpers());
            CATEGORIES.put(key, category);
            registration.addRecipeCategories(category);
//            registerRecipeHandler(registry, category);
//            registerRecipes(registry, key, value);
        }
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        for (var entry : CATEGORIES.values()) {
            var type = entry.getRecipeType();
            registerForType(registration, type);
        }
    }

    private <T extends ICRecipe> void registerForType(IRecipeRegistration registration, ICJeiRecipeType<T> type) {
        registration.addRecipes(type, ICRecipeManager.getRecipes(type.getType()).stream().map(ICRecipeHolder::recipe).toList());
    }
}
