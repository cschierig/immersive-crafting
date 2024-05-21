- ported to 1.20.6
- t

## Breaking Changes

- the mutually exclusive resource location and tag fields for the block condition are now grouped into the `block`
  category like so:

```json5
{
  // this is new
  "block": {
    "id": "minecraft:stone"
  },
  // nothing changed here
  "hardness": {
    "min": 1
  }
}
```

Instead of leaving out `id` and `tag`, one must now leave out the whole `block` key instead. If `block` is present,
it must contain `id` *xor* `tag`.

- support Neoforge instead of Forge
