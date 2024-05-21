package com.carlschierig.immersivecrafting.api.predicate.condition;

import com.carlschierig.immersivecrafting.api.context.RecipeContext;
import com.carlschierig.immersivecrafting.impl.util.ICTranslationHelper;
import com.mojang.serialization.MapCodec;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTextTooltip;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
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
    public void render(@NotNull GuiGraphics draw, int x, int y, float delta) {
        draw.drawString(Minecraft.getInstance().font, getName().getString().toUpperCase(), 0, 0, 0xffffffff);
    }

    @Override
    public @Nullable Component getName() {
        return Component.translatable(ICTranslationHelper.translateCondition(LANGUAGE_KEY));
    }

    @Override
    public @NotNull List<ClientTooltipComponent> getTooltip() {
        List<ClientTooltipComponent> list = new ArrayList<>(super.getTooltip());
        list.add(new ClientTextTooltip(
                Component.translatable(ICTranslationHelper.translateConditionDescription(LANGUAGE_KEY))
                        .getVisualOrderText()
        ));

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
