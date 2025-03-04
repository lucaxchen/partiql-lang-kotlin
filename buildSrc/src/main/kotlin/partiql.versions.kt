/*
 * Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

// Replace with `versionCatalogs` once stabilized
// https://docs.gradle.org/current/userguide/platforms.html

object Versions {
    // Language
    const val kotlin = "1.9.20"
    const val kotlinLanguage = "1.9"
    const val kotlinApi = "1.9"
    const val jvmTarget = "1.8"

    // Dependencies
    const val antlr = "4.10.1"
    const val awsSdk = "1.12.344"
    const val csv = "1.8"
    const val dotlin = "1.0.2"
    const val guava = "31.1-jre"
    const val ionElement = "1.0.0"
    const val ionSchema = "1.2.1"
    const val jansi = "2.4.0"
    const val jgenhtml = "1.6"
    const val jline = "3.21.0"
    const val joda = "2.12.1"
    const val kotlinPoet = "1.11.0"
    const val kotlinxCollections = "0.3.5"
    const val picoCli = "4.7.0"
    const val kasechange = "1.3.0"
    const val kotlinLombok = "1.9.20"
    const val kotlinxCoroutines = "1.8.1"
    const val kotlinxCoroutinesJdk8 = "1.8.1"
    const val ktlint = "0.42.1" // we're on an old version of ktlint. TODO upgrade https://github.com/partiql/partiql-lang-kotlin/issues/1418
    const val lombok = "1.18.34"

    // Testing
    const val assertj = "3.11.0"
    const val jacoco = "0.8.11"
    const val junit5 = "5.9.3"
    const val junit5PlatformLauncher = "1.9.3"
    const val junit4 = "4.12"
    const val junit4Params = "1.1.1"
    const val mockito = "4.5.0"
}

object Deps {
    // Language
    const val kotlin = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.kotlin}"
    const val kotlinReflect = "org.jetbrains.kotlin:kotlin-reflect:${Versions.kotlin}"

    // Dependencies
    const val antlr = "org.antlr:antlr4:${Versions.antlr}"
    const val antlrRuntime = "org.antlr:antlr4-runtime:${Versions.antlr}"
    const val awsSdkBom = "com.amazonaws:aws-java-sdk-bom:${Versions.awsSdk}"
    const val awsSdkDynamodb = "com.amazonaws:aws-java-sdk-dynamodb:${Versions.awsSdk}"
    const val awsSdkS3 = "com.amazonaws:aws-java-sdk-s3:${Versions.awsSdk}"
    const val csv = "org.apache.commons:commons-csv:${Versions.csv}"
    const val dotlin = "io.github.rchowell:dotlin:${Versions.dotlin}"
    const val guava = "com.google.guava:guava:${Versions.guava}"
    const val ionElement = "com.amazon.ion:ion-element:${Versions.ionElement}"
    const val ionSchema = "com.amazon.ion:ion-schema-kotlin:${Versions.ionSchema}"
    const val jansi = "org.fusesource.jansi:jansi:${Versions.jansi}"
    const val jgenhtml = "com.googlecode.jgenhtml:jgenhtml:${Versions.jgenhtml}"
    const val jline = "org.jline:jline:${Versions.jline}"
    const val joda = "joda-time:joda-time:${Versions.joda}"
    const val kasechange = "net.pearx.kasechange:kasechange:${Versions.kasechange}"
    const val kotlinPoet = "com.squareup:kotlinpoet:${Versions.kotlinPoet}"
    const val kotlinxCollections = "org.jetbrains.kotlinx:kotlinx-collections-immutable:${Versions.kotlinxCollections}"
    const val picoCli = "info.picocli:picocli:${Versions.picoCli}"
    const val kotlinxCoroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.kotlinxCoroutines}"
    const val kotlinxCoroutinesJdk8 = "org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:${Versions.kotlinxCoroutinesJdk8}"
    const val ktlint = "com.pinterest.ktlint:ktlint-core:${Versions.ktlint}"
    const val lombok = "org.projectlombok:lombok:${Versions.lombok}"

    // Testing
    const val assertj = "org.assertj:assertj-core:${Versions.assertj}"
    const val junit4 = "junit:junit:${Versions.junit4}"
    const val junit4Params = "pl.pragmatists:JUnitParams:${Versions.junit4Params}"
    const val junitApi = "org.junit.jupiter:junit-jupiter-api:${Versions.junit5}"
    const val junitParams = "org.junit.jupiter:junit-jupiter-params:${Versions.junit5}"
    const val junitPlatformLauncher = "org.junit.platform:junit-platform-launcher:${Versions.junit5PlatformLauncher}"
    const val junitVintage = "org.junit.vintage:junit-vintage-engine:${Versions.junit5}"
    const val kotlinTest = "org.jetbrains.kotlin:kotlin-test:${Versions.kotlin}"
    const val kotlinTestJunit = "org.jetbrains.kotlin:kotlin-test-junit5:${Versions.kotlin}"
    const val mockito = "org.mockito:mockito-junit-jupiter:${Versions.mockito}"
    const val ktlintTest = "com.pinterest.ktlint:ktlint-test:${Versions.ktlint}"
}

object Plugins {
    // PartiQL
    const val conventions = "partiql.conventions"
    const val pig = "org.partiql.pig.pig-gradle-plugin"
    const val publish = "org.partiql.gradle.plugin.publish"

    // 3P
    const val antlr = "org.gradle.antlr"
    const val application = "org.gradle.application"
    const val detekt = "io.gitlab.arturbosch.detekt"
    const val dokka = "org.jetbrains.dokka"
    const val library = "org.gradle.java-library"
    const val kotlinLombok = "org.jetbrains.kotlin.plugin.lombok"
    const val testFixtures = "org.gradle.java-test-fixtures"
}