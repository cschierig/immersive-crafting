package com.carlschierig.immersivecrafting.api.context;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;

public record CraftingContext(Level level, BlockPos pos, Direction direction) {

}
