package com.carlschierig.immersivecrafting.api.predicate.condition;

import com.carlschierig.immersivecrafting.api.context.ContextTypes;
import com.carlschierig.immersivecrafting.api.context.RecipeContext;
import com.carlschierig.immersivecrafting.api.context.ValidationContext;
import com.carlschierig.immersivecrafting.impl.predicate.RangePredicate;
import com.carlschierig.immersivecrafting.impl.render.KeyVaueTooltipComponent;
import com.carlschierig.immersivecrafting.impl.util.ICTranslationHelper;
import com.carlschierig.immersivecrafting.mixin.BlockStateAccessor;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BlockCondition implements ICCondition {
    public static String LANGUAGE_KEY = "block";
    private final Optional<BlockValue> block;

    private final Optional<RangePredicate> hardness;

    private BlockCondition(Optional<BlockValue> block, Optional<RangePredicate> hardness) {
        this.block = block;
        this.hardness = hardness;
    }

    @Override
    public boolean test(RecipeContext context) {
        var block = context.get(ContextTypes.BLOCK_STATE);
        var accessor = (BlockStateAccessor) block;

        boolean result = true;
        if (this.block.isPresent()) {
            var blockValue = this.block.get();
            if (blockValue.id != null) {
                result &= blockValue.id.equals(BuiltInRegistries.BLOCK.getKey(block.getBlock()));
            } else if (blockValue.tag != null) {
                result &= block.is(blockValue.tag);
            }
        }
        if (hardness.isPresent()) {
            result &= hardness.get().test(accessor.getDestroySpeed());
        }

        return result;
    }

    @Override
    public void render(@NotNull GuiGraphics draw, int x, int y, float delta) {
        var item = block.isPresent() ? BuiltInRegistries.BLOCK.getOptional(block.get().id).map(Block::asItem).orElse(Items.AIR) : Items.AIR;
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
        if (block.isPresent()) {
            var block = this.block.get();
            if (block.id != null) {
                list.add(new KeyVaueTooltipComponent(
                        Component.translatable(ICTranslationHelper.translateConditionDescription(LANGUAGE_KEY, "id")),
                        Component.literal(block.id.toString())));
            } else if (block.tag != null) {
                list.add(new KeyVaueTooltipComponent(
                        Component.translatable(ICTranslationHelper.translateConditionDescription(LANGUAGE_KEY, "tag")),
                        Component.literal(block.tag.location().toString())));
            }
        }
        if (hardness.isPresent()) {
            list.add(new KeyVaueTooltipComponent(
                    Component.translatable(ICTranslationHelper.translateConditionDescription(LANGUAGE_KEY, "hardness")),
                    Component.literal(hardness.get().toString())));
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

    private static class BlockValue {
        @Nullable
        public final ResourceLocation id;
        @Nullable
        public final TagKey<Block> tag;

        public BlockValue(ResourceLocation id) {
            this.id = id;
            this.tag = null;
        }

        public BlockValue(TagKey<Block> tag) {
            this.tag = tag;
            this.id = null;
        }

        public BlockValue(Optional<ResourceLocation> id, Optional<TagKey<Block>> tag) {
            this.tag = tag.orElse(null);
            this.id = id.orElse(null);
        }

        private static final Codec<BlockValue> RL_CODEC = RecordCodecBuilder.create(
                instance -> instance.group(ResourceLocation.CODEC.fieldOf("id").forGetter(val -> val.id)).apply(instance, BlockValue::new));
        private static final Codec<BlockValue> KEY_CODEC = RecordCodecBuilder.create(
                instance -> instance.group(TagKey.codec(Registries.BLOCK).fieldOf("tag").forGetter(val -> val.tag)).apply(instance, BlockValue::new));

        public static final Codec<BlockValue> CODEC = Codec.xor(
                RL_CODEC,
                KEY_CODEC
        ).xmap(
                either -> either.map(value -> value, value -> value),
                value -> {
                    if (value.id != null) {
                        return Either.left(value);
                    } else if (value.tag != null) {
                        return Either.right(value);
                    } else {
                        throw new UnsupportedOperationException("Either id or tag must be specified");
                    }
                }
        );

        public static final StreamCodec<RegistryFriendlyByteBuf, BlockValue> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.optional(ResourceLocation.STREAM_CODEC),
                val -> Optional.ofNullable(val.id),
                ByteBufCodecs.optional(ResourceLocation.STREAM_CODEC.map(id -> TagKey.create(Registries.BLOCK, id), TagKey::location)),
                val -> Optional.ofNullable(val.tag),
                BlockValue::new
        );
    }

    public static class Serializer implements ICConditionSerializer<BlockCondition> {
        public static final MapCodec<BlockCondition> CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance.group(BlockValue.CODEC.optionalFieldOf("block").forGetter(con -> con.block),
                        RangePredicate.getSerializer().codec().optionalFieldOf("hardness").forGetter(con -> con.hardness)
                ).apply(instance, BlockCondition::new)
        );

        public static final StreamCodec<RegistryFriendlyByteBuf, BlockCondition> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.optional(BlockValue.STREAM_CODEC),
                con -> con.block,
                ByteBufCodecs.optional(RangePredicate.getSerializer().streamCodec()),
                con -> con.hardness,
                BlockCondition::new
        );

        @Override
        public MapCodec<BlockCondition> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<? super RegistryFriendlyByteBuf, BlockCondition> streamCodec() {
            return STREAM_CODEC;
        }
    }

    public static class Builder {
        private Optional<BlockValue> block = Optional.empty();
        private Optional<RangePredicate> hardness = Optional.empty();

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
            block = Optional.of(new BlockValue(id));
            return this;
        }

        public Builder hardness(float min, float max) {
            hardness = Optional.of(new RangePredicate(min, max));
            return this;
        }

        public Builder tag(TagKey<Block> tag) {
            block = Optional.of(new BlockValue(tag));
            return this;
        }

        public Builder tag(ResourceLocation tag) {
            block = Optional.of(new BlockValue(TagKey.create(Registries.BLOCK, tag)));
            return this;
        }

        public Builder hardnessMinOnly(float min) {
            hardness = Optional.of(new RangePredicate(min, Float.POSITIVE_INFINITY));
            return this;
        }

        public Builder hardnessMaxOnly(float max) {
            hardness = Optional.of(new RangePredicate(Float.NEGATIVE_INFINITY, max));
            return this;
        }

        public BlockCondition build() {
            return new BlockCondition(block, hardness);
        }
    }
}
