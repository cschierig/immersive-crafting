package com.carlschierig.immersivecrafting.predicate.condition;

import com.carlschierig.immersivecrafting.ImmersiveCrafting;
import com.carlschierig.immersivecrafting.api.predicate.ICPredicate;
import com.carlschierig.immersivecrafting.api.predicate.condition.ICCondition;
import com.carlschierig.immersivecrafting.api.predicate.condition.ICConditionSerializer;
import com.carlschierig.immersivecrafting.api.registry.ICRegistries;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

public final class ICConditionSerializers {
    public static final ICConditionSerializer<ICPredicate> PREDICATE = register("predicate", new ICPredicate.Serializer());
    public static final ICConditionSerializer<OrCondition> OR = register("or", new OrCondition.Serializer());
    public static final ICConditionSerializer<AndCondition> AND = register("and", new AndCondition.Serializer());
    public static final ICConditionSerializer<BlockCondition> BLOCK = register("block", new BlockCondition.Serializer());
    public static final ICConditionSerializer<InvertedCondition> INVERT = register("invert", new InvertedCondition.Serializer());
    public static final ICConditionSerializer<DayTimeCondition> DAY_TIME = register("day_time", new DayTimeCondition.Serializer());

    private static <T extends ICCondition> ICConditionSerializer<T> register(String id, ICConditionSerializer<T> serializer) {
        return Registry.register(ICRegistries.CONDITION_SERIALIZER, new ResourceLocation(ImmersiveCrafting.MODID, id), serializer);
    }

    public static ICCondition[] fromJson(JsonObject json) throws JsonParseException {
        ICCondition[] conditions = new ICCondition[json.entrySet().size()];

        int i = 0;
        for (var condition : json.entrySet()) {
            if (!condition.getValue().isJsonObject()) {
                throw new JsonParseException("Conditions must be objects.");
            }
            var name = new ResourceLocation(condition.getKey());
            var value = condition.getValue().getAsJsonObject();

            // parse
            conditions[i] = fromJson(name, value);

            i++;
        }

        return conditions;
    }

    public static ICCondition fromJson(ResourceLocation serializer, JsonObject json) {
        return ICRegistries.CONDITION_SERIALIZER.get(serializer).fromJson(json);
    }

    public static JsonObject toJson(ICCondition[] conditions) {
        var json = new JsonObject();
        for (var condition : conditions) {
            addCondition(json, condition);
        }
        return json;
    }

    @SuppressWarnings("unchecked")
    public static <T extends ICCondition> void addCondition(JsonObject json, T condition) {
        final var registry = ICRegistries.CONDITION_SERIALIZER;
        var serializer = (ICConditionSerializer<T>) condition.getSerializer();
        json.add(registry.getKey(serializer).toString(), serializer.toJson(condition));
    }
}
