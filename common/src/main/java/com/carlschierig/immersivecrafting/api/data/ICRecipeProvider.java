package com.carlschierig.immersivecrafting.api.data;

import com.carlschierig.immersivecrafting.api.recipe.ICRecipe;
import com.carlschierig.immersivecrafting.api.recipe.ICRecipeSerializer;
import com.google.gson.JsonObject;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 * Provides a base for generating immersive crafting recipes.
 * Based on Fabric API's FabricRecipeProvider.
 */
public abstract class ICRecipeProvider implements DataProvider {
    private final PackOutput.PathProvider pathProvider;

    /**
     * Create a new ICRecipeProvider. Subclasses should pass on the {@link PackOutput} they were handed.
     *
     * @param output the data output to use.
     */
    public ICRecipeProvider(@NotNull PackOutput output) {
        pathProvider = output.createPathProvider(PackOutput.Target.DATA_PACK, "ic_recipes");
    }

    /**
     * Implement this method and offer recipes to the exporters using their builders
     *
     * @param exporter Offer the recipes to this supplier to save them.
     */
    public abstract void buildRecipes(@NotNull Consumer<ICRecipe> exporter);

    @Override
    public CompletableFuture<?> run(CachedOutput writer) {
        Set<ResourceLocation> generatedRecipes = new HashSet<>();
        List<CompletableFuture<?>> list = new ArrayList<>();
        buildRecipes(recipe -> {
            ResourceLocation identifier = recipe.getId();

            if (!generatedRecipes.add(identifier)) {
                throw new IllegalStateException("Duplicate recipe " + identifier);
            }

            var recipeJson = serializeRecipe(recipe);
            list.add(DataProvider.saveStable(writer, recipeJson, pathProvider.json(identifier)));
        });

        return CompletableFuture.allOf(list.toArray(CompletableFuture<?>[]::new));
    }

    @SuppressWarnings("unchecked")
    private <T extends ICRecipe> JsonObject serializeRecipe(T recipe) {
        var serializer = (ICRecipeSerializer<T>) recipe.getSerializer();
        return serializer.toJson(recipe);
    }

    @Override
    @NotNull
    public String getName() {
        return "Immersive Crafting Recipes";
    }
}
