import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.10"
    application
}

group = "de.zordid"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.7.10")
    implementation("org.reflections:reflections:0.9.12")
    implementation("com.github.ajalt.mordant:mordant:2.0.0-beta7")
    //implementation("guru.nidi:graphviz-kotlin:0.18.1")
    implementation("org.slf4j:slf4j-nop:2.0.0")
    //implementation("ch.qos.logback", "logback-classic", "1.2.3")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.0")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.9.0")
    testImplementation("io.kotest:kotest-assertions-core-jvm:5.4.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.0")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
}

application {
    mainClass.set("AdventOfCodeKt")
}
