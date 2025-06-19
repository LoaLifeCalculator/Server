package jun.watson.loalife.server.repository

import jun.watson.loalife.server.entity.Character
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface CharacterRepository : JpaRepository<Character, Long> {
    @Query("SELECT c FROM Character c JOIN FETCH c.group WHERE LOWER(c.characterName) = LOWER(:name)")
    fun findByCharacterNameIgnoreCase(name: String): Character?
}
