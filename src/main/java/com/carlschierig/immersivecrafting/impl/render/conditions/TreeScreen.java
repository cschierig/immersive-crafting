package com.carlschierig.immersivecrafting.impl.render.conditions;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class TreeScreen extends Screen {
    private final PredicateTree tree;
    private final Screen parent;

    public TreeScreen(PredicateTree tree, Screen parent) {
        super(Component.literal("Condition Tree"));
        this.tree = tree;
        this.parent = parent;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        this.renderDirtBackground(graphics);
        graphics.drawCenteredString(Minecraft.getInstance().font, "Work in Progress", width / 2, height / 2 - 50, 0xffffffff);
        //tree.render(graphics, mouseX, mouseY, delta);
    }

    @Override
    public void onClose() {
        getClient().setScreen(parent);
    }
}
