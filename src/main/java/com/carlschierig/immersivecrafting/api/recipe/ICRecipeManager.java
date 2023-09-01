package com.carlschierig.immersivecrafting.api.recipe;

import com.carlschierig.immersivecrafting.ImmersiveCrafting;
import com.carlschierig.immersivecrafting.api.context.RecipeContext;
import com.carlschierig.immersivecrafting.api.registry.ICRegistries;
import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.GsonHelper;
import org.jetbrains.annotations.NotNull;
import org.quiltmc.qsl.resource.loader.api.reloader.SimpleSynchronousResourceReloader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class ICRecipeManager implements SimpleSynchronousResourceReloader {
    private Map<ICRecipeType<?>, Map<ResourceLocation, ICRecipe>> recipes = ImmutableMap.of();

    /**
     * Returns the first recipe which matches the given {@link RecipeContext}
     *
     * @param context the context against which recipes should be tested.
     * @return The first recipe which matches the predicate, if any.
     */
    @SuppressWarnings("unchecked")
    public <T extends ICRecipe> Optional<T> getRecipe(ICRecipeType<T> type, RecipeContext context) {
        return ((Map<?, T>) recipes.get(type)).values().stream().filter(recipe -> recipe.matches(context)).findFirst();
    }

    @Override
    public void onResourceManagerReload(ResourceManager manager) {
        var builder = ImmutableMap.<ICRecipeType<?>, Map<ResourceLocation, ICRecipe>>builder();
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
