group = "world.chebur.kmp"
version = findProperty("version")?.toString() ?: "1.0.0"

plugins {
    alias(libs.plugins.kotlinMultiplatform) apply true
    alias(libs.plugins.androidMultiplatformLibrary) apply true
    alias(libs.plugins.kotlinXserialization) apply true
    alias(libs.plugins.wire) apply true
    `maven-publish`
}

wire {
    kotlin {
        // Мы используем sourceSets commonMain, поэтому Wire сам найдет .proto в src/commonMain/proto
    }
    sourcePath {
        srcDir("src/commonTest/proto")
    }
}

kotlin {
    jvmToolchain(21)

    androidLibrary {
        namespace = "world.chebur.kmp.storage"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = 24
        withDeviceTest { }
    }

    jvm()

    js {
        browser()
        binaries.executable()
    }

    @OptIn(org.jetbrains.kotlin.gradle.ExperimentalWasmDsl::class)
    wasmJs {
        browser()
        binaries.executable()
    }

    sourceSets {
        commonMain {
            dependencies {
                api(libs.kotlinx.serialization.json)
                api(libs.okio)
                api(libs.wire.runtime)
                api(libs.kotlinx.coroutines.core)
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(libs.kotlin.test)
                implementation(libs.kotlinx.coroutines.test)
            }
        }

        val dataStore by creating {
            dependsOn(commonMain.get())
            dependencies {
                api(libs.datastore.core)
                api(libs.datastore.okio)
                api(libs.datastore.preferences)
                api(libs.datastore.typed)
            }
        }

        val androidMain by getting {
            dependsOn(dataStore)
        }

        val androidDeviceTest by getting {
            dependsOn(commonTest)
            dependencies {
                implementation(libs.androidx.test.ext.junit)
                implementation(libs.androidx.test.runner)
            }
        }

        val jvmMain by getting {
            dependsOn(dataStore)
        }

        val webMain by creating {
            dependsOn(commonMain.get())
            dependencies {
                implementation(libs.kotlinx.browser)
            }
        }

        val webTest by creating {
            dependsOn(commonTest)
            dependsOn(webMain)
        }

        jsMain.get().dependsOn(webMain)

        val jsTest by getting {
            dependsOn(webTest)
        }

        wasmJsMain.get().dependsOn(webMain)

        val wasmJsTest by getting {
            dependsOn(webTest)
        }
    }
}

// Исправленная задача для диагностики, совместимая с configuration cache
tasks.register("printSourceSets") {
    group = "help"
    description = "Prints all Kotlin source set names"

    // Считываем имена на этапе конфигурации
    val kotlin = project.extensions.getByType(org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension::class.java)
    val sourceSetNames = kotlin.sourceSets.map { it.name }

    doLast { // Выводим на этапе выполнения
        println("\n--- Discovered Kotlin Source Sets ---")
        sourceSetNames.sorted().forEach { name ->
            println(name)
        }
        println("-------------------------------------\n")
    }
}

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/sinlos/kmp-storage-lib")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }

    publications.withType<MavenPublication> {
        groupId = "world.chebur.kmp"
        version = findProperty("version")?.toString() ?: "1.0.0"
        artifactId = when (name) {
            "kotlinMultiplatform" -> "kmp-storage"
            "android" -> "kmp-storage-android"
            "js" -> "kmp-storage-js"
            "jvm" -> "kmp-storage-jvm"
            "wasmJs" -> "kmp-storage-wasmjs"
            else -> "kmp-storage"
        }

        pom {
            name.set("KMP Storage Library")
            description.set("Kotlin Multiplatform Storage Library")
            url.set("https://github.com/sinlos/kmp-storage-lib")

            developers {
                developer {
                    id.set("sinlos")
                    name.set("Ilya Pogrebenko")
                }
            }

            scm {
                connection.set("scm:git:https://github.com/sinlos/kmp-storage-lib.git")
                developerConnection.set("scm:git:https://github.com/sinlos/kmp-storage-lib.git")
                url.set("https://github.com/sinlos/kmp-storage-lib")
            }
        }
    }
}
