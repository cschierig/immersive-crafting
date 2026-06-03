package com.carlschierig.immersivecrafting.api.predicate.condition;

import com.carlschierig.immersivecrafting.api.context.RecipeContext;
import com.carlschierig.immersivecrafting.api.context.ValidationContext;
import com.carlschierig.immersivecrafting.api.predicate.PredicateVisitor;
import com.carlschierig.immersivecrafting.api.predicate.Visitable;
import com.carlschierig.immersivecrafting.api.registry.ICRegistries;
import com.carlschierig.immersivecrafting.impl.predicate.ICConditionData;
import com.carlschierig.immersivecrafting.api.registry.ICRegistryKeys;
import com.mojang.serialization.Codec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * A condition which can be tested against a {@link RecipeContext}.
 * <p>
 * The condition also provides rendering and tooltip-methods.
 * These have a default implementation, but it is recommended to implement them so that
 */
public interface ICCondition extends Predicate<RecipeContext>, Visitable {
    Codec<ICCondition> CODEC = ICRegistries.CONDITION_SERIALIZER
            .byNameCodec()
            .dispatch(ICCondition::getSerializer, ICConditionSerializer::codec);

    StreamCodec<RegistryFriendlyByteBuf, ICCondition> STREAM_CODEC = ByteBufCodecs
            .registry(ICRegistryKeys.CONDITION_SERIALIZER)
            .dispatch(ICCondition::getSerializer, ICConditionSerializer::streamCodec);


    ICConditionSerializer<? extends ICCondition> getSerializer();

    /**
     * Returns a validation context specifying which context types need to be passed to the test method.
     *
     * @return a validation context specifying which context types need to be passed to the test method.
     */
    ValidationContext getRequirements();

    /**
     * Returns the name of the condition.
     *
     * @return the name of the condition.
     */
    @Nullable
    default Component getName() {
        return null;
    }

    /**
     * Returns a list of components which can be rendered as a tooltip.
     * The tooltip should explain what needs to be fulfilled for {@link #test} to return {@code true}.
     *
     * @return a list of tooltip components which can be rendered as a tooltip.
     */
    @NotNull
    @Contract("->new")
    default List<Component> getTooltip() {
        var name = getName();

        List<Component> list = new ArrayList<>();
        if (name != null) {
            list.add(Component.literal(name.getString()).withStyle(ICConditionData.CONDITION_NAME));
        }

        return list;
    }

    /**
     * Return an identifier for a renderer for the condition.
     * <p>
     * Condition icons should not be larger than a typical minecraft texture (16x16).
     */
    @Nullable
    default Identifier getRenderer() {
        return null;
    }

    @Override
    default void accept(PredicateVisitor visitor) {
        visitor.visitCondition(this);
    }
}
