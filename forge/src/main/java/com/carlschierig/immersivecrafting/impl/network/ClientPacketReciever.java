package com.carlschierig.immersivecrafting.impl.network;

import com.carlschierig.immersivecrafting.api.recipe.ICRecipe;
import com.carlschierig.immersivecrafting.impl.recipe.ICRecipeManagerImpl;
import net.minecraftforge.network.NetworkEvent;

import java.util.List;
import java.util.function.Supplier;

public class ClientPacketReciever {
    public static void receiveRecipes(List<ICRecipe> recipes, Supplier<NetworkEvent.Context> ctx) {
        ICRecipeManagerImpl.INSTANCE.setRecipes(recipes);
    }
}
