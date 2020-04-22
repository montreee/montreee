plugins {
    id("com.github.salomonbrys.gradle.sass") version "1.2.0"
    id("org.padler.gradle.minify") version "1.4.1"
}

dependencies {
    api(project(":module"))
}

sass {
    download {
        version = "1.26.3"
        downloadBaseUrl = "https://github.com/sass/dart-sass/releases/download"
        outputDir = file("$buildDir/dart-sass")
    }
}

sassCompile {
    source = fileTree("$projectDir/scss")
    outputDir = file("$buildDir/css")
}

minification {
    jsSrcDir = "$buildDir/minify/src/js"
    jsDstDir = "$buildDir/minify/dst/js"
    cssSrcDir = "$buildDir/minify/src/css"
    cssDstDir = "$buildDir/minify/dst/css"

    createCssSourceMaps = true
    createJsSourceMaps = true
}

tasks.distribute {
    dependsOn("copyScssIntoDist")
    dependsOn("copyJsIntoDist")
    dependsOn("copyCssIntoDist")
}

task("copyScssIntoDist", Copy::class) {
    group = "distribute"
    from("$projectDir/scss")
    into("$buildDir/dist/data/webui/scss")
}

task("copyJsIntoDist", Copy::class) {
    group = "distribute"
    from("$buildDir/js")
    into("$buildDir/dist/data/webui/js")
}

task("copyCssIntoDist", Copy::class) {
    group = "distribute"
    from("$buildDir/css")
    into("$buildDir/dist/data/webui/css")
}

tasks.build {
    dependsOn("buildWeb")
}

task("buildWeb") {
    group = "build"
    dependsOn("sassCompile")
    dependsOn("copyWebSrc")
    dependsOn("copyMinSrc")
    dependsOn("minify")
    dependsOn("copyMinDst")
    dependsOn("addSourceMappingURLToMinJsAndCssFiles")
}

task("copyWebSrc") {
    group = "build"
    dependsOn("copyJsWebSrc")
    dependsOn("copyCssWebSrc")
}

task("copyJsWebSrc", Copy::class) {
    group = "build"
    from("$projectDir/js")
    into("$buildDir/js")
}

task("copyCssWebSrc", Copy::class) {
    group = "build"
    from("$projectDir/css")
    into("$buildDir/css")
}

task("copyMinSrc") {
    group = "build"
    shouldRunAfter("sassCompile")
    shouldRunAfter("copyWebSrc")
    dependsOn("copyMinJsSrc")
    dependsOn("copyMinCssSrc")
}

task("copyMinJsSrc", Copy::class) {
    group = "build"
    from("$buildDir/js") {
        exclude("**/*.map")
        exclude("**/*.min.js")
    }
    into(minification.jsSrcDir)
}

task("copyMinCssSrc", Copy::class) {
    group = "build"
    from("$buildDir/css") {
        exclude("**/*.map")
        exclude("**/*.min.css")
    }

    //TODO remove when fixed in minify plugin
    filter { if (it.startsWith("@charset \"UTF-8\";")) it.removePrefix("@charset \"UTF-8\";") else it }

    into(minification.cssSrcDir)
}

tasks.minify {
    group = "build"
    dependsOn("copyMinSrc")
}

task("copyMinDst") {
    group = "build"
    dependsOn("copyMinJsDst")
    dependsOn("copyMinCssDst")
}

task("copyMinJsDst", Copy::class) {
    group = "build"
    dependsOn("minify")
    from(minification.jsDstDir)
    into("$buildDir/js")
}

task("copyMinCssDst", Copy::class) {
    group = "build"
    dependsOn("minify")
    from(minification.cssDstDir)
    into("$buildDir/css")
}

//TODO should happen in minify plugin
task("addSourceMappingURLToMinJsAndCssFiles") {
    group = "build"
    dependsOn("addSourceMappingURLToMinJsFiles")
    dependsOn("addSourceMappingURLToMinCssFiles")
}

//TODO should happen in minify plugin
task("addSourceMappingURLToMinJsFiles") {
    group = "build"
    dependsOn("copyMinDst")
    doFirst {
        File("$buildDir/js").walkTopDown().forEach {
            if (!it.isFile || !it.name.endsWith(".min.js")) return@forEach
            it.writeText("${it.readText()}\n//# sourceMappingURL=${it.name}.map\n")
        }
    }
}

//TODO should happen in minify plugin
task("addSourceMappingURLToMinCssFiles") {
    group = "build"
    dependsOn("copyMinDst")
    doFirst {
        File("$buildDir/css").walkTopDown().forEach {
            if (!it.isFile || !it.name.endsWith(".min.css")) return@forEach
            it.writeText("${it.readText()}\n/*# sourceMappingURL=${it.name}.map */\n")
        }
    }
}
