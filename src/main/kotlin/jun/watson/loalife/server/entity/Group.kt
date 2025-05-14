package jun.watson.loalife.server.entity

import jakarta.persistence.*
import jakarta.persistence.CascadeType.*
import jakarta.persistence.FetchType.*
import jakarta.persistence.GenerationType.*

@Entity
@Table(name = "character_group")
class Group(
    @OneToMany(mappedBy = "group", cascade = [ALL], fetch = LAZY)
    val characters: MutableList<Character> = mutableListOf()
) {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    val id: Long? = null

    fun join(characters: List<Character>) {
        this.characters.addAll(characters)
    }

}
