package jun.watson.entity

import jakarta.persistence.*
import jakarta.persistence.GenerationType.IDENTITY
import jun.watson.data.Item
import jun.watson.dto.Resource

@Entity
class ResourceEntity(
    @Enumerated(EnumType.STRING)
    var itemType: ItemEntityType,
    var avgPrice: Double = 0.0,
) {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    val id: Long? = null

    val item: Item
        get() = itemType.toSharedItem()

    fun toResource(): Resource {
        return Resource(
            item = itemType.toSharedItem(),
            avgPrice = avgPrice
        )
    }

}
