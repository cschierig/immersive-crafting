package com.carlschierig.immersivecrafting.api.predicate.condition;

import com.carlschierig.immersivecrafting.api.context.RecipeContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditions;
import org.jetbrains.annotations.NotNull;
import org.quiltmc.loader.api.minecraft.ClientOnly;

import java.util.function.Predicate;

public class OrCondition extends CompoundICCondition {
    private final Predicate<RecipeContext> predicate;

    public OrCondition(ICCondition... conditions) {
        super(conditions);
        predicate = LootItemConditions.orConditions(conditions);
    }

    @ClientOnly
    @Override
    public void render(@NotNull GuiGraphics draw, int x, int y, float delta) {
        draw.drawString(Minecraft.getInstance().font, "or", 0, 0, 0xffffffff);
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
        @Override
        protected OrCondition create(ICCondition[] conditions) {
            return new OrCondition(conditions);
        }
    }
}
