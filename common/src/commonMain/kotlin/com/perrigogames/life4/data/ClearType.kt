package com.perrigogames.life4.data

/**
 * Enum to describe the possible ways to finish a song, and consequently an entire folder.
 */
enum class ClearType(val stableId: Long,
                     val passing: Boolean = true) {
    NO_PLAY(0, false),
    FAIL(1, false),
    CLEAR(2),
    LIFE4_CLEAR(3),
    GOOD_FULL_COMBO(4),
    GREAT_FULL_COMBO(5),
    PERFECT_FULL_COMBO(6),
    MARVELOUS_FULL_COMBO(7);

    companion object {
        const val NO_PLAY_KEY = "no_play"
        const val FAIL_KEY = "fail"
        const val CLEAR_KEY = "clear"
        const val LIFE4C_KEY = "life4_clear"
        const val LIFE4_KEY = "life4"
        const val GOOD_KEY = "good"
        const val FC_KEY = "fc"
        const val GREAT_KEY = "great"
        const val GFC_KEY = "gfc"
        const val PERFECT_KEY = "perfect"
        const val PFC_KEY = "pfc"
        const val MARVELOUS_KEY = "marvelous"
        const val MFC_KEY = "mfc"

        fun fromStableId(stableId: Long?) = stableId?.let { id -> values().firstOrNull { it.stableId == id } }

        fun parse(v: String) = when(v) {
            FAIL_KEY -> FAIL
            CLEAR_KEY -> CLEAR
            LIFE4_KEY -> LIFE4_CLEAR
            FC_KEY, GOOD_KEY -> GOOD_FULL_COMBO
            GFC_KEY, GREAT_KEY -> GREAT_FULL_COMBO
            PFC_KEY, PERFECT_KEY -> PERFECT_FULL_COMBO
            MFC_KEY, MARVELOUS_KEY -> MARVELOUS_FULL_COMBO
            else -> null
        }
    }
}
