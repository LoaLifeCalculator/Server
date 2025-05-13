package jun.watson.entity

import jun.watson.data.Item

fun ItemEntityType.toSharedItem(): Item = Item.valueOf(this.name)

fun Item.toEntityItem(): ItemEntityType = ItemEntityType.valueOf(this.name)
