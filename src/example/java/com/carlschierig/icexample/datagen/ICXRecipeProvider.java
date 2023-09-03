package com.carlschierig.icexample.datagen;

import com.carlschierig.immersivecrafting.api.data.ICRecipeProvider;
import com.carlschierig.immersivecrafting.api.predicate.ICPredicate;
import com.carlschierig.immersivecrafting.api.predicate.condition.BlockCondition;
import com.carlschierig.immersivecrafting.api.predicate.condition.DayTimeCondition;
import com.carlschierig.immersivecrafting.api.recipe.ICRecipe;
import com.carlschierig.immersivecrafting.api.recipe.UseItemOnRecipe;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;

import java.util.function.Consumer;

/**
 * You must extend {@link ICRecipeProvider} to generate your own recipes
 */
public class ICXRecipeProvider extends ICRecipeProvider {
    public ICXRecipeProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void buildRecipes(Consumer<ICRecipe> exporter) {
        // offer recipes to the exporter

        // Turn pork-chop into cooked pork-chop when using it on a magma block.
        var porkChopRecipe = new UseItemOnRecipe.Builder(new ResourceLocation("ic_examples", "cooked_porkchop"))
                .ingredient(Ingredient.of(Items.PORKCHOP)) // Set porkchop to be the ingredient
                // The predicate does the magic. It can perform a variety of checks depending on the type of recipe
                // you are creating
                .predicate(new ICPredicate.Builder()
                        // we're adding a condition to the predicate so that the recipe is only triggered
                        // when clicking on a magma block
                        .with(new BlockCondition.Builder().idFromBlock(Blocks.MAGMA_BLOCK).build())
                        .build())
                // add the cooked porkchop as a result.
                .addResult(new ItemStack(Items.COOKED_PORKCHOP))
                .build();
        // we still need to offer the recipe to the consumer.
        exporter.accept(porkChopRecipe);

        // Another example: Turn 5 diamonds into a nether star when using them on a quartz block at night.
        var diamondToNetherStar = new UseItemOnRecipe.Builder(new ResourceLocation("ic_examples", "nether_star"))
                .ingredient(Ingredient.of(Items.DIAMOND))
                .amount(5)
                .predicate(new ICPredicate.Builder()
                        // Immersive Crafting provides some ready-to-use conditions
                        .with(DayTimeCondition.NIGHT)
                        .with(new BlockCondition.Builder().idFromBlock(Blocks.QUARTZ_BLOCK).build())
                        .build())
                .addResult(new ItemStack(Items.NETHER_STAR))
                .build();
        exporter.accept(diamondToNetherStar);
    }
}
