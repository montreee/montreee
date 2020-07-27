rootProject.name = "montreee"

fun Settings.includeFrom(dir: String, name: String) {
    include(name)
    val moculeDir = "./$dir/${(name.removePrefix(":")).replace(":", "/")}"
    project(name).projectDir = File(moculeDir)
}

fun Settings.includeFromSrcDir(name: String) = includeFrom("src", name)
fun Settings.includeModule(name: String) = includeFrom(".", name)

includeFromSrcDir(":amber")
includeFromSrcDir(":util")
includeFromSrcDir(":api")
includeFromSrcDir(":api:internal")
includeFromSrcDir(":api:public")
includeFromSrcDir(":api:private")
includeFromSrcDir(":application")
includeFromSrcDir(":module")
includeFromSrcDir(":modules")
includeFromSrcDir(":modules:standalone")
includeFromSrcDir(":modules:core")
includeFromSrcDir(":modules:api")
includeFromSrcDir(":modules:render")
includeFromSrcDir(":modules:database")
includeFromSrcDir(":modules:webui")
includeFromSrcDir(":cli")
includeFromSrcDir(":test")


//todo remove
pluginManagement {
    resolutionStrategy {
        eachPlugin {
            if (requested.id.id == "kotlin-multiplatform") {
                useModule("org.jetbrains.kotlin:kotlin-gradle-plugin:${requested.version}")
            }
            if (requested.id.id == "kotlinx-serialization") {
                useModule("org.jetbrains.kotlin:kotlin-serialization:${requested.version}")
            }
        }
    }
}
