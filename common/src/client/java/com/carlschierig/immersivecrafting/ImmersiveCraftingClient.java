package com.carlschierig.immersivecrafting;

import com.carlschierig.immersivecrafting.impl.client.predicate.PredicateRenderers;
import com.carlschierig.immersivecrafting.impl.client.render.FakeScreen;

public class ImmersiveCraftingClient {
    public static void init() {
        PredicateRenderers.init();
    }

    public static void lateInit() {
        new FakeScreen();
    }
}
