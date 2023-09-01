package com.carlschierig.immersivecrafting.api.data;

import com.carlschierig.immersivecrafting.api.recipe.ICRecipe;
import com.carlschierig.immersivecrafting.api.recipe.ICRecipeSerializer;
import com.google.gson.JsonObject;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 * Provides a base for generating immersive crafting recipes.
 * Based on {@link net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider}.
 *
 * @author Carl Schierig
 * @version 1.0
 * @see net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider
 */
public abstract class ICRecipeProvider implements DataProvider {
    protected final FabricDataOutput output;
    private final PackOutput.PathProvider pathProvider;

    public ICRecipeProvider(FabricDataOutput output) {
        this.output = output;
        pathProvider = output.createPathProvider(PackOutput.Target.DATA_PACK, "ic_recipes");
    }

    /**
     * Implement this method and offer recipes to the exporters using their builders
     *
     * @param exporter
     */
    public abstract void buildRecipes(Consumer<ICRecipe> exporter);

    @Override
    public CompletableFuture<?> run(CachedOutput writer) {
        Set<ResourceLocation> generatedRecipes = new HashSet<>();
        List<CompletableFuture<?>> list = new ArrayList<>();
        buildRecipes(recipe -> {
            ResourceLocation identifier = getRecipeIdentifier(recipe.getId());

            if (!generatedRecipes.add(identifier)) {
                throw new IllegalStateException("Duplicate recipe " + identifier);
            }

            // TODO: ask quilt to provide FabricDataGenConditions api
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
    public String getName() {
        return "Immersive Crafting Recipes";
    }

    /**
     * Override this method to change the recipe identifier. The default implementation normalizes the namespace to the mod ID.
     */
    protected ResourceLocation getRecipeIdentifier(ResourceLocation identifier) {
        return new ResourceLocation(output.getModId(), identifier.getPath());
    }
}
