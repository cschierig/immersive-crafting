package com.carlschierig.immersivecrafting.mixin;

import com.carlschierig.immersivecrafting.api.context.ContextTypes;
import com.carlschierig.immersivecrafting.api.context.SimpleRecipeContext;
import com.carlschierig.immersivecrafting.api.recipe.ICRecipeManager;
import com.carlschierig.immersivecrafting.api.recipe.ICRecipeTypes;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(Item.class)
public abstract class ItemMixin {

    @Inject(method = "useOn", at = @At("HEAD"), cancellable = true)
    void craftOnUse(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir) {
        var player = context.getPlayer();
        if (player != null) {
            var level = context.getLevel();

            var ingredients = List.of(
                    SimpleRecipeContext.of(ContextTypes.ITEM_STACK, player.getItemInHand(InteractionHand.MAIN_HAND)),
                    SimpleRecipeContext.of(ContextTypes.ITEM_STACK, player.getItemInHand(InteractionHand.OFF_HAND))
            );
            var recipeContext = new SimpleRecipeContext.Builder()
                    .putHolder(ContextTypes.PLAYER, player)
                    .putHolder(ContextTypes.BLOCK_STATE, level.getBlockState(context.getClickedPos()))
                    .putHolder(ContextTypes.LEVEL, level)
                    .putHolder(ContextTypes.BLOCK_POSITION, context.getClickedPos())
                    .putHolder(ContextTypes.DIRECTION, context.getClickedFace())
                    .putHolder(ContextTypes.INGREDIENTS, ingredients)
                    .putHolder(ContextTypes.RANDOM, level.getRandom())
                    .build();

            var optRecipe = ICRecipeManager.getRecipe(ICRecipeTypes.USE_ITEM, recipeContext);

            if (optRecipe.isPresent()) {
                var recipe = optRecipe.get();
                // TODO: random chance
                recipe.recipe().craft(recipeContext);

                cir.setReturnValue(InteractionResult.SUCCESS);
                cir.cancel();
            }
        }
    }
}
