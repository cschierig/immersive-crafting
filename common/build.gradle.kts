plugins {
    alias(libs.plugins.fabric.loom)
}

val modId: String by project
val enabledPlatforms: String by project

loom {
    val awPath = file("src/assets/resources/${modId}.accesswidener")
    if (awPath.exists()) {
        accessWidenerPath.set(awPath)
    }

    splitEnvironmentSourceSets()
}

sourceSets {
    val main by getting
    main {
        resources {
            srcDir("src/generated")
            exclude("src/generated/.cache")
        }
    }
}

dependencies {
    minecraft(libs.minecraft)

    compileOnly(libs.mixin)
    compileOnly(libs.mixinextras.common)
    annotationProcessor(libs.mixinextras.common)

    compileOnly(libs.compat.jei.common.api)
}

configurations {
    register("commonJava") {
        isCanBeResolved = false
        isCanBeConsumed = true
    }
    register("commonResources") {
        isCanBeResolved = false
        isCanBeConsumed = true
    }
}

artifacts {
    add("commonJava", sourceSets.main.get().java.sourceDirectories.singleFile)
    add("commonJava", sourceSets["client"].java.sourceDirectories.singleFile)
    sourceSets.main.get().resources.sourceDirectories.files.forEach {
        add("commonResources", it)
    }
}

// Implement mcgradleconventions loader attribute
val loaderAttribute = Attribute.of("io.github.mcgradleconventions.loader", String::class.java)
for (variant in arrayOf("apiElements", "runtimeElements", "sourcesElements", "javadocElements")) {
    configurations.named(variant) {
        attributes {
            attribute(loaderAttribute, "common")
        }
    }
}

sourceSets.configureEach {
    for (variant in arrayOf(compileClasspathConfigurationName, runtimeClasspathConfigurationName)) {
        configurations.named(variant) {
            attributes {
                attribute(loaderAttribute, "common")
            }
        }
    }
}
