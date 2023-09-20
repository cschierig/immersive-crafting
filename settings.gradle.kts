pluginManagement {
    repositories {
        maven("https://maven.fabricmc.net/")
        maven("https://maven.minecraftforge.net/")
        maven("https://repo.spongepowered.org/repository/maven-public/")
        gradlePluginPortal()
    }
}

rootProject.name = "immersive-crafting"
include("common", "fabric", "forge")
