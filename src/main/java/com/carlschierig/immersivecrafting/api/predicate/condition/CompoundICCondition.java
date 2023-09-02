package com.carlschierig.immersivecrafting.api.predicate.condition;

import com.carlschierig.immersivecrafting.api.context.ValidationContext;
import com.carlschierig.immersivecrafting.impl.util.ICByteBufHelperImpl;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * A condition which consists of multiple child conditions.
 * Subclasses can also use the {@link CompoundICCondition.Serializer} serializer.
 */
public abstract class CompoundICCondition implements ICCondition {
    protected final ICCondition[] conditions;

    /**
     * create a new compound condition from the given child conditions.
     *
     * @param conditions The children of the compound conditions.
     */
    public CompoundICCondition(ICCondition[] conditions) {
        this.conditions = conditions;
    }

    @Override
    public final ValidationContext getRequirements() {
        return ValidationContext.merge(Arrays.stream(conditions).map(ICCondition::getRequirements).collect(Collectors.toList()));
    }

    /**
     * A template {@link ICConditionSerializer} for {@link CompoundICCondition}s.
     * Subclasses must override {@link Serializer#create(ICCondition[])} to determine how a new instance should
     * be created.
     *
     * @param <T> The type of condition for which the serializer is used.
     */
    public static abstract class Serializer<T extends CompoundICCondition> implements ICConditionSerializer<T> {
        private static final String CONDITIONS = "conditions";

        /**
         * Create a new compound condition of type {@link T} using the given {@link ICCondition[]}.
         *
         * @param conditions The conditions which should be passed to the constructor.
         * @return a new compound condition of type {@link T}.
         */
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
            var conditions = ICByteBufHelperImpl.readList(buf, ICByteBufHelperImpl::readICCondition);
            return create(conditions.toArray(ICCondition[]::new));
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, T instance) {
            ICByteBufHelperImpl.writeList(buf, Arrays.asList(instance.conditions), ICByteBufHelperImpl::writeICCondition);
        }
    }
}
