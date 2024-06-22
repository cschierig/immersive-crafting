package com.carlschierig.immersivecrafting.nf.impl.network;

import com.carlschierig.immersivecrafting.impl.network.UpdateRecipesPayload;
import com.carlschierig.immersivecrafting.impl.recipe.ICRecipeManagerImpl;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ClientPacketReciever {
    public static void receiveRecipes(UpdateRecipesPayload payload, IPayloadContext ctx) {
        ctx.enqueueWork(
                () -> ICRecipeManagerImpl.INSTANCE.setRecipes(payload.recipes())
        );
    }
}
