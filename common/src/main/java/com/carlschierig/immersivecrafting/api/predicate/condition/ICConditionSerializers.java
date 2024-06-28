package com.carlschierig.immersivecrafting.api.predicate.condition;

import com.carlschierig.immersivecrafting.api.predicate.ICPredicate;
import com.carlschierig.immersivecrafting.api.predicate.condition.ingredient.ItemStackIngredient;
import com.carlschierig.immersivecrafting.api.predicate.condition.stack.ICBlockStateStack;
import com.carlschierig.immersivecrafting.api.predicate.condition.stack.ICItemStack;
import com.carlschierig.immersivecrafting.api.registry.ICRegistries;
import com.carlschierig.immersivecrafting.impl.registry.ICRegistriesImpl;
import com.carlschierig.immersivecrafting.impl.util.ICUtil;

public final class ICConditionSerializers {
    public static final ICConditionSerializer<ICPredicate> PREDICATE = register("predicate", new ICPredicate.Serializer());
    public static final ICConditionSerializer<OrCondition> OR = register("or", new OrCondition.Serializer());
    public static final ICConditionSerializer<AndCondition> AND = register("and", new AndCondition.Serializer());
    public static final ICConditionSerializer<BlockCondition> BLOCK = register("block", new BlockCondition.Serializer());
    public static final ICConditionSerializer<InvertedCondition> INVERT = register("invert", new InvertedCondition.Serializer());
    public static final ICConditionSerializer<DayTimeCondition> DAY_TIME = register("day_time", new DayTimeCondition.Serializer());
    public static final ICConditionSerializer<ItemStackIngredient> ITEM_STACK = register("item_stack", new ItemStackIngredient.Serializer());
    // Stacks
    public static final ICConditionSerializer<ICBlockStateStack> BLOCK_STATE = register("block_state", new ICBlockStateStack.Serializer());
    public static final ICConditionSerializer<ICItemStack> ITEM = register("item", new ICItemStack.Serializer());


    private static <T extends ICCondition> ICConditionSerializer<T> register(String id, ICConditionSerializer<T> serializer) {
        return ICRegistriesImpl.register(ICRegistries.CONDITION_SERIALIZER, ICUtil.getId(id), serializer);
    }

    public static void init() {
    }
}
