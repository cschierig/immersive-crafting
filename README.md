# Immersive Crafting

Immersive Crafting is a library mod adding support for data-driven in-world crafting.
Crafting recipes are completely data-driven and reside in the `data/{some-folder}/ic_recipes`
folder. Recipes can also be generated using the fabric-datagen API.

Immersive Crafting provides several built-in recipe types, such as crafting items
when clicking with an item on a block. Everything is designed to be highly customizable.

## Documentation

Up-to-date documentation can be found [here](https://github.com/cschierig/immersive-crafting/blob/dev/1.20.1/docs).

## Example

The following example crafts a nether star from five diamonds when the player right-clicks
on a quartz block with a stack of diamonds.

```json
{
  "type": "immersive_crafting:use_item_on",
  "ingredient": {
    "item": "minecraft:diamond"
  },
  "amount": 5,
  "predicate": {
    "immersive_crafting:day_time": {
      "start_time": 13000,
      "end_time": 0
    },
    "immersive_crafting:block": {
      "block": "minecraft:quartz_block"
    }
  },
  "result": [
    {
      "item": "minecraft:nether_star"
    }
  ]
}
```

## Usage & Dependencies

- Immersive Crafting depends on [Quilt Standard Libraries](https://modrinth.com/mod/qsl).
- The mod is currently Quilt-exclusive. I might add support for other loaders in the future,
  but I do not have the time to do so at the moment.

<!-- modrinth_exclude.start -->

## Contributing

If you have encountered a problem or are in need of a feature, feel free to open an issue.
I'm also accepting pull requests, but you should consider contacting me before submitting
bigger changes to the project.

<!-- modrinth_exclude.end -->
