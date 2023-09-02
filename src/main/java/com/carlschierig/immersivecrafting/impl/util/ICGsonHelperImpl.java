package com.carlschierig.immersivecrafting.impl.util;

import com.carlschierig.immersivecrafting.api.predicate.condition.ICCondition;
import com.carlschierig.immersivecrafting.api.predicate.condition.ICConditionSerializer;
import com.carlschierig.immersivecrafting.api.registry.ICRegistries;
import com.google.gson.JsonObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ItemStack;

public final class ICGsonHelperImpl {
    private ICGsonHelperImpl() {
    }

    public static JsonObject itemStackToJson(ItemStack stack) {
        var json = new JsonObject();
        json.addProperty("item", BuiltInRegistries.ITEM.getKey(stack.getItem()).toString());
        if (stack.getCount() > 1) {
            json.addProperty("count", stack.getCount());
        }

        return json;
    }

    @SuppressWarnings("unchecked")
    public static <T extends ICCondition> JsonObject conditionToJson(T condition) {
        final var registry = ICRegistries.CONDITION_SERIALIZER;
        var serializer = (ICConditionSerializer<T>) condition.getSerializer();
        var type = registry.getKey(serializer).toString();

        var json = serializer.toJson(condition);
        json.addProperty("type", type);

        return json;
    }
}
