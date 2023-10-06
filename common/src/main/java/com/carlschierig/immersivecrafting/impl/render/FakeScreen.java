package com.carlschierig.immersivecrafting.impl.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.stream.Collectors;

/**
 * A fake screen for getting tooltips, mostly copied from emi
 */
public class FakeScreen extends Screen {
    public static final FakeScreen INSTANCE = new FakeScreen();

    protected FakeScreen() {
        super(Component.literal(""));
        this.minecraft = Minecraft.getInstance();
    }

    public List<ClientTooltipComponent> getTooltipFromItem(ItemStack stack) {
        return getTooltipFromItem(minecraft, stack).stream()
                .map(Component::getVisualOrderText).map(ClientTooltipComponent::create).collect(Collectors.toList());
    }
}
