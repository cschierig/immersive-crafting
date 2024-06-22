package com.carlschierig.immersivecrafting.nf.impl.network;

import com.carlschierig.immersivecrafting.api.recipe.ICRecipeManager;
import com.carlschierig.immersivecrafting.impl.network.S2CPackets;
import com.carlschierig.immersivecrafting.impl.network.UpdateRecipesPayload;
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
