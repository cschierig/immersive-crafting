## 0.5.0

- Allow use item recipes to take multiple ingredients.
  The first ingredient contains the item stack in the main hand, the second one the item stack in the offhand.

  If less than one or more than two ingredients are present, parsing the recipe will fail.
- Validate recipe ingredients and predicates. This should prevent exceptions from being thrown at runtime.
- remove `spawn_at_player` in favor of `from_face`. This property determines how stacks are crafted.
    - Block Stacks (see below): If `from_face` is true, the block will be spawned at the block position next to the
      block
      on which the item was used. Otherwise, the block the player used the item on will be replaced.
- allow blocks to be possible results. The placement of blocks is determined based on the value of the `fromFace`
  property which was introduced in this release.
- Separate Ingredients and stacks. This means:
    - Stacks still test against recipe contexts, but contrary to ingredients and conditions, they should not return if
      they
      match a contexts, but rather true if they can be crafted under the current circumstances, e.g. if a block can be
      placed at the given `BlockPos`.
    - Stacks are now tested to check if they can be crafted, not if they meet certain conditions
    - the item and tag ingredients (which replace the item stack) have more freedom and can match against tags as well
      as
      specific items.

### Known issues:

EMI might not display recipes properly if connected to a dedicated server.
