package jun.watson.loalife.server.api

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDate

data class LostArkMarketResponseDto(

    @JsonProperty("Name")
    val name: String,

    @JsonProperty("TradeRemainCount")
    val tradeRemainCount: Int?,

    @JsonProperty("BundleCount")
    val bundleCount: Int,

    @JsonProperty("Stats")
    val stats: List<Stat>,

    @JsonProperty("ToolTip")
    val toolTip: String
) {
    data class Stat(
        @JsonProperty("Date")
        val date: LocalDate,

        @JsonProperty("AvgPrice")
        val avgPrice: Double,

        @JsonProperty("TradeCount")
        val tradeCount: Int
    )

    val avgPrice: Double
        get() = stats[0].avgPrice

}
