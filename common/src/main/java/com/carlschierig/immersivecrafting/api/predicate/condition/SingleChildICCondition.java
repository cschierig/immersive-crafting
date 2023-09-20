package com.carlschierig.immersivecrafting.api.predicate.condition;

import com.carlschierig.immersivecrafting.api.context.ValidationContext;
import com.carlschierig.immersivecrafting.api.predicate.PredicateVisitor;
import com.carlschierig.immersivecrafting.api.serialization.ICGsonHelper;
import com.carlschierig.immersivecrafting.impl.util.ICByteBufHelperImpl;
import com.google.gson.JsonObject;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;
import org.jetbrains.annotations.NotNull;

/**
 * This condition should be used for conditions which have a single child.
 */
public abstract class SingleChildICCondition implements ICCondition {
    protected final ICCondition child;

    protected SingleChildICCondition(ICCondition child) {
        this.child = child;
    }

    @Override
    public void render(@NotNull GuiGraphics draw, int x, int y, float delta) {
        child.render(draw, x, y, delta);
    }

    @Override
    public ValidationContext getRequirements() {
        return child.getRequirements();
    }

    @Override
    public void accept(PredicateVisitor visitor) {
        visitor.visitSingleChildCondition(this);
    }

    public ICCondition getChild() {
        return child;
    }

    public static abstract class Serializer<T extends SingleChildICCondition> implements ICConditionSerializer<T> {
        private static final String CONDITION = "condition";

        protected abstract T create(ICCondition conditions);

        @Override
        public T fromJson(JsonObject json) {
            var condition = GsonHelper.getAsJsonObject(json, CONDITION);
            return create(ICGsonHelper.getAsCondition(condition));
        }

        @Override
        public JsonObject toJson(T instance) {
            var json = new JsonObject();

            var condition = ICGsonHelper.conditionToJson(instance.child);
            json.add(CONDITION, condition);

            return json;
        }

        @Override
        public T fromNetwork(FriendlyByteBuf buf) {
            var condition = ICByteBufHelperImpl.readICCondition(buf);
            return create(condition);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, T instance) {
            ICByteBufHelperImpl.writeICCondition(buf, instance.child);
        }
    }
}
