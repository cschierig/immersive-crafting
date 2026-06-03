package com.carlschierig.immersivecrafting.api.client.registry;

import com.carlschierig.immersivecrafting.api.client.render.ICRenderer;
import com.carlschierig.immersivecrafting.impl.PlatformHelper;
import com.carlschierig.immersivecrafting.impl.util.ICUtil;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

public class ICClientRegistries {
    public static final Registry<ICRenderer<?>> RENDERABLE = createDefaultedRegistry(ICClientRegistryKeys.RENDERABLE, ICUtil.getId("unknown"));

    private static <T> Registry<T> createDefaultedRegistry(ResourceKey<Registry<T>> registryKey, Identifier defaultId) {
        return PlatformHelper.INSTANCE.createDefaultedRegistry(registryKey, defaultId);
    }
}
