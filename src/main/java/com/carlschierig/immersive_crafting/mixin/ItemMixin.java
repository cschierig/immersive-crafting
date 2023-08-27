package com.carlschierig.immersive_crafting.mixin;

import com.carlschierig.immersive_crafting.ImmersiveCrafting;
import com.carlschierig.immersive_crafting.context.PlayerContextHolder;
import com.carlschierig.immersive_crafting.context.RecipeContext;
import com.carlschierig.immersive_crafting.crafting.ICRecipeTypes;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public abstract class ItemMixin {

	@Inject(method = "useOn", at = @At("HEAD"))
	void craftOnUse(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir) {
		var player = context.getPlayer();
		var recipeContext = new RecipeContext.Builder()
			.putHolder(new PlayerContextHolder(player))
			.build();
		var optRecipe = ImmersiveCrafting.RECIPE_MANAGER.getRecipe(ICRecipeTypes.USE_ITEM, recipeContext);

		if (optRecipe.isPresent()) {
			var recipe = optRecipe.get();
			var result = recipe.assembleResults(recipeContext);

			var level = context.getLevel();
			for (var stack : result) {
				if (recipe.spawnAtPlayer()) {
					var entity = new ItemEntity(level, player.getX(), player.getY(), player.getZ(), stack);
				} else {
					Block.popResourceFromFace(level, context.getClickedPos(), context.getClickedFace(), stack);
				}
			}

			player.getInventory().getSelected().shrink(recipe.getAmount());
		}
	}
}
