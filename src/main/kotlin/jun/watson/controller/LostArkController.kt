package jun.watson.controller

import jun.watson.service.ExpeditionSearchService
import jun.watson.service.ResourceService
import jun.watson.dto.SearchResponseDto
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class LostArkController(
    val searchService: ExpeditionSearchService,
    val resourceService: ResourceService,
) {

    @GetMapping("/search")
    fun searchInfo(
        @RequestParam(name = "name") characterName: String,
        @RequestParam(required = false) apiKey: String?
    ): ResponseEntity<SearchResponseDto> {
        val expeditions = searchService.getExpeditionsInfo(characterName, apiKey)
        val resourceEntities = resourceService.getResources()

        return ResponseEntity.ok(
            SearchResponseDto(
                expeditions = expeditions,
                resources = resourceEntities.map { it.toResource() }
            )
        )
    }

}
