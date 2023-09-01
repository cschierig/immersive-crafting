package com.carlschierig.immersivecrafting.util;

import com.carlschierig.immersivecrafting.api.predicate.condition.ICCondition;
import com.carlschierig.immersivecrafting.api.predicate.condition.ICConditionSerializer;
import com.carlschierig.immersivecrafting.api.registry.ICRegistries;
import net.minecraft.network.FriendlyByteBuf;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

public final class ICByteBufHelper {
    private ICByteBufHelper() {
    }

    public static <T> List<T> readList(FriendlyByteBuf buf, Function<FriendlyByteBuf, T> deserializer) {
        int count = buf.readInt();
        List<T> items = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            items.add(deserializer.apply(buf));
        }
        return items;
    }

    public static <T> void writeList(FriendlyByteBuf buf, List<T> list, BiConsumer<FriendlyByteBuf, T> serializer) {
        buf.writeInt(list.size());

        for (var item : list) {
            serializer.accept(buf, item);
        }
    }

    public static ICCondition readICCondition(FriendlyByteBuf buf) {
        var id = buf.readResourceLocation();

        return ICRegistries.CONDITION_SERIALIZER.get(id).fromNetwork(buf);
    }

    @SuppressWarnings("unchecked")
    public static <T extends ICCondition> void writeICCondition(FriendlyByteBuf buf, T condition) {
        buf.writeResourceLocation(ICRegistries.CONDITION_SERIALIZER.getKey(condition.getSerializer()));

        ((ICConditionSerializer<T>) condition.getSerializer()).toNetwork(buf, condition);
    }
}
