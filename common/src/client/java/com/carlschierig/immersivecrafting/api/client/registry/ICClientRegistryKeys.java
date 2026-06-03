package com.carlschierig.immersivecrafting.api.client.registry;

import com.carlschierig.immersivecrafting.api.client.render.ICRenderer;
import com.carlschierig.immersivecrafting.impl.util.ICUtil;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public class ICClientRegistryKeys {
    public static final ResourceKey<Registry<ICRenderer<?>>> RENDERABLE = createRegistryKey("renderable");

    private static <T> ResourceKey<Registry<T>> createRegistryKey(String path) {
        return ResourceKey.createRegistryKey(ICUtil.getId(path));
    }
}
