package com.carlschierig.immersivecrafting.api.predicate.condition.ingredient;

import com.carlschierig.immersivecrafting.api.predicate.condition.ICCondition;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * An ingredient is something that can be consumed or produced by a recipe.
 * Recipes require {@link ICStack}s to be the output as they contain a craft method.
 *
 * @see ICStack
 */
public interface ICIngredient extends ICCondition {
    /**
     * A variant of {@link ICCondition#CODEC} which ensures the value is an {@link ICIngredient}.
     */
    Codec<ICIngredient> CODEC = ICCondition.CODEC.comapFlatMap(
            condition -> {
                if (condition instanceof ICIngredient ingredient) {
                    return DataResult.success(ingredient);
                } else {
                    return DataResult.error(() -> "Value must be an ingredient.");
                }
            },
            ingredient -> ingredient
    );

    StreamCodec<RegistryFriendlyByteBuf, ICIngredient> STREAM_CODEC = ICCondition.STREAM_CODEC.map(
            condition -> (ICIngredient) condition,
            ingredient -> ingredient
    );

    /**
     * Returns the amount which is needed by the ingredient.
     * <p>
     * For example, if a recipe needs five diamonds to be present, the amount should return 5.
     *
     * @return the amount which is needed by the ingredient.
     */
    int getAmount();


    SlotDisplay getDisplay();

    /**
     * Returns the chance that the ingredient is consumed/produced.
     * The value must be in the range [0, 1].
     *
     * @return the chance that the ingredient is consumed/produced.
     */
    float getChance();

    /**
     * Copies this ingredient. Must return a new instance on each call.
     *
     * @return A copy of this ingredient.
     */
    ICIngredient copy();

    /**
     * Returns the individual parts of this ingredient.
     * <p>
     * If this ingredient is a consumer,
     * the returned stacks have an <b>or relation</b>,
     * meaning that any of the returned stacks is valid for consumption to fulfill this ingredient.
     * <p>
     * If this ingredient is a producer, the returned stack have an <b>and relation</b>,
     * meaning that this ingredient will produce all the returned stacks.
     *
     * @return a list of stacks which are used by the ingredient.
     */
    List<ICStack> getParts();


    /**
     * Returns the name of this ingredient.
     *
     * @return the name of this ingredient.
     */
    @NotNull
    Component getName();

    /**
     * Returns whether this ingredient is empty.
     *
     * @return {@code true} if this ingredient is empty, {@code false} otherwise.
     */
    boolean isEmpty();
}
