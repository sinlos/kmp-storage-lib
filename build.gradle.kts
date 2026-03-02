group = "world.chebur.kmp"
version = findProperty("version")?.toString() ?: "1.0.0"

plugins {
    alias(libs.plugins.kotlinMultiplatform) apply true
    alias(libs.plugins.androidMultiplatformLibrary) apply true
    `maven-publish`
}

kotlin {
    jvmToolchain(21)

    androidLibrary {
        namespace = "world.chebur.kmp.storage"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = 24
        // В этом плагине настройки тестов могут задаваться здесь
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

        // В плагине Android Multiplatform Library инструментальные тесты 
        // обычно попадают в androidDeviceTest
        val androidDeviceTest by getting {
            dependsOn(commonTest)
            dependsOn(androidMain)
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

        jsMain.get().dependsOn(webMain)
        wasmJsMain.get().dependsOn(webMain)
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
