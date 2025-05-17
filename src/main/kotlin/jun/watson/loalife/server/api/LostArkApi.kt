package jun.watson.loalife.server.api

import jun.watson.loalife.server.data.Item
import jun.watson.loalife.server.redis.CacheName.API_CHARACTER_RESPONSE
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.Cacheable
import org.springframework.core.ParameterizedTypeReference
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient

@Component
class LostArkApi(
    private val webClient: WebClient,
    private val apiKeyManager: ApiKeyManager,
    private val cacheManager: CacheManager
) {

    @Cacheable(value = [API_CHARACTER_RESPONSE], key = "#queryName")
    fun searchCharacters(queryName: String, keyFromClient: String?): List<LostArkCharacterResponseDto> {
        val response = webClient.get()
            .uri("https://developer-lostark.game.onstove.com/characters/$queryName/siblings")
            .headers { headers ->
                headers.setBearerAuth(apiKeyManager.get(keyFromClient))
            }
            .retrieve()
            .bodyToFlux(LostArkCharacterResponseDto::class.java)
            .collectList()
            .block() ?: emptyList()

        for (name in response.map { it.characterName }) {
            cacheManager.getCache(API_CHARACTER_RESPONSE)?.put(name, response)
        }

        return response
    }

    fun searchResourcePrices(items: List<Item>): List<LostArkItemResponseDto> {
        return items.mapNotNull { item ->
            webClient.get()
                .uri("https://developer-lostark.game.onstove.com/markets/items/${item.id}")
                .headers { it.setBearerAuth(apiKeyManager.get()) }
                .retrieve()
                .bodyToMono(object : ParameterizedTypeReference<List<LostArkItemResponseDto>>() {})
                .block()
                ?.firstOrNull()
        }
    }

}
