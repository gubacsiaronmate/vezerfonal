import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.ktor)
    application
}

group = "com.smokinggunstudio.vezerfonal"
version = "1.0.0"
application {
    mainClass.set("com.smokinggunstudio.vezerfonal.ApplicationKt")
    
    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

dependencies {
    // my imports
    implementation(libs.ktor.server.auth)
    implementation(libs.exposed.core)
    implementation(libs.exposed.jdbc)
    implementation(libs.exposed.kotlin.datetime)
    implementation(libs.postgresql)
    implementation(libs.ktor.serialization.kotlinxJson)
    implementation(libs.ktor.server.contentNegotiation)
    implementation(libs.kotlinx.datetime)
    implementation(libs.bcrypt)
    
    // default imports
    implementation(projects.shared)
    implementation(libs.logback)
    implementation(libs.ktor.serverCore)
    implementation(libs.ktor.serverNetty)
    testImplementation(libs.ktor.serverTestHost)
    testImplementation(libs.kotlin.testJunit)
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_21)
        freeCompilerArgs.set(listOf("-Xcontext-parameters"))
    }
}

tasks.withType<JavaCompile> {
    options.release.set(21)
}

tasks.register<Exec>("deploy") {
    commandLine("bash", "-c", $$"scp $JAR_PATH deployment:$DEPLOY_PATH && rm -f $JAR_PATH")
}

tasks.named<ShadowJar>("shadowJar") {
    archiveFileName.set("vezerfonal.jar")
    mergeServiceFiles()
    finalizedBy("deploy")
}

