package com.carlschierig.immersive_crafting.predicate.condition;

import com.carlschierig.immersive_crafting.api.context.RecipeContext;
import com.carlschierig.immersive_crafting.api.predicate.condition.CompoundICCondition;
import com.carlschierig.immersive_crafting.api.predicate.condition.ICCondition;
import com.carlschierig.immersive_crafting.api.predicate.condition.ICConditionSerializer;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditions;

import java.util.function.Predicate;

public class AndCondition extends CompoundICCondition {
    private final Predicate<RecipeContext> predicate;

    public AndCondition(ICCondition[] conditions) {
        super(conditions);
        predicate = LootItemConditions.andConditions(conditions);
    }

    @Override
    public ICConditionSerializer<?> getSerializer() {
        return ICConditionSerializers.AND;
    }

    @Override
    public boolean test(RecipeContext context) {
        return predicate.test(context);
    }

    public static class Serializer extends CompoundICCondition.Serializer<AndCondition> {
        @Override
        protected AndCondition create(ICCondition[] conditions) {
            return new AndCondition(conditions);
        }
    }
}
