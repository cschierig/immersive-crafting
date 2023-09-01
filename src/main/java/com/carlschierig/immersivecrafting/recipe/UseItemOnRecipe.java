package com.carlschierig.immersivecrafting.recipe;

import com.carlschierig.immersivecrafting.api.context.RecipeContext;
import com.carlschierig.immersivecrafting.api.context.ValidationContext;
import com.carlschierig.immersivecrafting.api.predicate.ICPredicate;
import com.carlschierig.immersivecrafting.api.recipe.ICRecipe;
import com.carlschierig.immersivecrafting.api.recipe.ICRecipeSerializer;
import com.carlschierig.immersivecrafting.api.recipe.ICRecipeType;
import com.carlschierig.immersivecrafting.api.registry.ICRegistries;
import com.carlschierig.immersivecrafting.context.ContextTypes;
import com.carlschierig.immersivecrafting.predicate.condition.ICConditionSerializers;
import com.carlschierig.immersivecrafting.util.ICByteBufHelper;
import com.carlschierig.immersivecrafting.util.ICGsonHelper;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipe;

import java.util.ArrayList;
import java.util.List;

public class UseItemOnRecipe extends ICRecipe {
    private final ResourceLocation id;
    private final String group;
    private final Ingredient ingredient;
    private final int amount;
    private final ICPredicate predicate;
    private final List<ItemStack> results;
    private final boolean spawnAtPlayer;

    public UseItemOnRecipe(ResourceLocation id,
                           String group,
                           Ingredient ingredient,
                           int amount,
                           ICPredicate predicate,
                           List<ItemStack> results,
                           boolean spawnAtPlayer) {
        if (amount < 1) {
            throw new IllegalArgumentException("amount must not be greater than or equal to 1.");
        }

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
        var player = context.get(ContextTypes.PLAYER);
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

    public static class Serializer implements ICRecipeSerializer<UseItemOnRecipe> {
        private static final String GROUP = "group";
        private static final String INGREDIENT = "ingredient";
        private static final String AMOUNT = "amount";
        private static final String PREDICATE = "predicate";
        private static final String RESULT = "result";
        private static final String SPAWN_AT_PLAYER = "spawn_at_player";

        private static final ValidationContext context = new ValidationContext.Builder()
                .put(ContextTypes.PLAYER)
                .put(ContextTypes.LEVEL)
                .put(ContextTypes.BLOCK).build();

        @Override
        public UseItemOnRecipe fromJson(ResourceLocation id, JsonObject json) {
            String group = GsonHelper.getAsString(json, GROUP, "");
            Ingredient ingredient = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, INGREDIENT), false);
            // todo: check non negative
            int amount = GsonHelper.getAsInt(json, AMOUNT, 1);
            if (amount < 1) {
                throw new JsonParseException("amount must not be greater than or equal to 1.");
            }

            // todo: prettier validation
            var predicateObject = GsonHelper.getAsJsonObject(json, PREDICATE, new JsonObject());
            var predicate = ICConditionSerializers.PREDICATE.fromJson(predicateObject);
            context.validate(predicate);

            var resultJson = GsonHelper.getAsJsonArray(json, RESULT);
            List<ItemStack> results = new ArrayList<>();
            for (var result : resultJson) {
                results.add(ShapedRecipe.itemStackFromJson(result.getAsJsonObject()));
            }
            boolean spawnAtPlayer = GsonHelper.getAsBoolean(json, SPAWN_AT_PLAYER, false);
            return new UseItemOnRecipe(id, group, ingredient, amount, predicate, results, spawnAtPlayer);
        }

        @Override
        public JsonObject toJson(UseItemOnRecipe instance) {
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
                json.addProperty(SPAWN_AT_PLAYER, true);
            }

            json.addProperty("type", ICRegistries.RECIPE_SERIALIZER.getKey(instance.getSerializer()).toString());
            return json;
        }

        @Override
        public UseItemOnRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            String group = buf.readUtf();
            Ingredient ingredient = Ingredient.fromNetwork(buf);
            int amount = buf.readInt();

            var predicate = ICConditionSerializers.PREDICATE.fromNetwork(buf);

            var results = ICByteBufHelper.readList(buf, FriendlyByteBuf::readItem);
            var spawnAtPlayer = buf.readBoolean();

            return new UseItemOnRecipe(id, group, ingredient, amount, predicate, results, spawnAtPlayer);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, UseItemOnRecipe recipe) {
            buf.writeUtf(recipe.group);
            recipe.ingredient.toNetwork(buf);
            buf.writeInt(recipe.amount);

            ICConditionSerializers.PREDICATE.toNetwork(buf, recipe.predicate);
            ICByteBufHelper.writeList(buf, recipe.results, FriendlyByteBuf::writeItem);
            buf.writeBoolean(recipe.spawnAtPlayer);
        }
    }

    public static final class Builder {
        private final ResourceLocation id;
        private String group = "";
        private Ingredient ingredient;
        private int amount = 1;
        private ICPredicate predicate;
        private final List<ItemStack> results = new ArrayList<>();
        private boolean spawnAtPlayer = false;

        public Builder(ResourceLocation id) {
            this.id = id;
        }

        public Builder group(String group) {
            this.group = group;
            return this;
        }

        public Builder ingredient(Ingredient ingredient) {
            this.ingredient = ingredient;
            return this;
        }

        public Builder amount(int amount) {
            if (amount < 1) {
                throw new IllegalArgumentException("amount must not be greater than or equal to 1.");
            }
            this.amount = amount;
            return this;
        }

        public Builder predicate(ICPredicate predicate) {
            this.predicate = predicate;
            return this;
        }

        public Builder addResult(ItemStack result) {
            results.add(result);
            return this;
        }

        public Builder spawnAtPlayer() {
            spawnAtPlayer = true;
            return this;
        }

        public Builder spawnAtPlayer(boolean shouldSpawnAtPlayer) {
            spawnAtPlayer = shouldSpawnAtPlayer;
            return this;
        }

        public UseItemOnRecipe build() {
            return new UseItemOnRecipe(id, group, ingredient, amount, predicate, results, spawnAtPlayer);
        }
    }
}
