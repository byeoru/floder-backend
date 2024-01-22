package com.byeorustudio.domain.dtos

import kotlinx.serialization.Serializable

@Serializable
data class FoodResisterDto(
    val name: String,
    val description: String,
    val normalPrice: Int
)