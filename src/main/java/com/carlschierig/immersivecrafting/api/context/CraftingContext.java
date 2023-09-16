package com.carlschierig.immersivecrafting.api.context;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Provides information on the location where crafted resources should be put.
 *
 * @param level     The level in which the resources are crafted.
 * @param pos       The position at which the resources should be crafted.
 * @param direction The side of the block where the resources should be crafted.
 */
public record CraftingContext(@NotNull Level level, @NotNull BlockPos pos, @Nullable Direction direction) {

}
