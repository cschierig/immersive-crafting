package com.carlschierig.immersivecrafting.api.predicate.condition;

import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

/**
 * A serializer for serializing and deserializing {@link ICCondition}s.
 * The serializer supports both json and network (de)serialization.
 *
 * @param <T> The type of condition for which the serializer is used.
 */
public interface ICConditionSerializer<T extends ICCondition> {
    /**
     * The codec used for json (de)serialization of {@link ICCondition}s of type {@link T}.
     *
     * @return the codec used for json (de)serialization.
     */
    MapCodec<T> codec();

    /**
     * The codec used for (de)serialization of {@link ICCondition}s of type {@link T} into a byte stream.
     *
     * @return the codec used for byte stream (de)serialization.
     */
    StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec();
}
