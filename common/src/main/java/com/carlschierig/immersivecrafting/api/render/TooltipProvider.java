package com.carlschierig.immersivecrafting.api.render;

import com.carlschierig.immersivecrafting.impl.predicate.ICConditionData;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an object which can create a tooltip.
 */
public interface TooltipProvider {
    /**
     * Returns the name of the condition.
     *
     * @return the name of the condition.
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
}
