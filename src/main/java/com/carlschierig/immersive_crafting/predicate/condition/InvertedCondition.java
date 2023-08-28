package com.carlschierig.immersive_crafting.predicate.condition;

import com.carlschierig.immersive_crafting.api.context.RecipeContext;
import com.carlschierig.immersive_crafting.api.predicate.condition.ICCondition;
import com.carlschierig.immersive_crafting.api.predicate.condition.ICConditionSerializer;
import com.carlschierig.immersive_crafting.serialization.ICByteBufHelper;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;


public class InvertedCondition implements ICCondition {
	private final ICCondition original;

	public InvertedCondition(ICCondition original) {
		this.original = original;
	}

	@Override
	public boolean test(RecipeContext context) {
		return !original.test(context);
	}

	@Override
	public ICConditionSerializer<?> getSerializer() {
		return ICConditionSerializers.INVERT;
	}

	public static final class Serializer implements ICConditionSerializer<InvertedCondition> {

		@Override
		public InvertedCondition fromJson(JsonObject json) {
			// TODO: error handling
			var first = json.entrySet().stream().findFirst().get();
			var type = new ResourceLocation(first.getKey());
			var condition = first.getValue().getAsJsonObject();

			return new InvertedCondition(ICConditionSerializers.fromJson(type, condition));
		}

		@Override
		public JsonObject toJson(InvertedCondition instance) {
			var json = new JsonObject();

			var condition = instance.original;
			ICConditionSerializers.addCondition(json, condition);

			return json;
		}

		@Override
		public InvertedCondition fromNetwork(FriendlyByteBuf buf) {
			var condition = ICByteBufHelper.readICCondition(buf);
			return new InvertedCondition(condition);
		}

		@Override
		public void toNetwork(FriendlyByteBuf buf, InvertedCondition instance) {
			ICByteBufHelper.writeICCondition(buf, instance.original);
		}
	}
}
