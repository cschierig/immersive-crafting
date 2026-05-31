package com.carlschierig.immersivecrafting.api.recipe;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;

public record ICRecipeHolder<T extends ICRecipe>(Identifier id, T recipe) {
    public static final StreamCodec<RegistryFriendlyByteBuf, ICRecipeHolder<?>> STREAM_CODEC = StreamCodec.composite(
            Identifier.STREAM_CODEC,
            ICRecipeHolder::id,
            ICRecipe.STREAM_CODEC,
            ICRecipeHolder::recipe,
            ICRecipeHolder::new
    );


}
