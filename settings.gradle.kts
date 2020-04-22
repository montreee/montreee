rootProject.name = "montreee"

fun Settings.includeFrom(dir: String, name: String) {
    include("$name")
    val moculeDir = "./$dir/${(name.removePrefix(":")).replace(":", "/")}"
    project("$name").projectDir = File(moculeDir)
}

fun Settings.includeFromModuleDir(name: String) = includeFrom("modules", name)
fun Settings.includeModule(name: String) = includeFrom(".", name)

includeFromModuleDir(":amber")
includeFromModuleDir(":util")
includeFromModuleDir(":api")
includeFromModuleDir(":api:internal")
includeFromModuleDir(":api:public")
includeFromModuleDir(":api:private")
includeFromModuleDir(":application")
includeFromModuleDir(":module")
includeFromModuleDir(":modules")
includeFromModuleDir(":modules:standalone")
includeFromModuleDir(":modules:core")
includeFromModuleDir(":modules:api")
includeFromModuleDir(":modules:render")
includeFromModuleDir(":modules:database")
includeFromModuleDir(":modules:webui")
includeFromModuleDir(":cli")
includeFromModuleDir(":test")


//todo remove
pluginManagement {
    resolutionStrategy {
        eachPlugin {
            if (requested.id.id == "kotlin-multiplatform") {
                useModule("org.jetbrains.kotlin:kotlin-gradle-plugin:${requested.version}")
            }
            if (requested.id.id=="kotlinx-serialization") {
                useModule("org.jetbrains.kotlin:kotlin-serialization:${requested.version}")
            }
        }
    }
}
