plugins {
    alias(libs.plugins.kotlinMultiplatform) apply true
    alias(libs.plugins.androidMultiplatformLibrary) apply true
    `maven-publish`
}

kotlin {
    androidLibrary {
        namespace = "world.chebur.kmp.storage"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
	    group = "world.chebur.kmp"
	    version = "1.0.0"
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

        commonTest {
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

        androidMain.get().dependsOn(dataStore)
        jvmMain.get().dependsOn(dataStore)

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
    publications {
    }
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/sinlos/kmp-storage")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
}