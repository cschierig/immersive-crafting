package com.carlschierig.immersivecrafting.api.predicate;

import com.carlschierig.immersivecrafting.api.predicate.condition.*;
import com.carlschierig.immersivecrafting.impl.util.ICByteBufHelperImpl;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ICPredicate extends AndCondition {
    public ICPredicate(ICCondition[] conditions) {
        super(conditions);
    }

    @Override
    public ICConditionSerializer<?> getSerializer() {
        return ICConditionSerializers.PREDICATE;
    }

    public static class Serializer implements ICConditionSerializer<ICPredicate> {

        @Override
        public ICPredicate fromJson(JsonObject json) {
            var conditions = ICConditionSerializers.fromJson(json);
            return new ICPredicate(conditions);
        }

        @Override
        public JsonObject toJson(ICPredicate instance) {
            return ICConditionSerializers.toJson(instance.conditions);
        }

        @Override
        public ICPredicate fromNetwork(FriendlyByteBuf buf) {
            var conditions = ICByteBufHelperImpl.readList(buf, ICByteBufHelperImpl::readICCondition);
            return new ICPredicate(conditions.toArray(ICCondition[]::new));
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, ICPredicate instance) {
            ICByteBufHelperImpl.writeList(buf, Arrays.asList(instance.conditions), ICByteBufHelperImpl::writeICCondition);
        }
    }

    public static class Builder {
        private final List<ICCondition> builderConditions = new ArrayList<>();

        @Contract("_->this")
        public Builder with(ICCondition condition) {
            builderConditions.add(condition);
            return this;
        }

        @Contract("_->this")
        public Builder and(ICCondition... conditions) {
            builderConditions.add(new AndCondition(conditions));
            return this;
        }

        @Contract("_->this")
        public Builder or(ICCondition... conditions) {
            builderConditions.add(new OrCondition(conditions));
            return this;
        }

        public ICPredicate build() {
            return new ICPredicate(builderConditions.toArray(ICCondition[]::new));
        }
    }
}
