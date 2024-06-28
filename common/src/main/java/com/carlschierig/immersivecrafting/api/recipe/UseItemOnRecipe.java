package com.carlschierig.immersivecrafting.api.recipe;

import com.carlschierig.immersivecrafting.api.context.ContextTypes;
import com.carlschierig.immersivecrafting.api.context.RecipeContext;
import com.carlschierig.immersivecrafting.api.context.ValidationContext;
import com.carlschierig.immersivecrafting.api.predicate.ICPredicate;
import com.carlschierig.immersivecrafting.api.predicate.condition.ICConditionSerializers;
import com.carlschierig.immersivecrafting.api.predicate.condition.ingredient.ICIngredient;
import com.carlschierig.immersivecrafting.api.predicate.condition.stack.ICStack;
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
 * The recipe uses one to two ingredients as inputs.
 * <p>
 * The ingredients are determined as follows:
 * <ol>
 *     <li>The item in the player's main hand.</li>
 *     <li>The item in the player's offhand.</li>
 * </ol>
 */
public class UseItemOnRecipe extends ICRecipe {
    private final @NotNull ImmutableList<@NotNull ICIngredient> ingredients;
    private final @NotNull ICPredicate predicate;
    private final List<ICStack> results;
    private final boolean fromFace;

    public UseItemOnRecipe(@NotNull List<@NotNull ICIngredient> ingredients,
                           @NotNull ICPredicate predicate,
                           List<ICStack> results,
                           boolean fromFace) {
        this.ingredients = ImmutableList.copyOf(ingredients);
        this.predicate = predicate;
        this.results = results;
        this.fromFace = fromFace;
    }

    @Override
    public boolean matches(RecipeContext context) {
        for (int i = 0; i < ingredients.size(); i++) {
            if (!ingredients.get(i).test(context.forIngredient(i))) {
                return false;
            }
        }
        for (var result : results) {
            if (!result.test(context)) {
                return false;
            }
        }

        return predicate.test(context);
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
    public void craft(RecipeContext recipeContext) {
        for (int i = 0; i < ingredients.size(); i++) {
            ingredients.get(i).consume(recipeContext.forIngredient(i));
        }
        for (var stack : results) {
            stack.craft(recipeContext);
        }
    }

    @Override
    public void consumeIngredients(RecipeContext context) {
        for (int i = 0; i < ingredients.size(); i++) {
            ingredients.get(i).test(context.forIngredient(i));
        }
    }

    @Override
    public final ImmutableList<ICIngredient> getIngredients() {
        return ingredients;
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
            .put(ContextTypes.INGREDIENTS)
            .put(ContextTypes.BLOCK_POSITION)
            .put(ContextTypes.DIRECTION)
            .put(ContextTypes.RANDOM)
            .build();

    @Override
    public ValidationContext getPredicateRequirements() {
        return context;
    }

    private static final ValidationContext INGREDIENT_REQUIREMENTS = ValidationContext.of(ContextTypes.ITEM_STACK);

    @Override
    public ValidationContext getIngredientRequirements() {
        return INGREDIENT_REQUIREMENTS;
    }

    public static class Serializer implements ICRecipeSerializer<UseItemOnRecipe> {
        public static final MapCodec<UseItemOnRecipe> CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance.group(
                        Codec.list(ICIngredient.CODEC).fieldOf("ingredients").validate(ingredients -> validateIngredients(ingredients, INGREDIENT_REQUIREMENTS, 1, 2)).forGetter(recipe -> recipe.ingredients),
                        ICConditionSerializers.PREDICATE.codec().fieldOf("predicate").validate(predicate -> validatePredicate(predicate, context)).forGetter(UseItemOnRecipe::getPredicate),
                        Codec.list(ICStack.CODEC).fieldOf("result").validate(results -> validateResults(results, context)).forGetter(UseItemOnRecipe::getResults),
                        Codec.BOOL.optionalFieldOf("from_face", true).forGetter(recipe -> recipe.fromFace)
                ).apply(instance, UseItemOnRecipe::new)
        );

        public static final StreamCodec<RegistryFriendlyByteBuf, UseItemOnRecipe> STREAM_CODEC = StreamCodec.composite(
                ICIngredient.STREAM_CODEC.apply(ByteBufCodecs.list()),
                recipe -> recipe.ingredients,
                ICConditionSerializers.PREDICATE.streamCodec(),
                UseItemOnRecipe::getPredicate,
                ICStack.STREAM_CODEC.apply(ByteBufCodecs.list()),
                UseItemOnRecipe::getResults,
                ByteBufCodecs.BOOL,
                recipe -> recipe.fromFace,
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
        private final ICIngredient[] ingredients = new ICIngredient[2];
        private ICPredicate predicate;
        private final List<ICStack> results = new ArrayList<>();
        private boolean fromFace = true;

        /**
         * Sets the ingredient which has to be in the main hand.
         *
         * @param ingredient the ingredient used by the recipe.
         * @return this Builder.
         */
        public Builder mainHandIngredient(ICIngredient ingredient) {
            this.ingredients[0] = ingredient;
            return this;
        }

        /**
         * Sets the ingredient which has to be in the offhand.
         *
         * @param ingredient the ingredient used by the recipe.
         * @return this Builder.
         */
        public Builder offHandIngredient(ICIngredient ingredient) {
            this.ingredients[1] = ingredient;
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

        public Builder fromFace(boolean fromFace) {
            this.fromFace = fromFace;
            return this;
        }

        public UseItemOnRecipe build() {
            if (ingredients[0] == null && ingredients[1] == null) {
                throw new IllegalStateException("A main hand or offhand ingredient must be set.");
            }
            if (predicate == null) {
                throw new IllegalStateException("predicate must be set");
            }
            var ingredients = new ArrayList<ICIngredient>();
            for (var ing : this.ingredients) {
                if (ing != null) {
                    ingredients.add(ing);
                }
            }
            return new UseItemOnRecipe(ingredients, predicate, results, fromFace);
        }
    }
}
