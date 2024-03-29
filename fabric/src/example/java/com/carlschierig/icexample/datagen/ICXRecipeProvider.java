package com.carlschierig.icexample.datagen;

import com.carlschierig.immersivecrafting.api.data.ICRecipeProvider;
import com.carlschierig.immersivecrafting.api.predicate.ICPredicate;
import com.carlschierig.immersivecrafting.api.predicate.condition.BlockCondition;
import com.carlschierig.immersivecrafting.api.predicate.condition.DayTimeCondition;
import com.carlschierig.immersivecrafting.api.predicate.condition.ingredient.ICItemStack;
import com.carlschierig.immersivecrafting.api.recipe.ICRecipe;
import com.carlschierig.immersivecrafting.api.recipe.UseItemOnRecipe;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * You must extend {@link ICRecipeProvider} to generate your own recipes
 */
public class ICXRecipeProvider extends ICRecipeProvider {
    /**
     * Create a new
     *
     * @param output
     */
    public ICXRecipeProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void buildRecipes(@NotNull Consumer<ICRecipe> exporter) {
        // offer recipes to the exporter

        // Turn pork-chop into cooked pork-chop when using it on a magma block.
        var porkChopRecipe = new UseItemOnRecipe.Builder(new ResourceLocation("ic_examples", "cooked_porkchop"))
                .ingredient(new ICItemStack(Items.PORKCHOP)) // Set porkchop to be the ingredient
                // The predicate does the magic. It can perform a variety of checks depending on the type of recipe
                // you are creating
                .predicate(new ICPredicate.Builder()
                        // we're adding a condition to the predicate so that the recipe is only triggered
                        // when clicking on a magma block
                        .with(new BlockCondition.Builder().idFromBlock(Blocks.MAGMA_BLOCK).build())
                        .build())
                // add the cooked porkchop as a result.
                .addResult(new ICItemStack(Items.COOKED_PORKCHOP))
                .build();
        // we still need to offer the recipe to the consumer.
        exporter.accept(porkChopRecipe);

        // Another example: Turn 5 diamonds into a nether star when using them on a quartz block at night.
        var diamondToNetherStar = new UseItemOnRecipe.Builder(new ResourceLocation("ic_examples", "nether_star"))
                .ingredient(new ICItemStack(new ItemStack(Items.DIAMOND, 5)))
                .predicate(new ICPredicate.Builder()
                        // Immersive Crafting provides some ready-to-use conditions
                        .with(DayTimeCondition.NIGHT)
                        .with(new BlockCondition.Builder().idFromBlock(Blocks.QUARTZ_BLOCK).build())
                        .build())
                .addResult(new ICItemStack(Items.NETHER_STAR))
                .build();
        exporter.accept(diamondToNetherStar);

        // Break flint to find an amethyst with a 50% chance when hitting it against a hard block
        var flintToAmethyst = new UseItemOnRecipe.Builder(new ResourceLocation("ic_examples", "amethyst"))
                .ingredient(new ICItemStack(Items.FLINT))
                .predicate(new ICPredicate.Builder()
                        .with(new BlockCondition.Builder().hardnessMinOnly(2.5f).build())
                        .build())
                .addResult(new ICItemStack(new ItemStack(Items.AMETHYST_SHARD), 0.5f))
                .build();
        exporter.accept(flintToAmethyst);

        // cut sticks from logs with flint
        var flintToStick = new UseItemOnRecipe.Builder(new ResourceLocation("ic_examples", "stick"))
                .ingredient(new ICItemStack(Items.FLINT))
                .predicate(new ICPredicate.Builder()
                        .with(new BlockCondition.Builder().tag(new ResourceLocation("logs")).build())
                        .build())
                .addResult(new ICItemStack(Items.STICK))
                .build();
        exporter.accept(flintToStick);
    }
}
