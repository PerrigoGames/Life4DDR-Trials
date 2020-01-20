package com.perrigogames.life4

expect object SettingsAccess {

    /** @return a stored flag in the user preferences */
    fun getUserFlag(flag: String, def: Boolean): Boolean

    /** @return a stored int in the user preferences */
    fun getUserInt(flag: String, def: Int): Int

    /** @return a stored Long in the user preferences */
    fun getUserLong(flag: String, def: Long): Long

    /** @return a stored string in the user preferences */
    fun getUserString(flag: String, def: String? = null): String?

    /** @return a stored debug flag in the user preferences, also checking the debug state of the app */
    fun getDebugFlag(flag: String): Boolean

    fun setUserFlag(flag: String, v: Boolean)

    fun setUserInt(flag: String, v: Int)

    fun setUserLong(flag: String, v: Long)

    fun setUserString(flag: String, v: String? = null)

    fun setDebugFlag(flag: String, v: Boolean)
}