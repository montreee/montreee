object Versions {
    val kotlin = "1.3.72"
    val kotlinxCoroutines = "1.3.8"
    val kotlinxSerialization = "0.20.0"
    val kaml = "0.18.1"
    val ktor = "1.3.2"
    val klock = "1.0.0"
    val clikt = "2.4.0"

    val kotest = "4.1.2"
    val mockk = "1.10.0"
}

object Deps {
    val kotlinStdlib = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin}"
    val kotlinStdlibJdk8 = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.kotlin}"
    val kotlinCoroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.kotlinxCoroutines}"
    val kotlinSerializationRuntime =
            "org.jetbrains.kotlinx:kotlinx-serialization-runtime:${Versions.kotlinxSerialization}"
    val kaml = "com.charleskorn.kaml:kaml:${Versions.kaml}"
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
