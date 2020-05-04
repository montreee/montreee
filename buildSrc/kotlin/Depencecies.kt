object Versions {
    val kotlinStdlib = "1.3.71"
    val kotlinCoroutines = "1.2.1"
    val kotlinSerializationRuntime = "0.14.0"
    val kotlinSerializationYamlSupport = "0.15.0"
    val ktor = "1.3.0"
    val klock = "1.0.0"
    val clikt = "2.1.0"

    val kotest = "4.0.3"
    val mockk = "1.10.0"
}

object Deps {
    val kotlinStdlib = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlinStdlib}"
    val kotlinStdlibJdk8 = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.kotlinStdlib}"
    val kotlinCoroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.kotlinCoroutines}"
    val kotlinSerializationRuntime =
            "org.jetbrains.kotlinx:kotlinx-serialization-runtime:${Versions.kotlinSerializationRuntime}"
    val kotlinSerializationYamlSupport = "com.charleskorn.kaml:kaml:${Versions.kotlinSerializationYamlSupport}"
    val klock = "com.soywiz:klock-jvm:${Versions.klock}"
    val ktorServer = "io.ktor:ktor-server-core:${Versions.ktor}"
    val ktorServerWebSocket = "io.ktor:ktor-websockets:${Versions.ktor}"
    val ktorServerNetty = "io.ktor:ktor-server-netty:${Versions.ktor}"
    val ktorHTMLBuilder = "io.ktor:ktor-html-builder:${Versions.ktor}"
    val ktorClient = "io.ktor:ktor-client-core:${Versions.ktor}"
    val ktorClientJetty = "io.ktor:ktor-client-jetty:${Versions.ktor}"
    val ktorClientCio = "io.ktor:ktor-client-cio:${Versions.ktor}"
    val ktorClientApache = "io.ktor:ktor-client-apache:${Versions.ktor}"
    val ktorClientOkHttp = "io.ktor:ktor-client-okhttp:${Versions.ktor}"
    val clikt = "com.github.ajalt:clikt:${Versions.clikt}"

    val kotest = "io.kotest:kotest-runner-junit5:${Versions.kotest}"
    val kotestAssertionCore = "io.kotest:kotest-assertions-core-jvm:${Versions.kotest}"
    val mockk = "io.mockk:mockk:${Versions.mockk}"
}
