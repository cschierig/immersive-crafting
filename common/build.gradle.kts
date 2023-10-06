plugins {
    idea
    java
    `maven-publish`
    alias(libs.plugins.sponge.gradle)
}

val modName: String by project
val modId: String by project
val author: String by project
val version: String by project

dependencies {
    compileOnly(libs.mixin)
    compileOnly(libs.emi.common) { api(this) }

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.0.3")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.0.3")
}

minecraft {
    version(libs.versions.minecraft.get())

    val awFile = file("src/main/resources/${modId}.accesswidener")
    if (awFile.exists()) {
        accessWideners(awFile)
    }
}

sourceSets {
    named("main") {
        resources {
            srcDir(file("src/example/generated"))
            exclude("src/main/resources/.cache")
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
