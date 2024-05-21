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

/**
 * A condition which is true if all of its children are true.
 */
public class AndCondition extends CompoundICCondition {
    public static final String LANGUAGE_KEY = "and";
    private final Predicate<RecipeContext> predicate;

    /**
     * Construct a new {@link AndCondition} with the given children.
     *
     * @param conditions the conditions which need to be fulfilled in order for this condition to pass.
     */
    public AndCondition(ICCondition... conditions) {
        super(conditions);
        predicate = Util.allOf(List.of(conditions));
    }

    @Override
    public ICConditionSerializer<?> getSerializer() {
        return ICConditionSerializers.AND;
    }

    @Override
    public boolean test(RecipeContext context) {
        return predicate.test(context);
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

    public static class Serializer extends CompoundICCondition.Serializer<AndCondition> {
        public static final MapCodec<AndCondition> CODEC = createCodec(AndCondition::new);
        public static final StreamCodec<RegistryFriendlyByteBuf, AndCondition> STREAM_CODEC = createStreamCodec(AndCondition::new);

        @Override
        public MapCodec<AndCondition> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, AndCondition> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
