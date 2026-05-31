package com.carlschierig.immersivecrafting.api.data;

import com.carlschierig.immersivecrafting.api.recipe.ICRecipe;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

/**
 * Provides a base for generating immersive crafting recipes.
 * Based on Fabric API's FabricRecipeProvider.
 */
public abstract class ICRecipeProvider implements DataProvider {
    private final PackOutput.PathProvider pathProvider;
    private final CompletableFuture<HolderLookup.Provider> registries;

    /**
     * Create a new ICRecipeProvider. Subclasses should pass on the {@link PackOutput} they were handed.
     *
     * @param output the data output to use.
     */
    public ICRecipeProvider(@NotNull PackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        pathProvider = output.createPathProvider(PackOutput.Target.DATA_PACK, "ic_recipes");
        this.registries = registriesFuture;
    }

    /**
     * Implement this method and offer recipes to the exporters using their builders
     *
     * @param exporter Offer the recipes to this supplier to save them.
     */
    public abstract void buildRecipes(@NotNull BiConsumer<Identifier, ICRecipe> exporter);

    @Override
    public CompletableFuture<?> run(CachedOutput writer) {
        return this.registries.thenCompose(reg -> run(writer, reg));
    }

    protected CompletableFuture<?> run(CachedOutput writer, HolderLookup.Provider registries) {

        Set<Identifier> generatedRecipes = new HashSet<>();
        List<CompletableFuture<?>> list = new ArrayList<>();
        buildRecipes((identifier, recipe) -> {

            if (!generatedRecipes.add(identifier)) {
                throw new IllegalStateException("Duplicate recipe " + identifier);
            }

            list.add(DataProvider.saveStable(writer, registries, ICRecipe.CODEC, recipe, pathProvider.json(identifier)));
        });

        return CompletableFuture.allOf(list.toArray(CompletableFuture<?>[]::new));
    }

    @Override
    @NotNull
    public String getName() {
        return "Immersive Crafting Recipes";
    }
}
