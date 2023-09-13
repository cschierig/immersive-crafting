package com.carlschierig.immersivecrafting.api.predicate.condition;

import com.carlschierig.immersivecrafting.ImmersiveCrafting;
import com.carlschierig.immersivecrafting.api.predicate.ICPredicate;
import com.carlschierig.immersivecrafting.api.predicate.condition.ingredient.ICItemStack;
import com.carlschierig.immersivecrafting.api.registry.ICRegistries;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

public final class ICConditionSerializers {
    public static final ICConditionSerializer<ICPredicate> PREDICATE = register("predicate", new ICPredicate.Serializer());
    public static final ICConditionSerializer<OrCondition> OR = register("or", new OrCondition.Serializer());
    public static final ICConditionSerializer<AndCondition> AND = register("and", new AndCondition.Serializer());
    public static final ICConditionSerializer<BlockCondition> BLOCK = register("block", new BlockCondition.Serializer());
    public static final ICConditionSerializer<ICItemStack> ITEM = register("item", new ICItemStack.Serializer());
    public static final ICConditionSerializer<InvertedCondition> INVERT = register("invert", new InvertedCondition.Serializer());
    public static final ICConditionSerializer<DayTimeCondition> DAY_TIME = register("day_time", new DayTimeCondition.Serializer());


    private static <T extends ICCondition> ICConditionSerializer<T> register(String id, ICConditionSerializer<T> serializer) {
        return Registry.register(ICRegistries.CONDITION_SERIALIZER, new ResourceLocation(ImmersiveCrafting.MODID, id), serializer);
    }

    public static void init() {
    }
}
