package com.carlschierig.immersivecrafting.api.predicate.condition;

import com.carlschierig.immersivecrafting.api.context.RecipeContext;
import com.carlschierig.immersivecrafting.api.context.ValidationContext;
import com.carlschierig.immersivecrafting.api.serialization.ICGsonHelper;
import com.carlschierig.immersivecrafting.impl.util.ICByteBufHelperImpl;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;


public class InvertedCondition implements ICCondition {
    private final ICCondition original;

    public InvertedCondition(ICCondition original) {
        this.original = original;
    }

    @Override
    public boolean test(RecipeContext context) {
        return !original.test(context);
    }

    @Override
    public ICConditionSerializer<?> getSerializer() {
        return ICConditionSerializers.INVERT;
    }

    @Override
    public ValidationContext getRequirements() {
        return original.getRequirements();
    }

    public static final class Serializer implements ICConditionSerializer<InvertedCondition> {
        private static final String CONDITION = "condition";

        @Override
        public InvertedCondition fromJson(JsonObject json) {
            var condition = GsonHelper.getAsJsonObject(json, CONDITION);

            return new InvertedCondition(ICGsonHelper.getAsCondition(condition));
        }

        @Override
        public JsonObject toJson(InvertedCondition instance) {
            var json = new JsonObject();

            var condition = ICGsonHelper.conditionToJson(instance.original);
            json.add(CONDITION, condition);

            return json;
        }

        @Override
        public InvertedCondition fromNetwork(FriendlyByteBuf buf) {
            var condition = ICByteBufHelperImpl.readICCondition(buf);
            return new InvertedCondition(condition);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, InvertedCondition instance) {
            ICByteBufHelperImpl.writeICCondition(buf, instance.original);
        }
    }
}
