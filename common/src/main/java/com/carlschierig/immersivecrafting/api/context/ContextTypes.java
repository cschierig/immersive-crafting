package com.carlschierig.immersivecrafting.api.context;

import com.carlschierig.immersivecrafting.api.registry.ICRegistries;
import com.carlschierig.immersivecrafting.impl.registry.ICRegistriesImpl;
import com.carlschierig.immersivecrafting.impl.util.ICUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

/**
 * {@link ContextType}s provided by Immersive Crafting.
 */
public final class ContextTypes {
    public static final ContextType<BlockState> BLOCK_STATE = register("block_state");
    public static final ContextType<Player> PLAYER = register("player");
    public static final ContextType<Level> LEVEL = register("level");

    public static final ContextType<ItemStack> ITEM_STACK = register("item_stack");
    /**
     * This context type is special in the sense that it provides a list of sub contexts which
     * are used to validate the individual ingredients of a recipe. To extract the context for a given ingredient
     * and merge it with the root context, use {@link SimpleRecipeContext#forIngredient(int)}.
     */
    public static final ContextType<List<? extends RecipeContext>> INGREDIENTS = register("ingredients");
    public static final ContextType<BlockPos> BLOCK_POSITION = register("block_position");
    public static final ContextType<Direction> DIRECTION = register("direction");
    public static final ContextType<RandomSource> RANDOM = register("random");

    private static <T> ContextType<T> register(String name) {
        var id = ICUtil.getId(name);
        return ICRegistriesImpl.register(ICRegistries.CONTEXT_TYPE, id, new ContextType<>(id));
    }

    public static void init() {
    }
}
