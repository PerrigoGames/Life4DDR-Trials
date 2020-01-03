package com.perrigogames.life4

actual object SettingsAccess {

    /** @return a stored flag in the user preferences */
    actual fun getUserFlag(flag: String, def: Boolean) = false

    /** @return a stored int in the user preferences */
    actual fun getUserInt(flag: String, def: Int) = 0

    /** @return a stored Long in the user preferences */
    actual fun getUserLong(flag: String, def: Long) = 0L

    /** @return a stored string in the user preferences */
    actual fun getUserString(flag: String, def: String?): String? = ""

    /** @return a stored debug flag in the user preferences, also checking the debug state of the app */
    actual fun getDebugFlag(flag: String) = false

    actual fun setUserFlag(flag: String, v: Boolean) {}

    actual fun setUserInt(flag: String, v: Int) {}

    actual fun setUserLong(flag: String, v: Long) {}

    actual fun setUserString(flag: String, v: String?) {}

    actual fun setDebugFlag(flag: String, v: Boolean) {}
}