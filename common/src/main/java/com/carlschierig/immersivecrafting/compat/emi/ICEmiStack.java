package com.carlschierig.immersivecrafting.compat.emi;

import com.carlschierig.immersivecrafting.api.predicate.condition.ingredient.ICStack;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class ICEmiStack extends EmiStack {
    private final ICStack stack;

    public ICEmiStack(ICStack stack) {
        this.stack = stack;
    }


    @Override
    public EmiStack copy() {
        return new ICEmiStack(stack.copy());
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
    public List<ClientTooltipComponent> getTooltip() {
        List<ClientTooltipComponent> list = new ArrayList<>();
        list.addAll(stack.getTooltip());
        list.addAll(super.getTooltip());
        return list;
    }

    @Override
    public Component getName() {
        return stack.getName();
    }
}
