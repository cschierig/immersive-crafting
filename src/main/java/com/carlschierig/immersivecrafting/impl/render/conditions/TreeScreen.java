package com.carlschierig.immersivecrafting.impl.render.conditions;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

public class TreeScreen extends Screen {
    private final PredicateTree tree;
    private final Screen parent;

    private double xShift;
    private double yShift;

    public TreeScreen(PredicateTree tree, Screen parent) {
        super(Component.literal("Condition Tree"));
        this.tree = tree;
        this.parent = parent;
    }

    @Override
    protected void init() {
        xShift = (double) width / 2;
        yShift = (double) height / 2 - 50;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        this.renderDirtBackground(graphics);
        graphics.drawCenteredString(Minecraft.getInstance().font, "Work in Progress", width / 2, height / 4 - 50, 0xffffffff);
        graphics.pose().pushPose();
        graphics.pose().translate(xShift, yShift, 0);
        tree.render(graphics, (int) (mouseX - xShift), (int) (mouseY - yShift), delta);
        graphics.pose().popPose();
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
            xShift += deltaX;
            yShift += deltaY;
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public void onClose() {
        getClient().setScreen(parent);
    }
}
