import org.gradle.kotlin.dsl.support.uppercaseFirstChar
import java.util.*

plugins {
    java
    idea
    `maven-publish`
    alias(libs.plugins.forge.gradle)
    alias(libs.plugins.mixin)
    alias(libs.plugins.minotaur)
}

val modId: String by project
val recipeViewer: String by project
val common = project(":common")

dependencies {
    minecraft("net.minecraftforge:forge:${libs.versions.minecraft.get()}-${libs.versions.forge.mdk.get()}")
    compileOnly(project(":common"))
    annotationProcessor("org.spongepowered:mixin:0.8.5-SNAPSHOT:processor")

    recipeViewer(dependencies)
}

fun recipeViewer(deps: DependencyHandler) {
    // stolen from create fabric

    // emi
    deps.compileOnly(fg.deobf("dev.emi:emi-forge:${libs.versions.emi.get()}:api"))

    when (recipeViewer.lowercase(Locale.ROOT)) {
        "emi" -> deps.runtimeOnly(fg.deobf("dev.emi:emi-forge:${libs.versions.emi.get()}"))
        "disabled" -> Unit
        else -> println("Unknown recipe viewer specified: $recipeViewer. Must be JEI, REI, EMI, or disabled.")
    }
}

sourceSets.main.get().resources.srcDir("src/generated/resources")

tasks.withType<JavaCompile>().configureEach {
    source(common.sourceSets.main.get().allSource)
}

tasks.withType<Javadoc>().configureEach {
    source(common.sourceSets.main.get().allJava)
}

tasks.named<Jar>("sourcesJar") {
    from(common.sourceSets.main.get().allSource)
}

tasks.named<ProcessResources>("processResources") {
    from(common.sourceSets.main.get().resources)
}

tasks.named<Jar>("jar") {
    finalizedBy("reobfJar")
}
tasks.named<Jar>("sourcesJar") {
    finalizedBy("reobfJar")
}

mixin {
    add(sourceSets.main.get(), "${modId}.refmap.json")
    config("immersive-crafting.mixins.json")
}

minecraft {
    mappingChannel.set("official")
    mappingVersion.set(libs.versions.minecraft.get())

    val atFile = file("src/main/resources/META-INF/accesstransformer.cfg")
    if (atFile.exists()) {
        accessTransformer(atFile)
    }

    runs {
        create("client") {
            workingDirectory(project.file("run"))
            ideaModule("${rootProject.name}.${project.name}.main")
            taskName("Client")
            property("mixin.env.remapRefMap", "true")
            property("mixin.env.refMapRemappingFile", "${projectDir}/build/createSrgToMcp/output.srg")

            mods {
                create("modClientRun") {
                    source(sourceSets.main.get())
                    source(project(":common").sourceSets.main.get())
                }
            }
        }

        create("server") {
            workingDirectory(project.file("run"))
            ideaModule("${rootProject.name}.${project.name}.main")
            taskName("Server")
            property("mixin.env.remapRefMap", "true")
            property("mixin.env.refMapRemappingFile", "${projectDir}/build/createSrgToMcp/output.srg")

            mods {
                create("modServerRun") {
                    source(sourceSets.main.get())
                    source(project(":common").sourceSets.main.get())
                }
            }
        }

        create("data") {
            workingDirectory(project.file("run"))
            ideaModule("${rootProject.name}.${project.name}.main")
            args(listOf("--mod", modId, "--all", "--output", file("src/generated/resources/"), "--exisiting", file("src/main/resources")))
            taskName("Data")
            property("mixin.env.remapRefMap", "true")
            property("mixin.env.refMapRemappingFile", "${projectDir}/build/createSrgToMcp/output.srg")

            mods {
                create("modDataRun") {
                    source(sourceSets.main.get())
                    source(project(":common").sourceSets.main.get())
                }
            }
        }
    }
}

tasks.register<Jar>("apiJar") {
    archiveClassifier.set("api")
    from(sourceSets.main.get().allSource)
    from(sourceSets.main.get().output)
    include("mods.toml")
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
        loaders.set(listOf("forge", "neoforge"))
        detectLoaders.set(false)
        changelog.set(file("../CHANGELOG.md").readText())
    }
}
