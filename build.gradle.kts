plugins {
    java
    kotlin("jvm") version "1.5.10"

    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "test.plugin"
version = "1.0-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

repositories {
    mavenCentral()

    maven {
        name = "paper"
        url = uri("https://papermc.io/repo/repository/maven-public/")
    }
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(group = "org.jetbrains.kotlinx", name = "kotlinx-coroutines-core", version = "1.6.0-native-mt")

    implementation(group = "com.destroystokyo.paper", name = "paper-api", version = "1.16.5-R0.1-SNAPSHOT")
}

tasks {
    withType<JavaCompile> {
        options.encoding = "UTF-8"
    }

    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }

    val fatJar by named("shadowJar", com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar::class) {
        dependencies {
            include(dependency("org.jetbrains.kotlin:.*"))
            include(dependency("org.jetbrains.kotlinx:.*"))
        }

        relocate("kotlin", "test.plugin.shaded.kotlin")
        relocate("org.jetbrains", "test.plugin.shaded.org.jetbrains")
    }

    artifacts {
        add("archives", fatJar)
    }
}

(tasks.getByName("processResources") as ProcessResources).apply {
    duplicatesStrategy = DuplicatesStrategy.INCLUDE

    from("src/main/resources") {
        include("**/*.yml")
        filter<org.apache.tools.ant.filters.ReplaceTokens>("tokens" to mapOf("VERSION" to project.version))
    }
    filesMatching("application.properties") {
        expand(project.properties)
    }
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}