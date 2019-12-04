package com.perrigogames.life4trials

actual class SettingsAccess {
    /** @return a stored flag in the user preferences */
    fun getUserFlag(c: Any, flag: String, def: Boolean) = userPrefs(c).getBoolean(flag, def)

    /** @return a stored int in the user preferences */
    fun getUserInt(c: Any, flag: String, def: Int) = userPrefs(c).getInt(flag, def)

    /** @return a stored Long in the user preferences */
    fun getUserLong(c: Any, flag: String, def: Long) = userPrefs(c).getLong(flag, def)

    /** @return a stored string in the user preferences */
    fun getUserString(c: Any, flag: String, def: String?): String? = userPrefs(c).getString(flag, def)

    /** @return a stored debug flag in the user preferences, also checking the debug state of the app */
    fun getDebugFlag(c: Any, flag: String) = BuildConfig.DEBUG && getUserFlag(c, flag, false)

    fun setUserFlag(c: Any, flag: String, v: Boolean) = userPrefs(c).edit(true) { putBoolean(flag, v) }

    fun setUserInt(c: Any, flag: String, v: Int) = userPrefs(c).edit(true) { putInt(flag, v) }

    fun setUserLong(c: Any, flag: String, v: Long) = userPrefs(c).edit(true) { putLong(flag, v) }

    fun setUserString(c: Any, flag: String, v: String?) = userPrefs(c).edit(true) { putString(flag, v) }

    fun setDebugFlag(c: Any, flag: String, v: Boolean) = setUserFlag(c, flag, v)

    private fun userPrefs(c: Context) =
        PreferenceManager.getDefaultSharedPreferences(c)
}