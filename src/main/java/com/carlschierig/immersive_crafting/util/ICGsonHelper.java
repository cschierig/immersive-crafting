package com.carlschierig.immersive_crafting.util;

import com.google.gson.JsonObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ItemStack;

public final class ICGsonHelper {
    private ICGsonHelper() {
    }

    public static JsonObject itemStackToJson(ItemStack stack) {
        var json = new JsonObject();
        json.addProperty("item", BuiltInRegistries.ITEM.getKey(stack.getItem()).toString());
        if (stack.getCount() > 1) {
            json.addProperty("count", stack.getCount());
        }

        return json;
    }
}
