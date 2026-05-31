package com.carlschierig.immersivecrafting.impl;

import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public class FabricPlatformHelper extends PlatformHelper {
    public FabricPlatformHelper() {
        INSTANCE = this;
    }

    @Override
    public <T> Registry<T> createRegistry(ResourceKey<Registry<T>> registryKey) {
        return FabricRegistryBuilder.create(registryKey).attribute(RegistryAttribute.OPTIONAL).buildAndRegister();
    }
}
