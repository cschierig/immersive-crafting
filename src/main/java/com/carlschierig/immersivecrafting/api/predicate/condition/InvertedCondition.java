package com.carlschierig.immersivecrafting.api.predicate.condition;

import com.carlschierig.immersivecrafting.api.context.RecipeContext;
import com.carlschierig.immersivecrafting.impl.render.ICRenderHelper;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;


public class InvertedCondition extends SingleChildICCondition {
    public InvertedCondition(ICCondition original) {
        super(original);
    }

    @Override
    public boolean test(RecipeContext context) {
        return !child.test(context);
    }

    @Override
    public void render(GuiGraphics draw, int x, int y, float delta) {
        super.render(draw, x, y, delta);
        ICRenderHelper.renderItemAnnotation(draw, 0, 0, Component.literal("!"));
    }

    @Override
    public ICConditionSerializer<?> getSerializer() {
        return ICConditionSerializers.INVERT;
    }

    public static final class Serializer extends SingleChildICCondition.Serializer<InvertedCondition> {
        @Override
        protected InvertedCondition create(ICCondition conditions) {
            return new InvertedCondition(conditions);
        }
    }
}
