package com.carlschierig.immersivecrafting.impl.network;

import com.carlschierig.immersivecrafting.api.recipe.ICRecipeManager;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

public class S2CPacketsForge extends S2CPackets {
    @Override
    public void sendRecipes() {
        var list = ICRecipeManager.getRecipes();
        if (ServerLifecycleHooks.getCurrentServer() != null) {
            PacketDistributor.sendToAllPlayers(new UpdateRecipesPayload(list));
        }
    }

    @Override
    public void trySendRecipes(ServerPlayer player) {
        var list = ICRecipeManager.getRecipes();
        if (ServerLifecycleHooks.getCurrentServer() != null) {
            PacketDistributor.sendToPlayer(player, new UpdateRecipesPayload(list));
        }
    }
}
