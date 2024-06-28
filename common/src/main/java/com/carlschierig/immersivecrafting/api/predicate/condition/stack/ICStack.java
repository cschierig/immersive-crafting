package com.carlschierig.immersivecrafting.api.predicate.condition.stack;

import com.carlschierig.immersivecrafting.api.context.RecipeContext;
import com.carlschierig.immersivecrafting.api.predicate.condition.ICCondition;
import com.carlschierig.immersivecrafting.api.predicate.condition.ICConditionSerializer;
import com.carlschierig.immersivecrafting.api.render.ICFlagRenderable;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

/**
 * A stack is something that can be crafted and spawned into the world.
 */
public abstract class ICStack implements ICCondition, ICFlagRenderable {
    /**
     * A variant of {@link ICCondition#CODEC} which ensures the value is an {@link ICStack}.
     */
    public static final Codec<ICStack> CODEC = ICCondition.CODEC.comapFlatMap(
            condition -> {
                if (condition instanceof ICStack stack) {
                    return DataResult.success(stack);
                } else {
                    return DataResult.error(() -> "Value must be a stack.");
                }
            },
            stack -> stack
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, ICStack> STREAM_CODEC = ICCondition.STREAM_CODEC.map(
            condition -> (ICStack) condition,
            ingredient -> ingredient
    );

    /**
     * Returns the identifier of the underlying resource.
     *
     * @return the identifier of the underlying resource.
     */
    public abstract ResourceLocation getIdentifier();

    public abstract ICStack copy();

    /**
     * Used by recipe viewers to determine for which items recipes need to be shown.
     *
     * @return The resource which is represented by this stack.
     */
    public abstract Object getKey();

    /**
     * Craft the stack.
     *
     * @param recipeContext contains information of the surroundings.
     */
    public abstract void craft(RecipeContext recipeContext);

    /**
     * Returns whether this stack is empty.
     *
     * @return {@code true} if this stack is empty, {@code false} otherwise.
     */
    public abstract boolean isEmpty();

    public abstract ICConditionSerializer<? extends ICStack> getSerializer();
}
