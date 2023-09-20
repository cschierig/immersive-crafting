import java.text.SimpleDateFormat
import java.util.*

plugins {
    alias(libs.plugins.fabric.loom) apply false
    alias(libs.plugins.forge.gradle) apply false
    alias(libs.plugins.sponge.gradle) apply false
    alias(libs.plugins.mixin) apply false
}

val modArchiveName: String by project
val modName: String by project
val modId: String by project
val modGroup: String by project
val author: String by project
val modVersion: String by project

subprojects {
    // Java
    apply(plugin = "java")
    val libs = rootProject.libs

    configure<JavaPluginExtension> {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17

        withSourcesJar()
        withJavadocJar()
    }

    tasks.withType<Jar>().configureEach {
        from("LICENSE") {
            rename { "${it}_${modName}" }
        }
    }

    tasks.named<Jar>("jar") {
        manifest {
            attributes(mapOf(
                    "Specification-Title" to modName,
                    "Specification-Vendor" to author,
                    "Specification-Version" to archiveVersion,
                    "Implementation-Title" to modName,
                    "Implementation-Vendor" to author,
                    "Implementation-Version" to archiveVersion,
                    "Implementation-Timestamp" to SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").format(Date()),
                    "Timestamp" to System.currentTimeMillis(),
                    "Built-On-Java" to "${System.getProperty("java.vm.version")} (${System.getProperty("java.vm.vendor")})",
                    "Built-On-Minecraft" to libs.versions.minecraft.get()
            ))
        }
    }

    tasks.withType<JavaCompile>().configureEach {
        options.encoding = "UTF-8"
        options.release.set(17)
    }

    tasks.withType<ProcessResources> {
        filesMatching(listOf("pack.mcmeta", "fabric.mod.json", "mods.toml", "*.mixins.json")) {
            expand(project.properties)
        }
    }

    tasks.withType<GenerateModuleMetadata>().configureEach {
        enabled = false
    }

    repositories {
        // EMI
        maven("https://maven.terraformersmc.com/")
        // REI
        maven("https://maven.shedaniel.me")
        // JEI
        maven("https://maven.blamejared.com/")
        maven("https://repo.spongepowered.org/repository/maven-public/")
    }

    configure<BasePluginExtension> {
        archivesName.set("${modArchiveName}-${project.name}")
    }
    version = "${modVersion}+${libs.versions.minecraft.get()}"
    group = modGroup
}

tasks.register("release") {
    dependsOn(project("fabric").tasks.named("modrinth").get())
    dependsOn(project("forge").tasks.named("modrinth").get())
    dependsOn(project("fabric").tasks.named("modrinthSyncBody").get())
}
