package com.carlschierig.immersivecrafting.api.predicate.condition;

import com.carlschierig.immersivecrafting.api.context.RecipeContext;
import com.carlschierig.immersivecrafting.impl.util.ICTranslationHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTextTooltip;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditions;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.quiltmc.loader.api.minecraft.ClientOnly;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class OrCondition extends CompoundICCondition {
    public static final String LANGUAGE_KEY = "or";
    private final Predicate<RecipeContext> predicate;

    public OrCondition(ICCondition... conditions) {
        super(conditions);
        predicate = LootItemConditions.orConditions(conditions);
    }

    @ClientOnly
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
        @Contract("_->new")
        @Override
        protected OrCondition create(ICCondition[] conditions) {
            return new OrCondition(conditions);
        }
    }
}
