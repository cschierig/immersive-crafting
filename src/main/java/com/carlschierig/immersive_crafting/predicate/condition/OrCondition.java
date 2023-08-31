package com.carlschierig.immersive_crafting.predicate.condition;

import com.carlschierig.immersive_crafting.api.context.RecipeContext;
import com.carlschierig.immersive_crafting.api.predicate.condition.CompoundICCondition;
import com.carlschierig.immersive_crafting.api.predicate.condition.ICCondition;
import com.carlschierig.immersive_crafting.api.predicate.condition.ICConditionSerializer;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditions;

import java.util.function.Predicate;

public class OrCondition extends CompoundICCondition {
    private final Predicate<RecipeContext> predicate;

    public OrCondition(ICCondition[] conditions) {
        super(conditions);
        predicate = LootItemConditions.orConditions(conditions);
    }

    @Override
    public ICConditionSerializer<OrCondition> getSerializer() {
        return ICConditionSerializers.OR;
    }

    @Override
    public boolean test(RecipeContext context) {
        return predicate.test(context);
    }

    public static class Serializer extends CompoundICCondition.Serializer<OrCondition> {
        @Override
        protected OrCondition create(ICCondition[] conditions) {
            return new OrCondition(conditions);
        }
    }
}
