package com.carlschierig.immersivecrafting.api.serialization;

import com.carlschierig.immersivecrafting.api.predicate.condition.ICCondition;
import com.carlschierig.immersivecrafting.api.recipe.ICRecipe;
import com.carlschierig.immersivecrafting.api.recipe.ICRecipeSerializer;
import com.carlschierig.immersivecrafting.api.registry.ICRegistries;
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

    /**
     * Writes the given {@link ICRecipe} to the byte buf.
     *
     * @param buf    The byte buf to which the recipe should be written.
     * @param recipe The recipe which should be written to the byte buf.
     */
    @SuppressWarnings("unchecked")
    public static <T extends ICRecipe> void writeICRecipe(FriendlyByteBuf buf, T recipe) {
        buf.writeResourceLocation(ICRegistries.RECIPE_SERIALIZER.getKey(recipe.getSerializer()));
        buf.writeResourceLocation(recipe.getId());
        ((ICRecipeSerializer<T>) recipe.getSerializer()).toNetwork(buf, recipe);
    }

    /**
     * Reads an {@link ICRecipe} from the byte buf.
     *
     * @param buf The byte buf from which the recipe should be read.
     * @return the read recipe.
     */
    public static ICRecipe readICRecipe(FriendlyByteBuf buf) {
        var serializer = buf.readResourceLocation();
        var id = buf.readResourceLocation();
        return ICRegistries.RECIPE_SERIALIZER.get(serializer).fromNetwork(id, buf);
    }
}
