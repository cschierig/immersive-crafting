package com.carlschierig.immersivecrafting.compat.emi;

import com.carlschierig.immersivecrafting.api.predicate.condition.ingredient.ICIngredient;
import com.carlschierig.immersivecrafting.api.predicate.condition.ingredient.ICStack;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;

import java.util.List;
import java.util.stream.Collectors;

public class EmiConversionUtil {
    public static List<EmiStack> convertStacks(List<ICStack> stacks) {
        return stacks.stream().map(Stack::new).collect(Collectors.toList());
    }

    public static List<EmiIngredient> convertIngredients(List<ICIngredient> stacks) {
        return stacks.stream().map(ICEmiIngredient::new).collect(Collectors.toList());
    }
}
