package com.carlschierig.immersivecrafting.impl.network;

import com.carlschierig.immersivecrafting.api.recipe.ICRecipeHolder;
import com.carlschierig.immersivecrafting.impl.util.ICUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import java.util.ArrayList;
import java.util.Collection;

public record UpdateRecipesPayload(Collection<ICRecipeHolder<?>> recipes) implements CustomPacketPayload {
    public static final Type<UpdateRecipesPayload> TYPE = ICUtil.getType("update_recipes");
    public static final StreamCodec<RegistryFriendlyByteBuf, UpdateRecipesPayload> STREAM_CODEC = ICRecipeHolder.STREAM_CODEC
            .apply(ByteBufCodecs.list())
            .map(UpdateRecipesPayload::new, payload -> new ArrayList<>(payload.recipes()));

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
