plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
    `embedded-kotlin`
}

repositories {
    jcenter()
    mavenCentral()
    maven("https://kotlin.bintray.com/kotlinx")
    maven("https://dl.bintray.com/soywiz/soywiz")
}

dependencies {
    api("com.soywiz:klock-jvm:1.0.0")
}

the<JavaPluginConvention>().sourceSets.getByName("main").java.srcDirs("kotlin")
