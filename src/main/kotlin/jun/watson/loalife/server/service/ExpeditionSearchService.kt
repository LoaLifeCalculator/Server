package jun.watson.loalife.server.service

import jun.watson.loalife.server.api.LostArkApi
import jun.watson.loalife.server.api.LostArkCharacterResponseDto
import jun.watson.loalife.server.dto.CharacterResponseDto
import jun.watson.loalife.server.dto.Expeditions
import jun.watson.loalife.server.entity.Character
import jun.watson.loalife.server.entity.Group
import jun.watson.loalife.server.exception.CacheNotExistException
import jun.watson.loalife.server.exception.CharacterNotExistException
import jun.watson.loalife.server.repository.CharacterRepository
import jun.watson.loalife.server.repository.GroupRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.HttpServerErrorException
import java.lang.Exception

@Service
class ExpeditionSearchService(
    private val lostArkApi: LostArkApi,
    private val characterRepository: CharacterRepository,
    private val groupRepository: GroupRepository
) {

    @Transactional
    fun getExpeditionsInfo(name: String, keyFromClient: String?): Expeditions {
        try {
            val characterResponses = lostArkApi.searchCharacters(name, keyFromClient)
            if (characterResponses.isEmpty()) {
                throw CharacterNotExistException("해당 닉네임을 지닌 캐릭터를 조회할 수 없습니다.")
            }

            removeCachesIfExist(name)
            insertCaches(characterResponses)

            return Expeditions(characterResponses.map(CharacterResponseDto::from))
        } catch (e: Exception) {
            when (e) {
                is HttpClientErrorException,
                is HttpServerErrorException -> {
                    val expeditions = getExpeditionsFromCache(name)
                        ?: throw CacheNotExistException("로스트아크 API에 연동할 수 없으며, 캐시 데이터도 존재하지 않습니다.")

                    return expeditions
                }
                else -> throw e
            }
        }
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
        return characterRepository.findByCharacterName(name)?.group
    }

    private fun getExpeditionsFromCache(name: String): Expeditions? {
        val characters = findGroupByName(name)?.characters

        return if (characters != null) {
            Expeditions(characters.map(CharacterResponseDto::from))
        } else {
            null
        }
    }

}
