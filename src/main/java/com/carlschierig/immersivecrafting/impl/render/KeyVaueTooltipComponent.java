package com.carlschierig.immersivecrafting.impl.render;

import net.minecraft.client.gui.screens.inventory.tooltip.ClientTextTooltip;
import net.minecraft.network.chat.Component;
import org.quiltmc.loader.api.minecraft.ClientOnly;


@ClientOnly
public class KeyVaueTooltipComponent extends ClientTextTooltip {
    public KeyVaueTooltipComponent(Component key, Component value) {
        super(key.copy().append(": ").append(value).getVisualOrderText());
    }
}
