package jun.watson.loalife.server.service

import jun.watson.loalife.server.api.LostArkCharacterResponseDto
import jun.watson.loalife.server.entity.Character
import jun.watson.loalife.server.entity.Group
import jun.watson.loalife.server.redis.CacheName.API_CHARACTER_RESPONSE
import jun.watson.loalife.server.repository.CharacterRepository
import jun.watson.loalife.server.repository.GroupRepository
import org.springframework.cache.CacheManager
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation.REQUIRES_NEW
import org.springframework.transaction.annotation.Transactional

@Service
class CharacterCacheManager(
    private val groupRepository: GroupRepository,
    private val characterRepository: CharacterRepository,
    private val cacheManager: CacheManager,
) {
    @Transactional(propagation = REQUIRES_NEW)
    fun removeCacheIfExist(group: Group?) {
        group?: return

        groupRepository.delete(group)
    }

    @Transactional(propagation = REQUIRES_NEW)
    fun insertCache(characterDtos: List<LostArkCharacterResponseDto>) {
        val characters = characterDtos.map { characterDto -> Character.of(characterDto) }
        val group = Group(characters = characters)

        groupRepository.save(group)
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

    private fun findGroupByName(name: String): Group? {
        return characterRepository.findByCharacterNameIgnoreCase(name)?.group
    }
}
