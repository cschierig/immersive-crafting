package com.carlschierig.immersivecrafting.impl.datagen;

import com.carlschierig.immersivecrafting.api.predicate.condition.*;
import com.carlschierig.immersivecrafting.api.recipe.ICRecipeTypes;
import com.carlschierig.immersivecrafting.impl.util.ICTranslationHelper;
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
        // And
        builder.add(ICTranslationHelper.translateCondition(AndCondition.LANGUAGE_KEY), "And");
        builder.add(ICTranslationHelper.translateConditionDescription(AndCondition.LANGUAGE_KEY), "All sub conditions must be fulfilled.");
        // Or
        builder.add(ICTranslationHelper.translateCondition(OrCondition.LANGUAGE_KEY), "Or");
        builder.add(ICTranslationHelper.translateConditionDescription(OrCondition.LANGUAGE_KEY), "Any sub condition must be fulfilled.");
        // Block
        builder.add(ICTranslationHelper.translateCondition(BlockCondition.LANGUAGE_KEY), "Block");
        builder.add(ICTranslationHelper.translateConditionDescription(BlockCondition.LANGUAGE_KEY, "id"), "ID");
        builder.add(ICTranslationHelper.translateConditionDescription(BlockCondition.LANGUAGE_KEY, "tag"), "Tag");
        builder.add(ICTranslationHelper.translateConditionDescription(BlockCondition.LANGUAGE_KEY, "hardness"), "Hardness");
        // Day Time
        builder.add(ICTranslationHelper.translateCondition(DayTimeCondition.LANGUAGE_KEY), "Day Time");
        builder.add(ICTranslationHelper.translateConditionDescription(DayTimeCondition.LANGUAGE_KEY, "timeRange"), "It must be between");
        builder.add(ICTranslationHelper.translateConditionDescription(DayTimeCondition.LANGUAGE_KEY, "and"), "and");
        builder.add(ICTranslationHelper.translateConditionDescription(DayTimeCondition.LANGUAGE_KEY, "earlyMorning"), "Early Morning");
        builder.add(ICTranslationHelper.translateConditionDescription(DayTimeCondition.LANGUAGE_KEY, "morning"), "Morning");
        builder.add(ICTranslationHelper.translateConditionDescription(DayTimeCondition.LANGUAGE_KEY, "lateMorning"), "Late Morning");
        builder.add(ICTranslationHelper.translateConditionDescription(DayTimeCondition.LANGUAGE_KEY, "beforeNoon"), "Before Noon");
        builder.add(ICTranslationHelper.translateConditionDescription(DayTimeCondition.LANGUAGE_KEY, "noon"), "Noon");
        builder.add(ICTranslationHelper.translateConditionDescription(DayTimeCondition.LANGUAGE_KEY, "earlyAfternoon"), "Early Afternoon");
        builder.add(ICTranslationHelper.translateConditionDescription(DayTimeCondition.LANGUAGE_KEY, "afternoon"), "Afternoon");
        builder.add(ICTranslationHelper.translateConditionDescription(DayTimeCondition.LANGUAGE_KEY, "lateAfternoon"), "Late Afternoon");
        builder.add(ICTranslationHelper.translateConditionDescription(DayTimeCondition.LANGUAGE_KEY, "evening"), "Evening");
        builder.add(ICTranslationHelper.translateConditionDescription(DayTimeCondition.LANGUAGE_KEY, "sunset"), "Sunset");
        builder.add(ICTranslationHelper.translateConditionDescription(DayTimeCondition.LANGUAGE_KEY, "nightfall"), "Nightfall");
        builder.add(ICTranslationHelper.translateConditionDescription(DayTimeCondition.LANGUAGE_KEY, "beforeMidnight"), "Before Midnight");
        builder.add(ICTranslationHelper.translateConditionDescription(DayTimeCondition.LANGUAGE_KEY, "midnight"), "Midnight");
        builder.add(ICTranslationHelper.translateConditionDescription(DayTimeCondition.LANGUAGE_KEY, "afterMidnight"), "After Midnight");
        builder.add(ICTranslationHelper.translateConditionDescription(DayTimeCondition.LANGUAGE_KEY, "sunrise"), "Sunrise");
        // Inverted
        builder.add(ICTranslationHelper.translateCondition(InvertedCondition.LANGUAGE_KEY), "Inverted");
        builder.add(ICTranslationHelper.translateConditionDescription(InvertedCondition.LANGUAGE_KEY), "The following must not be true:");
    }
}
