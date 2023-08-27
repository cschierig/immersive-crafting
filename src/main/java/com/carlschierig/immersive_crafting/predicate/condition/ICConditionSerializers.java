package com.carlschierig.immersive_crafting.predicate.condition;

import com.carlschierig.immersive_crafting.ICRegistries;
import com.carlschierig.immersive_crafting.ImmersiveCrafting;
import com.carlschierig.immersive_crafting.predicate.ICPredicate;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

public final class ICConditionSerializers {
	public static final ICConditionSerializer<ICPredicate> PREDICATE = register("predicate", new ICPredicate.Serializer());
	public static final ICConditionSerializer<OrCondition> OR = register("or", new OrCondition.Serializer());
	public static final ICConditionSerializer<AndCondition> AND = register("and", new AndCondition.Serializer());

	private static <T extends ICCondition> ICConditionSerializer<T> register(String id, ICConditionSerializer<T> serializer) {
		return Registry.register(ICRegistries.CONDITION_SERIALIZER, new ResourceLocation(ImmersiveCrafting.MODID, id), serializer);
	}

	public static ICCondition[] fromJson(JsonObject json) throws JsonParseException {
		final var registry = ICRegistries.CONDITION_SERIALIZER;
		ICCondition[] conditions = new ICCondition[json.entrySet().size()];

		int i = 0;
		for (var condition : json.entrySet()) {
			if (!condition.getValue().isJsonObject()) {
				throw new JsonParseException("Conditions must be objects.");
			}
			var name = new ResourceLocation(condition.getKey());
			var value = condition.getValue().getAsJsonObject();

			// parse
			conditions[i] = registry.get(name).fromJson(value);

			i++;
		}

		return conditions;
	}

	public static JsonObject toJson(ICCondition[] conditions) {
		var json = new JsonObject();
		for (var condition : conditions) {
			addCondition(json, condition);
		}
		return json;
	}

	@SuppressWarnings("unchecked")
	private static <T extends ICCondition> void addCondition(JsonObject json, T condition) {
		final var registry = ICRegistries.CONDITION_SERIALIZER;
		var serializer = (ICConditionSerializer<T>) condition.getSerializer();
		json.add(registry.getKey(serializer).toString(), serializer.toJson(condition));
	}
}
