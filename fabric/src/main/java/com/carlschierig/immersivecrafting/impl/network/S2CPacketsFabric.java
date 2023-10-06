package com.carlschierig.immersivecrafting.impl.network;

import com.carlschierig.immersivecrafting.api.recipe.ICRecipeManager;
import com.carlschierig.immersivecrafting.api.serialization.ICByteBufHelper;
import com.carlschierig.immersivecrafting.impl.util.ICByteBufHelperImpl;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerPlayer;

public class S2CPacketsFabric extends S2CPackets {
    @Override
    public void sendRecipes() {
        var buf = PacketByteBufs.create();
        ICByteBufHelperImpl.writeList(buf, ICRecipeManager.getRecipes(), ICByteBufHelper::writeICRecipe);
        for (var player : PLAYERS) {
            ServerPlayNetworking.send(player, ICMessages.UPDATE, buf);
        }
    }

    @Override
    public void trySendRecipes(ServerPlayer player) {
        if (PLAYERS.contains(player)) {
            var buf = PacketByteBufs.create();
            ICByteBufHelperImpl.writeList(buf, ICRecipeManager.getRecipes(), ICByteBufHelper::writeICRecipe);
            ServerPlayNetworking.send(player, ICMessages.UPDATE, buf);
        }
    }
}
