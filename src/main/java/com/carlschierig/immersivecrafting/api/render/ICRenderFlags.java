package com.carlschierig.immersivecrafting.api.render;

public enum ICRenderFlags {
    RENDER_ICON(1),
    RENDER_AMOUNT(2),
    RENDER_INGREDIENT(4),
    RENDER_REMAINDER(8),
    ALL(-1);

    private final int value;

    ICRenderFlags(int value) {
        this.value = value;
    }

    public boolean test(int flagHolder) {
        return (flagHolder & value) != 0;
    }
}
