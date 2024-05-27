# Predicates and Conditions

Conditions are the heart of the immersive crafting system.
They are used to determine whether a recipe can be crafted under the given circumstances.
Conditions are designed to be arranged as a tree, with there being several conditions that can contain children.

## Predicates

An `ICPredicate` marks the root of a condition tree.
It is an `AndCondition`, a condition which is true if only if all of its child nodes are true.

Each [ICRecipe](Recipes.md) has a predicate which can be accessed through `ICRecipe#getPredicate()`.
This predicate is used to check if all conditions a specific recipe are fulfilled.

## Built-in Conditions

Immersive Crafting provides several built-in conditions which can be used to create custom recipes.
