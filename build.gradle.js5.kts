import org.jetbrains.kotlin.gradle.tasks.Kotlin2JsCompile

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
	classpath("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${extra["kotlinVersion"]}")
    }
}

plugins {
    id("org.jetbrains.kotlin.js") version "${extra["kotlinVersion"]}"
}

group = "kotlin-minichain"

repositories {
    jcenter()
}

dependencies {
    implementation(kotlin("stdlib-js"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${extra["kotlinVersion"]}")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-js:${extra["kotlinVersion"]}")
    implementation("org.jetbrains.kotlin:kotlin-stdlib:${extra["kotlinVersion"]}")
    implementation("com.squareup.moshi:moshi:${extra["moshiVersion"]}")
    implementation("com.squareup.okio:okio:${extra["okioVersion"]}")
    implementation("com.squareup.okhttp3:okhttp:${extra["okhttpVersion"]}")
}

tasks {
    "compileKotlin2Js"(Kotlin2JsCompile::class)  {
        kotlinOptions.metaInfo = true
    }
}
