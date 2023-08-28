package com.carlschierig.immersive_crafting.context;

import com.carlschierig.immersive_crafting.api.context.ContextHolder;
import net.minecraft.world.entity.player.Player;

public class PlayerContextHolder extends ContextHolder {
	private final Player player;

	public PlayerContextHolder(Player player) {
		this.player = player;
	}

	public Player getPlayer() {
		return player;
	}
}