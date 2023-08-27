package com.carlschierig.immersive_crafting.crafting;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public interface ICRecipeSerializer<T extends ICRecipe> {
    T fromJson(ResourceLocation id, JsonObject object);
    JsonObject toJson(T instance);
    T fromNetwork(ResourceLocation id, FriendlyByteBuf buf);
    void toNetwork(FriendlyByteBuf buf, T instance);
}
