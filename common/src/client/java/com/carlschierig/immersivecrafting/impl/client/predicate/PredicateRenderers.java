package com.carlschierig.immersivecrafting.impl.client.predicate;

import com.carlschierig.immersivecrafting.api.client.registry.ICClientRegistries;
import com.carlschierig.immersivecrafting.api.client.render.ICRenderer;
import com.carlschierig.immersivecrafting.api.predicate.condition.*;
import com.carlschierig.immersivecrafting.api.predicate.condition.ingredient.ICItemStack;
import com.carlschierig.immersivecrafting.impl.client.render.ICRenderHelper;
import com.carlschierig.immersivecrafting.impl.util.ICUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

public class PredicateRenderers {
    public static final ICRenderer<ICCondition> UNKNOWN = register("unknown", new ICRenderer<>() {
        @Override
        public void render(ICCondition element, @NotNull GuiGraphicsExtractor draw, int x, int y, float delta) {
            // TODO: proper question mark texture
            draw.text(Minecraft.getInstance().font, "?", 0, 0, 0xffffffff);
        }
    });

    public static final ICRenderer<AndCondition> TEXT = register("text", new ICRenderer<>() {
        @Override
        public void render(AndCondition element, @NotNull GuiGraphicsExtractor draw, int x, int y, float delta) {
            draw.text(Minecraft.getInstance().font, element.getName().getString().toUpperCase(), 0, 0, 0xffffffff);
        }
    });

    public static final ICRenderer<BlockCondition> BLOCK = register("block", new ICRenderer<>() {
        @Override
        public void render(BlockCondition element, @NotNull GuiGraphicsExtractor draw, int x, int y, float delta) {
            var item = element.block().isPresent() ? BuiltInRegistries.BLOCK.getOptional(element.block().get().id).map(Block::asItem).orElse(Items.AIR) : Items.AIR;
            if (item != Items.AIR) {
                draw.item(new ItemStack(item), 0, 0);
            } else {
                // TODO: proper question mark texture
                draw.text(Minecraft.getInstance().font, "?", 0, 0, 0xffffffff);
            }
        }
    });

    public static final ICRenderer<InvertedCondition> INVERTED = register("inverted", new ICRenderer<>() {
        @Override
        public void render(InvertedCondition element, @NotNull GuiGraphicsExtractor draw, int x, int y, float delta) {
            var child = ICClientRegistries.RENDERABLE.getValue(element.getChild().getRenderer());
            child.renderUnknown(element.getChild(), draw, x, y, delta);
            ICRenderHelper.renderItemAnnotation(draw, 0, 0, Component.literal("!"));
        }
    });

    public static final ICRenderer<ICItemStack> ITEM_STACK = register("item_stack", new ICRenderer<>() {
        @Override
        public void render(ICItemStack element, @NotNull GuiGraphicsExtractor draw, int x, int y, float delta) {
            draw.item(element.getStack().create(), x, y);

            // TODO: fix this
//            Lighting.Entry.setupFor3DItems();
            draw.item(element.getStack().create(), x, y);
            draw.itemDecorations(Minecraft.getInstance().font, element.getStack().create(), x, y, "");

            String count = "";
            if (element.getAmount() != 1) {
                count += element.getAmount();
            }
            ICRenderHelper.renderItemAnnotation(draw, x, y, Component.literal(count));
        }
    });

    public static final ICRenderer<DayTimeCondition> TIME = register("time", new ICRenderer<>() {
        @Override
        public void render(DayTimeCondition element, @NotNull GuiGraphicsExtractor draw, int x, int y, float delta) {
            draw.item(new ItemStack(Items.CLOCK), 0, 0);
        }
    });

    private static <T> ICRenderer<T> register(String id, ICRenderer<T> renderable) {
        return Registry.register(ICClientRegistries.RENDERABLE, ICUtil.getId(id), renderable);
    }

    public static void init() {
    }
}
