dependencies {
    api(project(":amber"))
    api(project(":util"))
    api(project(":api"))
    api(project(":application"))
    api(project(":module"))
    api(project(":modules"))
    api(project(":cli"))

    api(Deps.kotlinTest)
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}
