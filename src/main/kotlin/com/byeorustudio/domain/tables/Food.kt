package com.byeorustudio.domain.tables

import com.byeorustudio.domain.tables.common.BaseEntity
import com.byeorustudio.domain.tables.common.BaseEntityClass
import com.byeorustudio.domain.tables.common.BaseLongIdTable
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.ReferenceOption

object Foods : BaseLongIdTable("foods", "food_pk") {
    val name = varchar("name", 30)
    val description = varchar("description", 100)
    val normalPrice = integer("normal_price")
    val disPrice = integer("dis_price").nullable()
    val count = integer("count").nullable()
    val restaurant = reference("restaurant", Restaurants, onDelete = ReferenceOption.CASCADE)
}

class Food(id: EntityID<Long>) : BaseEntity(id, Foods) {
    companion object : BaseEntityClass<Food>(Foods)

    var name by Foods.name
    var description by Foods.description
    var normalPrice by Foods.normalPrice
    var disPrice by Foods.disPrice
    var count by Foods.count
    var restaurant by Restaurant referencedOn Foods.restaurant
}