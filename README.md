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
on a quartz block with a stack of diamonds at night.

```json
{
  "type": "immersive_crafting:use_item_on",
  "ingredient": {
    "type": "immersive_crafting:item",
    "stack": {
      "count": 5,
      "item": "minecraft:diamond"
    }
  },
  "predicate": {
    "conditions": [
      {
        "type": "immersive_crafting:invert",
        "condition": {
          "type": "immersive_crafting:day_time",
          "end_time": 12999,
          "start_time": 0
        }
      },
      {
        "type": "immersive_crafting:block",
        "id": "minecraft:quartz_block"
      }
    ]
  },
  "result": [
    {
      "type": "immersive_crafting:item",
      "stack": {
        "item": "minecraft:nether_star"
      }
    }
  ]
}
```

## Usage & Dependencies

- Immersive Crafting for Fabric depends on [Fabric API](https://modrinth.com/mod/fabric-api).
- It is recommended to use EMI to view the recipes. REI and JEI support will be added in the future.

<!-- modrinth_exclude.start -->

## Contributing

If you have encountered a problem or are in need of a feature, feel free to open an issue.
I'm also accepting pull requests, but you should consider contacting me before submitting
bigger changes to the project.

<!-- modrinth_exclude.end -->
