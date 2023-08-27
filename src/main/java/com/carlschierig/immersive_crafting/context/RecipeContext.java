package com.carlschierig.immersive_crafting.context;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

public final class RecipeContext {
	private final Map<Class<? extends ContextHolder>, ContextHolder> holders;

	private RecipeContext(Map<Class<? extends ContextHolder>, ContextHolder> holders) {
		this.holders = holders;
	}

	@SuppressWarnings("unchecked")
	public <T extends ContextHolder> T getHolder(Class<T> holderClass) {
		var holder = holders.get(holderClass);
		if (holder != null) {
			// can only break if the insertion logic is messed with
			return (T) holder;
		}
		throw new NoSuchElementException("No '" + holderClass.getCanonicalName() + "' holder in this recipe context.");
	}

	public static final class Builder {
		private final Map<Class<? extends ContextHolder>, ContextHolder> holders = new HashMap<>();

		public <T extends ContextHolder> Builder putHolder(T holder) {
			holders.put(holder.getClass(), holder);
			return this;
		}

		public RecipeContext build() {
			return new RecipeContext(holders);
		}
	}
}
