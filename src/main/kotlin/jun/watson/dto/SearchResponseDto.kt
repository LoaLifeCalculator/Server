package jun.watson.dto

import jun.watson.entity.Resource

data class SearchResponseDto(
    val expeditions: Expeditions,
    val resources: List<Resource>
)
