package jun.watson.loalife.server.dto

class Expeditions private constructor(
    val expeditions: Map<String, List<CharacterResponseDto>>
) {

    companion object {
        operator fun invoke(expeditions: List<CharacterResponseDto>): Expeditions {
            val groupedCharacters = expeditions
                .groupBy { it.serverName }
                .mapValues { (_, characters) ->
                    characters.sortedByDescending { it.level }
                }

            return Expeditions(groupedCharacters)
        }
    }
}
