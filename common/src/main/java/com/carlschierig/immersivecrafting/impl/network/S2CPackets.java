package com.carlschierig.immersivecrafting.impl.network;

import net.minecraft.server.level.ServerPlayer;

import java.util.HashSet;
import java.util.Set;

public abstract class S2CPackets {
    public static final Set<ServerPlayer> PLAYERS = new HashSet<>();
    public static S2CPackets INSTANCE;

    public abstract void sendRecipes();

    public abstract void trySendRecipes(ServerPlayer player);
}
