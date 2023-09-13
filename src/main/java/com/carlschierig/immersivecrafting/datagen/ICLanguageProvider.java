package com.carlschierig.immersivecrafting.datagen;

import com.carlschierig.immersivecrafting.api.data.ICTranslationHelper;
import com.carlschierig.immersivecrafting.api.recipe.ICRecipeTypes;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;

public class ICLanguageProvider extends FabricLanguageProvider {
    protected ICLanguageProvider(FabricDataOutput dataOutput) {
        super(dataOutput);
    }

    @Override
    public void generateTranslations(TranslationBuilder translationBuilder) {
        translationBuilder.add(ICTranslationHelper.translate(ICRecipeTypes.USE_ITEM), "Use Item on");
        translationBuilder.add("immersive_crafting.recipeView.conditions", "Conditions");
    }


}
