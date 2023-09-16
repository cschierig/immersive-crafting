package com.carlschierig.immersivecrafting.api.predicate.condition;

import com.carlschierig.immersivecrafting.api.context.RecipeContext;
import com.carlschierig.immersivecrafting.api.data.ICTranslationHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTextTooltip;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * A condition which is true if all of its children are true.
 */
public class AndCondition extends CompoundICCondition {
    private final Predicate<RecipeContext> predicate;

    /**
     * Construct a new {@link AndCondition} with the given children.
     *
     * @param conditions the conditions which need to be fulfilled in order for this condition to pass.
     */
    public AndCondition(ICCondition... conditions) {
        super(conditions);
        predicate = LootItemConditions.andConditions(conditions);
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
    public void render(GuiGraphics draw, int x, int y, float delta) {
        draw.drawString(Minecraft.getInstance().font, getName().getString().toUpperCase(), 0, 0, 0xffffffff);
    }

    @Override
    public @Nullable Component getName() {
        return Component.translatable(ICTranslationHelper.translateCondition("andCondition"));
    }

    @Override
    public @NotNull List<ClientTooltipComponent> getTooltip() {
        List<ClientTooltipComponent> list = new ArrayList<>();

        list.addAll(super.getTooltip());
        list.add(new ClientTextTooltip(FormattedCharSequence.forward("All sub conditions must be fulfilled.", Style.EMPTY)));

        return list;
    }

    public static class Serializer extends CompoundICCondition.Serializer<AndCondition> {
        @Override
        protected AndCondition create(ICCondition[] conditions) {
            return new AndCondition(conditions);
        }
    }
}
