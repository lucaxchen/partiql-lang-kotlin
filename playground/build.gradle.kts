import java.nio.file.Paths

plugins {
    id("java")
    id("application")
}
application {
    mainClass.set("org.partiql.example.Main")
}

val compiled = tasks.withType<Jar> {
}

group = "org.partiql.example"
version = "1.0-SNAPSHOT"

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

    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.6.20")
    implementation("com.google.code.gson:gson:2.12.1")
    implementation(platform("software.amazon.awssdk:bom:2.20.100"))
    implementation("software.amazon.awssdk:s3:2.20.100")
}

tasks.test {
    useJUnitPlatform()
}


// `./gradlew plugin` will build a `Catalog` SPI plugin jar and install it in
// `~/.partiql/plugins/plk-httptable-demo]plk-httptable-demo-1.0-SNAPSHOT.jar`
//
// Note: There is not currently an SPI loader for PLK to load this plugin.
var pluginPath = file(Paths.get(System.getProperty("user.home")).resolve(".partiql/plugins/plk-httptable-demo"))
tasks.register<Copy>("plugin") {
    into(pluginPath)

    from(compiled)
}
