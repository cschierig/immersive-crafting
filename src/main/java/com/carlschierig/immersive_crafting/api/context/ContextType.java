package com.carlschierig.immersive_crafting.api.context;

import net.minecraft.resources.ResourceLocation;

public class ContextType<T> {
    private final ResourceLocation id;

    public ContextType(ResourceLocation id) {
        this.id = id;
    }

    public ResourceLocation getId() {
        return id;
    }
}
