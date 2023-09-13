package com.carlschierig.immersivecrafting.impl.render;

import com.carlschierig.immersivecrafting.api.render.ICRenderable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public enum DefaultCategoryRenderer implements ICRenderable {
    INSTANCE;

    @Override
    public void render(GuiGraphics draw, int x, int y, float delta) {
        draw.drawString(Minecraft.getInstance().font, "IC", x + 4, y + 4, 0xffffff);
    }
}
