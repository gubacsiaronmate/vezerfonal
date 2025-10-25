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

tasks.register("deploy") {
    doLast {
        val home = System.getProperty("user.home")
            ?: throw GradleException("user.home property is not set")
        
        val pb = ProcessBuilder("bash", "$home/mover.sh")
        
        val sshSock = System.getenv("SSH_AUTH_SOCK") ?: run {
            print("Enter SSH_AUTH_SOCK: ")
            System.`in`.bufferedReader().readLine().also {
                if (it.isBlank()) throw GradleException("SSH_AUTH_SOCK not provided")
            }
        }
        pb.environment()["SSH_AUTH_SOCK"] = sshSock
        
        pb.inheritIO()
        
        println(home)
        println(pb)
        println(sshSock)
        
        val process = pb.start()
        
        Thread { process.inputStream.copyTo(System.out) }.start()
        Thread { process.errorStream.copyTo(System.err) }.start()
        System.`in`?.let { Thread { it.copyTo(process.outputStream) }.start() }
            ?: println("stdin is null")
        
        process.waitFor()
    }
}

tasks.named<ShadowJar>("shadowJar") {
    archiveFileName.set("vezerfonal.jar")
    mergeServiceFiles()
    finalizedBy("deploy")
}

