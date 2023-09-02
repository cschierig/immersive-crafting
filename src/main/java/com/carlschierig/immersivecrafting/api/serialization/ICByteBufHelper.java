package com.carlschierig.immersivecrafting.api.serialization;

import com.carlschierig.immersivecrafting.api.predicate.condition.ICCondition;
import com.carlschierig.immersivecrafting.impl.util.ICByteBufHelperImpl;
import net.minecraft.network.FriendlyByteBuf;

/**
 * Provides helper methods to read and write Immersive Crafting data types from and to {@link FriendlyByteBuf}.
 */
public class ICByteBufHelper {
    private ICByteBufHelper() {
    }

    /**
     * Reads an {@link ICCondition} from the byte buf.
     *
     * @param buf The byte buf from which the condition should be read.
     * @return the read condition.
     */
    public static ICCondition readICCondition(FriendlyByteBuf buf) {
        return ICByteBufHelperImpl.readICCondition(buf);
    }

    /**
     * Writes the given {@link ICCondition} to the byte buf.
     *
     * @param buf       The byte buf to which the condition should be written.
     * @param condition The condition which should be written to the byte buf.
     */
    public static void writeICCondition(FriendlyByteBuf buf, ICCondition condition) {
        ICByteBufHelperImpl.writeICCondition(buf, condition);
    }
}
