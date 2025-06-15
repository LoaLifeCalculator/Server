package jun.watson.loalife.server.service

import jun.watson.loalife.server.api.LostArkApi
import jun.watson.loalife.server.api.LostArkCharacterResponseDto
import jun.watson.loalife.server.dto.CharacterResponseDto
import jun.watson.loalife.server.dto.Expeditions
import jun.watson.loalife.server.entity.Character
import jun.watson.loalife.server.entity.Group
import jun.watson.loalife.server.exception.CacheNotExistException
import jun.watson.loalife.server.exception.CharacterNotExistException
import jun.watson.loalife.server.redis.CacheName.API_CHARACTER_RESPONSE
import jun.watson.loalife.server.repository.CharacterRepository
import jun.watson.loalife.server.repository.GroupRepository
import org.springframework.cache.CacheManager
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.StringUtils
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.HttpServerErrorException
import org.springframework.web.reactive.function.client.WebClientRequestException
import org.springframework.web.reactive.function.client.WebClientResponseException
import java.lang.Exception

const val DEFAULT_NAME = "Suchu"

@Service
class ExpeditionSearchService(
    private val lostArkApi: LostArkApi,
    private val characterRepository: CharacterRepository,
    private val groupRepository: GroupRepository,
    private val cacheManager: CacheManager
) {

    @Transactional
    fun getExpeditionsInfo(name: String?, keyFromClient: String?): Expeditions {
        val queryName = getQueryName(name)

        try {
            val characterResponses = lostArkApi.searchCharacters(queryName, keyFromClient)
            if (characterResponses.isEmpty()) {
                throw CharacterNotExistException("해당 닉네임을 지닌 캐릭터를 조회할 수 없습니다.")
            }

            removeCachesIfExist(queryName)
            insertCaches(characterResponses)

            return Expeditions(characterResponses.map(CharacterResponseDto::from))
        } catch (e: Exception) {
            if (!isHttpException(e)) {
                throw e
            }

            val expeditions = getExpeditionsFromCache(queryName)
                ?: throw CacheNotExistException("로스트아크 API에 연동할 수 없으며, 캐시 데이터도 존재하지 않습니다.")

            return expeditions
        }
    }

    @Transactional(readOnly = true)
    fun removeMemoryCache(name: String) {
        val queryName = getQueryName(name)

        val cache = cacheManager.getCache(API_CHARACTER_RESPONSE) ?: return
        val group = findGroupByName(queryName) ?: return

        for (character in group.characters) {
            val queryCharacterName = getQueryName(character.characterName)
            cache.evict(queryCharacterName)
        }
    }

    private fun getQueryName(name: String?): String {
        if (name == null || !StringUtils.hasText(name)) {
            return DEFAULT_NAME
        }

        return name.lowercase().trim()
    }

    private fun removeCachesIfExist(name: String) {
        val group = findGroupByName(name)

        if (group != null) {
            groupRepository.delete(group)
        }
    }

    private fun insertCaches(characterDtos: List<LostArkCharacterResponseDto>) {
        val group = Group()
        val characters = characterDtos.map { characterDto -> Character.of(characterDto, group) }

        group.join(characters)

        groupRepository.save(group)
    }

    private fun findGroupByName(name: String): Group? {
        return characterRepository.findByCharacterNameIgnoreCase(name)?.group
    }

    private fun getExpeditionsFromCache(name: String): Expeditions? {
        val characters = findGroupByName(name)?.characters

        return if (characters != null) {
            Expeditions(characters.map(CharacterResponseDto::from))
        } else {
            null
        }
    }

    private fun isHttpException(e: Exception): Boolean {
        return when (e) {
            is HttpClientErrorException,
            is WebClientRequestException,
            is WebClientResponseException,
            is HttpServerErrorException -> {
                true
            }

            else -> false
        }
    }

}
