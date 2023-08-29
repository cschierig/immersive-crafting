package com.carlschierig.immersive_crafting.serialization;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;

public interface ICSerializer<T> {

    T fromJson(JsonObject json);

    JsonObject toJson(T instance);

    T fromNetwork(FriendlyByteBuf buf);

    void toNetwork(FriendlyByteBuf buf, T instance);
}
