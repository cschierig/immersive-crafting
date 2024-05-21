pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://maven.fabricmc.net/") {
            name = "Fabric"
        }
        maven("https://maven.neoforged.net/releases") {
            name = "Neoforge"
        }
    }
}

rootProject.name = "immersive-crafting"
include("common", "fabric", "neoforge")
