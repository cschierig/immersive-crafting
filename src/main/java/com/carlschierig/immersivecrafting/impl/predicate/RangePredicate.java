package com.carlschierig.immersivecrafting.impl.predicate;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;

import java.util.function.Predicate;

public class RangePredicate implements Predicate<Float> {
    private final float min;
    private final float max;

    public RangePredicate(float min, float max) {
        if (min > max) {
            throw new IllegalArgumentException("Minimum may not be larger than maximum.");
        }
        this.min = min;
        this.max = max;
    }

    public boolean test(Float value) {
        return value >= min && value <= max;
    }

    private static final Serializer serializer = new Serializer();

    public static PredicateSerializer<RangePredicate> getSerializer() {
        return serializer;
    }

    public static class Serializer implements PredicateSerializer<RangePredicate> {
        private static final String MIN = "min";
        private static final String MAX = "max";

        @Override
        public RangePredicate fromJson(JsonObject json) {
            if (!json.has(MIN) && !json.has(MAX)) {
                throw new JsonParseException("Range must include minimum or maximum value.");
            }
            var min = GsonHelper.getAsFloat(json, MIN, Float.NEGATIVE_INFINITY);
            var max = GsonHelper.getAsFloat(json, MIN, Float.POSITIVE_INFINITY);
            if (min > max) {
                throw new JsonParseException("Minimum may not be larger than maximum.");
            }
            return new RangePredicate(min, max);
        }

        @Override
        public JsonObject toJson(RangePredicate instance) {
            var json = new JsonObject();
            if (!Float.isInfinite(instance.min)) {
                json.addProperty(MIN, instance.min);
            }
            if (!Float.isInfinite(instance.max)) {
                json.addProperty(MIN, instance.max);
            }
            return json;
        }

        @Override
        public RangePredicate fromNetwork(FriendlyByteBuf buf) {
            var min = buf.readFloat();
            var max = buf.readFloat();
            return new RangePredicate(min, max);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, RangePredicate instance) {
            buf.writeFloat(instance.min);
            buf.writeFloat(instance.max);
        }
    }
}
