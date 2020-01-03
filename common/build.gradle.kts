import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    id("com.android.application")
    kotlin("multiplatform")
    id("kotlinx-serialization")
}

android {
    compileSdkVersion(28)
    defaultConfig {
        minSdkVersion(18)
        targetSdkVersion(28)
    }
    sourceSets {
        getByName("main") {
            setRoot("src/androidMain")
        }
        getByName("release") {
            setRoot("src/androidMainRelease")
        }
        getByName("debug") {
            setRoot("src/androidMainDebug")
        }
        getByName("test") {
            setRoot("src/androidUnitTest")
        }
        getByName("testRelease") {
            setRoot("src/androidUnitTestRelease")
        }
        getByName("testDebug") {
            setRoot("src/androidUnitTestDebug")
        }
    }
}

kotlin {
    //select iOS target platform depending on the Xcode environment variables
    val iOSTarget: (String, KotlinNativeTarget.() -> Unit) -> KotlinNativeTarget =
        if (System.getenv("SDK_NAME")?.startsWith("iphoneos") == true)
            ::iosArm64
        else
            ::iosX64

    iOSTarget("ios") {
        binaries {
            framework {
                baseName = "life4common"
            }
        }
    }

    android {
    }

    sourceSets["commonMain"].dependencies {
        implementation("org.jetbrains.kotlin:kotlin-stdlib-common")
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime-common:0.14.0")
    }

    sourceSets["androidMain"].dependencies {
        implementation("org.jetbrains.kotlin:kotlin-stdlib")
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:0.14.0")
        implementation("androidx.core:core-ktx:1.1.0")
    }
}


val packForXcode by tasks.creating(Sync::class) {
    group = "build"

    //selecting the right configuration for the iOS framework depending on the Xcode environment variables
    val mode = System.getenv("CONFIGURATION") ?: "DEBUG"
    val framework = kotlin.targets.getByName<KotlinNativeTarget>("ios").binaries.getFramework(mode)

    inputs.property("mode", mode)
    dependsOn(framework.linkTask)

    val targetDir = File(buildDir, "xcode-frameworks")
    from({ framework.outputDirectory })
    into(targetDir)

    doLast {
        val gradlew = File(targetDir, "gradlew")
        gradlew.writeText("#!/bin/bash\nexport 'JAVA_HOME=${System.getProperty("java.home")}'\ncd '${rootProject.rootDir}'\n./gradlew \$@\n")
        gradlew.setExecutable(true)
    }
}

tasks.getByName("build").dependsOn(packForXcode)
