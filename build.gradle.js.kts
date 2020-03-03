buildscript {
    extra["assertjVersion"] = "3.10.0"
    extra["gradleVersion"]  = "0.20.0"
    extra["jacocoVersion"]  = "0.8.1"
    extra["junitVersion"]   = "4.12"
    extra["kotlinVersion"]  = "1.3.61"
    extra["mockitoVersion"] = "2.12.0"
    extra["moshiVersion"]   = "1.4.0"
    extra["okhttpVersion"]  = "4.4.0"
    extra["okioVersion"]  = "2.4.3"
    
    repositories {
        jcenter()
    }

    dependencies {
        classpath(kotlin("gradle-plugin", version = "${extra["kotlinVersion"]}"))
	classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${extra["kotlinVersion"]}")
        classpath("com.github.ben-manes:gradle-versions-plugin:${extra["gradleVersion"]}")
    }
}

plugins {
    jacoco
    java
    kotlin("js") version "${extra["kotlinVersion"]}"
    application
    "com.github.ben-manes.versions"
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

jacoco {
 toolVersion = "${extra["jacocoVersion"]}"
}

group = "minichain"

repositories {
    jcenter()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:${extra["kotlinVersion"]}")
    implementation("com.squareup.moshi:moshi:${extra["moshiVersion"]}")
    implementation("com.squareup.okio:okio:${extra["okioVersion"]}")
    implementation("com.squareup.okhttp3:okhttp:${extra["okhttpVersion"]}")

    testImplementation("junit:junit:${extra["junitVersion"]}")
    testImplementation("org.mockito:mockito-core:${extra["mockitoVersion"]}")
    testImplementation("com.squareup.okhttp3:mockwebserver:${extra["okhttpVersion"]}")
    testImplementation("org.assertj:assertj-core:${extra["assertjVersion"]}")
}

application {
    mainClassName = "io.ipfs.kotlin.MainIpfsKt"
}
