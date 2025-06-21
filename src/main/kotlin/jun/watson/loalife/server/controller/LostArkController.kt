package jun.watson.loalife.server.controller

import jun.watson.loalife.server.dto.SearchResponseDto
import jun.watson.loalife.server.entity.Resource
import jun.watson.loalife.server.exception.CacheNotExistException
import jun.watson.loalife.server.exception.CharacterNotExistException
import jun.watson.loalife.server.service.CharacterCacheManager
import jun.watson.loalife.server.service.ExpeditionSearchService
import jun.watson.loalife.server.service.ResourceService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
class LostArkController(
    val searchService: ExpeditionSearchService,
    val characterCacheManager: CharacterCacheManager,
    val resourceService: ResourceService,
    val apiCallCounter: ApiCallCounter
) {
    @GetMapping("/search")
    fun searchInfo(
        @RequestParam(name = "name") characterName: String?,
        @RequestParam(required = false) apiKey: String?
    ): ResponseEntity<SearchResponseDto> {
        apiCallCounter.increment()

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
    ): ResponseEntity<SearchResponseDto> {
        apiCallCounter.increment()

        characterCacheManager.removeMemoryCache(characterName)
        val expeditions = searchService.getExpeditionsInfo(characterName, apiKey)
        val resources = resourceService.getResources()

        return ResponseEntity.ok(
            SearchResponseDto(
                expeditions = expeditions,
                resources = resources
            )
        )
    }

    @GetMapping("/resource")
    fun findResources(): ResponseEntity<List<Resource>> {
        apiCallCounter.increment()

        val resources = resourceService.getResources()

        return ResponseEntity.ok(resources)
    }

    @GetMapping("/count")
    fun getCount(): ResponseEntity<Map<String, Any>> {
        return ResponseEntity.ok(
            mapOf(
                "count" to apiCallCounter.count,
                "createdAt" to apiCallCounter.createdAt
            )
        )
    }

    @ExceptionHandler(Exception::class)
    fun handleException(exception: Exception): ResponseEntity<String> {
        return when (exception) {
            is CacheNotExistException,
            is CharacterNotExistException -> {
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.message)
            }

            else -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.message)
        }
    }
}
