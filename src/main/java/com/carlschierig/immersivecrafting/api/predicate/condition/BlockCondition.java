package com.carlschierig.immersivecrafting.api.predicate.condition;

import com.carlschierig.immersivecrafting.api.context.ContextTypes;
import com.carlschierig.immersivecrafting.api.context.RecipeContext;
import com.carlschierig.immersivecrafting.api.context.ValidationContext;
import com.carlschierig.immersivecrafting.impl.predicate.RangePredicate;
import com.carlschierig.immersivecrafting.mixin.BlockStateAccessor;
import com.google.gson.JsonObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

public class BlockCondition implements ICCondition {
    @Nullable
    private final ResourceLocation id;
    @Nullable
    private final RangePredicate hardness;

    protected BlockCondition(@Nullable ResourceLocation id, @Nullable RangePredicate hardness) {
        this.id = id;
        this.hardness = hardness;
    }

    @Override
    public boolean test(RecipeContext context) {
        var block = context.get(ContextTypes.BLOCK_STATE);
        var accessor = (BlockStateAccessor) block;

        boolean result = true;
        if (id != null) {
            result &= id.equals(BuiltInRegistries.BLOCK.getKey(block.getBlock()));
        }
        if (hardness != null) {
            result &= hardness.test(accessor.getDestroySpeed());
        }

        return result;
    }

    private static final ValidationContext context = ValidationContext.of(ContextTypes.BLOCK_STATE);

    @Override
    public ValidationContext getRequirements() {
        return context;
    }

    @Override
    public ICConditionSerializer<?> getSerializer() {
        return ICConditionSerializers.BLOCK;
    }

    public static class Serializer implements ICConditionSerializer<BlockCondition> {
        private static final String ID = "id";
        private static final String HARDNESS = "hardness";

        @Override
        public BlockCondition fromJson(JsonObject json) {
            var block = GsonHelper.getAsString(json, ID, null);
            var id = block != null ? new ResourceLocation(block) : null;
            var hardness = json.has(HARDNESS)
                    ? RangePredicate.getSerializer().fromJson(GsonHelper.getAsJsonObject(json, HARDNESS))
                    : null;
            return new BlockCondition(id, hardness);
        }

        @Override
        public JsonObject toJson(BlockCondition instance) {
            var json = new JsonObject();
            if (instance.id != null) {
                json.addProperty(ID, instance.id.toString());
            }
            if (instance.hardness != null) {
                json.add(HARDNESS, RangePredicate.getSerializer().toJson(instance.hardness));
            }
            return json;
        }

        @Override
        public BlockCondition fromNetwork(FriendlyByteBuf buf) {
            var id = buf.readNullable(FriendlyByteBuf::readResourceLocation);
            var hardness = buf.readNullable(b -> RangePredicate.getSerializer().fromNetwork(b));
            return new BlockCondition(id, hardness);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, BlockCondition instance) {
            buf.writeNullable(instance.id, FriendlyByteBuf::writeResourceLocation);
            buf.writeNullable(instance.hardness, (b, t) -> RangePredicate.getSerializer().toNetwork(b, t));
        }
    }

    public static class Builder {
        @Nullable
        private ResourceLocation id;
        @Nullable
        private RangePredicate hardness;

        public Builder() {
        }

        @Contract("_->this")
        public Builder id(ResourceLocation id) {
            this.id = id;
            return this;
        }

        @Contract("_->this")
        public Builder hardness(float min, float max) {
            hardness = new RangePredicate(min, max);
            return this;
        }

        @Contract("_->this")
        public Builder hardnessMinOnly(float min) {
            hardness = new RangePredicate(min, Float.POSITIVE_INFINITY);
            return this;
        }

        @Contract("_->this")
        public Builder hardnessMaxOnly(float max) {
            hardness = new RangePredicate(Float.NEGATIVE_INFINITY, max);
            return this;
        }

        public BlockCondition build() {
            return new BlockCondition(id, hardness);
        }
    }
}
