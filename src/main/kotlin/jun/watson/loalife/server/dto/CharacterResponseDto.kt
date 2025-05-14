package jun.watson.loalife.server.dto

import jun.watson.loalife.server.api.LostArkCharacterResponseDto
import jun.watson.loalife.server.entity.Character

data class CharacterResponseDto(
    val characterName: String,
    val serverName: String,
    val level: Double,
    val className: String,
) {

    companion object {
        fun from(entity: Character): CharacterResponseDto {
            return CharacterResponseDto(
                characterName = entity.characterName,
                serverName = entity.serverName,
                level = entity.itemMaxLevel,
                className = entity.characterClassName,
            )
        }

        fun from(dto: LostArkCharacterResponseDto): CharacterResponseDto {
            return CharacterResponseDto(
                characterName = dto.characterName,
                serverName = dto.serverName,
                level = dto.itemMaxLevel,
                className = dto.characterClassName,
            )
        }
    }

}
