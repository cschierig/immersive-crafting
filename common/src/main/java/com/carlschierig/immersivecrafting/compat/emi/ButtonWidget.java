package com.carlschierig.immersivecrafting.compat.emi;

import dev.emi.emi.api.widget.Bounds;
import dev.emi.emi.api.widget.Widget;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;

public class ButtonWidget extends Widget {
    private final Button button;

    public ButtonWidget(Button button) {
        this.button = button;
    }

    @Override
    public Bounds getBounds() {
        return new Bounds(button.getX(), button.getY(), button.getWidth(), button.getHeight());
    }

    @Override
    public void render(GuiGraphics draw, int mouseX, int mouseY, float delta) {
        button.render(draw, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int button) {
        this.button.onClick(mouseX, mouseY);
        return true;
    }
}
