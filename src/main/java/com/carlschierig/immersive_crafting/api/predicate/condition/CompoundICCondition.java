package com.carlschierig.immersive_crafting.api.predicate.condition;

import com.carlschierig.immersive_crafting.api.context.ValidationContext;
import com.carlschierig.immersive_crafting.predicate.condition.ICConditionSerializers;
import com.carlschierig.immersive_crafting.util.ICByteBufHelper;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;

import java.util.Arrays;
import java.util.stream.Collectors;

public abstract class CompoundICCondition implements ICCondition {
    protected final ICCondition[] conditions;

    public CompoundICCondition(ICCondition[] conditions) {
        this.conditions = conditions;
    }

    public final ICCondition[] getConditions() {
        return conditions;
    }

    @Override
    public final ValidationContext getRequirements() {
        return ValidationContext.merge(Arrays.stream(conditions).map(ICCondition::getRequirements).collect(Collectors.toList()));
    }

    public static abstract class Serializer<T extends CompoundICCondition> implements ICConditionSerializer<T> {
        private static final String CONDITIONS = "conditions";

        protected abstract T create(ICCondition[] conditions);

        @Override
        public T fromJson(JsonObject json) {
            var conditions = ICConditionSerializers.fromJson(GsonHelper.getAsJsonObject(json, CONDITIONS));
            return create(conditions);
        }

        @Override
        public JsonObject toJson(T instance) {
            var conditions = ICConditionSerializers.toJson(instance.conditions);
            var json = new JsonObject();
            json.add(CONDITIONS, conditions);
            return json;
        }

        @Override
        public T fromNetwork(FriendlyByteBuf buf) {
            var conditions = ICByteBufHelper.readList(buf, ICByteBufHelper::readICCondition);
            return create(conditions.toArray(ICCondition[]::new));
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, T instance) {
            ICByteBufHelper.writeList(buf, Arrays.asList(instance.conditions), ICByteBufHelper::writeICCondition);
        }
    }
}
