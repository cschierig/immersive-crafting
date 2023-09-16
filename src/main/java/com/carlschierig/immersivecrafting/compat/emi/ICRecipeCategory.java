package com.carlschierig.immersivecrafting.compat.emi;

import com.carlschierig.immersivecrafting.api.data.ICTranslationHelper;
import com.carlschierig.immersivecrafting.api.recipe.ICRecipeType;
import com.carlschierig.immersivecrafting.api.registry.ICRegistries;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import net.minecraft.network.chat.Component;

public class ICRecipeCategory extends EmiRecipeCategory {
    private final ICRecipeType<?> type;

    public ICRecipeCategory(ICRecipeType<?> type) {
        super(ICRegistries.RECIPE_TYPE.getKey(type), type.getRenderer()::render, type.getSimplifiedRenderer()::render);
        this.type = type;
    }

    @Override
    public Component getName() {
        return Component.translatable(ICTranslationHelper.translateRecipeType(type));
    }
}
