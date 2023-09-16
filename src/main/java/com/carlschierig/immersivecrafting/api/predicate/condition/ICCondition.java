package com.carlschierig.immersivecrafting.api.predicate.condition;

import com.carlschierig.immersivecrafting.api.context.RecipeContext;
import com.carlschierig.immersivecrafting.api.context.ValidationContext;
import com.carlschierig.immersivecrafting.api.predicate.PredicateVisitor;
import com.carlschierig.immersivecrafting.api.predicate.Visitable;
import com.carlschierig.immersivecrafting.api.render.ICRenderable;
import com.carlschierig.immersivecrafting.impl.predicate.ICConditionData;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * A condition which can be tested against a {@link RecipeContext}.
 * <p>
 * The condition also provides rendering and tooltip-methods.
 * These have a default implementation, but it is recommended to implement them so that
 */
public interface ICCondition extends Predicate<RecipeContext>, Visitable, ICRenderable {
    ICConditionSerializer<?> getSerializer();

    ValidationContext getRequirements();

    /**
     * Returns the name of the condition.
     *
     * @return
     */
    @Nullable
    default Component getName() {
        return null;
    }

    /**
     * Returns a list of tooltip components which can be rendered as a tooltip.
     * The tooltip should explain what needs to be fulfilled for {@link #test} to return {@code true}.
     *
     * @return a list of tooltip components which can be rendered as a tooltip.
     */
    @NotNull
    @Contract("->new")
    default List<ClientTooltipComponent> getTooltip() {
        var name = getName();

        List<ClientTooltipComponent> list = new ArrayList<>();
        if (name != null) {
            list.add(ClientTooltipComponent.create(FormattedCharSequence.forward(name.getString(), ICConditionData.CONDITION_NAME)));
        }

        return list;
    }

    @Override
    default void accept(PredicateVisitor visitor) {
        visitor.visitCondition(this);
    }
}
