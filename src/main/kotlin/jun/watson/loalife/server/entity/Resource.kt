package jun.watson.loalife.server.entity

import jakarta.persistence.*
import jakarta.persistence.GenerationType.IDENTITY
import jun.watson.loalife.server.data.Item

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
