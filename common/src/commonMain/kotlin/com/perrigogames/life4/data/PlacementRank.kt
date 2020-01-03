package com.perrigogames.life4.data

enum class PlacementRank(val stableId: Long,
                         val parent: LadderRank) {

    WOOD(20, LadderRank.WOOD3),
    BRONZE(25, LadderRank.BRONZE3),
    SILVER(20, LadderRank.SILVER3),
    GOLD(25, LadderRank.GOLD3)
}