package com.perrigogames.life4trials

object SettingsKeys {
    const val KEY_LIST_TINT_COMPLETED = "KEY_LIST_TINT_COMPLETED"
    const val KEY_LIST_SHOW_EX = "KEY_LIST_SHOW_EX"
    const val KEY_LIST_SHOW_EX_REMAINING = "KEY_LIST_SHOW_EX_REMAINING"
    const val KEY_LIST_HIGHLIGHT_NEW = "KEY_LIST_HIGHLIGHT_NEW"
    const val KEY_DETAILS_PHOTO_SELECT = "KEY_DETAILS_PHOTO_SELECT"
    const val KEY_DETAILS_EXPERT = "KEY_DETAILS_EXPERT"
    const val KEY_DETAILS_ENFORCE_EXPERT = "KEY_DETAILS_ENFORCE_EXPERT"
    const val KEY_INFO_NAME = "KEY_INFO_NAME"
    const val KEY_INFO_RANK = "KEY_INFO_RANK"
    const val KEY_INFO_RIVAL_CODE = "KEY_INFO_RIVAL_CODE"
    const val KEY_INFO_TWITTER_NAME = "KEY_INFO_TWITTER_NAME"
    const val KEY_INFO_IMPORT = "KEY_INFO_IMPORT"
    const val KEY_SUBMISSION_NOTIFICAION = "KEY_SUBMISSION_NOTIFICAION"
    const val KEY_SUBMISSION_NOTIFICAION_TEST = "KEY_SUBMISSION_NOTIFICAION_TEST"
    const val KEY_RECORDS_REMAINING_EX = "KEY_RECORDS_REMAINING_EX"
    const val KEY_SHOP_LIFE4 = "KEY_SHOP_LIFE4"
    const val KEY_SHOP_DANGERSHARK = "KEY_SHOP_DANGERSHARK"
    const val KEY_IMPORT_GAME_VERSION = "KEY_IMPORT_GAME_VERSION"
    const val KEY_FEEDBACK = "KEY_FEEDBACK"
    const val KEY_FIND_US_TWITTER = "KEY_FIND_US_TWITTER"
    const val KEY_CREDITS = "KEY_CREDITS"
    const val KEY_CATEGORY_IMPORT = "KEY_CATEGORY_IMPORT"
    const val KEY_IMPORT_DATA = "KEY_IMPORT_DATA"
    const val KEY_IMPORT_SKIP_DIRECTIONS = "KEY_IMPORT_SKIP_DIRECTIONS"
    const val KEY_IMPORT_VIEW_LIST = "KEY_IMPORT_VIEW_LIST"
    const val KEY_LADDER_CLEAR = "KEY_LADDER_CLEAR"
    const val KEY_SONG_RESULTS_CLEAR = "KEY_SONG_RESULTS_CLEAR"
    const val KEY_REFRESH_SONG_DB = "KEY_REFRESH_SONG_DB"
    const val KEY_RECORDS_CLEAR = "KEY_RECORDS_CLEAR"

    const val KEY_DEBUG = "KEY_DEBUG"
    const val KEY_DEBUG_DETAILS_DISPLAY_ALL_RANKS = "KEY_DEBUG_DETAILS_DISPLAY_ALL_RANKS"
    const val KEY_DEBUG_BYPASS_STAT_ENTRY = "KEY_DEBUG_BYPASS_STAT_ENTRY"
    const val KEY_DEBUG_ACCEPT_INVALID = "KEY_DEBUG_ACCEPT_INVALID"
    const val KEY_DEBUG_BYPASS_CAMERA = "KEY_DEBUG_BYPASS_CAMERA"
    const val KEY_DEBUG_INDUCE_CRASH = "KEY_DEBUG_INDUCE_CRASH"
    const val KEY_DEBUG_LEADERBOARD = "KEY_DEBUG_LEADERBOARD"
    const val KEY_DEBUG_DATA_DUMP = "KEY_DEBUG_DATA_DUMP"
    const val KEY_DEBUG_NOTIF_PLACEMENT = "KEY_DEBUG_NOTIF_PLACEMENT"
    const val KEY_DEBUG_NOTIF_LADDER_RANK = "KEY_DEBUG_NOTIF_LADDER_RANK"
    const val KEY_DEBUG_NOTIF_TRIAL_RANK = "KEY_DEBUG_NOTIF_TRIAL_RANK"
    const val KEY_DEBUG_NOTIF_A20 = "KEY_DEBUG_NOTIF_A20"
    const val KEY_DEBUG_SONG_RECORDS = "KEY_DEBUG_SONG_RECORDS"

    const val KEY_DEBUG_RANK_PREFIX = "KEY_DEBUG_RANK_PREFIX"
}


/** @return a stored flag in the user preferences */
expect fun getUserFlag(c: Any, flag: String, def: Boolean)

/** @return a stored int in the user preferences */
expect fun getUserInt(c: Any, flag: String, def: Int)

/** @return a stored Long in the user preferences */
expect fun getUserLong(c: Any, flag: String, def: Long)

/** @return a stored string in the user preferences */
expect fun getUserString(c: Any, flag: String, def: String?

/** @return a stored debug flag in the user preferences, also checking the debug state of the app */
expect fun getDebugFlag(c: Any, flag: String)

expect fun setUserFlag(c: Any, flag: String, v: Boolean)

expect fun setUserInt(c: Any, flag: String, v: Int)

expect fun setUserLong(c: Any, flag: String, v: Long)

expect fun setUserString(c: Any, flag: String, v: String?

expect fun setDebugFlag(c: Any, flag: String, v: Boolean)