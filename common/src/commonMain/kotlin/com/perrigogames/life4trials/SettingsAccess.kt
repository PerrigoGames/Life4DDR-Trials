package com.perrigogames.life4trials

expect class SettingsAccess {

    /** @return a stored flag in the user preferences */
    fun getUserFlag(c: Any, flag: String, def: Boolean)

    /** @return a stored int in the user preferences */
    fun getUserInt(c: Any, flag: String, def: Int)

    /** @return a stored Long in the user preferences */
    fun getUserLong(c: Any, flag: String, def: Long)

    /** @return a stored string in the user preferences */
    fun getUserString(c: Any, flag: String, def: String? = null)

    /** @return a stored debug flag in the user preferences, also checking the debug state of the app */
    fun getDebugFlag(c: Any, flag: String)

    fun setUserFlag(c: Any, flag: String, v: Boolean)

    fun setUserInt(c: Any, flag: String, v: Int)

    fun setUserLong(c: Any, flag: String, v: Long)

    fun setUserString(c: Any, flag: String, v: String? = null)

    fun setDebugFlag(c: Any, flag: String, v: Boolean)
}