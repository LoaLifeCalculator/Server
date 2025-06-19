package jun.watson.loalife.server.service

import jun.watson.loalife.server.api.LostArkApi
import jun.watson.loalife.server.dto.CharacterResponseDto
import jun.watson.loalife.server.dto.Expeditions
import jun.watson.loalife.server.entity.Group
import jun.watson.loalife.server.exception.CacheNotExistException
import jun.watson.loalife.server.exception.CharacterNotExistException
import jun.watson.loalife.server.repository.CharacterRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.HttpServerErrorException
import org.springframework.web.reactive.function.client.WebClientRequestException
import org.springframework.web.reactive.function.client.WebClientResponseException

const val DEFAULT_NAME = "Suchu"

@Service
class ExpeditionSearchService(
    private val lostArkApi: LostArkApi,
    private val characterRepository: CharacterRepository,
    private val characterCacheManager: CharacterCacheManager,
) {
    private val log: Logger = LoggerFactory.getLogger(this::class.java)

    @Transactional
    fun getExpeditionsInfo(name: String?, keyFromClient: String?): Expeditions {
        val queryName = getQueryName(name)

        try {
            val characterResponses = lostArkApi.searchCharacters(queryName, keyFromClient)
            if (characterResponses.isEmpty()) {
                throw CharacterNotExistException("해당 닉네임을 지닌 캐릭터를 조회할 수 없습니다.")
            }

            try {
                characterCacheManager.removeCacheIfExist(findGroupByName(queryName))
                characterCacheManager.insertCache(characterResponses)
            } catch (e: DataIntegrityViolationException) {
                log.info("캐시 중복 발생으로 캐시를 처리하지 않음", e)
            }

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
