package com.byeorustudio.domain.dtos

import kotlinx.serialization.Serializable

@Serializable
data class RestaurantResisterDto(
    val name: String,
    val address: String,
    val availableTable: Int,
    val x: Double,
    val y: Double
)

@Serializable
class RestaurantSimpleDto {
    var pk: Long? = null
    var name: String? = null

    companion object {
        fun fromMap(map: Map<String, Any>) =
            RestaurantSimpleDto().apply {
                this.pk = map["restaurant_pk"] as Long
                this.name = map["name"] as String
        }
    }
}