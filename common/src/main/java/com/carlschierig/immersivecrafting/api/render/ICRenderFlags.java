package com.carlschierig.immersivecrafting.api.render;

import com.carlschierig.immersivecrafting.api.predicate.condition.ingredient.ICStack;

/**
 * These flags indicate which parts of an {@link ICStack} should be rendered.
 */
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

    /**
     * Checks if the given integer contains this flag.
     *
     * @param flagHolder An integer representing a set of flags.
     * @return {@code true} if the integer contains the flag, {@code false} otherwise.
     */
    public boolean test(int flagHolder) {
        return (flagHolder & value) != 0;
    }

    /**
     * Creates an integer containing all the passed flags.
     * The flags can then be checked with {@link #test(int)}
     *
     * @param flags The flags which should be stored in the return value.
     * @return an integer containing all the passed flags
     * @see #test(int flagHolder)
     */
    public static int of(ICRenderFlags... flags) {
        int value = 0;
        for (var flag : flags) {
            value |= flag.value;
        }
        return value;
    }
}
