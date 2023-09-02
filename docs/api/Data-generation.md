# Data Generation

Immersive Crafting provides Data Generators which can be used to
generate recipes using code instead of manually writing them, which
can be tedious and error-prone.

## Generating Recipes

To generate recipes, you will need to extend `ICRecipeProvider`
and override `buildRecipes(Consumer<ICRecipe>)`.
