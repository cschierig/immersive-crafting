package com.carlschierig.immersivecrafting.impl.render;

import com.carlschierig.immersivecrafting.api.render.ICRenderable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

@ApiStatus.Internal
public enum DefaultCategoryRenderer implements ICRenderable {
    INSTANCE;

    @Override
    public void render(@NotNull GuiGraphicsExtractor draw, int x, int y, float delta) {
        draw.text(Minecraft.getInstance().font, "IC", x + 4, y + 4, 0xffffff);
    }
}
