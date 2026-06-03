package com.carlschierig.immersivecrafting.api.predicate.condition;

import com.carlschierig.immersivecrafting.api.context.RecipeContext;
import com.carlschierig.immersivecrafting.impl.util.ICTranslationHelper;
import com.carlschierig.immersivecrafting.impl.util.ICUtil;
import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * A condition which is true if and only if its child is false.
 */
public class InvertedCondition extends SingleChildICCondition {
    public static String LANGUAGE_KEY = "inverted";

    public InvertedCondition(ICCondition original) {
        super(original);
    }

    @Override
    public boolean test(RecipeContext context) {
        return !child.test(context);
    }

    @Override
    public @Nullable Identifier getRenderer() {
        return ICUtil.getId("inverted");
    }

    @Override
    public @Nullable Component getName() {
        return Component.translatable(ICTranslationHelper.translateCondition(LANGUAGE_KEY));
    }

    @Override
    public @NotNull List<Component> getTooltip() {
        var list = new ArrayList<>(super.getTooltip());
        list.add(Component.translatable(ICTranslationHelper.translateConditionDescription(LANGUAGE_KEY)));
        list.add(Component.empty());
        list.addAll(child.getTooltip());
        return list;
    }

    @Override
    public ICConditionSerializer<?> getSerializer() {
        return ICConditionSerializers.INVERT;
    }

    public static final class Serializer extends SingleChildICCondition.Serializer<InvertedCondition> {
        public static final MapCodec<InvertedCondition> CODEC = createCodec(InvertedCondition::new);
        public static final StreamCodec<RegistryFriendlyByteBuf, InvertedCondition> STREAM_CODEC = createStreamCodec(InvertedCondition::new);

        @Override
        public MapCodec<InvertedCondition> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<? super RegistryFriendlyByteBuf, InvertedCondition> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
