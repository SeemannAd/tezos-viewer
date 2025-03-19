package de.showcase.tezos_viewer.domains.shared.extensions

import android.content.Context
import android.content.SharedPreferences

private const val PREF_NAME = "Tezos-Preferences"

private fun Context.getPreferences(): SharedPreferences {
    return this.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
}

/**
 * Write to Shared Preferences
 * @value: from type String
 */
fun Context.write(key: String, value: String) : Boolean {
    val editor = getPreferences().edit()
    editor.putString(key, value)
    return editor.commit()
}

/**
 * Read from Shared Preferences
 * @value: from type String
 */
fun Context.read(key: String): String {
    return getPreferences().getString(key, "") ?: ""
}

/**
 * Clear Shared Preferences
 */
fun Context.clear() {
    getPreferences().all.clear()
}