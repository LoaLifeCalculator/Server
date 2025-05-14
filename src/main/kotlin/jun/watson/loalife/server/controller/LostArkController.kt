package jun.watson.loalife.server.controller

import jun.watson.loalife.server.service.ExpeditionSearchService
import jun.watson.loalife.server.service.ResourceService
import jun.watson.loalife.server.dto.SearchResponseDto
import jun.watson.loalife.server.exception.CacheNotExistException
import jun.watson.loalife.server.exception.CharacterNotExistException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
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
        val resources = resourceService.getResources()

        return ResponseEntity.ok(
            SearchResponseDto(
                expeditions = expeditions,
                resources = resources
            )
        )
    }

    @ExceptionHandler(Exception::class)
    fun handleException(exception: Exception): ResponseEntity<String> {
        when (exception) {
            is CacheNotExistException,
            is CharacterNotExistException -> {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.message)
            }
            else -> throw exception
        }
    }

}
