package com.carlschierig.immersivecrafting.compat.emi;

import com.carlschierig.immersivecrafting.api.recipe.ICRecipeType;
import com.carlschierig.immersivecrafting.api.registry.ICRegistries;
import com.carlschierig.immersivecrafting.compat.emi.category.DefaultCategoryRenderer;
import com.carlschierig.immersivecrafting.mixin.InventoryMenuAccessor;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.recipe.handler.StandardRecipeHandler;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ICEmiPlugin implements EmiPlugin {
    private static final Map<ResourceLocation, EmiRecipeCategory> categories = new HashMap<>();

    @Override
    public void register(EmiRegistry registry) {
        // registerCategories(registry);
        // registerRecipeHandlers(registry);
    }

    private void registerCategories(EmiRegistry registry) {
        for (var entry : ICRegistries.RECIPE_TYPE.entrySet()) {
            var key = entry.getKey().location();
            var value = entry.getValue();

            // TODO: add proper icons
            var category = new EmiRecipeCategory(key, DefaultCategoryRenderer.INSTANCE);
            categories.put(key, category);

            registry.addCategory(category);
        }
    }

    private void registerRecipeHandlers(EmiRegistry registry) {
        for (var entry : ICRegistries.RECIPE_TYPE.entrySet()) {
            var key = entry.getKey().location();
            var value = entry.getValue();
            //registerRecipeHandler(registry, key);
            registerRecipes(registry, key, value);
        }
    }

    private void registerRecipeHandler(EmiRegistry registry, ResourceLocation type) {
        registry.addRecipeHandler(null,
                new StandardRecipeHandler<InventoryMenu>() {
                    @Override
                    public boolean supportsRecipe(EmiRecipe recipe) {
                        return recipe.getCategory().equals(categories.get(type));
                    }

                    @Override
                    public List<Slot> getInputSources(InventoryMenu handler) {
                        return handler.slots.subList(InventoryMenu.INV_SLOT_START, InventoryMenu.USE_ROW_SLOT_END);
                    }

                    @Override
                    public List<Slot> getCraftingSlots(InventoryMenu handler) {
                        var slot = ((InventoryMenuAccessor) handler).getOwner().getInventory().selected;
                        return List.of(handler.slots.get(slot));
                    }
                }
        );
    }

    private void registerRecipes(EmiRegistry registry, ResourceLocation id, ICRecipeType<?> value) {
    }
}
