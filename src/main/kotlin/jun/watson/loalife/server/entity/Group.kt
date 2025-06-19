package jun.watson.loalife.server.entity

import jakarta.persistence.*
import jakarta.persistence.CascadeType.*
import jakarta.persistence.FetchType.*
import jakarta.persistence.GenerationType.*

@Entity
@Table(name = "character_group")
class Group(
    @OneToMany(mappedBy = "group", cascade = [ALL], fetch = LAZY)
    val characters: List<Character>
) {

    companion object {
        operator fun invoke(characters: List<Character>): Group {
            val group = Group(characters = characters.toList())
            for (character in characters) {
                character.group = group
            }

            return group
        }
    }

    @Id
    @GeneratedValue(strategy = IDENTITY)
    val id: Long? = null

}
