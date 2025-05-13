package jun.watson.dto

import jun.watson.data.Item

data class Resource(
    var item: Item,
    var avgPrice: Double = 0.0,
)
