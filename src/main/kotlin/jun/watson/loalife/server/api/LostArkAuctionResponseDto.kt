package jun.watson.loalife.server.api

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.Instant

/**
 * API 응답: 경매 아이템 목록
 */
data class LostArkAuctionResponseDto(
    @JsonProperty("PageNo")
    val pageNo: Int,

    @JsonProperty("PageSize")
    val pageSize: Int,

    @JsonProperty("TotalCount")
    val totalCount: Int,

    @JsonProperty("Items")
    val items: List<ItemDto>
) {
    /**
     * 단일 아이템 정보
     */
    data class ItemDto(
        @JsonProperty("Name")
        val name: String,

        @JsonProperty("Grade")
        val grade: String,

        @JsonProperty("Tier")
        val tier: Int,

        @JsonProperty("Level")
        val level: Int?,

        @JsonProperty("Icon")
        val icon: String,

        @JsonProperty("GradeQuality")
        val gradeQuality: Int?,

        @JsonProperty("AuctionInfo")
        val auctionInfo: AuctionInfoDto
    )

    /**
     * 경매 상세 정보
     */
    data class AuctionInfoDto(
        @JsonProperty("StartPrice")
        val startPrice: Int,

        @JsonProperty("BuyPrice")
        val buyPrice: Int?,

        @JsonProperty("BidPrice")
        val bidPrice: Int,

        @JsonProperty("BidCount")
        val bidCount: Int,

        @JsonProperty("BidStartPrice")
        val bidStartPrice: Int,

        @JsonProperty("IsCompetitive")
        val isCompetitive: Boolean,

        @JsonProperty("TradeAllowCount")
        val tradeAllowCount: Int,

        @JsonProperty("UpgradeLevel")
        val upgradeLevel: Int?
    )

    val price: Int
        get() {
            var sum = 0
            var devider = 0

            for (auctionInfo in items.map { it.auctionInfo }) {
                if (auctionInfo.buyPrice != null) {
                    sum += auctionInfo.buyPrice
                    devider++
                }
            }

            if (devider == 0) {
                return items.sumOf { it.auctionInfo.startPrice } / items.size
            }

            return sum / devider
        }

}
