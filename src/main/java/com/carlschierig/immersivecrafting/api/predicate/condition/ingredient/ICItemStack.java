package com.carlschierig.immersivecrafting.api.predicate.condition.ingredient;

import com.carlschierig.immersivecrafting.api.context.ContextTypes;
import com.carlschierig.immersivecrafting.api.context.CraftingContext;
import com.carlschierig.immersivecrafting.api.context.RecipeContext;
import com.carlschierig.immersivecrafting.api.context.ValidationContext;
import com.carlschierig.immersivecrafting.api.predicate.condition.ICConditionSerializer;
import com.carlschierig.immersivecrafting.api.predicate.condition.ICConditionSerializers;
import com.carlschierig.immersivecrafting.api.render.ICRenderFlags;
import com.carlschierig.immersivecrafting.impl.render.FakeScreen;
import com.carlschierig.immersivecrafting.impl.render.ICRenderHelper;
import com.carlschierig.immersivecrafting.impl.util.ICGsonHelperImpl;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.mojang.blaze3d.platform.Lighting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;
import org.quiltmc.loader.api.minecraft.ClientOnly;

import java.util.List;

/**
 * ICStack wrapper containing an item stack.
 */
public class ICItemStack extends ICStack {
    private final ItemStack stack;
    private final float chance;

    public ICItemStack(ItemLike item) {
        this(new ItemStack(item, 1));
    }

    public ICItemStack(ItemStack stack) {
        this(stack, 1);
    }

    public ICItemStack(ItemStack stack, float chance) {
        this.stack = stack;
        this.chance = chance;
    }

    @Override
    public void craft(RecipeContext recipeContext, CraftingContext craftingContext) {
        var chance = craftingContext.random().nextFloat();
        if (this.chance >= chance) {
            Block.popResourceFromFace(craftingContext.level(), craftingContext.pos(), craftingContext.direction(), stack.copy());
        }
    }

    @Override
    public Object getKey() {
        return stack.getItem();
    }

    @ClientOnly
    @Override
    public void render(GuiGraphics draw, int x, int y, float delta, int flags) {
        draw.renderItem(stack, x, y);

        if (ICRenderFlags.RENDER_ICON.test(flags)) {
            Lighting.setupFor3DItems();
            draw.renderItem(stack, x, y);
            draw.renderItemDecorations(Minecraft.getInstance().font, stack, x, y, "");
        }
        if (ICRenderFlags.RENDER_AMOUNT.test(flags)) {
            String count = "";
            if (getAmount() != 1) {
                count += getAmount();
            }
            ICRenderHelper.renderItemAnnotation(draw, x, y, Component.literal(count));
        }
    }

    @Override
    public int getAmount() {
        return stack.getCount();
    }

    @Override
    public float getChance() {
        return chance;
    }

    @Override
    public @NotNull Component getName() {
        return stack.getDisplayName();
    }

    @Override
    public boolean isEmpty() {
        return stack.isEmpty();
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
        return stack.is(stackContext.getItem()) && stack.getCount() <= stackContext.getCount();
    }

    @Override
    public ResourceLocation getIdentifier() {
        return BuiltInRegistries.ITEM.getKey(stack.getItem());
    }

    @Override
    public ICItemStack copy() {
        return new ICItemStack(stack.copy(), chance);
    }

    @Override
    public @NotNull @ClientOnly List<ClientTooltipComponent> getTooltip() {
        return FakeScreen.INSTANCE.getTooltipFromItem(stack);
    }

    public static class Serializer implements ICConditionSerializer<ICItemStack> {
        private static final String STACK = "stack";
        private static final String CHANCE = "chance";
        private static final String NBT = "nbt";

        @Override
        public ICItemStack fromJson(JsonObject json) {
            var stack = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, STACK));
            var chance = GsonHelper.getAsFloat(json, CHANCE, 1);

            if (chance < 0 || chance > 1) {
                throw new JsonSyntaxException("Chance must be between 0 and 1.");
            }
            return new ICItemStack(stack, chance);
        }

        @Override
        public JsonObject toJson(ICItemStack instance) {
            var json = new JsonObject();

            json.add(STACK, ICGsonHelperImpl.itemStackToJson(instance.stack));
            if (instance.chance != 1) {
                json.addProperty(CHANCE, instance.chance);
            }
            return json;
        }

        @Override
        public ICItemStack fromNetwork(FriendlyByteBuf buf) {
            var item = buf.readItem();
            var chance = buf.readFloat();
            return new ICItemStack(item, chance);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, ICItemStack instance) {
            buf.writeItem(instance.stack);
            buf.writeFloat(instance.chance);
        }
    }
}
