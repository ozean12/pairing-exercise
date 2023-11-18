import java.io.File
import java.io.FileInputStream
import java.util.*
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.flywaydb.flyway") version "9.3.1"
    id("org.springframework.boot") version "2.7.3"
    id("io.spring.dependency-management") version "1.0.14.RELEASE"
    kotlin("jvm") version "1.5.0"
    kotlin("plugin.spring") version "1.5.0"
    kotlin("kapt") version "1.9.10"
    kotlin("plugin.allopen") version "1.8.0"
    kotlin("plugin.jpa") version "1.8.22"
    application
    distribution
}

group = "io.billie"
version = "0.0.1"
java.sourceCompatibility = JavaVersion.VERSION_11

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.Embeddable")
    annotation("jakarta.persistence.MappedSuperclass")
}

val dbConf = Properties().apply {
    load(FileInputStream(File(rootProject.rootDir, "database.env")))
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("org.mapstruct:mapstruct:1.5.5.Final")
    kapt("org.mapstruct:mapstruct-processor:1.5.5.Final")

    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-web")

    implementation("org.springdoc:springdoc-openapi-data-rest:1.6.11")
    implementation("org.springdoc:springdoc-openapi-ui:1.6.11")
    implementation("org.springdoc:springdoc-openapi-kotlin:1.6.11")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    runtimeOnly("org.postgresql:postgresql")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

flyway {
    url = dbConf.getProperty("DATABASE_URL")
    user = dbConf.getProperty("POSTGRES_USER")
    password = dbConf.getProperty("POSTGRES_PASSWORD")
    locations = arrayOf(dbConf.getProperty("DATABASE_MIGRATION"))
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
