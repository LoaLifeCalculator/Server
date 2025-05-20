package jun.watson.loalife.server.data

import jun.watson.loalife.server.data.CategoryCode.*
import jun.watson.loalife.server.data.SearchType.*

const val DEFAULT_ID = 0

enum class Item(
    val korean: String,
    val id: Int,
    val tier: Int,
    val categoryCode: CategoryCode,
    val priceDivider: Int,
    val searchType: SearchType
) {

    DESTINY_DESTRUCTION_STONE(
        "운명의 파괴석",
        66102006,
        4,
        REFINING_ADDITIONAL_MATERIAL,
        10,
        MARKET
    ),
    REFINED_OBLITERATION_STONE(
        "정제된 파괴강석",
        66102005,
        4,
        REFINING_ADDITIONAL_MATERIAL,
        10,
        MARKET
    ),
    OBLITERATION_STONE(
        "파괴강석",
        66102004,
        3,
        REFINING_ADDITIONAL_MATERIAL,
        10,
        MARKET
    ),
    DESTRUCTION_STONE_CRYSTAL(
        "파괴석 결정",
        66102003,
        3,
        REFINING_ADDITIONAL_MATERIAL,
        10,
        MARKET
    ),

    DESTINY_GUARDIAN_STONE(
        "운명의 수호석",
        66102106,
        4,
        REFINING_ADDITIONAL_MATERIAL,
        10,
        MARKET
    ),
    REFINED_PROTECTION_STONE(
        "정제된 수호강석",
        66102105,
        4,
        REFINING_ADDITIONAL_MATERIAL,
        10,
        MARKET
    ),
    PROTECTION_STONE(
        "수호강석",
        66102104,
        3,
        REFINING_ADDITIONAL_MATERIAL,
        10,
        MARKET
    ),
    GUARDIAN_STONE_CRYSTAL(
        "수호석 결정",
        66102103,
        3,
        REFINING_ADDITIONAL_MATERIAL,
        10,
        MARKET
    ),

    DESTINY_SHARD(
        "운명의 파편 주머니(소)",
        66130141,
        4,
        REFINING_ADDITIONAL_MATERIAL,
        1000,
        MARKET
    ),
    HONOR_SHARD(
        "명예의 파편 주머니(소)",
        66130131,
        3,
        REFINING_ADDITIONAL_MATERIAL,
        1000,
        MARKET
    ),

    DESTINY_LEAPSTONE(
        "운명의 돌파석",
        66110225,
        4,
        REFINING_ADDITIONAL_MATERIAL,
        1,
        MARKET
    ),
    RADIANT_HONOR_LEAPSTONE(
        "찬란한 명예의 돌파석",
        66110224,
        3,
        REFINING_ADDITIONAL_MATERIAL,
        1,
        MARKET
    ),
    MARVELOUS_HONOR_LEAPSTONE(
        "경이로운 명예의 돌파석",
        66110223,
        3,
        REFINING_ADDITIONAL_MATERIAL,
        1,
        MARKET
    ),
    GREAT_HONOR_LEAPSTONE(
        "위대한 명예의 돌파석",
        66110222,
        3,
        REFINING_ADDITIONAL_MATERIAL,
        1,
        MARKET
    ),

    GEM_TIER_3(
        "1레벨 멸화의 보석",
        DEFAULT_ID,
        3,
        GEM,
        1,
        AUCTION
    ),
    GEM_TIER_4(
        "1레벨 겁화의 보석",
        DEFAULT_ID,
        4,
        GEM,
        1,
        AUCTION
    );

    companion object {
        val marketItems: List<Item>
            get() = entries.filter { it.searchType == MARKET }

        val auctionItems: List<Item>
            get() = entries.filter { it.searchType == AUCTION }
    }

}
