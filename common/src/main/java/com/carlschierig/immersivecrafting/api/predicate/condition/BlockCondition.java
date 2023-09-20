package com.carlschierig.immersivecrafting.api.predicate.condition;

import com.carlschierig.immersivecrafting.api.context.ContextTypes;
import com.carlschierig.immersivecrafting.api.context.RecipeContext;
import com.carlschierig.immersivecrafting.api.context.ValidationContext;
import com.carlschierig.immersivecrafting.impl.predicate.RangePredicate;
import com.carlschierig.immersivecrafting.impl.render.KeyVaueTooltipComponent;
import com.carlschierig.immersivecrafting.impl.util.ICTranslationHelper;
import com.carlschierig.immersivecrafting.mixin.BlockStateAccessor;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class BlockCondition implements ICCondition {
    public static String LANGUAGE_KEY = "block";
    @Nullable
    private final ResourceLocation id;
    @Nullable
    private final TagKey<Block> tag;
    @Nullable
    private final RangePredicate hardness;

    protected BlockCondition(@Nullable ResourceLocation id, @Nullable TagKey<Block> tag, @Nullable RangePredicate hardness) {
        this.id = id;
        this.tag = tag;
        this.hardness = hardness;
    }

    @Override
    public boolean test(RecipeContext context) {
        var block = context.get(ContextTypes.BLOCK_STATE);
        var accessor = (BlockStateAccessor) block;

        boolean result = true;
        if (id != null) {
            result &= id.equals(BuiltInRegistries.BLOCK.getKey(block.getBlock()));
        } else if (tag != null) {
            result &= block.is(tag);
        }
        if (hardness != null) {
            result &= hardness.test(accessor.getDestroySpeed());
        }

        return result;
    }

    @Override
    public void render(@NotNull GuiGraphics draw, int x, int y, float delta) {
        var item = BuiltInRegistries.BLOCK.getOptional(id).map(Block::asItem).orElse(Items.AIR);
        if (item != Items.AIR) {
            draw.renderItem(new ItemStack(item), 0, 0);
        } else {
            // TODO: proper question mark texture
            draw.drawString(Minecraft.getInstance().font, "?", 0, 0, 0xffffffff);
        }
    }

    @Override
    public @Nullable Component getName() {
        return Component.translatable(ICTranslationHelper.translateCondition(LANGUAGE_KEY));
    }

    @Override
    public @NotNull List<ClientTooltipComponent> getTooltip() {
        List<ClientTooltipComponent> list = new ArrayList<>(ICCondition.super.getTooltip());
        if (id != null) {
            list.add(new KeyVaueTooltipComponent(
                    Component.translatable(ICTranslationHelper.translateConditionDescription(LANGUAGE_KEY, "id")),
                    Component.literal(id.toString())));
        } else if (tag != null) {
            list.add(new KeyVaueTooltipComponent(
                    Component.translatable(ICTranslationHelper.translateConditionDescription(LANGUAGE_KEY, "tag")),
                    Component.literal(tag.location().toString())));
        }
        if (hardness != null) {
            list.add(new KeyVaueTooltipComponent(
                    Component.translatable(ICTranslationHelper.translateConditionDescription(LANGUAGE_KEY, "hardness")),
                    Component.literal(hardness.toString())));
        }

        return list;
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
        private static final String TAG = "tag";
        private static final String HARDNESS = "hardness";

        @Override
        public BlockCondition fromJson(JsonObject json) {
            var block = GsonHelper.getAsString(json, ID, null);
            var id = block != null ? new ResourceLocation(block) : null;
            var tagId = GsonHelper.getAsString(json, TAG, null);
            var tag = tagId != null ? TagKey.create(Registries.BLOCK, new ResourceLocation(tagId)) : null;
            if (tag != null && id != null) {
                throw new JsonSyntaxException("May not use both id and tag field.");
            }
            var hardness = json.has(HARDNESS)
                    ? RangePredicate.getSerializer().fromJson(GsonHelper.getAsJsonObject(json, HARDNESS))
                    : null;
            return new BlockCondition(id, tag, hardness);
        }

        @Override
        public JsonObject toJson(BlockCondition instance) {
            var json = new JsonObject();
            if (instance.id != null) {
                json.addProperty(ID, instance.id.toString());
            } else if (instance.tag != null) {
                json.addProperty(TAG, instance.tag.location().toString());
            }
            if (instance.hardness != null) {
                json.add(HARDNESS, RangePredicate.getSerializer().toJson(instance.hardness));
            }
            return json;
        }

        @Override
        public BlockCondition fromNetwork(FriendlyByteBuf buf) {
            var id = buf.readNullable(FriendlyByteBuf::readResourceLocation);
            var tag = buf.readNullable(FriendlyByteBuf::readResourceLocation);
            var hardness = buf.readNullable(b -> RangePredicate.getSerializer().fromNetwork(b));
            return new BlockCondition(id, tag != null ? TagKey.create(Registries.BLOCK, tag) : null, hardness);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, BlockCondition instance) {
            buf.writeNullable(instance.id, FriendlyByteBuf::writeResourceLocation);
            buf.writeNullable(instance.tag != null ? instance.tag.location() : null, FriendlyByteBuf::writeResourceLocation);
            buf.writeNullable(instance.hardness, (b, t) -> RangePredicate.getSerializer().toNetwork(b, t));
        }
    }

    public static class Builder {
        @Nullable
        private ResourceLocation id;
        @Nullable TagKey<Block> tag;
        @Nullable
        private RangePredicate hardness;

        public Builder() {
        }

        /**
         * Uses the {@link ResourceLocation} of the given block as the resource location for the condition.
         *
         * @param block The block whose resource location should be used for the condition.
         * @return this Builder.
         */
        public Builder idFromBlock(Block block) {
            return id(BuiltInRegistries.BLOCK.getKey(block));
        }

        public Builder id(ResourceLocation id) {
            this.id = id;
            return this;
        }

        public Builder hardness(float min, float max) {
            hardness = new RangePredicate(min, max);
            return this;
        }

        public Builder tag(TagKey<Block> tag) {
            this.tag = tag;
            return this;
        }

        public Builder tag(ResourceLocation tag) {
            this.tag = TagKey.create(Registries.BLOCK, tag);
            return this;
        }

        public Builder hardnessMinOnly(float min) {
            hardness = new RangePredicate(min, Float.POSITIVE_INFINITY);
            return this;
        }

        public Builder hardnessMaxOnly(float max) {
            hardness = new RangePredicate(Float.NEGATIVE_INFINITY, max);
            return this;
        }

        public BlockCondition build() {
            return new BlockCondition(id, tag, hardness);
        }
    }
}
