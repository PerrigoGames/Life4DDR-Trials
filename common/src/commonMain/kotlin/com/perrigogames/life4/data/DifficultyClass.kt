package com.perrigogames.life4.data

/**
 * Enum to describe a class of difficulty, inside which more specific difficulties
 * are more or less in the same range as each other.
 */
enum class DifficultyClass(val stableId: Long) {
    BEGINNER(1),
    BASIC(2),
    DIFFICULT(3),
    EXPERT(4),
    CHALLENGE(5);

    companion object {
        fun fromStableId(stableId: Long?) = stableId?.let { id -> values().firstOrNull { it.stableId == id } }
        fun parse(chartString: String): DifficultyClass? = when {
            chartString.startsWith("b") -> BEGINNER
            chartString.startsWith("B") -> BASIC
            chartString.startsWith("D") -> DIFFICULT
            chartString.startsWith("E") -> EXPERT
            chartString.startsWith("C") -> CHALLENGE
            else -> null
        }
    }
}
