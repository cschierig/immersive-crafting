import net.darkhax.curseforgegradle.TaskPublishCurseForge
import org.gradle.kotlin.dsl.support.uppercaseFirstChar

plugins {
    id("multiloader-loader")
    alias(libs.plugins.neoforge.moddev)
    alias(libs.plugins.minotaur)
    alias(libs.plugins.curseforgegradle)
}

val modId: String by project
val modGroup: String by project
val recipeViewer: String by project
val withSourcesJar = property("withSourcesJar").toString().toBoolean()
val withApiJar = property("withApiJar").toString().toBoolean()
val compatMods = property("compatMods").toString().toBoolean()
val modrinthId: String by project
val modrinthType: String by project
val curseforgeId: String by project

val commonProject = project(":common")

dependencies {
    compileOnly(libs.compat.jei.neoforge.api)
    if (compatMods) {
        runtimeOnly(libs.compat.jei.neoforge)
    }
}

neoForge {
    version = libs.versions.neoforge.mdk.get()
    val atFile = file("src/main/resources/META-INF/accesstransformer.cfg")
    if (atFile.exists()) {
        accessTransformers.from(atFile)
    }

    runs {
        configureEach {
            systemProperty("neoforge.enabledGameTestNamespaces", modId)
            ideName = "NeoForge ${name.capitalize()} (${project.path})" // Unify the run config names with fabric
        }
        create("client") {
            client()
            gameDirectory.set(mkdir(file("runs/client")))
        }
        create("server") {
            server()
//            file("runs/server").createParentDirectories()
            gameDirectory.set(mkdir(file("runs/server")))
        }
    }
    mods {
        create(modId) {
            sourceSet(sourceSets.main.get())
        }
    }
}

if (withApiJar) {
    tasks.register<Jar>("apiJar") {
        archiveClassifier.set("api")
        dependsOn(tasks.named("jar"))
        from(zipTree(tasks.named("jar").get().outputs.files.asPath))
        include("neoforge.mods.toml")
        include("*.mixins.json")
        include("${modGroup.replace('.', '/')}/api/**")
    }

    tasks.named("build") {
        dependsOn(tasks.named("apiJar"))
    }
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
        uploadFile.set(tasks.named("jar"))
        additionalFiles.set(files.map { tasks.named(it) })
        syncBodyFrom.set(rootProject.file("README.md").readText())
        dependencies {
            optional.project("jei")
        }
        gameVersions.set(listOf(libs.versions.minecraft.get()))
        loaders.set(listOf("neoforge"))
        detectLoaders.set(false)
        changelog.set(file("../CHANGELOG.md").readText())
    }
    tasks.named("modrinth") { dependsOn(":fabric:runDatagen") }
}

if (System.getenv("CURSEFORGE_TOKEN") != null) {
    tasks.register<TaskPublishCurseForge>("curseforge") {
        apiToken = System.getenv("CURSEFORGE_TOKEN")

        upload(curseforgeId, tasks.named("jar")) {
            releaseType = modrinthType
            gameVersions.clear()
            addGameVersion(libs.versions.minecraft.get())
            addModLoader("neoforge")
            changelog = file("../CHANGELOG.md").readText()
            changelogType = "markdown"

            // Dependencies
            addOptional("jei")
        }

        disableVersionDetection()
    }
    tasks.named("curseforge") { dependsOn(":fabric:runDatagen") }
}

// Implement mcgradleconventions loader attribute
val loaderAttribute = Attribute.of("io.github.mcgradleconventions.loader", String::class.java)
for (variant in arrayOf(
    "apiElements",
    "runtimeElements",
    "sourcesElements",
    "javadocElements",
)) {
    configurations.named(variant) {
        attributes {
            attribute(loaderAttribute, "neoforge")
        }
    }
}

sourceSets.configureEach {
    for (variant in arrayOf(compileClasspathConfigurationName, runtimeClasspathConfigurationName)) {
        configurations.named(variant) {
            attributes {
                attribute(loaderAttribute, "neoforge")
            }
        }
    }
}
