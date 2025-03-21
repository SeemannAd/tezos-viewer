package de.showcase.tezos_viewer.tester

import android.content.Context
import android.os.Build
import java.io.BufferedReader
import java.io.InputStreamReader

class Tester {

    @Target(AnnotationTarget.FUNCTION)
    @Retention(AnnotationRetention.RUNTIME)
    annotation class TestDescription(
        val Description: String = "",
        val GIVEN: String,
        val WHEN: String,
        val THEN: String,
    )

    companion object {
        fun loadJsonFromAssets(context: Context, fileName: String): String {
            val inputStream = context.assets.open(fileName)
            val bufferedReader = BufferedReader(InputStreamReader(inputStream))
            return bufferedReader.use { it.readText() }
        }

        fun isRunningOnEmulator(): Boolean {
            return Build.FINGERPRINT.startsWith("generic") ||
                    Build.FINGERPRINT.startsWith("unknown") ||
                    Build.MODEL.contains("google_sdk") ||
                    Build.MODEL.contains("Emulator") ||
                    Build.MODEL.contains("Android SDK built for x86") ||
                    Build.BOARD.contains("QC_Reference_Phone") ||
                    Build.MANUFACTURER.contains("Genymotion") ||
                    Build.HARDWARE.contains("goldfish") ||
                    Build.HARDWARE.contains("ranchu")
        }
    }
}

