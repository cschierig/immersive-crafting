package com.carlschierig.immersivecrafting.api.predicate.condition;

import com.carlschierig.immersivecrafting.api.context.RecipeContext;
import com.carlschierig.immersivecrafting.api.context.ValidationContext;
import com.carlschierig.immersivecrafting.impl.util.ICByteBufHelperImpl;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;


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

        @Override
        public InvertedCondition fromJson(JsonObject json) {
            // TODO: error handling
            var first = json.entrySet().stream().findFirst().get();
            var type = new ResourceLocation(first.getKey());
            var condition = first.getValue().getAsJsonObject();

            return new InvertedCondition(ICConditionSerializers.fromJson(type, condition));
        }

        @Override
        public JsonObject toJson(InvertedCondition instance) {
            var json = new JsonObject();

            var condition = instance.original;
            ICConditionSerializers.addCondition(json, condition);

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
