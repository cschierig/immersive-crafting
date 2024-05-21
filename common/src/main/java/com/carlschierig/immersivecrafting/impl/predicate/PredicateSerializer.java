package com.carlschierig.immersivecrafting.impl.predicate;

import com.mojang.serialization.Codec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public interface PredicateSerializer<T> {
    Codec<T> codec();

    StreamCodec<RegistryFriendlyByteBuf, T> streamCodec();
}
