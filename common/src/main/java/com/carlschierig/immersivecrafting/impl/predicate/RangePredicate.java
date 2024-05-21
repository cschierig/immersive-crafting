package com.carlschierig.immersivecrafting.impl.predicate;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

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
        var e = 0.0001f;
        return value >= (min - e) && value <= (max + e);
    }

    private static final Serializer serializer = new Serializer();

    public static PredicateSerializer<RangePredicate> getSerializer() {
        return serializer;
    }

    @Override
    public String toString() {
        return "[" + min + ", " + max + "]";
    }

    public static class Serializer implements PredicateSerializer<RangePredicate> {
        public static final Codec<RangePredicate> CODEC = RecordCodecBuilder.create(
                instance -> instance.group(
                        Codec.FLOAT.optionalFieldOf("min", Float.NEGATIVE_INFINITY).forGetter(predicate -> predicate.min),
                        Codec.FLOAT.optionalFieldOf("max", Float.POSITIVE_INFINITY).forGetter(predicate -> predicate.max)
                ).apply(instance, RangePredicate::new)
        );

        public static final StreamCodec<RegistryFriendlyByteBuf, RangePredicate> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.FLOAT,
                pred -> pred.min,
                ByteBufCodecs.FLOAT,
                pred -> pred.max,
                RangePredicate::new
        );

        @Override
        public Codec<RangePredicate> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, RangePredicate> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
