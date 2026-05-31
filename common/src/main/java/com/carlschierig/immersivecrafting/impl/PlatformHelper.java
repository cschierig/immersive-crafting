package com.carlschierig.immersivecrafting.impl;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public abstract class PlatformHelper {
    public static PlatformHelper INSTANCE;

    public abstract <T> Registry<T> createRegistry(ResourceKey<Registry<T>> registryKey);
}
