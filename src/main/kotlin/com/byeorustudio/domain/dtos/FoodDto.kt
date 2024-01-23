package com.byeorustudio.domain.dtos

import com.byeorustudio.domain.tables.Food
import kotlinx.serialization.Serializable

@Serializable
data class FoodResisterDto(
    val name: String,
    val description: String,
    val normalPrice: Int
)

@Serializable
class FoodListDto {
    var pk: Long? = null
    var name: String? = null
    var description: String? = null
    var normalPrice: Int? = null
    var disPrice: Int? = null

    companion object {
        fun fromEntity(entity: Food) =
            FoodListDto().apply {
                this.pk = entity.id.value
                this.name = entity.name
                this.description = entity.description
                this.normalPrice = entity.normalPrice
                this.disPrice = entity.disPrice
            }
    }
}