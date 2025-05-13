package jun.watson.service

import jun.watson.api.LostArkApi
import jun.watson.api.LostArkCharacterResponseDto
import jun.watson.data.ChaosDungeon
import jun.watson.data.Guardian
import jun.watson.data.Raid
import jun.watson.dto.CharacterResponseDto
import jun.watson.dto.ContentReward
import jun.watson.dto.Expeditions
import org.springframework.stereotype.Service

@Service
class ExpeditionSearchService(
    private val lostArkApi: LostArkApi,
) {

    fun getExpeditionsInfo(name: String, keyFromClient: String?): Expeditions {
        val characterResponseDtos = lostArkApi.searchCharacters(name, keyFromClient)
            .map(::getCharacterFromDto)

        return Expeditions(characterResponseDtos)
    }

    private fun getCharacterFromDto(dto: LostArkCharacterResponseDto): CharacterResponseDto {
        val level = dto.itemMaxLevel

        return CharacterResponseDto(
            characterName = dto.characterName,
            serverName = dto.serverName,
            level = level,
            className = dto.characterClassName,
        )
    }

}
