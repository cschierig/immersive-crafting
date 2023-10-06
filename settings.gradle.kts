pluginManagement {
    repositories {
        maven("https://maven.fabricmc.net/")
        maven("https://maven.minecraftforge.net/") {
            name = "Forge"
        }
        maven("https://repo.spongepowered.org/repository/maven-public/") {
            name = "Sponge / Mixin"
        }

        gradlePluginPortal()
    }
}

rootProject.name = "immersive-crafting"
include("common", "fabric", "forge")
