package jun.watson.api

import com.fasterxml.jackson.annotation.JsonProperty

data class LostArkItemRequestDto(
    @JsonProperty("Sort")
    val sort: String = "GRADE",

    @JsonProperty("CategoryCode")
    val categoryCode: Int,

    @JsonProperty("CharacterClass")
    val characterClass: String? = null,

    @JsonProperty("ItemTier")
    val itemTier: Int? = null,

    @JsonProperty("ItemGrade")
    val itemGrade: String? = null,

    @JsonProperty("ItemName")
    val itemName: String? = null,

    @JsonProperty("PageNo")
    val pageNo: Int = 0,

    @JsonProperty("SortCondition")
    val sortCondition: String = "ASC"
)
