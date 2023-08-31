package com.carlschierig.immersive_crafting.mixin;

import com.carlschierig.immersive_crafting.ImmersiveCrafting;
import com.carlschierig.immersive_crafting.api.context.RecipeContext;
import com.carlschierig.immersive_crafting.context.ContextTypes;
import com.carlschierig.immersive_crafting.recipe.ICRecipeTypes;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public abstract class ItemMixin {

    @Inject(method = "useOn", at = @At("HEAD"), cancellable = true)
    void craftOnUse(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir) {
        var player = context.getPlayer();
        var level = context.getLevel();

        var recipeContext = new RecipeContext.Builder()
                .putHolder(ContextTypes.PLAYER, player)
                .putHolder(ContextTypes.BLOCK, level.getBlockState(context.getClickedPos()).getBlock())
                .putHolder(ContextTypes.LEVEL, level)
                .build();

        var optRecipe = ImmersiveCrafting.RECIPE_MANAGER.getRecipe(ICRecipeTypes.USE_ITEM, recipeContext);

        if (optRecipe.isPresent()) {
            var recipe = optRecipe.get();
            var result = recipe.assembleResults(recipeContext);

            for (var stack : result) {
                if (recipe.spawnAtPlayer()) {
                    var entity = new ItemEntity(level, player.getX(), player.getY(), player.getZ(), stack);
                } else {
                    Block.popResourceFromFace(level, context.getClickedPos(), context.getClickedFace(), stack);
                }
            }

            player.getInventory().getSelected().shrink(recipe.getAmount());

            cir.setReturnValue(InteractionResult.SUCCESS);
            cir.cancel();
        }
    }
}
