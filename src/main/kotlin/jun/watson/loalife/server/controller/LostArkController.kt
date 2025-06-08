package jun.watson.loalife.server.controller

import jun.watson.loalife.server.dto.Expeditions
import jun.watson.loalife.server.dto.SearchResponseDto
import jun.watson.loalife.server.entity.Resource
import jun.watson.loalife.server.exception.CacheNotExistException
import jun.watson.loalife.server.exception.CharacterNotExistException
import jun.watson.loalife.server.service.ExpeditionSearchService
import jun.watson.loalife.server.service.ResourceService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
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
        val resources = resourceService.getResources()

        return ResponseEntity.ok(
            SearchResponseDto(
                expeditions = expeditions,
                resources = resources
            )
        )
    }

    @GetMapping("/renewal")
    fun renewSearch(
        @RequestParam(name = "name") characterName: String,
        @RequestParam(required = false) apiKey: String?
    ): ResponseEntity<Expeditions> {
        searchService.removeMemoryCache(characterName)
        val expeditions = searchService.getExpeditionsInfo(characterName, apiKey)

        return ResponseEntity.ok(expeditions)
    }

    @GetMapping("/resource")
    fun findResources(): ResponseEntity<List<Resource>> {
        val resources = resourceService.getResources()

        return ResponseEntity.ok(resources)
    }

    @ExceptionHandler(Exception::class)
    fun handleException(exception: Exception): ResponseEntity<String> {
        when (exception) {
            is CacheNotExistException,
            is CharacterNotExistException -> {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.message)
            }
            else -> return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.message)
        }
    }

}
