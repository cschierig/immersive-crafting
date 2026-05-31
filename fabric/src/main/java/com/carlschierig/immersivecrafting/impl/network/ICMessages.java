package com.carlschierig.immersivecrafting.impl.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

import java.util.List;

public class ICMessages {
    public static void registerClientReceivers() {
        ClientPlayNetworking.registerGlobalReceiver(UpdateRecipesPayload.TYPE, ClientPacketReciever::receiveRecipes);
    }

    public static void registerPayloadsClientbound() {
        PayloadTypeRegistry.clientboundPlay().register(UpdateRecipesPayload.TYPE, UpdateRecipesPayload.STREAM_CODEC);
    }

    public static void registerPlayer(ServerGamePacketListenerImpl handler, PacketSender sender, MinecraftServer server, List<Identifier> channels) {
        // TODO: check if this still works
        if (channels.contains(UpdateRecipesPayload.TYPE.id())) {
            S2CPacketsFabric.PLAYERS.add(handler.player);
        }
    }

    public static void unregisterPlayer(ServerGamePacketListenerImpl handler, MinecraftServer server) {
        S2CPacketsFabric.PLAYERS.remove(handler.player);
    }
}
