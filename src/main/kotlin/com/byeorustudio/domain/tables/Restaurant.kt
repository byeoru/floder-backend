package com.byeorustudio.domain.tables

import com.byeorustudio.domain.tables.common.BaseEntity
import com.byeorustudio.domain.tables.common.BaseEntityClass
import com.byeorustudio.domain.tables.common.BaseLongIdTable
import com.byeorustudio.domain.tables.custom.point
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.ReferenceOption

object Restaurants : BaseLongIdTable("restaurants", "restaurant_pk") {
    val name = varchar("name", 30)
    val notice = varchar("notice", 200).nullable()
    val address = varchar("address", 100)
    val waitingTime = integer("waiting_time").nullable()
    val waitingCount = integer("waiting_count").nullable()
    val availableTables = integer("available_tables")
    val coords = point("coords")
    val owner = reference("owner", Users, onDelete = ReferenceOption.CASCADE)
}

class Restaurant(id: EntityID<Long>) : BaseEntity(id, Restaurants) {
    companion object : BaseEntityClass<Restaurant>(Restaurants)

    var name by Restaurants.name
    var notice by Restaurants.notice
    var address by Restaurants.address
    var waitingTime by Restaurants.waitingTime
    var waitingCount by Restaurants.waitingCount
    var availableTables by Restaurants.availableTables
    var coords by Restaurants.coords
    var owner by User referencedOn Restaurants.owner
}
