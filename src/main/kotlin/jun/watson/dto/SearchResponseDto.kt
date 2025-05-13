package jun.watson.dto

data class SearchResponseDto(
    val expeditions: Expeditions,
    val resources: List<Resource>
)
