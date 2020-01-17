package com.perrigogames.life4.data

enum class GameVersion(val stableId: Long) {
    UNKNOWN(0),
    DDR_1ST_MIX(1),
    DDR_2ND_MIX(2),
    DDR_3RD_MIX(3),
    DDR_4TH_MIX(4),
    DDR_5TH_MIX(5),
    MAX(6),
    MAX2(7),
    EXTREME(8),
    SUPERNOVA(9),
    SUPERNOVA2(10),
    X(11),
    X2(12),
    X3_VS_2ND_MIX(13),
    DDR_2013(14),
    DDR_2014(15),
    ACE(16),
    A20(17);

    companion object {
        fun fromStableId(stableId: Long?) = stableId?.let { id -> values().firstOrNull { it.stableId == id } }
    }
}
