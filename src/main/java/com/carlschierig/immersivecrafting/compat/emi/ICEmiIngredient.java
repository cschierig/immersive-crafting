package com.carlschierig.immersivecrafting.compat.emi;

import com.carlschierig.immersivecrafting.api.predicate.condition.ingredient.ICIngredient;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;

import java.util.List;

public class ICEmiIngredient implements EmiIngredient {
    private final ICIngredient ingredient;
    private long amount;
    private float chance;

    public ICEmiIngredient(ICIngredient ingredient) {
        this.ingredient = ingredient;
        amount = ingredient.getAmount();
        chance = ingredient.getChance();
    }

    @Override
    public List<EmiStack> getEmiStacks() {
        return EmiConversionUtil.convertStacks(ingredient.getParts());
    }

    @Override
    public EmiIngredient copy() {
        return new ICEmiIngredient(ingredient.copy());
    }

    @Override
    public long getAmount() {
        return amount;
    }

    @Override
    public EmiIngredient setAmount(long amount) {
        this.amount = amount;
        return this;
    }

    @Override
    public float getChance() {
        return chance;
    }

    @Override
    public EmiIngredient setChance(float chance) {
        this.chance = chance;
        return this;
    }

    @Override
    public void render(GuiGraphics draw, int x, int y, float delta, int flags) {
        ingredient.render(draw, x, y, delta, flags);
    }

    @Override
    public List<ClientTooltipComponent> getTooltip() {
        return List.of();
    }
}
