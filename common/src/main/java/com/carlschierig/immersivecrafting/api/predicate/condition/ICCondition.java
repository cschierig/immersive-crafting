package com.carlschierig.immersivecrafting.api.predicate.condition;

import com.carlschierig.immersivecrafting.api.context.RecipeContext;
import com.carlschierig.immersivecrafting.api.context.ValidationContext;
import com.carlschierig.immersivecrafting.api.predicate.PredicateVisitor;
import com.carlschierig.immersivecrafting.api.predicate.Visitable;
import com.carlschierig.immersivecrafting.api.registry.ICRegistries;
import com.carlschierig.immersivecrafting.api.render.ICRenderable;
import com.carlschierig.immersivecrafting.api.render.TooltipProvider;
import com.carlschierig.immersivecrafting.impl.registry.ICRegistryKeys;
import com.mojang.serialization.Codec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.function.Predicate;

/**
 * A condition which can be tested against a {@link RecipeContext}.
 * <p>
 * The condition also provides rendering methods.
 */
public interface ICCondition extends Predicate<RecipeContext>, Visitable, ICRenderable, TooltipProvider {
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

    @Override
    default void accept(PredicateVisitor visitor) {
        visitor.visitCondition(this);
    }
}
