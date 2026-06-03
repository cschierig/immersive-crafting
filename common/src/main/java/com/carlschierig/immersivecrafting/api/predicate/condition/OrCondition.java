package com.carlschierig.immersivecrafting.api.predicate.condition;

import com.carlschierig.immersivecrafting.api.context.RecipeContext;
import com.carlschierig.immersivecrafting.impl.util.ICTranslationHelper;
import com.carlschierig.immersivecrafting.impl.util.ICUtil;
import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class OrCondition extends CompoundICCondition {
    public static final String LANGUAGE_KEY = "or";
    private final Predicate<RecipeContext> predicate;

    public OrCondition(ICCondition... conditions) {
        super(conditions);
        predicate = Util.anyOf(List.of(conditions));
    }

    @Override
    public @Nullable Identifier getRenderer() {
        return ICUtil.getId("text");
    }

    @Override
    public @Nullable Component getName() {
        return Component.translatable(ICTranslationHelper.translateCondition(LANGUAGE_KEY));
    }

    @Override
    public @NotNull List<Component> getTooltip() {
        var list = new ArrayList<>(super.getTooltip());
        list.add(Component.translatable(ICTranslationHelper.translateConditionDescription(LANGUAGE_KEY)));

        return list;
    }

    @Override
    public ICConditionSerializer<OrCondition> getSerializer() {
        return ICConditionSerializers.OR;
    }

    @Override
    public boolean test(RecipeContext context) {
        return predicate.test(context);
    }

    public static class Serializer extends CompoundICCondition.Serializer<OrCondition> {
        public static final MapCodec<OrCondition> CODEC = createCodec(OrCondition::new);
        public static final StreamCodec<RegistryFriendlyByteBuf, OrCondition> STREAM_CODEC = createStreamCodec(OrCondition::new);

        @Override
        public MapCodec<OrCondition> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<? super RegistryFriendlyByteBuf, OrCondition> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
