plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
}

buildscript {
    dependencies {
        classpath (libs.kotlin.gradle.plugin)
        classpath (libs.gradle)
    }
}