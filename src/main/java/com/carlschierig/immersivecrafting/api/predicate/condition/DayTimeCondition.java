package com.carlschierig.immersivecrafting.api.predicate.condition;

import com.carlschierig.immersivecrafting.api.context.ContextTypes;
import com.carlschierig.immersivecrafting.api.context.RecipeContext;
import com.carlschierig.immersivecrafting.api.context.ValidationContext;
import com.carlschierig.immersivecrafting.impl.util.ICTranslationHelper;
import com.google.gson.JsonObject;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTextTooltip;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.quiltmc.loader.api.minecraft.ClientOnly;

import java.util.ArrayList;
import java.util.List;

public class DayTimeCondition implements ICCondition {
    public static final String LANGUAGE_KEY = "dayTime";
    public static final ICCondition DAY = new DayTimeCondition(0, 12999);
    public static final ICCondition NIGHT = new InvertedCondition(DAY);
    private final int startTime;
    private final int endTime;

    public DayTimeCondition(int startTime, int endTime) {
        if (startTime < 0 || startTime > 24000 || endTime < 0 || endTime > 24000) {
            throw new IllegalArgumentException("time must be between 0 and 24000 (inclusive).");
        }

        this.startTime = startTime;
        this.endTime = endTime;
    }

    @Override
    public boolean test(RecipeContext recipeContext) {
        var level = recipeContext.get(ContextTypes.LEVEL);
        var time = level.getDayTime() % 24000;

        if (startTime <= endTime) {
            return startTime <= time && time <= endTime;
        } else {
            return startTime <= time && time <= 24000 || 0 <= time && time <= endTime;
        }
    }

    @ClientOnly
    @Override
    public void render(@NotNull GuiGraphics draw, int x, int y, float delta) {
        draw.renderItem(new ItemStack(Items.CLOCK), 0, 0);
    }

    @Override
    public @Nullable Component getName() {
        return Component.translatable(ICTranslationHelper.translateCondition(LANGUAGE_KEY));
    }

    @Override
    public @NotNull List<ClientTooltipComponent> getTooltip() {
        List<ClientTooltipComponent> list = new ArrayList<>(ICCondition.super.getTooltip());

        ClientTooltipComponent first;
        ClientTooltipComponent second;
        if (startTime <= endTime) {
            first = new ClientTextTooltip(getTimeComponent(startTime));
            second = new ClientTextTooltip(getTimeComponent(endTime));
        } else {
            first = new ClientTextTooltip(getTimeComponent(endTime));
            second = new ClientTextTooltip(getTimeComponent(startTime));
        }
        list.add(new ClientTextTooltip(
                Component.translatable(ICTranslationHelper.translateConditionDescription(LANGUAGE_KEY, "timeRange")).getVisualOrderText()));
        list.add(first);
        list.add(new ClientTextTooltip(
                Component.translatable(ICTranslationHelper.translateConditionDescription(LANGUAGE_KEY, "and")).getVisualOrderText()));
        list.add(second);

        return list;
    }

    private FormattedCharSequence getTimeComponent(int time) {
        var timeString = switch ((time + 500) / 1000) {
            case 0 -> "earlyMorning";
            case 1 -> "morning";
            case 2, 3 -> "lateMorning";
            case 4, 5 -> "beforeNoon";
            case 6 -> "noon";
            case 7, 8 -> "earlyAfternoon";
            case 9 -> "afternoon";
            case 10, 11 -> "lateAfternoon";
            case 12 -> "evening";
            case 13 -> "sunset";
            case 14 -> "nightfall";
            case 15, 16, 17 -> "beforeMidnight";
            case 18 -> "midnight";
            case 19, 20, 21 -> "afterMidnight";
            default -> "sunrise";
        };
        var textStyle = Style.EMPTY.withColor(0xEEEEEE).withItalic(true);
        return FormattedCharSequence.composite(
                FormattedCharSequence.forward(Integer.toString(time), Style.EMPTY),
                FormattedCharSequence.forward(" (", textStyle),
                Component.translatable(ICTranslationHelper.translateConditionDescription(LANGUAGE_KEY, timeString)).setStyle(textStyle).getVisualOrderText(),
                FormattedCharSequence.forward(")", textStyle)
        );
    }

    @Override
    public ICConditionSerializer<?> getSerializer() {
        return ICConditionSerializers.DAY_TIME;
    }

    private static final ValidationContext context = ValidationContext.of(ContextTypes.LEVEL);

    @Override
    public ValidationContext getRequirements() {
        return context;
    }

    public static final class Serializer implements ICConditionSerializer<DayTimeCondition> {
        private static final String START_TIME = "start_time";
        private static final String END_TIME = "end_time";

        @Override
        public DayTimeCondition fromJson(JsonObject json) {
            var start = GsonHelper.getAsInt(json, START_TIME);
            var end = GsonHelper.getAsInt(json, END_TIME);

            return new DayTimeCondition(start, end);
        }

        @Override
        public JsonObject toJson(DayTimeCondition instance) {
            var json = new JsonObject();
            json.addProperty(START_TIME, instance.startTime);
            json.addProperty(END_TIME, instance.endTime);

            return json;
        }

        @Override
        public DayTimeCondition fromNetwork(FriendlyByteBuf buf) {
            var start = buf.readInt();
            var end = buf.readInt();

            return new DayTimeCondition(start, end);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, DayTimeCondition instance) {
            buf.writeInt(instance.startTime);
            buf.writeInt(instance.endTime);
        }
    }
}
