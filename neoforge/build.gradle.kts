import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import net.fabricmc.loom.task.RemapJarTask
import org.gradle.kotlin.dsl.support.uppercaseFirstChar
import java.util.*

plugins {
    alias(libs.plugins.shadow)
    alias(libs.plugins.minotaur)
}

val modId: String by project
val withSourcesJar = property("withSourcesJar").toString().toBoolean()
val withApiJar = property("withApiJar").toString().toBoolean()
val modrinthId: String by project
val modrinthType: String by project
val recipeViewer: String by project

val commonProject = project(":common")

architectury {
    platformSetupLoomIde()
    neoForge()
}

val common by configurations.creating
val shadowBundle by configurations.creating
configurations {
    "common" {
        isCanBeResolved = true
        isCanBeConsumed = false
    }

    compileClasspath.get().extendsFrom(common)
    runtimeClasspath.get().extendsFrom(common)
    getByName("developmentNeoForge").extendsFrom(common)

    "shadowBundle" {
        isCanBeResolved = true
        isCanBeConsumed = false
    }
}

repositories {
    maven("https://maven.neoforged.net/releases") {
        name = "NeoForged"
    }
}

dependencies {
    neoForge(libs.neoforge.mdk)

    common(project(":common", "namedElements")) { isTransitive = false }
    shadowBundle(project(":common", "transformProductionNeoForge"))

    recipeViewer(dependencies)
}

fun recipeViewer(deps: DependencyHandler) {
// stolen from create fabric

    // emi
    deps.compileOnly("dev.emi:emi-neoforge:${libs.versions.emi.get()}:api")

    when (recipeViewer.lowercase(Locale.ROOT)) {
        "emi" -> deps.runtimeOnly("dev.emi:emi-neoforge:${libs.versions.emi.get()}")
        "disabled" -> Unit
        else -> println("Unknown recipe viewer specified: $recipeViewer. Must be JEI, REI, EMI, or disabled.")
    }
}

tasks.getByName<ShadowJar>("shadowJar") {
    configurations = listOf(shadowBundle)
    archiveClassifier.set("dev-shadow")
}

tasks.withType<RemapJarTask>() {
    inputFile.set(tasks.getByName<ShadowJar>("shadowJar").archiveFile)
    dependsOn(tasks.getByName<ShadowJar>("shadowJar"))
}

if (System.getenv("MODRINTH_TOKEN") != null) {
    val files = ArrayList<String>()
    if (withSourcesJar) {
        files.add("sourcesJar")
    }
    if (withApiJar) {
        files.add("apiJar")
    }

    modrinth {
        token.set(System.getenv("MODRINTH_TOKEN"))
        projectId.set(modrinthId)
        versionNumber.set(project.version.toString())
        versionName.set(project.version.toString() + " - " + project.name.uppercaseFirstChar())
        versionType.set(modrinthType)
        uploadFile.set(tasks.named("remapJar"))
        additionalFiles.set(files.map { tasks.named(it) })
        syncBodyFrom.set(rootProject.file("README.md").readText())
        dependencies {
            optional.project("emi")
        }
        gameVersions.set(listOf(libs.versions.minecraft.get()))
        loaders.set(listOf("neoforge"))
        detectLoaders.set(false)
        changelog.set(file("../CHANGELOG.md").readText())
    }
}

tasks.register<Jar>("apiJar") {
    archiveClassifier.set("api")
    from(sourceSets.main.get().allSource)
    from(sourceSets.main.get().output)
    include("neoforge.mods.toml")
    include("*.mixins.json")
    include("com/carlschierig/immersivecrafting/api/**")
}
