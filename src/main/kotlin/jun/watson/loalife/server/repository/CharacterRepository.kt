package jun.watson.loalife.server.repository

import jun.watson.loalife.server.entity.Character
import org.springframework.data.jpa.repository.JpaRepository

interface CharacterRepository : JpaRepository<Character, Long> {
    fun findByCharacterNameIgnoreCase(name: String): Character?
}
