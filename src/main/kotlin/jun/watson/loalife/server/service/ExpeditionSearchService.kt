package jun.watson.loalife.server.service

import jun.watson.loalife.server.api.LostArkApi
import jun.watson.loalife.server.api.LostArkCharacterResponseDto
import jun.watson.loalife.server.data.ChaosDungeon
import jun.watson.loalife.server.data.Guardian
import jun.watson.loalife.server.data.Raid
import jun.watson.loalife.server.dto.CharacterResponseDto
import jun.watson.loalife.server.dto.ContentReward
import jun.watson.loalife.server.dto.Expeditions
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
