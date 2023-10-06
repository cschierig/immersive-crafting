package com.carlschierig.immersivecrafting.impl.predicate;

import net.minecraft.network.chat.Style;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class ICConditionData {
    /**
     * An aqua bold name
     */
    public static final Style CONDITION_NAME = Style.EMPTY.withColor(0x55ffff)
            .withBold(true);
}
