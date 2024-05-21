package com.carlschierig.immersivecrafting.impl.network;

import com.carlschierig.immersivecrafting.api.recipe.ICRecipeManager;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerPlayer;

public class S2CPacketsFabric extends S2CPackets {
    @Override
    public void sendRecipes() {
        for (var player : PLAYERS) {
            ServerPlayNetworking.send(player, new UpdateRecipesPayload(ICRecipeManager.getRecipes()));
        }
    }

    @Override
    public void trySendRecipes(ServerPlayer player) {
        if (PLAYERS.contains(player)) {
            ServerPlayNetworking.send(player, new UpdateRecipesPayload(ICRecipeManager.getRecipes()));
        }
    }
}
