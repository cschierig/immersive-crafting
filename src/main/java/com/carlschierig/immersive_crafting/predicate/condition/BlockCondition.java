package com.carlschierig.immersive_crafting.predicate.condition;

import com.carlschierig.immersive_crafting.api.context.RecipeContext;
import com.carlschierig.immersive_crafting.api.context.ValidationContext;
import com.carlschierig.immersive_crafting.api.predicate.condition.ICCondition;
import com.carlschierig.immersive_crafting.api.predicate.condition.ICConditionSerializer;
import com.carlschierig.immersive_crafting.context.ContextTypes;
import com.google.gson.JsonObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;

public class BlockCondition implements ICCondition {
    private final ResourceLocation blockID;

    public BlockCondition(ResourceLocation blockID) {
        this.blockID = blockID;
    }

    @Override
    public boolean test(RecipeContext context) {
        var block = context.get(ContextTypes.BLOCK);
        return blockID.equals(BuiltInRegistries.BLOCK.getKey(block));
    }

    private static final ValidationContext context = ValidationContext.of(ContextTypes.BLOCK);

    @Override
    public ValidationContext getRequirements() {
        return context;
    }

    @Override
    public ICConditionSerializer<?> getSerializer() {
        return ICConditionSerializers.BLOCK;
    }

    public static class Serializer implements ICConditionSerializer<BlockCondition> {
        private static final String BLOCK = "block";

        @Override
        public BlockCondition fromJson(JsonObject json) {
            var block = GsonHelper.getAsString(json, BLOCK);
            return new BlockCondition(new ResourceLocation(block));
        }

        @Override
        public JsonObject toJson(BlockCondition instance) {
            var json = new JsonObject();
            json.addProperty(BLOCK, instance.blockID.toString());
            return json;
        }

        @Override
        public BlockCondition fromNetwork(FriendlyByteBuf buf) {
            return new BlockCondition(buf.readResourceLocation());
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, BlockCondition instance) {
            buf.writeResourceLocation(instance.blockID);
        }
    }
}
