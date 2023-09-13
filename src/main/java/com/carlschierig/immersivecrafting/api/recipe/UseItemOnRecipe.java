package com.carlschierig.immersivecrafting.api.recipe;

import com.carlschierig.immersivecrafting.api.context.ContextTypes;
import com.carlschierig.immersivecrafting.api.context.CraftingContext;
import com.carlschierig.immersivecrafting.api.context.RecipeContext;
import com.carlschierig.immersivecrafting.api.context.ValidationContext;
import com.carlschierig.immersivecrafting.api.predicate.ICPredicate;
import com.carlschierig.immersivecrafting.api.predicate.condition.ICConditionSerializers;
import com.carlschierig.immersivecrafting.api.predicate.condition.ingredient.ICIngredient;
import com.carlschierig.immersivecrafting.api.predicate.condition.ingredient.ICStack;
import com.carlschierig.immersivecrafting.api.registry.ICRegistries;
import com.carlschierig.immersivecrafting.api.serialization.ICByteBufHelper;
import com.carlschierig.immersivecrafting.api.serialization.ICGsonHelper;
import com.carlschierig.immersivecrafting.impl.recipe.ICRecipeSerializers;
import com.carlschierig.immersivecrafting.impl.util.ICByteBufHelperImpl;
import com.google.common.collect.ImmutableList;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class UseItemOnRecipe extends ICRecipe {
    private final ResourceLocation id;
    private final String group;
    private final @NotNull ICIngredient ingredient;
    private final @NotNull ICPredicate predicate;
    private final List<ICStack> results;
    private final boolean spawnAtPlayer;

    public UseItemOnRecipe(ResourceLocation id,
                           String group,
                           @NotNull ICIngredient ingredient,
                           @NotNull ICPredicate predicate,
                           List<ICStack> results,
                           boolean spawnAtPlayer) {

        this.id = id;
        this.group = group;
        this.ingredient = ingredient;
        this.predicate = predicate;
        this.results = results;
        this.spawnAtPlayer = spawnAtPlayer;
    }

    @Override
    public boolean matches(RecipeContext context) {
        return ingredient.test(context) && predicate.test(context);
    }

    @Override
    public List<ICStack> getResults() {
        List<ICStack> resultList = new ArrayList<>(results.size());
        for (var stack : results) {
            resultList.add(stack.copy());
        }
        return resultList;
    }

    @Override
    public void craft(RecipeContext recipeContext, CraftingContext craftingContext) {
        for (var stack : results) {
            stack.craft(recipeContext, craftingContext);
        }
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public final ImmutableList<ICIngredient> getIngredients() {
        return ImmutableList.of(ingredient);
    }

    @Override
    public @NotNull ICPredicate getPredicate() {
        return predicate;
    }

    @Override
    public ICRecipeSerializer<?> getSerializer() {
        return ICRecipeSerializers.USE_ITEM_RECIPE;
    }

    @Override
    public ICRecipeType<?> getType() {
        return ICRecipeTypes.USE_ITEM;
    }

    private static final ValidationContext context = new ValidationContext.Builder()
            .put(ContextTypes.PLAYER)
            .put(ContextTypes.LEVEL)
            .put(ContextTypes.BLOCK_STATE)
            .put(ContextTypes.ITEM_STACK)
            .put(ContextTypes.BLOCK_POSITION)
            .put(ContextTypes.DIRECTION)
            .build();

    @Override
    public ValidationContext getRequirements() {
        return context;
    }

    private static final ValidationContext ingredientRequirements = ValidationContext.of(ContextTypes.ITEM_STACK);

    @Override
    public ValidationContext getIngredientRequirements() {
        return ingredientRequirements;
    }

    public boolean spawnAtPlayer() {
        return spawnAtPlayer;
    }

    public static class Serializer implements ICRecipeSerializer<UseItemOnRecipe> {
        private static final String GROUP = "group";
        private static final String INGREDIENT = "ingredient";
        private static final String PREDICATE = "predicate";
        private static final String RESULT = "result";
        private static final String SPAWN_AT_PLAYER = "spawn_at_player";


        @Override
        public UseItemOnRecipe fromJson(ResourceLocation id, JsonObject json) {
            String group = GsonHelper.getAsString(json, GROUP, "");

            var ingredient = ICGsonHelper.getAsIngredient(GsonHelper.getAsJsonObject(json, INGREDIENT));
            ingredientRequirements.validate(ingredient);

            var predicateObject = GsonHelper.getAsJsonObject(json, PREDICATE, new JsonObject());
            var predicate = ICConditionSerializers.PREDICATE.fromJson(predicateObject);
            context.validate(predicate);

            var resultJson = GsonHelper.getAsJsonArray(json, RESULT);
            List<ICStack> results = new ArrayList<>();
            for (var result : resultJson) {
                results.add(ICGsonHelper.getAsStack(result.getAsJsonObject()));
            }
            boolean spawnAtPlayer = GsonHelper.getAsBoolean(json, SPAWN_AT_PLAYER, false);
            return new UseItemOnRecipe(id, group, ingredient, predicate, results, spawnAtPlayer);
        }

        @Override
        public JsonObject toJson(UseItemOnRecipe instance) {
            JsonObject json = new JsonObject();
            if (!instance.group.isEmpty()) {
                json.addProperty("group", instance.group);
            }
            json.add(INGREDIENT, ICGsonHelper.conditionToJson(instance.ingredient));
            json.add(PREDICATE, ICConditionSerializers.PREDICATE.toJson(instance.predicate));

            var resultArray = ICGsonHelper.conditionsToJson(instance.results);
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

            var ingredient = (ICIngredient) ICByteBufHelper.readICCondition(buf);
            var predicate = ICConditionSerializers.PREDICATE.fromNetwork(buf);

            var results = ICByteBufHelperImpl.readList(buf, buf1 -> (ICStack) ICByteBufHelper.readICCondition(buf1));
            var spawnAtPlayer = buf.readBoolean();

            return new UseItemOnRecipe(id, group, ingredient, predicate, results, spawnAtPlayer);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, UseItemOnRecipe recipe) {
            buf.writeUtf(recipe.group);

            ICByteBufHelper.writeICCondition(buf, recipe.ingredient);
            ICConditionSerializers.PREDICATE.toNetwork(buf, recipe.predicate);

            ICByteBufHelperImpl.writeList(buf, recipe.results, ICByteBufHelper::writeICCondition);
            buf.writeBoolean(recipe.spawnAtPlayer);
        }
    }

    public static final class Builder {
        private final ResourceLocation id;
        private String group = "";
        private ICIngredient ingredient;
        private ICPredicate predicate;
        private final List<ICStack> results = new ArrayList<>();
        private boolean spawnAtPlayer = false;

        public Builder(ResourceLocation id) {
            this.id = id;
        }

        public Builder group(String group) {
            this.group = group;
            return this;
        }

        public Builder ingredient(ICIngredient ingredient) {
            this.ingredient = ingredient;
            return this;
        }

        public Builder predicate(ICPredicate predicate) {
            context.validate(predicate);
            this.predicate = predicate;
            return this;
        }

        public Builder addResult(ICStack result) {
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
            if (ingredient == null) {
                throw new IllegalStateException("ingredient must be set");
            }
            if (predicate == null) {
                throw new IllegalStateException("predicate must be set");
            }
            return new UseItemOnRecipe(id, group, ingredient, predicate, results, spawnAtPlayer);
        }
    }
}
