package com.carlschierig.immersivecrafting.api.predicate.condition;

import com.carlschierig.immersivecrafting.api.context.ValidationContext;
import com.carlschierig.immersivecrafting.api.predicate.PredicateVisitor;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * A condition which consists of multiple child conditions.
 * Subclasses can also use the {@link CompoundICCondition.Serializer} serializer.
 */
public abstract class CompoundICCondition implements ICCondition {
    protected final ICCondition[] conditions;

    /**
     * create a new compound condition from the given child conditions.
     *
     * @param conditions The children of the compound conditions.
     */
    public CompoundICCondition(ICCondition[] conditions) {
        this.conditions = conditions;
    }

    @Override
    public final ValidationContext getRequirements() {
        return ValidationContext.merge(Arrays.stream(conditions).map(ICCondition::getRequirements).collect(Collectors.toList()));
    }

    public ICCondition[] getChildren() {
        return conditions;
    }

    @Override
    public final void accept(PredicateVisitor visitor) {
        visitor.visitCompound(this);
    }

    /**
     * A template {@link ICConditionSerializer} for {@link CompoundICCondition}s.
     * Provides factory methods for creating codecs and stream codecs.
     *
     * @param <T> The type of condition for which the serializer is used.
     */
    public static abstract class Serializer<T extends CompoundICCondition> implements ICConditionSerializer<T> {
        /**
         * Create a codec for a compound ic condition using the given factory.
         *
         * @param factory Function to create a new compound condition of type {@link T} using the given {@link ICCondition[]}.
         * @return a new codec for (de)serializing conditions of type {@link T}.
         */
        protected static <T extends CompoundICCondition> MapCodec<T> createCodec(Function<@NotNull ICCondition[], T> factory) {
            return Codec.list(ICCondition.CODEC).fieldOf("conditions").xmap(
                    list -> factory.apply(list.toArray(ICCondition[]::new)),
                    condition -> Arrays.asList(condition.conditions)
            );
        }

        /**
         * Create a stream codec for a compound ic condition using the given factory.
         *
         * @param factory Function to create a new compound condition of type {@link T} using the given {@link ICCondition[]}.
         * @return a new stream codec for (de)serializing conditions of type {@link T}.
         */
        protected static <T extends CompoundICCondition> StreamCodec<RegistryFriendlyByteBuf, T> createStreamCodec(Function<@NotNull ICCondition[], T> factory) {
            return ICCondition.STREAM_CODEC.apply(ByteBufCodecs.list()).map(
                    list -> factory.apply(list.toArray(ICCondition[]::new)),
                    condition -> Arrays.asList(condition.conditions)
            );
        }
    }
}
