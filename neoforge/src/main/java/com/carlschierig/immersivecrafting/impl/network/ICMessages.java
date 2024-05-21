package com.carlschierig.immersivecrafting.impl.network;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class ICMessages {
    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event) {
        // Sets the current network version
        final PayloadRegistrar registrar = event.registrar("1").optional();
        registrar.playToClient(
                UpdateRecipesPayload.TYPE,
                UpdateRecipesPayload.STREAM_CODEC,
                ClientPacketReciever::receiveRecipes
        );
    }
}
