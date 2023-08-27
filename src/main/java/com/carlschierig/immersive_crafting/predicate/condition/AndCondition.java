package com.carlschierig.immersive_crafting.predicate.condition;

import com.carlschierig.immersive_crafting.context.RecipeContext;
import com.carlschierig.immersive_crafting.serialization.ICByteBufHelper;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditions;

import java.util.Arrays;
import java.util.function.Predicate;

public class AndCondition implements ICCondition {
	protected final ICCondition[] conditions;
	private final Predicate<RecipeContext> predicate;

	public AndCondition(ICCondition[] conditions) {
		this.conditions = conditions;
		predicate = LootItemConditions.andConditions(conditions);
	}

	@Override
	public ICConditionSerializer<?> getSerializer() {
		return ICConditionSerializers.AND;
	}

	@Override
	public boolean test(RecipeContext context) {
		return predicate.test(context);
	}

	public static class Serializer implements ICConditionSerializer<AndCondition> {
		public static final String CONDITIONS = "conditions";

		@Override
		public AndCondition fromJson(JsonObject object) {
			var conditions = ICConditionSerializers.fromJson(GsonHelper.getAsJsonObject(object, CONDITIONS));
			return new AndCondition(conditions);
		}

		@Override
		public JsonObject toJson(AndCondition instance) {
			var conditions = ICConditionSerializers.toJson(instance.conditions);
			var json = new JsonObject();
			json.add(CONDITIONS, conditions);
			return json;
		}

		@Override
		public AndCondition fromNetwork(FriendlyByteBuf buf) {
			var conditions = ICByteBufHelper.readList(buf, ICByteBufHelper::readICCondition);
			return new AndCondition(conditions.toArray(ICCondition[]::new));
		}

		@Override
		public void toNetwork(FriendlyByteBuf buf, AndCondition instance) {
			ICByteBufHelper.writeList(buf, Arrays.asList(instance.conditions), ICByteBufHelper::writeICCondition);
		}
	}
}
