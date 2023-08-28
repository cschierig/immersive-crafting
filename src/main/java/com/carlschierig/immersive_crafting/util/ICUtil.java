package com.carlschierig.immersive_crafting.util;

import com.carlschierig.immersive_crafting.ImmersiveCrafting;
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