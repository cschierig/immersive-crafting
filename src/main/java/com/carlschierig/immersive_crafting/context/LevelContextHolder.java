package com.carlschierig.immersive_crafting.context;

import com.carlschierig.immersive_crafting.api.context.ContextHolder;
import net.minecraft.world.level.Level;


public class LevelContextHolder extends ContextHolder {
	private final Level level;

	public LevelContextHolder(Level level) {
		this.level = level;
	}

	public Level getLevel() {
		return level;
	}
}
