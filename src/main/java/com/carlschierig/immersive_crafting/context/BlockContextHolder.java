package com.carlschierig.immersive_crafting.context;

import com.carlschierig.immersive_crafting.api.context.ContextHolder;
import net.minecraft.world.level.block.Block;

public class BlockContextHolder extends ContextHolder {
	private final Block block;

	public BlockContextHolder(Block block) {
		this.block = block;
	}

	public Block getBlock() {
		return block;
	}
}
