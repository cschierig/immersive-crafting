package com.carlschierig.immersivecrafting;

import com.carlschierig.immersivecrafting.impl.network.ICMessages;
import net.fabricmc.api.ClientModInitializer;

public class ImmersiveCraftingClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ICMessages.registerClientReceivers();
    }
}
