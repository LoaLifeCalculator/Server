package jun.watson.loalife.server.api

import com.fasterxml.jackson.annotation.JsonProperty
import jun.watson.loalife.server.data.Item

class LostArkAuctionRequestDto(
    @JsonProperty("Sort")
    val sort: String = "BUY_PRICE",

    @JsonProperty("CategoryCode")
    val categoryCode: Int,

    @JsonProperty("ItemTier")
    val itemTier: Int,

    @JsonProperty("ItemName")
    val itemName: String,

    @JsonProperty("PageNo")
    val pageNo: Int = 0,

    @JsonProperty("SortCondition")
    val sortCondition: String = "ASC"
) {

    companion object {
        fun from(item: Item): LostArkAuctionRequestDto {
            return LostArkAuctionRequestDto(
                categoryCode = item.categoryCode.value,
                itemTier = item.tier,
                itemName = item.korean
            )
        }
    }

}
