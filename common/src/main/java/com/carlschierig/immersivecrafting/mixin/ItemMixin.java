package com.carlschierig.immersivecrafting.mixin;

import com.carlschierig.immersivecrafting.api.context.ContextTypes;
import com.carlschierig.immersivecrafting.api.context.CraftingContext;
import com.carlschierig.immersivecrafting.api.context.RecipeContext;
import com.carlschierig.immersivecrafting.api.recipe.ICRecipeManager;
import com.carlschierig.immersivecrafting.api.recipe.ICRecipeTypes;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public abstract class ItemMixin {

    @Inject(method = "useOn", at = @At("HEAD"), cancellable = true)
    void craftOnUse(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir) {
        var player = context.getPlayer();
        if (player != null) {
            var level = context.getLevel();

            var recipeContext = new RecipeContext.Builder()
                    .putHolder(ContextTypes.PLAYER, player)
                    .putHolder(ContextTypes.BLOCK_STATE, level.getBlockState(context.getClickedPos()))
                    .putHolder(ContextTypes.LEVEL, level)
                    .putHolder(ContextTypes.BLOCK_POSITION, context.getClickedPos())
                    .putHolder(ContextTypes.DIRECTION, context.getClickedFace())
                    .putHolder(ContextTypes.ITEM_STACK, player.getInventory().getSelected())
                    .build();

            var optRecipe = ICRecipeManager.getRecipe(ICRecipeTypes.USE_ITEM, recipeContext);

            if (optRecipe.isPresent()) {
                var recipe = optRecipe.get();
                recipe.craft(recipeContext, new CraftingContext(level, context.getClickedPos(), context.getClickedFace(), level.getRandom()));

                // TODO: random chance
                player.getInventory().getSelected().shrink(recipe.getIngredients().get(0).getAmount());

                cir.setReturnValue(InteractionResult.SUCCESS);
                cir.cancel();
            }
        }
    }
}
