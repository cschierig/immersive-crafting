package com.carlschierig.immersivecrafting.impl.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import org.joml.Matrix4f;


public class KeyVaueTooltipComponent implements ClientTooltipComponent {
    private static final Minecraft CLIENT = Minecraft.getInstance();
    private final Component key;
    private final Component value;

    public KeyVaueTooltipComponent(Component key, Component value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public int getHeight() {
        return CLIENT.font.lineHeight;
    }

    @Override
    public int getWidth(Font textRenderer) {
        return textRenderer.width(getText());
    }

    private FormattedCharSequence getText() {
        return key.copy().append(": ").append(value).getVisualOrderText();
    }

    @Override
    public void renderText(Font textRenderer, int x, int y, Matrix4f modelMatrix, MultiBufferSource.BufferSource vertexConsumer) {
        // TODO: find out how light works
        textRenderer.drawInBatch(
                getText(),
                x,
                y,
                -1,
                true,
                modelMatrix,
                vertexConsumer,
                Font.DisplayMode.NORMAL,
                0,
                15728880
        );
    }
}
