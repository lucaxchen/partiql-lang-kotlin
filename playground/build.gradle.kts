import java.nio.file.Paths

plugins {
    kotlin("jvm")
    id("java")
    id("application")
}



repositories {
    mavenCentral()
}


dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
//    implementation("org.partiql:partiql-lang:1.1+")

    implementation(project(":partiql-ast"))
    implementation(project(":partiql-eval"))
    implementation(project(":partiql-parser", configuration = "shadow"))
    implementation(project(":partiql-plan"))
    implementation(project(":partiql-planner"))
    implementation(project(":partiql-spi"))
    implementation(project(":partiql-cli"))

    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.6.20")
    implementation("com.google.code.gson:gson:2.12.1")
    implementation(platform("software.amazon.awssdk:bom:2.20.100"))
    implementation("software.amazon.awssdk:s3:2.20.100")
}

application {
    applicationName = "partiql-playground"
    mainClass.set("org.partiql.playground.Main")
}

tasks.test {
    useJUnitPlatform()
}