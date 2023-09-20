package com.carlschierig.immersivecrafting.impl.network;

import com.carlschierig.immersivecrafting.api.serialization.ICByteBufHelper;
import com.carlschierig.immersivecrafting.impl.recipe.ICRecipeManagerImpl;
import com.carlschierig.immersivecrafting.impl.util.ICByteBufHelperImpl;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;

public class ClientPacketReciever {
    public static void receiveRecipes(Minecraft client, ClientPacketListener handler, FriendlyByteBuf buf, PacketSender responseSender) {
        var list = ICByteBufHelperImpl.readList(buf, ICByteBufHelper::readICRecipe);
        ICRecipeManagerImpl.INSTANCE.setRecipes(list);
    }
}
