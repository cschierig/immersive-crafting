package com.carlschierig.immersive_crafting.context;

import com.carlschierig.immersive_crafting.api.context.ContextType;
import com.carlschierig.immersive_crafting.api.registry.ICRegistries;
import com.carlschierig.immersive_crafting.util.ICUtil;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public final class ContextTypes {
    public static final ContextType<Block> BLOCK = register("block");
    public static final ContextType<Player> PLAYER = register("player");
    public static final ContextType<Level> LEVEL = register("level");

    private static <T> ContextType<T> register(String name) {
        var id = ICUtil.createResourceLocation(name);
        return Registry.register(ICRegistries.CONTEXT_TYPE, id, new ContextType<>(id));
    }
}
