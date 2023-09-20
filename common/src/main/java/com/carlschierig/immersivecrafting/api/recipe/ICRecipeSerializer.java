package com.carlschierig.immersivecrafting.api.recipe;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Contract;

/**
 * A serializer for serializing and deserializing {@link ICRecipe}s.
 * The serializer supports both json and network (de)serialization.
 *
 * @param <T> The type of recipe for which the serializer is used.
 */
public interface ICRecipeSerializer<T extends ICRecipe> {
    /**
     * Returns a new {@link ICRecipe} of type {@link T} based on the given {@link JsonObject}.
     *
     * @param id   The id of the recipe.
     * @param json The json from which the condition is read.
     * @return a new {@link ICRecipe} of type {@link T}.
     */
    @Contract(pure = true)
    T fromJson(ResourceLocation id, JsonObject json);

    /**
     * Serializes the recipe to json.
     *
     * @param instance The recipe which should be serialized.
     * @return a new {@link JsonObject} containing a json representation of the passed instance.
     */
    @Contract(value = "_->new", pure = true)
    JsonObject toJson(T instance);

    /**
     * Reads a recipe of type {@link T} from the given {@link FriendlyByteBuf} and returns it.
     *
     * @param id  The id of the recipe.
     * @param buf The {@link FriendlyByteBuf} from which the recipe should be read.
     * @return a new {@link ICRecipe} of type {@link T}.
     */
    T fromNetwork(ResourceLocation id, FriendlyByteBuf buf);

    /**
     * Serializes the recipe to json.
     *
     * @param buf      The byte buffer to which the instance data should be appended.
     * @param instance The recipe which should be serialized.
     */
    void toNetwork(FriendlyByteBuf buf, T instance);
}
