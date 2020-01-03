package com.perrigogames.life4.data

/**
 * Enum class representing a Rank that a player can earn in a LIFE4 Trial.
 */
enum class TrialRank(val stableId: Long,
                     val parent: LadderRank) {

    WOOD(10, LadderRank.WOOD3),
    BRONZE(15, LadderRank.BRONZE3),
    SILVER(20, LadderRank.SILVER3),
    GOLD(25, LadderRank.GOLD3),
    PLATINUM(27, LadderRank.PLATINUM3),
    DIAMOND(30, LadderRank.DIAMOND3),
    COBALT(35, LadderRank.COBALT3),
    AMETHYST(40, LadderRank.AMETHYST3),
    EMERALD(45, LadderRank.EMERALD3);

    val next get() = when(this) {
        WOOD -> BRONZE
        BRONZE -> SILVER
        SILVER -> GOLD
        GOLD -> DIAMOND // PLATINUM isn't really used for Trials
        PLATINUM -> DIAMOND
        DIAMOND -> COBALT
        COBALT -> AMETHYST
        AMETHYST -> EMERALD
        EMERALD -> EMERALD
    }

    /**
     * Generates a list of this and all [TrialRank]s that are higher than this.
     */
    val andUp: Array<TrialRank>
        get() = values().let { it.copyOfRange(this.ordinal, it.size) }

    companion object {
        fun parse(s: String?): TrialRank? = when (s) {
            null, "NONE" -> null
            else -> valueOf(s)
        }

        fun parse(stableId: Long): TrialRank? = values().firstOrNull { it.stableId == stableId }

        fun fromLadderRank(userRank: LadderRank?, parsePlatinum: Boolean) = when(userRank) {
            null -> null
            LadderRank.WOOD1, LadderRank.WOOD2, LadderRank.WOOD3 -> WOOD
            LadderRank.BRONZE1, LadderRank.BRONZE2, LadderRank.BRONZE3 -> BRONZE
            LadderRank.SILVER1, LadderRank.SILVER2, LadderRank.SILVER3 -> SILVER
            LadderRank.GOLD1, LadderRank.GOLD2, LadderRank.GOLD3 -> GOLD
            LadderRank.PLATINUM1, LadderRank.PLATINUM2, LadderRank.PLATINUM3 -> if (parsePlatinum) PLATINUM else GOLD
            LadderRank.DIAMOND1, LadderRank.DIAMOND2, LadderRank.DIAMOND3 -> DIAMOND
            LadderRank.COBALT1, LadderRank.COBALT2, LadderRank.COBALT3 -> COBALT
            LadderRank.AMETHYST1, LadderRank.AMETHYST2, LadderRank.AMETHYST3 -> AMETHYST
            LadderRank.EMERALD1, LadderRank.EMERALD2, LadderRank.EMERALD3 -> EMERALD
        }
    }
}