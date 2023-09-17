package com.carlschierig.immersivecrafting.impl.network;

import com.carlschierig.immersivecrafting.api.recipe.ICRecipeManager;
import com.carlschierig.immersivecrafting.api.serialization.ICByteBufHelper;
import com.carlschierig.immersivecrafting.impl.util.ICByteBufHelperImpl;
import net.minecraft.server.level.ServerPlayer;
import org.quiltmc.qsl.networking.api.PacketByteBufs;
import org.quiltmc.qsl.networking.api.ServerPlayNetworking;

import java.util.HashSet;
import java.util.Set;

public class S2CPackets {
    public static final Set<ServerPlayer> PLAYERS = new HashSet<>();

    public static void sendRecipes() {
        var buf = PacketByteBufs.create();
        ICByteBufHelperImpl.writeList(buf, ICRecipeManager.getRecipes(), ICByteBufHelper::writeICRecipe);
        for (var player : PLAYERS) {
            ServerPlayNetworking.send(player, ICMessages.UPDATE, buf);
        }
    }

    public static void trySendRecipes(ServerPlayer player) {
        if (PLAYERS.contains(player)) {
            var buf = PacketByteBufs.create();
            ICByteBufHelperImpl.writeList(buf, ICRecipeManager.getRecipes(), ICByteBufHelper::writeICRecipe);
            ServerPlayNetworking.send(player, ICMessages.UPDATE, buf);
        }
    }
}
