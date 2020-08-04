version = "0.0.1"

val versionName = "Montreee Sputnik 1"

val buildId = randomAlphanumericString(16)
val buildTime = com.soywiz.klock.DateTime.now().formatDefault()
val buildInfoText = "Montreee is a CMS by ContentUp UG © 2020"

plugins {
    idea
    kotlin("jvm") version Versions.kotlin
    id("kotlinx-serialization") version Versions.kotlin
}

allprojects {
    version = rootProject.version
    apply {
        plugin("org.jetbrains.kotlin.jvm")
        plugin("kotlinx-serialization")
    }
    repositories {
        jcenter()
        mavenCentral()
        maven("https://kotlin.bintray.com/kotlinx")
        maven("https://dl.bintray.com/soywiz/soywiz")
        maven("https://mvnrepository.com/")
        maven("https://jitpack.io")
    }
}

allprojects {
    sourceSets.forEach {
        when (it.name) {
            "main" -> {
                it.resources.srcDirs("resources")
            }
            "test" -> {
                it.resources.srcDirs("testResources")
            }
        }
    }

    tasks.compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    tasks.compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }

    kotlin {
        sourceSets.forEach {
            when (it.name) {
                "main" -> {
                    it.kotlin.srcDirs("kotlin")
                }
                "test" -> {
                    it.kotlin.srcDirs("test")
                }
            }
        }
    }
}

allprojects {
    dependencies {
        api(Deps.kotlinStdlib)
        api(Deps.kotlinStdlibJdk8)
        subprojects.forEach {
            implementation(project(it.path))
            api(project(it.path))
        }
    }
}

allprojects {
    val lib by configurations.creating
    dependencies {
        lib(project(path))
        subprojects.forEach {
            lib(project(it.path, "lib"))
        }
    }
}

allprojects {
    task("distribute") {
        group = "distribute"
        dependsOn("build")
        dependsOn("copyDependenciesIntoLib")
        dependsOn("copyLibIntoDist")
        dependsOn("copyArtifactsIntoDist")
        dependsOn("copyStaticIntoDist")
        dependsOn("copySubProjectDist")
    }
}

allprojects {
    task("copyDependenciesIntoLib", Copy::class) {
        group = "distribute"
        shouldRunAfter("build")
        includeEmptyDirs = true
        from(configurations.named<Configuration>("lib").map {
            it.asFileTree
        })
        into("$buildDir/lib")
    }
}

allprojects {
    task("copyLibIntoDist", Copy::class) {
        group = "distribute"
        shouldRunAfter("copyDependenciesIntoLib")
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
        from("$buildDir/lib")
        into("$buildDir/dist/lib")
    }
}

allprojects {
    task("copyArtifactsIntoDist", Copy::class) {
        group = "distribute"
        shouldRunAfter("copyDependenciesIntoLib")
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
        from("$buildDir/artifacts")
        into("$buildDir/dist/lib")
    }
}

allprojects {
    task("copyStaticIntoDist", Copy::class) {
        group = "distribute"
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
        from("static")
        into("$buildDir/dist")
    }
}

allprojects {
    task("copySubProjectDist", Copy::class) {
        group = "distribute"
        subprojects.forEach {
            dependsOn(it.tasks.findByName("distribute"))
        }
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
        subprojects.forEach {
            from("${it.buildDir}/dist")
        }
        into("$buildDir/dist")
    }
}

allprojects {
    tasks.build {
        subprojects.forEach {
            dependsOn(it.tasks.findByPath("build") ?: return@forEach)
        }
    }
}

allprojects {
    tasks.test {
        subprojects.forEach {
            dependsOn(it.tasks.findByPath("test") ?: return@forEach)
        }
    }
}

task("install") {
    group = "install"
    dependsOn("copyDistIntoRun")
}

task("copyDistIntoRun", Sync::class) {
    group = "install"
    dependsOn("distribute")
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
    from("$buildDir/dist")
    into("run")
}

subprojects {
    tasks.jar {
        val jarName = "${rootProject.name}${project.path.replace(":", "-")}-${project.version}.jar"
        archiveFileName.set(jarName)
        destinationDirectory.set(file("$buildDir/artifacts"))
    }
}

tasks.jar {
    archiveFileName.set("${rootProject.name}-${rootProject.version}.jar")
    destinationDirectory.set(file("$buildDir/artifacts"))
}

tasks.build {
    dependsOn("writeVersionFile")
}

task("writeVersionFile") {
    group = "build"
    doFirst {
        val file = File("$buildDir/dist/build.txt")
        if (!file.exists()) {
            file.parentFile?.mkdirs()
            file.createNewFile()
        }
        file.writeText(buildString {
            appendln("Name: $versionName")
            appendln("Version: ${rootProject.version}")
            appendln("Build: $buildId")
            appendln("BuildTime: $buildTime")
            appendln("Info: $buildInfoText")
        })
    }
}

tasks.clean {
    doFirst {
        delete {
            delete("run")
        }
    }
}
