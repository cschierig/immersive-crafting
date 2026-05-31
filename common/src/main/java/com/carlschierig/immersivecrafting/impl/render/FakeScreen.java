package com.carlschierig.immersivecrafting.impl.render;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.List;

/**
 * A fake screen for getting tooltips, mostly copied from emi
 */
public class FakeScreen extends Screen {
    public static final FakeScreen INSTANCE = new FakeScreen();

    protected FakeScreen() {
        super(Component.literal(""));
    }

    public List<Component> getTooltipFromItem(ItemStack stack) {
        return getTooltipFromItem(minecraft, stack);
    }
}
