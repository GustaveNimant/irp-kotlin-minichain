// https://github.com/gradle/kotlin-dsl-samples/blob/master/samples/hello-js/build.gradle.kts
group = "minichain"

// import org.jetbrains.kotlin.gradle.tasks.Kotlin2JsCompile

buildscript {
    extra["kotlinVersion"]  = "1.3.61"
    repositories {
        jcenter()
    }
    dependencies {
	classpath(kotlin("gradle-plugin", version = "${extra["kotlinVersion"]}"))
	classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${extra["kotlinVersion"]}")
    }
}

plugins {
    id("org.jetbrains.kotlin.js") version "${extra["kotlinVersion"]}"
}

repositories {
    jcenter()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-js:${extra["kotlinVersion"]}")
}

