package jun.watson.api

import jun.watson.entity.Item
import org.springframework.core.ParameterizedTypeReference
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient

@Component
class LostArkApi(
    val webClient: WebClient,
    val apiKeyManager: ApiKeyManager
) {

    fun searchCharacters(name: String, keyFromClient: String?): List<LostArkCharacterResponseDto> {
        return webClient.get()
            .uri("https://developer-lostark.game.onstove.com/characters/$name/siblings")
            .headers { headers ->
                headers.setBearerAuth(apiKeyManager.get(keyFromClient))
            }
            .retrieve()
            .bodyToFlux(LostArkCharacterResponseDto::class.java)
            .collectList()
            .block() ?: emptyList()
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
