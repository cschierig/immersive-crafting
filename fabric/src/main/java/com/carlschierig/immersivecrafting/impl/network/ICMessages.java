package com.carlschierig.immersivecrafting.impl.network;

import com.carlschierig.immersivecrafting.impl.util.ICUtil;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

import java.util.List;

public class ICMessages {
    public static final ResourceLocation UPDATE = ICUtil.getId("update_recipes");

    public static void registerClientReceivers() {
        ClientPlayNetworking.registerGlobalReceiver(UPDATE, ClientPacketReciever::receiveRecipes);
    }

    public static void registerPlayer(ServerGamePacketListenerImpl handler, PacketSender sender, MinecraftServer server, List<ResourceLocation> channels) {
        if (channels.contains(UPDATE)) {
            S2CPacketsFabric.PLAYERS.add(handler.player);
        }
    }

    public static void unregisterPlayer(ServerGamePacketListenerImpl handler, MinecraftServer server) {
        S2CPacketsFabric.PLAYERS.remove(handler.player);
    }
}
