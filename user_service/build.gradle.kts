import org.gradle.api.tasks.testing.logging.TestExceptionFormat

plugins {
    checkstyle
    java
    jacoco
    id("org.springframework.boot") version "3.0.6"
    id("io.spring.dependency-management") version "1.1.0"
    id("org.jsonschema2pojo") version "1.2.1"
    id("com.diffplug.spotless") version "6.25.0"
    kotlin("jvm") version "1.9.0"
}

group = "odin.zvonok"
version = "0.0.1-SNAPSHOT"

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    /**
     * Spring boot starters
     */
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign:4.0.2")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    /**
     * Database
     */
    implementation("org.liquibase:liquibase-core")
    implementation("redis.clients:jedis:4.3.2")
    runtimeOnly("org.postgresql:postgresql")

    /**
     * Amazon S3
     */
    implementation("com.amazonaws:aws-java-sdk-s3:1.12.464")
    implementation("net.coobird:thumbnailator:0.4.19")

    /**
     * Utils & Logging
     */
    implementation("com.fasterxml.jackson.core:jackson-databind:2.14.2")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
    implementation("org.slf4j:slf4j-api:2.0.5")
    implementation("ch.qos.logback:logback-classic:1.4.6")
    implementation("org.projectlombok:lombok:1.18.26")
    annotationProcessor("org.projectlombok:lombok:1.18.26")
    implementation("org.mapstruct:mapstruct:1.5.3.Final")
    annotationProcessor("org.mapstruct:mapstruct-processor:1.5.3.Final")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.0.2")

    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-csv:2.13.0")

    /**
     * Test containers
     */
    implementation(platform("org.testcontainers:testcontainers-bom:1.17.6"))
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:postgresql")
    testImplementation("com.redis.testcontainers:testcontainers-redis-junit-jupiter:1.4.6")

    /**
     * Tests
     */
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.9.2")
    testImplementation("org.assertj:assertj-core:3.24.2")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

jsonSchema2Pojo {
    setSource(files("src/main/resources/json"))
    targetDirectory = file("${project.buildDir}/generated-sources/js2p")
    targetPackage = "com.json.student"
    setSourceType("jsonschema")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

val test by tasks.getting(Test::class) { testLogging.showStandardStreams = true }

tasks.bootJar {
    archiveFileName.set("service.jar")
}

checkstyle {
    toolVersion = "10.17.0"
    configFile = file("${project.rootDir}/config/checkstyle/checkstyle.xml")
    configProperties = mapOf(
        "checkstyle.suppressions.file" to "${project.rootDir}/config/checkstyle/checkstyle-suppressions.xml"
    )
    checkstyle.enableExternalDtdLoad.set(true)
    isIgnoreFailures = false
}

tasks.named<Checkstyle>("checkstyleMain") {
    source = fileTree("${project.rootDir}/src/main/java") {
        include("**/*.java")
        exclude("**/entity/**", "**/repository/**")
    }
    classpath = files()
}

tasks.named<Checkstyle>("checkstyleTest") {
    source = fileTree("${project.rootDir}/src/test") {
        include("/*.java")
    }
    classpath = files()
}

kotlin {
    jvmToolchain(17)
}

spotless {
    java {
        target("src/**/*.java")
        googleJavaFormat().aosp()
        indentWithSpaces(4)
        trimTrailingWhitespace()
        endWithNewline()
    }
}

/**
 * JaCoCo Configuration
 */
val jacocoIncludes = listOf(
    "**/controller/**",
    "**/filter/**",
    "**/mapper/**",
    "**/service/**",
    "**/validation/**"
)

val jacocoExcludes = listOf(
    "**/adapter/**",
    "**/client/**",
    "**/config/**",
    "**/dto/**",
    "**/entity/**",
    "**/exception/**",
    "**/repository/**"
)

jacoco {
    toolVersion = "0.8.12"
    reportsDirectory.set(layout.buildDirectory.dir("reports/jacoco"))
}

tasks.test {
    finalizedBy(tasks.jacocoTestReport)

    testLogging {
        events("passed", "skipped", "failed")
        exceptionFormat = TestExceptionFormat.FULL
        showStandardStreams = true
    }
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)

    reports {
        html.required.set(true)
        xml.required.set(false)
        csv.required.set(false)

        classDirectories.setFrom(
            sourceSets.main.get().output.asFileTree.matching {
                include(jacocoIncludes)
                exclude(jacocoExcludes)
            }
        )
    }
}

tasks.jacocoTestCoverageVerification {
    dependsOn(tasks.jacocoTestReport)

    violationRules {
        rule {
            element = "CLASS"
            limit {
                minimum = "0.7".toBigDecimal()
            }
        }
    }
}