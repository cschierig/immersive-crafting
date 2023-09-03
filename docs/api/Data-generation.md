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

``
