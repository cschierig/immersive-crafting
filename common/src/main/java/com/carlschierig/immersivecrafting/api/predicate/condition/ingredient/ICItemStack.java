package com.carlschierig.immersivecrafting.api.predicate.condition.ingredient;

import com.carlschierig.immersivecrafting.api.context.ContextTypes;
import com.carlschierig.immersivecrafting.api.context.CraftingContext;
import com.carlschierig.immersivecrafting.api.context.RecipeContext;
import com.carlschierig.immersivecrafting.api.context.ValidationContext;
import com.carlschierig.immersivecrafting.api.predicate.condition.ICConditionSerializer;
import com.carlschierig.immersivecrafting.api.predicate.condition.ICConditionSerializers;
import com.carlschierig.immersivecrafting.impl.util.ICUtil;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * ICStack wrapper containing an item stack.
 */
public class ICItemStack extends ICStack {
    private final ItemStackTemplate stack;
    private final float chance;

    public ICItemStack(ItemLike item) {
        this(new ItemStackTemplate(item.asItem(), 1));
    }

    public ICItemStack(ItemStackTemplate stack) {
        this(stack, 1);
    }

    public ICItemStack(ItemStackTemplate stack, float chance) {
        this.stack = stack;
        this.chance = chance;
    }

    @Override
    public void craft(RecipeContext recipeContext, CraftingContext craftingContext) {
        var chance = craftingContext.random().nextFloat();
        if (this.chance >= chance) {
            // TODO: direction might be null
            Block.popResourceFromFace(craftingContext.level(), craftingContext.pos(), craftingContext.direction(), stack.create());
        }
    }

    @Override
    public Object getKey() {
        return stack.item();
    }


    @Override
    public SlotDisplay getDisplay() {
        return new SlotDisplay.ItemStackSlotDisplay(stack);
    }

    public ItemStackTemplate getStack() {
        return stack;
    }

    @Override
    public int getAmount() {
        return stack.count();
    }

    @Override
    public float getChance() {
        return chance;
    }

    @Override
    public @NotNull Component getName() {
        return stack.create().getDisplayName();
    }

    @Override
    public boolean isEmpty() {
        return stack.count() == 0;
    }

    @Override
    public ICConditionSerializer<?> getSerializer() {
        return ICConditionSerializers.ITEM;
    }

    private static final ValidationContext context = ValidationContext.of(ContextTypes.ITEM_STACK);

    @Override
    public ValidationContext getRequirements() {
        return context;
    }

    @Override
    public boolean test(RecipeContext context) {
        var stackContext = context.get(ContextTypes.ITEM_STACK);
        // todo: nbt
        return stack.is(stackContext.getItem()) && stack.count() <= stackContext.getCount();
    }

    @Override
    public Identifier getIdentifier() {
        return BuiltInRegistries.ITEM.getKey(stack.item().value());
    }

    @Override
    public ICItemStack copy() {
        return new ICItemStack(stack, chance);
    }

    @Override
    public Identifier getRenderer() {
        return super.getRenderer();
    }

    @Override
    public @NotNull List<Component> getTooltip() {
        return ICUtil.getTooltipFromItem(stack.create());
    }

    public static class Serializer implements ICConditionSerializer<ICItemStack> {
        public static final MapCodec<ICItemStack> CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance.group(
                        ItemStackTemplate.CODEC.fieldOf("stack").forGetter(stack -> stack.stack),
                        Codec.floatRange(0, 1).optionalFieldOf("chance", 1f).forGetter(stack -> stack.chance)
                ).apply(instance, ICItemStack::new)
        );

        public static final StreamCodec<RegistryFriendlyByteBuf, ICItemStack> STREAM_CODEC = StreamCodec.composite(
                ItemStackTemplate.STREAM_CODEC,
                stack -> stack.stack,
                ByteBufCodecs.FLOAT,
                stack -> stack.chance,
                ICItemStack::new
        );

        @Override
        public MapCodec<ICItemStack> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<? super RegistryFriendlyByteBuf, ICItemStack> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
