plugins {
    id("java")
}

group = "org.gloatyuk"
version = "BETA"

tasks.jar {
    manifest {
        attributes["Main-Class"] = "com.gloatyuk.solvex.Main"
    }
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

val fatJar = tasks.register<Jar>("fatJar") {
    archiveBaseName.set("${project.name}-all")
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    manifest {
        attributes["Main-Class"] = "com.gloatyuk.solvex.Main"
    }
    from(sourceSets.main.get().output)
    dependsOn(configurations.runtimeClasspath)
    from({
        configurations.runtimeClasspath.get().map {
            if (it.isDirectory) it else zipTree(it)
        }
    })
}