package com.carlschierig.immersivecrafting.api.recipe;

import com.carlschierig.immersivecrafting.api.context.ContextTypes;
import com.carlschierig.immersivecrafting.api.context.CraftingContext;
import com.carlschierig.immersivecrafting.api.context.RecipeContext;
import com.carlschierig.immersivecrafting.api.context.ValidationContext;
import com.carlschierig.immersivecrafting.api.predicate.ICPredicate;
import com.carlschierig.immersivecrafting.api.predicate.condition.ICConditionSerializers;
import com.carlschierig.immersivecrafting.api.predicate.condition.ingredient.ICIngredient;
import com.carlschierig.immersivecrafting.api.predicate.condition.ingredient.ICStack;
import com.carlschierig.immersivecrafting.impl.recipe.ICRecipeSerializers;
import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * A recipe which is triggered when a player uses an item on a block.
 */
public class UseItemOnRecipe extends ICRecipe {
    public final @NotNull ICIngredient ingredient;
    private final @NotNull ICPredicate predicate;
    private final List<ICStack> results;
    private final boolean spawnAtPlayer;

    public UseItemOnRecipe(@NotNull ICIngredient ingredient,
                           @NotNull ICPredicate predicate,
                           List<ICStack> results,
                           boolean spawnAtPlayer) {
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

    public static class Serializer implements ICRecipeSerializer<UseItemOnRecipe> {
        public static final MapCodec<UseItemOnRecipe> CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance.group(
                        ICIngredient.CODEC.fieldOf("ingredient").forGetter(recipe -> recipe.ingredient),
                        ICConditionSerializers.PREDICATE.codec().fieldOf("predicate").forGetter(UseItemOnRecipe::getPredicate),
                        Codec.list(ICStack.CODEC).fieldOf("result").forGetter(UseItemOnRecipe::getResults),
                        Codec.BOOL.optionalFieldOf("spawn_at_player", false).forGetter(recipe -> recipe.spawnAtPlayer)
                ).apply(instance, UseItemOnRecipe::new)
        );

        public static final StreamCodec<RegistryFriendlyByteBuf, UseItemOnRecipe> STREAM_CODEC = StreamCodec.composite(
                ICIngredient.STREAM_CODEC,
                recipe -> recipe.ingredient,
                ICConditionSerializers.PREDICATE.streamCodec(),
                UseItemOnRecipe::getPredicate,
                ICStack.STREAM_CODEC.apply(ByteBufCodecs.list()),
                UseItemOnRecipe::getResults,
                ByteBufCodecs.BOOL,
                recipe -> recipe.spawnAtPlayer,
                UseItemOnRecipe::new
        );

        @Override
        public MapCodec<UseItemOnRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, UseItemOnRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }

    /**
     * A builder for creating Use Item On Recipes.
     */
    public static final class Builder {
        private ICIngredient ingredient;
        private ICPredicate predicate;
        private final List<ICStack> results = new ArrayList<>();
        private boolean spawnAtPlayer = false;

        /**
         * Sets the ingredient used by the recipe.
         *
         * @param ingredient the ingredient used by the recipe.
         * @return this Builder.
         */
        public Builder ingredient(ICIngredient ingredient) {
            this.ingredient = ingredient;
            return this;
        }

        /**
         * Sets the predicate used by the recipe.
         *
         * @param predicate the predicate used by the recipe.
         * @return this Builder.
         */
        public Builder predicate(ICPredicate predicate) {
            context.validate(predicate);
            this.predicate = predicate;
            return this;
        }

        /**
         * Adds a result stack to the recipe.
         *
         * @param result the result added to the recipe.
         * @return this Builder.
         */
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
            return new UseItemOnRecipe(ingredient, predicate, results, spawnAtPlayer);
        }
    }
}
