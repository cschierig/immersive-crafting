package com.carlschierig.immersivecrafting.impl.render.conditions;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

public class TreeScreen extends Screen {
    private final PredicateTree tree;
    private final Screen parent;

    private float xShift;
    private float yShift;

    public TreeScreen(PredicateTree tree, Screen parent) {
        super(Component.literal("Condition Tree"));
        this.tree = tree;
        this.parent = parent;
    }

    @Override
    protected void init() {
        xShift = (float) width / 2;
        yShift = (float) height / 2 - 50;
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
        //this.renderDirtBackground(graphics);
        graphics.centeredText(Minecraft.getInstance().font, "Work in Progress", width / 2, height / 4 - 50, 0xffffffff);
        graphics.pose().pushMatrix();
        graphics.pose().translate(xShift, yShift);
        tree.render(graphics, (int) (mouseX - xShift), (int) (mouseY - yShift), delta);
        graphics.pose().popMatrix();
    }

    @Override
    public boolean mouseDragged(MouseButtonEvent event, double dx, double dy) {
        if (event.button() == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
            xShift += (float) dx;
            yShift += (float) dy;
            return true;
        }
        return super.mouseDragged(event, dx, dy);
    }

    @Override
    public void onClose() {
        Minecraft.getInstance().setScreen(parent);
    }
}
