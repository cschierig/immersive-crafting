package com.carlschierig.immersivecrafting.compat.emi;

import com.carlschierig.immersivecrafting.mixin.InventoryMenuAccessor;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.handler.StandardRecipeHandler;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;

import java.util.List;

public class ICRecipeHandler implements StandardRecipeHandler<InventoryMenu> {
    private final ICRecipeCategory category;

    public ICRecipeHandler(ICRecipeCategory category) {
        this.category = category;
    }

    @Override
    public boolean supportsRecipe(EmiRecipe recipe) {
        return recipe.getCategory().equals(category);
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
