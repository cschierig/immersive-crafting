package com.carlschierig.immersivecrafting.impl.network;

import com.carlschierig.immersivecrafting.api.recipe.ICRecipeManager;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.server.ServerLifecycleHooks;

public class S2CPacketsForge extends S2CPackets {
    @Override
    public void sendRecipes() {
        var list = ICRecipeManager.getRecipes();
        if (ServerLifecycleHooks.getCurrentServer() != null) {
            ICMessages.CHANNEL.send(PacketDistributor.ALL.noArg(), new RecipeMessage(list));
        }
    }

    @Override
    public void trySendRecipes(ServerPlayer player) {
        var list = ICRecipeManager.getRecipes();
        if (ServerLifecycleHooks.getCurrentServer() != null) {
            ICMessages.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new RecipeMessage(list));
        }
    }
}
