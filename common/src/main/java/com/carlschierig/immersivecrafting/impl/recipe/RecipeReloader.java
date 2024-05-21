package com.carlschierig.immersivecrafting.impl.recipe;

import com.carlschierig.immersivecrafting.api.context.RecipeContext;
import com.carlschierig.immersivecrafting.api.recipe.ICRecipe;
import com.carlschierig.immersivecrafting.api.recipe.ICRecipeHolder;
import com.carlschierig.immersivecrafting.api.recipe.ICRecipeType;
import com.carlschierig.immersivecrafting.impl.network.S2CPackets;
import com.carlschierig.immersivecrafting.impl.util.ICUtil;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.mojang.serialization.JsonOps;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import org.jetbrains.annotations.ApiStatus;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApiStatus.Internal
public class RecipeReloader extends ICRecipeManagerImpl implements ResourceManagerReloadListener {
    private Multimap<ICRecipeType<?>, ICRecipeHolder<?>> recipes = ImmutableMultimap.of();

    public RecipeReloader() {
        ICRecipeManagerImpl.INSTANCE = this;
    }

    @SuppressWarnings("unchecked")
    public <T extends ICRecipe> Optional<ICRecipeHolder<T>> getRecipe(ICRecipeType<T> type, RecipeContext context) {
        return recipes.get(type).stream().filter(recipe -> recipe.recipe().matches(context)).map(val -> (ICRecipeHolder<T>) val).findFirst();
    }

    @SuppressWarnings("unchecked")
    public <T extends ICRecipe> Collection<ICRecipeHolder<T>> getRecipes(ICRecipeType<T> type) {
        return recipes.get(type).stream().map(val -> (ICRecipeHolder<T>) val).collect(Collectors.toList());
    }

    @Override
    public void onResourceManagerReload(ResourceManager manager) {
        List<ICRecipeHolder<?>> recipes = new ArrayList<>();
        for (var entry : manager.listResources("ic_recipes", path -> path.getPath().endsWith(".json")).entrySet()) {
            var id = entry.getKey();
            var resource = entry.getValue();

            try (var reader = new InputStreamReader(resource.open())) {
                var json = JsonParser.parseReader(reader);

                var recipe = ICRecipe.CODEC.parse(JsonOps.INSTANCE, json);

                recipes.add(new ICRecipeHolder<>(id, recipe.getOrThrow(JsonParseException::new)));
            } catch (IOException exception) {
                ICUtil.LOG.error("Could not load recipes from '{}'", id);
            } catch (JsonSyntaxException exception) {
                ICUtil.LOG.error("Could not parse '{}' recipe syntax: {}", id, exception.getMessage());
                throw exception;
            }
        }
        setRecipes(recipes);
        S2CPackets.INSTANCE.sendRecipes();
    }

    public void setRecipes(Iterable<ICRecipeHolder<?>> recipes) {
        var builder = ImmutableMultimap.<ICRecipeType<?>, ICRecipeHolder<?>>builder();

        for (var holder : recipes) {
            var type = holder.recipe().getType();
            builder.put(type, holder);
        }
        this.recipes = builder.build();
    }

    public Collection<ICRecipeHolder<?>> getRecipes() {
        return recipes.values();
    }
}
