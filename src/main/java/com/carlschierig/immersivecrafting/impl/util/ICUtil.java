package com.carlschierig.immersivecrafting.impl.util;

import com.carlschierig.immersivecrafting.ImmersiveCrafting;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public final class ICUtil {
    private ICUtil() {
    }

    public static ResourceLocation createResourceLocation(String value) {
        return new ResourceLocation(ImmersiveCrafting.MODID, value);
    }
}
