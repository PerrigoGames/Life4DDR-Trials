package com.perrigogames.life4

import android.content.Context
import android.preference.PreferenceManager
import androidx.core.content.edit

actual object SettingsAccess {

    private lateinit var context: Context
    private var isDebug: Boolean = false

    fun initiate(context: Context, isDebug: Boolean) {
        this.context = context
        this.isDebug = isDebug
    }

    /** @return a stored flag in the user preferences */
    actual fun getUserFlag(flag: String, def: Boolean) = userPrefs.getBoolean(flag, def)

    /** @return a stored int in the user preferences */
    actual fun getUserInt(flag: String, def: Int) = userPrefs.getInt(flag, def)

    /** @return a stored Long in the user preferences */
    actual fun getUserLong(flag: String, def: Long) = userPrefs.getLong(flag, def)

    /** @return a stored string in the user preferences */
    actual fun getUserString(flag: String, def: String?) = userPrefs.getString(flag, def)

    /** @return a stored debug flag in the user preferences, also checking the debug state of the app */
    actual fun getDebugFlag(flag: String) = isDebug && getUserFlag(flag, false)

    actual fun setUserFlag(flag: String, v: Boolean) = userPrefs.edit(true) { putBoolean(flag, v) }

    actual fun setUserInt(flag: String, v: Int) = userPrefs.edit(true) { putInt(flag, v) }

    actual fun setUserLong(flag: String, v: Long) = userPrefs.edit(true) { putLong(flag, v) }

    actual fun setUserString(flag: String, v: String?) = userPrefs.edit(true) { putString(flag, v) }

    actual fun setDebugFlag(flag: String, v: Boolean) = setUserFlag(flag, v)

    private val userPrefs get() = PreferenceManager.getDefaultSharedPreferences(context)
}