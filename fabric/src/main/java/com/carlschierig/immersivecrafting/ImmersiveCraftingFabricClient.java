package com.carlschierig.immersivecrafting;

import com.carlschierig.immersivecrafting.impl.network.ICMessages;
import net.fabricmc.api.ClientModInitializer;

public class ImmersiveCraftingFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ImmersiveCraftingClient.init();
        ImmersiveCraftingClient.lateInit();
        ICMessages.registerClientReceivers();
    }
}
