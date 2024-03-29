package com.carlschierig.immersivecrafting.api.serialization;

import com.carlschierig.immersivecrafting.api.predicate.condition.ICCondition;
import com.carlschierig.immersivecrafting.api.predicate.condition.ingredient.ICIngredient;
import com.carlschierig.immersivecrafting.api.predicate.condition.ingredient.ICStack;
import com.carlschierig.immersivecrafting.api.registry.ICRegistries;
import com.carlschierig.immersivecrafting.impl.util.ICGsonHelperImpl;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;

import java.util.Collection;

/**
 * Provides helper methods for json (de)serialization of Immersive Crafting objects.
 */
public class ICGsonHelper {
    /**
     * Deserializes a json object as a {@link ICCondition } array.
     *
     * @param json A serialized {@link ICCondition} array.
     * @return An {@link ICCondition} array based on the given json object.
     * @throws JsonParseException if elements of the given json object aren't themselves json objects.
     */
    public static ICCondition[] getAsConditions(JsonArray json) throws JsonParseException {
        ICCondition[] conditions = new ICCondition[json.size()];

        int i = 0;
        for (var condition : json) {
            if (!condition.isJsonObject()) {
                throw new JsonParseException("Conditions must be objects.");
            }
            var value = condition.getAsJsonObject();

            // parse
            conditions[i] = getAsCondition(value);

            i++;
        }

        return conditions;
    }

    /**
     * Deserializes the json object as a condition.
     *
     * @param json A serialized {@link ICCondition}.
     * @return An {@link ICCondition} based on the given json object.
     */
    public static ICCondition getAsCondition(JsonObject json) {
        var serializer = new ResourceLocation(GsonHelper.getAsString(json, "type"));
        return ICRegistries.CONDITION_SERIALIZER.get(serializer).fromJson(json);
    }

    /**
     * Deserializes the json object as an ingredient.
     *
     * @param json A serialized {@link ICIngredient}.
     * @return An {@link ICIngredient} based on the given json object.
     */
    public static ICIngredient getAsIngredient(JsonObject json) {
        var result = getAsCondition(json);
        if (result instanceof ICIngredient) {
            return (ICIngredient) result;
        }
        throw new JsonSyntaxException("Json object must be an ingredient");
    }

    /**
     * Deserializes the json object as a stack.
     *
     * @param json A serialized {@link ICStack}.
     * @return An {@link ICStack} based on the given json object.
     */
    public static ICStack getAsStack(JsonObject json) {
        var result = getAsCondition(json);
        if (result instanceof ICStack) {
            return (ICStack) result;
        }
        throw new JsonSyntaxException("Json object must be a stack");
    }

    /**
     * Serializes the given {@link ICCondition} array.
     *
     * @param conditions The conditions array to serialize.
     * @return a json array containing the conditions
     */
    public static JsonArray conditionsToJson(Collection<? extends ICCondition> conditions) {
        var json = new JsonArray();
        for (var condition : conditions) {
            json.add(conditionToJson(condition));
        }
        return json;
    }

    /**
     * Serializes the given {@link ICCondition} array.
     *
     * @param conditions The conditions array to serialize.
     * @return a json array containing the conditions
     */
    public static JsonArray conditionsToJson(ICCondition[] conditions) {
        var json = new JsonArray();
        for (var condition : conditions) {
            json.add(conditionToJson(condition));
        }
        return json;
    }

    /**
     * Serializes the given {@link ICCondition}.
     *
     * @param condition The condition to serialize.
     * @return a json object containing the serialized condition.
     */
    public static JsonObject conditionToJson(ICCondition condition) {
        return ICGsonHelperImpl.conditionToJson(condition);
    }
}
