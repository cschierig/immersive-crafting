package com.carlschierig.immersivecrafting.compat.emi;

import com.carlschierig.immersivecrafting.api.predicate.condition.ingredient.ICStack;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class Stack extends EmiStack {
    private final ICStack stack;

    public Stack(ICStack stack) {
        this.stack = stack;
    }


    @Override
    public EmiStack copy() {
        return new Stack(stack.copy());
    }

    @Override
    public void render(GuiGraphics draw, int x, int y, float delta, int flags) {
        stack.render(draw, x, y, delta);
    }

    @Override
    public boolean isEmpty() {
        return stack.isEmpty();
    }

    @Override
    public CompoundTag getNbt() {
        return null;
    }

    @Override
    public Object getKey() {
        return stack.getKey();
    }

    @Override
    public ResourceLocation getId() {
        return stack.getIdentifier();
    }

    @Override
    public List<Component> getTooltipText() {
        return List.of();
    }

    @Override
    public Component getName() {
        return stack.getName();
    }
}
