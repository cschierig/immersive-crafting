# Data Generation

Immersive Crafting provides Data Generators which can be used
to generate recipes using code instead of manually writing them,
which can be tedious and error-prone.

## Generating Recipes

To generate recipes, you will need to extend `ICRecipeProvider`
and override `buildRecipes(Consumer<ICRecipe>)`.

Proceed by creating an instance of an `ICRecipe`.
Most built-in objects with non-trivial constructors provide Builder classes.
As an example, we are going to create a recipe, which converts raw porkchop into cooked porkchop
when using it on a magma-block.

```java
@Override
public void buildRecipes(Consumer<ICRecipe> exporter) {
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
}
```
The example above is for a `UseItemRecipe`, but you can use this method for all
`ICRecipe`s.

## Registering The Provider

Lastly, you need to register your provider so that the data generator knows that it exists.
Refer to [This Fabric wiki article](https://fabricmc.net/wiki/tutorial:datagen_setup)
to learn how to set up data generation for your project.

You can add the provider to your pack using 
```java
pack.addProvider(YourICRecipeProvider::new);
```
