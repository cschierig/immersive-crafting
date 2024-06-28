package com.carlschierig.immersivecrafting.api.predicate.condition.ingredient;

import com.carlschierig.immersivecrafting.api.context.ContextTypes;
import com.carlschierig.immersivecrafting.api.context.RecipeContext;
import com.carlschierig.immersivecrafting.api.context.ValidationContext;
import com.carlschierig.immersivecrafting.api.predicate.condition.ICCondition;
import com.carlschierig.immersivecrafting.api.predicate.condition.ICConditionSerializer;
import com.carlschierig.immersivecrafting.api.predicate.condition.ICConditionSerializers;
import com.carlschierig.immersivecrafting.api.predicate.condition.stack.ICItemStack;
import com.carlschierig.immersivecrafting.api.predicate.condition.stack.ICStack;
import com.carlschierig.immersivecrafting.impl.render.ICRenderHelper;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ItemStackIngredient implements ICIngredient {
    private final ItemStack stack;
    private final float chance;

    public ItemStackIngredient(ItemStack stack) {
        this(stack, 1);
    }

    public ItemStackIngredient(ItemLike item) {
        this(item, 1);
    }

    public ItemStackIngredient(ItemLike item, int amount) {
        this(item, amount, 1);
    }

    public ItemStackIngredient(ItemStack stack, float chance) {
        this.stack = stack.copy();
        this.chance = chance;
    }

    public ItemStackIngredient(ItemLike item, int amount, float chance) {
        this.stack = new ItemStack(item, amount);
        this.chance = chance;
    }

    @Override
    public int getAmount() {
        return stack.getCount();
    }

    @Override
    public void render(GuiGraphics draw, int x, int y, float delta, int flags) {
        ICRenderHelper.renderItem(stack, draw, x, y, delta, flags);
    }

    @Override
    public float getChance() {
        return chance;
    }

    @Override
    public ICIngredient copy() {
        return new ItemStackIngredient(stack, chance);
    }

    @Override
    public List<ICStack> getParts() {
        return List.of(new ICItemStack(stack));
    }

    @Override
    public @NotNull Component getName() {
        return stack.getDisplayName();
    }

    @Override
    public ICConditionSerializer<? extends ICCondition> getSerializer() {
        return ICConditionSerializers.ITEM_STACK;
    }

    private static final ValidationContext CONTEXT = ValidationContext.of(ContextTypes.ITEM_STACK);

    @Override
    public ValidationContext getRequirements() {
        return CONTEXT;
    }

    @Override
    public void consume(RecipeContext context) {
        var stack = context.get(ContextTypes.ITEM_STACK);
        // TODO: random chance
        stack.shrink(getAmount());
    }

    @Override
    public boolean test(RecipeContext context) {
        var stackContext = context.get(ContextTypes.ITEM_STACK);
        // todo: data components
        return stack.is(stackContext.getItem()) && stack.getCount() <= stackContext.getCount();
    }

    public static class Serializer implements ICConditionSerializer<ItemStackIngredient> {
        public static final MapCodec<ItemStackIngredient> CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance.group(
                        ItemStack.CODEC.fieldOf("stack").forGetter(stack -> stack.stack),
                        Codec.floatRange(0, 1).optionalFieldOf("chance", 1f).forGetter(stack -> stack.chance)
                ).apply(instance, ItemStackIngredient::new)
        );

        public static final StreamCodec<RegistryFriendlyByteBuf, ItemStackIngredient> STREAM_CODEC = StreamCodec.composite(
                ItemStack.STREAM_CODEC,
                stack -> stack.stack,
                ByteBufCodecs.FLOAT,
                stack -> stack.chance,
                ItemStackIngredient::new
        );

        @Override
        public MapCodec<ItemStackIngredient> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<? super RegistryFriendlyByteBuf, ItemStackIngredient> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
