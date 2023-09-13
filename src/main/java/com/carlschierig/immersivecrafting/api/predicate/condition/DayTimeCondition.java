package com.carlschierig.immersivecrafting.api.predicate.condition;

import com.carlschierig.immersivecrafting.api.context.ContextTypes;
import com.carlschierig.immersivecrafting.api.context.RecipeContext;
import com.carlschierig.immersivecrafting.api.context.ValidationContext;
import com.google.gson.JsonObject;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class DayTimeCondition implements ICCondition {
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
        var time = level.getDayTime();

        if (startTime <= endTime) {
            return startTime <= time && time <= endTime;
        } else {
            return startTime <= time && time <= 24000 || 0 <= time && time <= endTime;
        }
    }

    @Override
    public void render(GuiGraphics draw, int x, int y, float delta) {
        draw.renderItem(new ItemStack(Items.CLOCK), 0, 0);
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
