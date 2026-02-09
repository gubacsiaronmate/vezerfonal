import org.gradle.kotlin.dsl.implementation
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeHotReload)
    alias(libs.plugins.nativeCocoapods)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.parcelize)
    alias(libs.plugins.google.services)
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    
    cocoapods {
        summary = "Shared module for iOS"
        homepage = "https://example.com"
        ios.deploymentTarget = "15.0"
        
        version = "1.0.0"   // << REQUIRED
        
        framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }
    
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }
    
    jvm()
    
    js {
        browser()
        binaries.executable()
    }
    
    configurations.all {
        resolutionStrategy {
            force(libs.kotlinx.datetime)
        }
    }
    
    sourceSets {
        androidMain.dependencies {
            implementation(libs.frontend.firebase.messaging)
            implementation(libs.server.firebase.messaging)
            implementation(libs.androidx.animation.core)
            implementation(libs.ktor.client.okhttp)
            implementation(compose.animation)
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.bcrypt)
            implementation(libs.kvault)
            implementation(libs.kotlinx.datetime)
        }
        
        commonMain.dependencies {
            implementation(libs.voyager.navigator)
            implementation(libs.kotlinx.datetime)
            implementation(libs.multiplatformSettings)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinxJson)
            implementation(compose.material3)
            implementation(compose.materialIconsExtended)
            implementation(compose.components.resources)
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.animation)
            implementation(compose.animationGraphics)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(projects.shared)
        }
        
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
            implementation(libs.skiko)
            implementation(libs.kvault)
            implementation(libs.kotlinx.datetime)
        }
        
        jsMain.dependencies {
            implementation(libs.ktor.client.js)
            implementation(npm("bcryptjs", "2.4.3"))
            implementation(libs.skiko)
            implementation(libs.multiplatformSettings)
            implementation(libs.kotlinx.datetime)
        }
        
        jvmMain.dependencies {
            implementation(libs.bcrypt)
            implementation(libs.ktor.client.cio)
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutinesSwing)
            implementation(libs.skiko)
            implementation(libs.multiplatformSettings)
            implementation(libs.kotlinx.datetime)
        }
    }
}

android {
    namespace = "com.smokinggunstudio.vezerfonal"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    
    defaultConfig {
        applicationId = "com.smokinggunstudio.vezerfonal"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlin {
        compilerOptions {
            freeCompilerArgs.set(listOf("-Xexpect-actual-classes"))
        }
    }
}

dependencies {
    implementation(libs.ktor.client.logging)
    debugImplementation(compose.uiTooling)
}

compose.desktop {
    application {
        mainClass = "com.smokinggunstudio.vezerfonal.MainKt"
        
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.smokinggunstudio.vezerfonal"
            packageVersion = "1.0.0"
        }
    }
}
