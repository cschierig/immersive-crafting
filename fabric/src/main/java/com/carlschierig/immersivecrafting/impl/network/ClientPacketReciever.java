package com.carlschierig.immersivecrafting.impl.network;

import com.carlschierig.immersivecrafting.impl.recipe.ICRecipeManagerImpl;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class ClientPacketReciever {
    public static void receiveRecipes(UpdateRecipesPayload payload, ClientPlayNetworking.Context context) {
        ICRecipeManagerImpl.INSTANCE.setRecipes(payload.recipes());
    }
}
