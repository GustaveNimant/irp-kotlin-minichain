// https://github.com/gradle/kotlin-dsl-samples/blob/master/samples/hello-js/build.gradle.kts
group 'org.example'
version '1.0-SNAPSHOT'

import org.jetbrains.kotlin.gradle.tasks.Kotlin2JsCompile

buildscript {
    extra["kotlinVersion"]  = "1.3.61"
    repositories {
        jcenter()
    }
    dependencies {
	classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${extra["kotlinVersion"]}")
    }
}

plugins {
  id("org.decembrist.kotlin2js.reflection") version "0.1.0-beta-1"
}

repositories {
    jcenter()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-js:${extra["kotlinVersion"]}")
}

application {
    mainClassName = "io.ipfs.kotlin.MainIpfsKt"
}
