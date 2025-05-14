package jun.watson.loalife.server.dto

import jun.watson.loalife.server.entity.Resource

data class SearchResponseDto(
    val expeditions: Expeditions,
    val resources: List<Resource>
)
