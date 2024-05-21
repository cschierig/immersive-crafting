import org.gradle.kotlin.dsl.support.uppercaseFirstChar
import java.util.*

plugins {
    idea
    java
    `maven-publish`
    alias(libs.plugins.fabric.loom)
    alias(libs.plugins.minotaur)
}

val modId: String by project
val recipeViewer: String by project
val common = project(":common")

val exampleModCompileOnlyApi by configurations.creating;
exampleModCompileOnlyApi.extendsFrom(configurations.modCompileOnlyApi.get())


sourceSets {
    create("example") {
        compileClasspath = sourceSets.main.get().compileClasspath
        compileClasspath += exampleModCompileOnlyApi
        runtimeClasspath += sourceSets.main.get().runtimeClasspath

        resources {
            srcDir(file("src/example/generated"))
            exclude("src/example/generated/resources/.cache")
        }
    }
    getByName("main") {
        resources {
            srcDir(file("src/main/generated"))
            exclude("src/main/generated/resources/.cache")
        }
    }
}

loom {
    val awPath = common.file("src/main/resources/${modId}.accesswidener")
    if (awPath.exists()) {
        accessWidenerPath.set(awPath)
    }
    mixin {
        defaultRefmapName.set("${modId}.refmap.json")
    }
    runs {
        getByName("client") {
            client()
            configName = "Fabric Client"
            ideConfigGenerated(true)
            runDir("run")
        }
        getByName("server") {
            client()
            configName = "Fabric Server"
            ideConfigGenerated(true)
            runDir("run")
        }
        create("example") {
            client()
            configName = "Example Mod"
            ideConfigGenerated(true)
            source(project.sourceSets.getByName("example"))
        }
        create("exampleServer") {
            server()
            configName = "Example Mod Server"
            ideConfigGenerated(true)
            source(project.sourceSets.getByName("example"))
        }
        create("exampleDatagen") {
            inherit(getByName("example"))
            name("Example Mod Data Generation")
            vmArg("-Dfabric-api.datagen")
            vmArg("-Dfabric-api.datagen.output-dir=${file("src/example/generated")}")
            vmArg("-Dfabric-api.datagen.modid=ic_examples")

            runDir("build/exampleDatagen")
        }
        create("datagen") {
            inherit(getByName("client"))
            name("Data Generation")
            vmArg("-Dfabric-api.datagen")
            vmArg("-Dfabric-api.datagen.output-dir=${common.file("src/main/generated")}")
            vmArg("-Dfabric-api.datagen.modid=immersive_crafting")

            runDir("build/datagen")
        }
    }
}

tasks.withType<JavaCompile>().configureEach {
    source(common.sourceSets.main.get().allSource)
}

tasks.withType<Javadoc>().configureEach {
    source(common.sourceSets.main.get().allJava)
}

tasks.named<Jar>("sourcesJar") {
    from(common.sourceSets.main.get().allSource)
}

tasks.withType<ProcessResources>() {
    from(common.sourceSets.main.get().resources)
}

tasks.register<Jar>("apiJar") {
    archiveClassifier.set("api")
    dependsOn(tasks.named("remapJar"))
    from(zipTree(tasks.named("remapJar").get().outputs.files.asPath))
    include("fabric.mod.json")
    include("*.mixins.json")
    include("com/carlschierig/immersivecrafting/api/**")
}

tasks.named("build") {
    dependsOn(tasks.named("apiJar"))
}

dependencies {
    minecraft(libs.minecraft)
    mappings(loom.layered {
        officialMojangMappings()
        parchment("org.parchmentmc.data:parchment-${libs.versions.minecraft.get()}:${libs.versions.parchment.get()}@zip")
    })

    modImplementation(libs.fabric.loader)
    modImplementation(libs.fabric.api)

    implementation(project(":common"))

    recipeViewer(dependencies)

    exampleModCompileOnlyApi(tasks.getByName("apiJar").outputs.files)
}

fun recipeViewer(deps: DependencyHandler) {
    // stolen from create fabric

    // emi
    deps.modCompileOnly(libs.emi.fabric) { api(this) }

    // rei
    deps.modCompileOnly("me.shedaniel:RoughlyEnoughItems-api-fabric:${libs.versions.rei.get()}")
    deps.modCompileOnly("me.shedaniel:RoughlyEnoughItems-default-plugin-fabric:${libs.versions.rei.get()}")
    // jei
    // deps.modCompileOnly("mezz.jei:jei-${libs.versions.minecraft.get()}-common-api:${libs.versions.jei.get()}")
    // deps.modCompileOnly("mezz.jei:jei-${libs.versions.minecraft.get()}-fabric-api:${libs.versions.jei.get()}")

    when (recipeViewer.lowercase(Locale.ROOT)) {
        "emi" -> deps.modLocalRuntime(libs.emi.fabric)
        "rei" -> deps.modLocalRuntime(libs.rei)
        // "jei" -> deps.modLocalRuntime("mezz.jei:jei-${libs.versions.minecraft.get()}-fabric:${libs.versions.jei.get()}")
        "disabled" -> Unit
        else -> println("Unknown recipe viewer specified: $recipeViewer. Must be JEI, REI, EMI, or disabled.")
    }
}

if (System.getenv("MODRINTH_TOKEN") != null) {
    modrinth {
        token.set(System.getenv("MODRINTH_TOKEN"))
        projectId.set("immersive-crafting")
        versionNumber.set(project.version.toString())
        versionName.set(project.version.toString() + " - " + project.name.uppercaseFirstChar())
        versionType.set("alpha")
        uploadFile.set(tasks.named("remapJar"))
        additionalFiles.set(listOf(
            "remapSourcesJar",
            "apiJar"
        ).map { tasks.named(it) })
        syncBodyFrom.set(rootProject.file("README.md").readText())
        dependencies {
            required.project("fabric-api")
            optional.project("emi")
        }
        gameVersions.set(listOf(libs.versions.minecraft.get()))
        loaders.set(listOf("fabric", "quilt"))
        detectLoaders.set(false)
        changelog.set(file("../CHANGELOG.md").readText())
    }
    tasks.named("modrinth") { dependsOn("runDatagen") }
}

fun api(dep: ExternalModuleDependency) {
    dep.artifact {
        classifier = "api"
    }
}
