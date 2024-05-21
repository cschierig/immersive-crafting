plugins {
    idea
    java
    `maven-publish`
    alias(libs.plugins.fabric.loom)
}

val modName: String by project
val modId: String by project
val author: String by project
val version: String by project

// tasks.forEach {
//     it.group = null
// }

dependencies {
    minecraft(libs.minecraft)
    mappings(loom.layered {
        officialMojangMappings()
        parchment("org.parchmentmc.data:parchment-${libs.versions.minecraft.get()}:${libs.versions.parchment.get()}@zip")
    })

    compileOnly(libs.mixin)
    compileOnly(libs.emi.common) { api(this) }

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.0.3")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.0.3")
}

loom {

    val awFile = file("src/main/resources/${modId}.accesswidener")
    if (awFile.exists()) {
        accessWidenerPath.set(awFile)
    }

    mixin {
        defaultRefmapName.set("${modId}.refmap.json")
    }
}

sourceSets {
    named("main") {
        resources {
            srcDir(file("src/main/generated"))
            exclude("src/main/generated/.cache")
        }
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

fun api(dep: ExternalModuleDependency) {
    dep.artifact {
        classifier = "api"
    }
}
