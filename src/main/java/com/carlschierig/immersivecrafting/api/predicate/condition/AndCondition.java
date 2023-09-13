package com.carlschierig.immersivecrafting.api.predicate.condition;

import com.carlschierig.immersivecrafting.api.context.RecipeContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditions;

import java.util.function.Predicate;

public class AndCondition extends CompoundICCondition {
    private final Predicate<RecipeContext> predicate;

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
        draw.drawString(Minecraft.getInstance().font, "and", 0, 0, 0xffffffff);
    }

    public static class Serializer extends CompoundICCondition.Serializer<AndCondition> {
        @Override
        protected AndCondition create(ICCondition[] conditions) {
            return new AndCondition(conditions);
        }
    }
}
