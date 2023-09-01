package com.carlschierig.immersivecrafting.api.predicate.condition;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;

public interface ICConditionSerializer<T extends ICCondition> {
    T fromJson(JsonObject json);

    JsonObject toJson(T instance);

    T fromNetwork(FriendlyByteBuf buf);

    void toNetwork(FriendlyByteBuf buf, T instance);
}
