package com.carlschierig.immersivecrafting;

import com.carlschierig.immersivecrafting.impl.network.ICMessages;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;

public class ImmersiveCraftingClient implements ClientModInitializer {
    @Override
    public void onInitializeClient(ModContainer mod) {
        ICMessages.registerClientReceivers();
    }
}
