package com.carlschierig.immersivecrafting.impl;

import com.carlschierig.immersivecrafting.impl.PlatformHelper;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;

import java.util.ArrayList;
import java.util.List;

public class NeoPlatformHelper extends PlatformHelper {
    private final List<Registry<?>> registries = new ArrayList<>();

    public NeoPlatformHelper() {
        INSTANCE = this;
    }

    @Override
    public <T> Registry<T> createRegistry(ResourceKey<Registry<T>> registryKey) {
        var registry = new RegistryBuilder<>(registryKey).sync(true).create();
        registries.add(registry);
        return registry;
    }

    @Override
    public <T> Registry<T> createDefaultedRegistry(ResourceKey<Registry<T>> registryKey, Identifier defaultId) {
        var registry = new RegistryBuilder<>(registryKey).sync(true).defaultKey(defaultId).create();
        registries.add(registry);
        return registry;
    }

    @SubscribeEvent
    public void registerRegistries(NewRegistryEvent event) {
        for (var registry : registries) {
            event.register(registry);
        }
    }
}
