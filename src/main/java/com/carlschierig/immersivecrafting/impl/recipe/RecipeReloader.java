package com.carlschierig.immersivecrafting.impl.recipe;

import com.carlschierig.immersivecrafting.ImmersiveCrafting;
import com.carlschierig.immersivecrafting.api.context.RecipeContext;
import com.carlschierig.immersivecrafting.api.recipe.ICRecipe;
import com.carlschierig.immersivecrafting.api.recipe.ICRecipeType;
import com.carlschierig.immersivecrafting.api.registry.ICRegistries;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.GsonHelper;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.quiltmc.qsl.resource.loader.api.reloader.SimpleSynchronousResourceReloader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@ApiStatus.Internal
public final class RecipeReloader implements SimpleSynchronousResourceReloader {
    private static ImmutableMap<ICRecipeType<?>, ImmutableMap<ResourceLocation, ICRecipe>> recipes = ImmutableMap.of();

    public static <T extends ICRecipe> Optional<T> getRecipe(ICRecipeType<T> type, RecipeContext context) {
        return ((Map<?, T>) recipes.get(type)).values().stream().filter(recipe -> recipe.matches(context)).findFirst();
    }

    public static ImmutableCollection<ICRecipe> getRecipes(ICRecipeType<?> type) {
        return recipes.get(type).values();
    }

    @Override
    public void onResourceManagerReload(ResourceManager manager) {
        var builder = ImmutableMap.<ICRecipeType<?>, ImmutableMap<ResourceLocation, ICRecipe>>builder();
        var maps = new HashMap<ICRecipeType<?>, ImmutableMap.Builder<ResourceLocation, ICRecipe>>();

        for (var type : ICRegistries.RECIPE_TYPE) {
            maps.put(type, new ImmutableMap.Builder<>());
        }
        for (var entry : manager.listResources("ic_recipes", path -> path.getPath().endsWith(".json")).entrySet()) {
            var id = entry.getKey();
            var resource = entry.getValue();

            try (var reader = new InputStreamReader(resource.open())) {
                var json = JsonParser.parseReader(reader).getAsJsonObject();
                var recipe = ICRegistries.RECIPE_SERIALIZER.get(
                        new ResourceLocation(GsonHelper.getAsString(json, "type"))
                ).fromJson(id, json);


                maps.get(recipe.getType()).put(id, recipe);
            } catch (IOException exception) {
                ImmersiveCrafting.LOG.error("Could not load recipes from '{}'", id);
            } catch (JsonSyntaxException exception) {
                ImmersiveCrafting.LOG.error("Could not parse '{}' recipe syntax: {}", id, exception.getMessage());
                throw exception;
            }
        }

        for (var map : maps.entrySet()) {
            builder.put(map.getKey(), map.getValue().build());
        }
        recipes = builder.build();
    }

    @Override
    public @NotNull ResourceLocation getQuiltId() {
        return new ResourceLocation(ImmersiveCrafting.MODID, "recipe_reloader");
    }
}
