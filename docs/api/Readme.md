# API Documentation

This section covers the Immersive Crafting API.
> Note: The API is not yet stable and may change, though that will be mentioned in the changelog and
> while the mod is in alpha, indicated by a minor version bump.
> Starting with version `1.0.0`, the API will only change between minecraft versions and only
> if deemed necessary.

## Setup

To use the API in your mod, you will need to add immersive crafting
as a dependency to your project. To do so, you will need to add

```groovy
repositories {
    exclusiveContent {
        forRepository {
            maven {
                name = "Modrinth"
                url = "https://api.modrinth.com/maven"
            }
        }
        filter {
            includeGroup "maven.modrinth"
        }
    }
}

dependencies {
    modCompileOnly "maven.modrinth:immersive-crafting:version:api"
    modRuntimeOnly "maven.modrinth:immersive-crafting:version"
}
```

to your project's `build.gradle` and replace `version` with the version
you want to use, e.g. `0.1.0+1.20.1`.
