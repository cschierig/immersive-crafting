package com.carlschierig.immersivecrafting.api.context;

import com.carlschierig.immersivecrafting.api.registry.ICRegistries;
import com.carlschierig.immersivecrafting.impl.util.ICUtil;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

/**
 * {@link ContextType}s provided by Immersive Crafting.
 */
public final class ContextTypes {
    public static final ContextType<Block> BLOCK = register("block");
    public static final ContextType<Player> PLAYER = register("player");
    public static final ContextType<Level> LEVEL = register("level");

    private static <T> ContextType<T> register(String name) {
        var id = ICUtil.createResourceLocation(name);
        return Registry.register(ICRegistries.CONTEXT_TYPE, id, new ContextType<>(id));
    }
}
