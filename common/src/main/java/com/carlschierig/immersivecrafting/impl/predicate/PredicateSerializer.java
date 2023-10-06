package com.carlschierig.immersivecrafting.impl.predicate;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;

public interface PredicateSerializer<T> {
    T fromJson(JsonObject json);

    JsonObject toJson(T instance);

    T fromNetwork(FriendlyByteBuf buf);

    void toNetwork(FriendlyByteBuf buf, T instance);
}
