package com.carlschierig.immersivecrafting.impl.util;

import net.minecraft.network.FriendlyByteBuf;
import org.jetbrains.annotations.ApiStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

@ApiStatus.Internal
public final class ICByteBufHelperImpl {
    private ICByteBufHelperImpl() {
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
}
