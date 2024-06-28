package com.carlschierig.immersivecrafting.api.recipe;

import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

/**
 * A serializer for serializing and deserializing {@link ICRecipe}s.
 * The serializer supports both json and network (de)serialization.
 *
 * @param <T> The type of recipe for which the serializer is used.
 */
public interface ICRecipeSerializer<T extends ICRecipe> {
    /**
     * A codec for serializing {@link ICRecipe}s of type {@link T}.
     * The codec should validate the ingredients and the predicate using a validation context.
     *
     * @return a codec for serializing {@link ICRecipe}s of type {@link T}.
     */
    MapCodec<T> codec();

    StreamCodec<RegistryFriendlyByteBuf, T> streamCodec();
}
