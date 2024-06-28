package com.carlschierig.immersivecrafting.api.predicate.condition.stack;

import com.carlschierig.immersivecrafting.api.context.ContextTypes;
import com.carlschierig.immersivecrafting.api.context.RecipeContext;
import com.carlschierig.immersivecrafting.api.context.ValidationContext;
import com.carlschierig.immersivecrafting.api.predicate.condition.ICConditionSerializer;
import com.carlschierig.immersivecrafting.api.predicate.condition.ICConditionSerializers;
import com.carlschierig.immersivecrafting.api.render.ICRenderFlags;
import com.carlschierig.immersivecrafting.impl.util.ICUtil;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class ICBlockStateStack extends ICStack {
    private final BlockState block;
    private final float chance;

    public ICBlockStateStack(BlockState block) {
        this(block, 1);
    }

    public ICBlockStateStack(BlockState block, float chance) {
        this.block = block;
        this.chance = chance;
    }

    @Override
    public ResourceLocation getIdentifier() {
        return BuiltInRegistries.BLOCK.getKey(block.getBlock());
    }

    @Override
    public ICStack copy() {
        return new ICBlockStateStack(block, chance);
    }

    @Override
    public ICConditionSerializer<ICBlockStateStack> getSerializer() {
        return ICConditionSerializers.BLOCK_STATE;
    }

    private static final ValidationContext CONTEXT = new ValidationContext.Builder()
            .put(ContextTypes.BLOCK_POSITION)
            .put(ContextTypes.LEVEL)
            .put(ContextTypes.RANDOM)
            .build();

    @Override
    public boolean test(RecipeContext recipeContext) {
        var pos = recipeContext.get(ContextTypes.BLOCK_POSITION);
        var level = recipeContext.get(ContextTypes.LEVEL);
        var direction = recipeContext.tryGet(ContextTypes.DIRECTION);
        if (direction.isPresent()) {
            pos = pos.relative(direction.get());
        }
        return block.canSurvive(level, pos);
    }

    @Override
    public ValidationContext getRequirements() {
        return CONTEXT;
    }

    @Override
    public @NotNull Component getName() {
        return block.getBlock().getName();
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public Object getKey() {
        return block;
    }

    @Override
    public void craft(RecipeContext context) {
        var chance = context.get(ContextTypes.RANDOM).nextFloat();
        if (this.chance >= chance) {
            var direction = context.tryGet(ContextTypes.DIRECTION);
            var pos = context.get(ContextTypes.BLOCK_POSITION);
            if (direction.isPresent()) {
                pos = pos.relative(direction.get());
            }
            context.get(ContextTypes.LEVEL).setBlock(pos, block, 1 | 2);
        }
    }

    @Override
    public void render(@NotNull GuiGraphics draw, int x, int y, float delta, int flags) {
        if (ICRenderFlags.RENDER_ICON.test(flags)) {
            Lighting.setupFor3DItems();
            var stack = block.getBlock().asItem().getDefaultInstance();
            draw.renderItem(stack, x, y);
        }
    }

    public static class Serializer implements ICConditionSerializer<ICBlockStateStack> {
        public static final MapCodec<ICBlockStateStack> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                        BlockState.CODEC.fieldOf("state").forGetter(stack -> stack.block),
                        Codec.floatRange(0, 1).optionalFieldOf("chance", 1f).forGetter(stack -> stack.chance)
                ).apply(instance, ICBlockStateStack::new)
        );

        public static final StreamCodec<ByteBuf, ICBlockStateStack> STREAM_CODEC = StreamCodec.composite(
                ICUtil.BLOCK_STATE_STREAM_CODEC,
                stack -> stack.block,
                ByteBufCodecs.FLOAT,
                stack -> stack.chance,
                ICBlockStateStack::new
        );

        @Override
        public MapCodec<ICBlockStateStack> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<? super RegistryFriendlyByteBuf, ICBlockStateStack> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
