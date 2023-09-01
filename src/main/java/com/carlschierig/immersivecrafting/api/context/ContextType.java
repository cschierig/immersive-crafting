package com.carlschierig.immersivecrafting.api.context;

import net.minecraft.resources.ResourceLocation;

/**
 * Used as a key for objects in {@link RecipeContext}s.
 * Each context type corresponds to one object of type {@link T}.
 *
 * @param id  The id of the type.
 * @param <T> The type of object this type corresponds to.
 */
public record ContextType<T>(ResourceLocation id) {
    @Override
    public String toString() {
        return "<context type " + id.toString() + ">";
    }
}
