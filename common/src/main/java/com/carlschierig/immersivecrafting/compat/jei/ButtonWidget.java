package com.carlschierig.immersivecrafting.compat.jei;

import mezz.jei.api.gui.inputs.IJeiInputHandler;
import mezz.jei.api.gui.inputs.IJeiUserInput;
import mezz.jei.api.gui.widgets.IRecipeWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.navigation.ScreenPosition;
import net.minecraft.client.gui.navigation.ScreenRectangle;

public class ButtonWidget implements IRecipeWidget, IJeiInputHandler {
    private final Button button;

    public ButtonWidget(Button button) {
        this.button = button;
    }

    @Override
    public void drawWidget(GuiGraphicsExtractor guiGraphics, double mouseX, double mouseY) {
        button.extractRenderState(guiGraphics, (int) mouseX, (int) mouseY, 0);
        System.out.println(mouseX + "," + mouseY);
    }

    @Override
    public ScreenRectangle getArea() {
        return new ScreenRectangle(button.getX(), button.getY(), button.getWidth(), button.getHeight());
    }

    @Override
    public boolean handleInput(double mouseX, double mouseY, IJeiUserInput input) {
        // TODO: don't know if something else would be better
        if (input.is(Minecraft.getInstance().options.keyAttack)) {
            if (!input.isSimulate()) {
                this.button.onClick(null, false);
            }
            return true;
        }
        return false;
    }

    @Override
    public ScreenPosition getPosition() {
        return new ScreenPosition(0, 0);
    }
}
