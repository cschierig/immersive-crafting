package com.carlschierig.immersivecrafting.api.predicate.condition;

import com.carlschierig.immersivecrafting.api.context.RecipeContext;
import com.carlschierig.immersivecrafting.impl.render.ICRenderHelper;
import com.carlschierig.immersivecrafting.impl.util.ICTranslationHelper;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTextTooltip;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.FormattedCharSequence;
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
    public void render(@NotNull GuiGraphics draw, int x, int y, float delta) {
        super.render(draw, x, y, delta);
        ICRenderHelper.renderItemAnnotation(draw, 0, 0, Component.literal("!"));
    }

    @Override
    public @Nullable Component getName() {
        return Component.translatable(ICTranslationHelper.translateCondition(LANGUAGE_KEY));
    }

    @Override
    public @NotNull List<ClientTooltipComponent> getTooltip() {
        List<ClientTooltipComponent> list = new ArrayList<>(super.getTooltip());
        list.add(new ClientTextTooltip(
                Component.translatable(ICTranslationHelper.translateConditionDescription(LANGUAGE_KEY)).getVisualOrderText()));
        list.add(new ClientTextTooltip(FormattedCharSequence.EMPTY));
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
