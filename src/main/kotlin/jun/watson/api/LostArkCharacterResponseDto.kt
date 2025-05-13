package jun.watson.api

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize

data class LostArkCharacterResponseDto(
    @JsonProperty("ServerName")
    val serverName: String,

    @JsonProperty("CharacterName")
    val characterName: String,

    @JsonProperty("CharacterLevel")
    val characterLevel: Int,

    @JsonProperty("CharacterClassName")
    val characterClassName: String,

    @JsonProperty("ItemAvgLevel")
    @JsonDeserialize(using = LevelParser::class)
    val itemAvgLevel: Double,

    @JsonProperty("ItemMaxLevel")
    @JsonDeserialize(using = LevelParser::class)
    val itemMaxLevel: Double
)
