package jun.watson.loalife.server.entity

import jakarta.persistence.*
import jakarta.persistence.GenerationType.*
import jun.watson.loalife.server.api.LostArkCharacterResponseDto

@Entity
@Table(name = "loa_character")
class Character(
    val serverName: String,
    val characterName: String,
    val characterLevel: Int,
    val characterClassName: String,
    val itemAvgLevel: Double,
    val itemMaxLevel: Double,
    @ManyToOne
    val group: Group,
) {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    val id: Long? = null

    companion object {
        fun of(dto: LostArkCharacterResponseDto, group: Group): Character {
            return Character(
                serverName = dto.serverName,
                characterName = dto.characterName,
                characterLevel = dto.characterLevel,
                characterClassName = dto.characterClassName,
                itemAvgLevel = dto.itemAvgLevel,
                itemMaxLevel = dto.itemMaxLevel,
                group = group
            )
        }
    }

}
