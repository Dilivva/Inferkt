import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.publish)
}

kotlin {
    androidTarget {
        publishAllLibraryVariants()
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "library"
            isStatic = true
        }
        it.compilations.getByName("main") {
            cinterops {
                val libinferkt by creating{
                    val headerPath = "$projectDir/src/native/src"
                    includeDirs(headerPath)
                }
            }
        }
    }

    sourceSets {
        commonMain.dependencies {
            //put your multiplatform dependencies here
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

android {
    namespace = "com.dilivva.inferkt"
    compileSdk = 35

    defaultConfig {
        minSdk = 24

        externalNativeBuild {
            cmake {
                arguments("-DLLAMA_BUILD_COMMON=ON","-DGGML_LLAMAFILE=OFF","-DGGML_OPENMP=OFF","-DCMAKE_BUILD_TYPE=Release")
            }
        }
        ndk {
            abiFilters.add("arm64-v8a")
        }
    }


    externalNativeBuild {
        cmake {
            path = file("./src/androidMain/cpp/CMakeLists.txt")
            version = "3.22.1"
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}
