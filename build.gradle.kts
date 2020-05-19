import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    application
    kotlin("jvm") version "1.3.72"
    id("com.github.johnrengelman.shadow") version "5.2.0"
    id("io.spring.dependency-management") version "1.0.9.RELEASE"
}

application {
//    mainClassName = "main.ConsoleAppKt"
    mainClassName = "main.WebAppKt"
}

tasks {
    named<ShadowJar>("shadowJar") {
        archiveBaseName.set("reactor-workshop")
        mergeServiceFiles()
        manifest {
            description = "Workshop on Reactive Programming using Project Reactor"
        }
    }
}

group = "com.github.sithengineer"
version = "2020.1"

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}

dependencyManagement { // 2) add this whole part
    imports {
        mavenBom("io.projectreactor:reactor-bom:Californium-RELEASE")
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))

    implementation("io.projectreactor.netty:reactor-netty")
    implementation("io.projectreactor:reactor-core:3.3.5.RELEASE")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions:1.0.2.RELEASE")

    testImplementation("io.projectreactor:reactor-test:3.3.5.RELEASE")
    testImplementation("org.junit.jupiter:junit-jupiter:5.6.2")
}