package com.carlschierig.immersivecrafting.impl.recipe;

import com.carlschierig.immersivecrafting.impl.util.ICUtil;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

@ApiStatus.Internal
public final class RecipeReloaderFabric extends RecipeReloader implements SimpleSynchronousResourceReloadListener {
    @Override
    public @NotNull ResourceLocation getFabricId() {
        return new ResourceLocation(ICUtil.MODID, "recipe_reloader");
    }

}
