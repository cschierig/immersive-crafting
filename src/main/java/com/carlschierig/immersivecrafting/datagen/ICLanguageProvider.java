package com.carlschierig.immersivecrafting.datagen;

import com.carlschierig.immersivecrafting.api.data.ICTranslationHelper;
import com.carlschierig.immersivecrafting.api.predicate.condition.AndCondition;
import com.carlschierig.immersivecrafting.api.predicate.condition.BlockCondition;
import com.carlschierig.immersivecrafting.api.recipe.ICRecipeTypes;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;

public class ICLanguageProvider extends FabricLanguageProvider {
    protected ICLanguageProvider(FabricDataOutput dataOutput) {
        super(dataOutput);
    }

    @Override
    public void generateTranslations(TranslationBuilder builder) {
        translateUIElements(builder);
        translateRecipeTypes(builder);
        translateConditions(builder);
    }

    private void translateUIElements(TranslationBuilder builder) {
        builder.add("immersive_crafting.recipeView.conditions", "Conditions");
    }

    private void translateRecipeTypes(TranslationBuilder builder) {
        builder.add(ICTranslationHelper.translateRecipeType(ICRecipeTypes.USE_ITEM), "Use Item on");
    }

    private void translateConditions(TranslationBuilder builder) {
        builder.add(ICTranslationHelper.translateCondition(AndCondition.LANGUAGE_KEY), "And");
        builder.add(ICTranslationHelper.translateConditionDescription(AndCondition.LANGUAGE_KEY), "All sub conditions must be fulfilled.");
        builder.add(ICTranslationHelper.translateCondition(BlockCondition.LANGUAGE_KEY), "Block");
        builder.add(ICTranslationHelper.translateConditionDescription(BlockCondition.LANGUAGE_KEY, "id"), "ID");
        builder.add(ICTranslationHelper.translateConditionDescription(BlockCondition.LANGUAGE_KEY, "hardness"), "Hardness");
    }
}
