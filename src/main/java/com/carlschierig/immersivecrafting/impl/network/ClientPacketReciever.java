package com.carlschierig.immersivecrafting.impl.network;

import com.carlschierig.immersivecrafting.api.serialization.ICByteBufHelper;
import com.carlschierig.immersivecrafting.impl.recipe.RecipeReloader;
import com.carlschierig.immersivecrafting.impl.util.ICByteBufHelperImpl;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;
import org.quiltmc.qsl.networking.api.PacketSender;

public class ClientPacketReciever {
    public static void receiveRecipes(Minecraft client, ClientPacketListener handler, FriendlyByteBuf buf, PacketSender responseSender) {
        var list = ICByteBufHelperImpl.readList(buf, ICByteBufHelper::readICRecipe);
        RecipeReloader.setRecipes(list);
    }
}
