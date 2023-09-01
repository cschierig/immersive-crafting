package com.carlschierig.immersivecrafting.api.predicate.condition;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;

/**
 * A serializer for serializing and deserializing {@link ICCondition}s.
 * The serializer supports both json and network (de)serialization.
 *
 * @param <T> The type of condition for which the serializer is used.
 */
public interface ICConditionSerializer<T extends ICCondition> {
    /**
     * Returns a new {@link ICCondition} of type {@link T} based on the given {@link JsonObject}.
     *
     * @param json The json from which the condition is read.
     * @return a new {@link ICCondition} of type {@link T}.
     */
    T fromJson(JsonObject json);

    /**
     * Serializes the condition to json.
     *
     * @param instance The condition which should be serialized.
     * @return a new {@link JsonObject} containing a json representation of the passed instance.
     */
    JsonObject toJson(T instance);

    /**
     * Reads a condition of type {@link T} from the given {@link FriendlyByteBuf} and returns it.
     *
     * @param buf The {@link FriendlyByteBuf} from which the condition should be read.
     * @return a new condition of type {@link T}.
     */
    T fromNetwork(FriendlyByteBuf buf);

    /**
     * Serializes the condition to json.
     *
     * @param buf      The byte buffer to which the instance data should be appanded.
     * @param instance The condition which should be serialized.
     */
    void toNetwork(FriendlyByteBuf buf, T instance);
}
