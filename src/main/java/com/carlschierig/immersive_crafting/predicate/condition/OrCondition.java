package com.carlschierig.immersive_crafting.predicate.condition;

import com.carlschierig.immersive_crafting.api.context.RecipeContext;
import com.carlschierig.immersive_crafting.api.predicate.condition.ICCondition;
import com.carlschierig.immersive_crafting.api.predicate.condition.ICConditionSerializer;
import com.carlschierig.immersive_crafting.serialization.ICByteBufHelper;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditions;

import java.util.Arrays;
import java.util.function.Predicate;

public class OrCondition implements ICCondition {
    private final ICCondition[] conditions;
    private final Predicate<RecipeContext> predicate;

    public OrCondition(ICCondition[] conditions) {
        this.conditions = conditions;
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

    public static class Serializer implements ICConditionSerializer<OrCondition> {
        public static final String CONDITIONS = "conditions";

        @Override
        public OrCondition fromJson(JsonObject object) {
            var conditions = ICConditionSerializers.fromJson(GsonHelper.getAsJsonObject(object, CONDITIONS));
            return new OrCondition(conditions);
        }

        @Override
        public JsonObject toJson(OrCondition instance) {
            var conditions = ICConditionSerializers.toJson(instance.conditions);
            var json = new JsonObject();
            json.add(CONDITIONS, conditions);
            return json;
        }

        @Override
        public OrCondition fromNetwork(FriendlyByteBuf buf) {
            var conditions = ICByteBufHelper.readList(buf, ICByteBufHelper::readICCondition);
            return new OrCondition(conditions.toArray(ICCondition[]::new));
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, OrCondition instance) {
            ICByteBufHelper.writeList(buf, Arrays.asList(instance.conditions), ICByteBufHelper::writeICCondition);
        }
    }
}
