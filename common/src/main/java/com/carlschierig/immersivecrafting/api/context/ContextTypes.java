package com.carlschierig.immersivecrafting.api.context;

import com.carlschierig.immersivecrafting.api.registry.ICRegistries;
import com.carlschierig.immersivecrafting.impl.util.ICUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

/**
 * {@link ContextType}s provided by Immersive Crafting.
 */
public final class ContextTypes {
    public static final ContextType<BlockState> BLOCK_STATE = register("block_state");
    public static final ContextType<Player> PLAYER = register("player");
    public static final ContextType<Level> LEVEL = register("level");
    public static final ContextType<ItemStack> ITEM_STACK = register("item_stack");
    public static final ContextType<BlockPos> BLOCK_POSITION = register("block_position");
    public static final ContextType<Direction> DIRECTION = register("direction");

    private static <T> ContextType<T> register(String name) {
        var id = ICUtil.getId(name);
        return Registry.register(ICRegistries.CONTEXT_TYPE, id, new ContextType<>(id));
    }
}
