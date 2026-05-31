package com.carlschierig.immersivecrafting;

import com.carlschierig.immersivecrafting.impl.network.ICMessages;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.ClientTooltipComponentCallback;

public class ImmersiveCraftingClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ICMessages.registerClientReceivers();
    }
}
