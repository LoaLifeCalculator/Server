package jun.watson.entity

import jakarta.persistence.*
import jakarta.persistence.GenerationType.IDENTITY

@Entity
class Resource(
    @Enumerated(EnumType.STRING)
    var item: Item,
    var avgPrice: Double = 0.0,
) {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    val id: Long? = null

}
