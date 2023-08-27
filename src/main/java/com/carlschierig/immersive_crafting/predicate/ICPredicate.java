package com.carlschierig.immersive_crafting.predicate;

import com.carlschierig.immersive_crafting.predicate.condition.AndCondition;
import com.carlschierig.immersive_crafting.predicate.condition.ICCondition;
import com.carlschierig.immersive_crafting.predicate.condition.ICConditionSerializer;
import com.carlschierig.immersive_crafting.predicate.condition.ICConditionSerializers;
import com.carlschierig.immersive_crafting.serialization.ICByteBufHelper;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;

import java.util.Arrays;

public class ICPredicate extends AndCondition {
	public ICPredicate(ICCondition[] conditions) {
		super(conditions);
	}

	@Override
	public ICConditionSerializer<?> getSerializer() {
		return ICConditionSerializers.PREDICATE;
	}

	public static class Serializer implements ICConditionSerializer<ICPredicate> {

		@Override
		public ICPredicate fromJson(JsonObject json) {
			var conditions = ICConditionSerializers.fromJson(json);
			return new ICPredicate(conditions);
		}

		@Override
		public JsonObject toJson(ICPredicate instance) {
			return ICConditionSerializers.toJson(instance.conditions);
		}

		@Override
		public ICPredicate fromNetwork(FriendlyByteBuf buf) {
			var conditions = ICByteBufHelper.readList(buf, ICByteBufHelper::readICCondition);
			return new ICPredicate(conditions.toArray(ICCondition[]::new));
		}

		@Override
		public void toNetwork(FriendlyByteBuf buf, ICPredicate instance) {
			ICByteBufHelper.writeList(buf, Arrays.asList(instance.conditions), ICByteBufHelper::writeICCondition);
		}
	}
}
