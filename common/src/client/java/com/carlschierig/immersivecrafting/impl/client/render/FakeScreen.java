package com.carlschierig.immersivecrafting.impl.client.render;

import com.carlschierig.immersivecrafting.impl.util.ICUtil;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.List;

/**
 * A fake screen for getting tooltips, mostly copied from emi
 */
public class FakeScreen extends Screen {
    public FakeScreen() {
        super(Component.literal(""));
        ICUtil.setTooltipFunction(this::getTooltipFromItem);
    }

    public List<Component> getTooltipFromItem(ItemStack stack) {
        return getTooltipFromItem(minecraft, stack);
    }
}
