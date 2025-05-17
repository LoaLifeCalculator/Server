plugins {
    id("org.springframework.boot") version "3.2.5"
    id("io.spring.dependency-management") version "1.1.4"
    kotlin("jvm") version "2.0.0"
    kotlin("plugin.spring") version "2.0.0"
    kotlin("plugin.jpa") version "2.0.0"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("jakarta.validation:jakarta.validation-api:3.0.2")

    // Spring Boot Starter
    implementation("org.springframework.boot:spring-boot-starter-web")

    // Spring Data JPA
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    // MySQL JDBC Driver
    runtimeOnly("com.mysql:mysql-connector-j")

    // Kotlin 관련
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    // 테스트용 (선택)
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    // WebFlux(WebClient)
    implementation("org.springframework.boot:spring-boot-starter-webflux")

    // Spring Data Redis
    implementation("org.springframework.boot:spring-boot-starter-data-redis")

    // Spring Cache (캐시 어노테이션 지원용)
    implementation("org.springframework.boot:spring-boot-starter-cache")

    // Jackson 기반 JSON 직렬화 (선택, Redis에서 DTO를 JSON으로 저장할 때 유용)
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "17"
    }
}
