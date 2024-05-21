package com.carlschierig.immersivecrafting.api.predicate.condition;

import com.carlschierig.immersivecrafting.api.context.ValidationContext;
import com.carlschierig.immersivecrafting.api.predicate.PredicateVisitor;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

/**
 * This condition should be used for conditions which have a single child.
 */
public abstract class SingleChildICCondition implements ICCondition {
    protected final ICCondition child;

    protected SingleChildICCondition(ICCondition child) {
        this.child = child;
    }

    @Override
    public void render(@NotNull GuiGraphics draw, int x, int y, float delta) {
        child.render(draw, x, y, delta);
    }

    @Override
    public ValidationContext getRequirements() {
        return child.getRequirements();
    }

    @Override
    public void accept(PredicateVisitor visitor) {
        visitor.visitSingleChildCondition(this);
    }

    public ICCondition getChild() {
        return child;
    }

    public static abstract class Serializer<T extends SingleChildICCondition> implements ICConditionSerializer<T> {
        /**
         * Create a codec for a single child condition using the given factory.
         *
         * @param factory Function to create a new single child condition of type {@link T} using the given {@link ICCondition[]}.
         * @return a new codec for (de)serializing conditions of type {@link T}.
         */
        protected static <T extends SingleChildICCondition> MapCodec<T> createCodec(Function<@NotNull ICCondition, T> factory) {
            return ICCondition.CODEC.fieldOf("condition").xmap(
                    factory,
                    con -> con.child
            );
        }

        /**
         * Create a stream codec for a single child condition using the given factory.
         *
         * @param factory Function to create a new single child condition of type {@link T} using the given {@link ICCondition[]}.
         * @return a new stream codec for (de)serializing conditions of type {@link T}.
         */
        protected static <T extends SingleChildICCondition> StreamCodec<RegistryFriendlyByteBuf, T> createStreamCodec(Function<@NotNull ICCondition, T> factory) {
            return ICCondition.STREAM_CODEC.map(
                    factory,
                    condition -> condition.child
            );
        }
    }
}
