package com.carlschierig.immersivecrafting.impl.network;

import com.carlschierig.immersivecrafting.impl.recipe.ICRecipeManagerImpl;
import com.carlschierig.immersivecrafting.impl.util.ICUtil;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class ClientPacketReceiver {
    public static void receiveRecipes(UpdateRecipesPayload payload, ClientPlayNetworking.Context context) {
        ICUtil.LOG.warn("receiving recipes");
        ICRecipeManagerImpl.INSTANCE.setRecipes(payload.recipes());
    }
}
