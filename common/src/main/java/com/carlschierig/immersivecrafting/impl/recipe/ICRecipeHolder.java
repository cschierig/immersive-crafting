package com.carlschierig.immersivecrafting.impl.recipe;

import com.carlschierig.immersivecrafting.api.recipe.ICRecipe;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

public record ICRecipeHolder<T extends ICRecipe>(ResourceLocation id, T recipe) {
    public static final StreamCodec<RegistryFriendlyByteBuf, ICRecipeHolder<?>> STREAM_CODEC = StreamCodec.composite(
            ResourceLocation.STREAM_CODEC,
            ICRecipeHolder::id,
            ICRecipe.STREAM_CODEC,
            ICRecipeHolder::recipe,
            ICRecipeHolder::new
    );


}
