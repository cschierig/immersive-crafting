package com.carlschierig.immersive_crafting.crafting;

import com.carlschierig.immersive_crafting.context.PlayerContextHolder;
import com.carlschierig.immersive_crafting.context.RecipeContext;
import com.carlschierig.immersive_crafting.predicate.ICPredicate;
import com.carlschierig.immersive_crafting.predicate.condition.ICConditionSerializers;
import com.carlschierig.immersive_crafting.serialization.ICByteBufHelper;
import com.carlschierig.immersive_crafting.serialization.ICGsonHelper;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipe;

import java.util.ArrayList;
import java.util.List;

public class UseItemRecipe extends ICRecipe {
	private final ResourceLocation id;
	private final String group;
	private final Ingredient ingredient;
	private final int amount;
	private final ICPredicate predicate;
	private final List<ItemStack> results;
	private final boolean spawnAtPlayer;

	public UseItemRecipe(ResourceLocation id,
						 String group,
						 Ingredient ingredient,
						 int amount,
						 ICPredicate predicate,
						 List<ItemStack> results,
						 boolean spawnAtPlayer) {
		this.id = id;
		this.group = group;
		this.ingredient = ingredient;
		this.amount = amount;
		this.predicate = predicate;
		this.results = results;
		this.spawnAtPlayer = spawnAtPlayer;
	}

	@Override
	public boolean matches(RecipeContext context) {
		var player = context.getHolder(PlayerContextHolder.class).getPlayer();
		var inventory = player.getInventory();
		var selected = inventory.getSelected();
		return ingredient.test(selected)
			&& selected.getCount() >= amount
			&& predicate.test(context);
	}

	@Override
	public List<ItemStack> assembleResults(RecipeContext context) {
		List<ItemStack> resultList = new ArrayList<>(results.size());
		for (var stack : results) {
			resultList.add(stack.copy());
		}
		return resultList;
	}

	@Override
	public ResourceLocation getId() {
		return id;
	}

	@Override
	public ICRecipeSerializer<?> getSerializer() {
		return ICRecipeSerializers.USE_ITEM_RECIPE;
	}

	@Override
	public ICRecipeType<?> getType() {
		return ICRecipeTypes.USE_ITEM;
	}

	public int getAmount() {
		return amount;
	}

	public boolean spawnAtPlayer() {
		return spawnAtPlayer;
	}

	public static class Serializer implements ICRecipeSerializer<UseItemRecipe> {
		private static final String GROUP = "group";
		private static final String INGREDIENT = "ingredient";
		private static final String AMOUNT = "amount";
		private static final String PREDICATE = "predicate";
		private static final String RESULT = "result";
		private static final String SPAWN_AT_PLAYER = "spawn_at_player";

		@Override
		public UseItemRecipe fromJson(ResourceLocation id, JsonObject json) {
			String group = GsonHelper.getAsString(json, GROUP, "");
			Ingredient ingredient = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, INGREDIENT), false);
			// todo: check non negative
			int amount = GsonHelper.getAsInt(json, AMOUNT, 1);

			var predicateObject = GsonHelper.getAsJsonObject(json, PREDICATE, new JsonObject());
			var predicate = ICConditionSerializers.PREDICATE.fromJson(predicateObject);

			var resultJson = GsonHelper.getAsJsonArray(json, RESULT);
			List<ItemStack> results = new ArrayList<>();
			for (var result : resultJson) {
				results.add(ShapedRecipe.itemStackFromJson(result.getAsJsonObject()));
			}
			boolean spawnAtPlayer = GsonHelper.getAsBoolean(json, SPAWN_AT_PLAYER, false);
			return new UseItemRecipe(id, group, ingredient, amount, predicate, results, spawnAtPlayer);
		}

		@Override
		public JsonObject toJson(UseItemRecipe instance) {
			JsonObject json = new JsonObject();
			if (!instance.group.isEmpty()) {
				json.addProperty("group", instance.group);
			}
			json.add(INGREDIENT, instance.ingredient.toJson());
			if (instance.amount > 1) {
				json.addProperty(AMOUNT, instance.amount);
			}
			json.add(PREDICATE, ICConditionSerializers.PREDICATE.toJson(instance.predicate));

			var resultArray = new JsonArray();
			for (var resultStack : instance.results) {
				resultArray.add(ICGsonHelper.itemStackToJson(resultStack));
			}
			json.add(RESULT, resultArray);

			if (instance.spawnAtPlayer) {
				json.addProperty(SPAWN_AT_PLAYER, instance.spawnAtPlayer);
			}

			return json;
		}

		@Override
		public UseItemRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
			String group = buf.readUtf();
			Ingredient ingredient = Ingredient.fromNetwork(buf);
			int amount = buf.readInt();

			var predicate = ICConditionSerializers.PREDICATE.fromNetwork(buf);

			var results = ICByteBufHelper.readList(buf, FriendlyByteBuf::readItem);
			var spawnAtPlayer = buf.readBoolean();

			return new UseItemRecipe(id, group, ingredient, amount, predicate, results, spawnAtPlayer);
		}

		@Override
		public void toNetwork(FriendlyByteBuf buf, UseItemRecipe recipe) {
			buf.writeUtf(recipe.group);
			recipe.ingredient.toNetwork(buf);
			buf.writeInt(recipe.amount);

			ICConditionSerializers.PREDICATE.toNetwork(buf, recipe.predicate);
			ICByteBufHelper.writeList(buf, recipe.results, FriendlyByteBuf::writeItem);
			buf.writeBoolean(recipe.spawnAtPlayer);
		}
	}
}
