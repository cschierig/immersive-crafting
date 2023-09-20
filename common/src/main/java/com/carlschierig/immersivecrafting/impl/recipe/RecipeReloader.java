package com.carlschierig.immersivecrafting.impl.recipe;

import com.carlschierig.immersivecrafting.api.context.RecipeContext;
import com.carlschierig.immersivecrafting.api.recipe.ICRecipe;
import com.carlschierig.immersivecrafting.api.recipe.ICRecipeType;
import com.carlschierig.immersivecrafting.api.registry.ICRegistries;
import com.carlschierig.immersivecrafting.impl.network.S2CPackets;
import com.carlschierig.immersivecrafting.impl.util.ICUtil;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.util.GsonHelper;
import org.jetbrains.annotations.ApiStatus;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApiStatus.Internal
public class RecipeReloader extends ICRecipeManagerImpl implements ResourceManagerReloadListener {
    private ImmutableMap<ICRecipeType<?>, ImmutableMap<ResourceLocation, ICRecipe>> recipes = ImmutableMap.of();

    public RecipeReloader() {
        ICRecipeManagerImpl.INSTANCE = this;
    }

    @SuppressWarnings("unchecked")
    public <T extends ICRecipe> Optional<T> getRecipe(ICRecipeType<T> type, RecipeContext context) {
        return ((ImmutableMap<?, T>) recipes.get(type)).values().stream().filter(recipe -> recipe.matches(context)).findFirst();
    }

    public ImmutableCollection<ICRecipe> getRecipes(ICRecipeType<?> type) {
        return recipes.get(type).values();
    }

    @Override
    public void onResourceManagerReload(ResourceManager manager) {
        List<ICRecipe> recipes = new ArrayList<>();
        for (var entry : manager.listResources("ic_recipes", path -> path.getPath().endsWith(".json")).entrySet()) {
            var id = entry.getKey();
            var resource = entry.getValue();

            try (var reader = new InputStreamReader(resource.open())) {
                var json = JsonParser.parseReader(reader).getAsJsonObject();
                var recipe = ICRegistries.RECIPE_SERIALIZER.get(
                        new ResourceLocation(GsonHelper.getAsString(json, "type"))
                ).fromJson(id, json);
                recipes.add(recipe);
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

    public void setRecipes(Iterable<ICRecipe> recipes) {
        var builder = ImmutableMap.<ICRecipeType<?>, ImmutableMap<ResourceLocation, ICRecipe>>builder();
        var maps = new HashMap<ICRecipeType<?>, ImmutableMap.Builder<ResourceLocation, ICRecipe>>();

        for (var type : ICRegistries.RECIPE_TYPE) {
            maps.put(type, new ImmutableMap.Builder<>());
        }
        for (var recipe : recipes) {
            var id = recipe.getId();
            maps.get(recipe.getType()).put(id, recipe);
        }

        for (var map : maps.entrySet()) {
            builder.put(map.getKey(), map.getValue().build());
        }
        this.recipes = builder.build();
    }

    public List<ICRecipe> getRecipes() {
        return recipes.values().stream().flatMap(map -> map.values().stream()).collect(Collectors.toList());
    }
}
