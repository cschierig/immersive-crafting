import org.gradle.kotlin.dsl.support.uppercaseFirstChar
import java.util.*

plugins {
    java
    idea
    `maven-publish`
    alias(libs.plugins.neoforge.gradle)
    alias(libs.plugins.minotaur)
}

val modId: String by project
val recipeViewer: String by project
val common = project(":common")

dependencies {
    implementation("net.neoforged:neoforge:${libs.versions.neoforge.mdk.get()}")

    compileOnly(project(":common"))

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

sourceSets.main.get().resources.srcDir("src/generated/resources")

// taken from sodium
// NeoGradle compiles the game, but we don't want to add our common code to the game's code
val notNeoTask: (Task) -> Boolean = { it: Task -> !it.name.startsWith("neo") && !it.name.startsWith("compileService") }
tasks.withType<JavaCompile>().matching(notNeoTask).configureEach {
    source(common.sourceSets.main.get().allSource)
}

tasks.withType<Javadoc>().matching(notNeoTask).configureEach {
    source(common.sourceSets.main.get().allJava)
}

tasks.named<Jar>("sourcesJar") {
    from(common.sourceSets.main.get().allSource)
}

tasks.withType<ProcessResources>().matching(notNeoTask).configureEach {
    from(common.sourceSets.main.get().resources)
}

subsystems {
    parchment {
        minecraftVersion = libs.versions.minecraft.get()
        mappingsVersion = libs.versions.parchment.get()
    }
}

minecraft {
    val atFile = file("src/main/resources/META-INF/accesstransformer.cfg")
    if (atFile.exists()) {
        file(atFile)
    }
}

runs {
    configureEach {
        modSource(project.sourceSets.main.get())
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

artifacts {
    archives(tasks.named("apiJar"))
    archives(tasks.named("sourcesJar"))
}

tasks.named("build") {
    dependsOn(tasks.named("apiJar"))
    dependsOn(tasks.named("jar"))
    dependsOn(tasks.named("sourcesJar"))
}

if (System.getenv("MODRINTH_TOKEN") != null) {
    modrinth {
        token.set(System.getenv("MODRINTH_TOKEN"))
        projectId.set("immersive-crafting")
        versionNumber.set(project.version.toString())
        versionName.set(project.version.toString() + " - " + project.name.uppercaseFirstChar())
        versionType.set("alpha")
        uploadFile.set(tasks.named<Jar>("jar"))
        additionalFiles.set(listOf(
            "sourcesJar",
            "apiJar"
        ).map { tasks.named(it) })
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
